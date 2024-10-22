package com.example.backend.utils;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class VoiceUtil {

    private final S3Util s3Util;

    public String generateVoice(String text, String userVoiceId) {
        String modelId = "eleven_multilingual_v2"; // 적합한 모델 ID로 교체

        // 요청 본문에 model_id 추가
        String requestBody = "{\n" +
                "  \"text\": \"" + text + "\",\n" +
                "  \"voice_settings\": {\n" +
                "    \"stability\": 0.1,\n" +
                "    \"similarity_boost\": 0.3,\n" +
                "    \"style\": 0.2\n" +
                "  },\n" +
                "  \"model_id\": \"" + modelId + "\"" +
                "}";

        try {
            // POST 요청
            HttpResponse<byte[]> response = Unirest.post("https://api.elevenlabs.io/v1/text-to-speech/" + userVoiceId)
                    .header("xi-api-key", "sk_dd3e66d094ebadc941c704104af955edffa33f77acebad6c")  // 실제 API 키로 교체
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .asBytes();
            // response = kong.unirest.ByteResponse@5d842f8c

            if (response.getStatus() == 200) {
                // S3에 업로드할 파일 이름 생성
                String fileName = "voice_" + System.currentTimeMillis() + ".mp3";
                ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBody());

                // MultipartFile로 변환
                MultipartFile audioFile = new InMemoryMultipartFile(fileName, response.getBody(), "audio/mpeg"); // com.example.backend.utils.VoiceUtil$InMemoryMultipartFile@d93f867

                // S3에 업로드
                String s3Url = s3Util.upload("voices", audioFile);

                // 업로드된 URL을 반환 (필요에 따라 반환 형식 조정 가능)
                return s3Url; // 또는 S3 URL을 반환할 수도 있음
            } else {
                throw new RuntimeException("음성 생성 실패: " + response.getStatus() + " - " + new String(response.getBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate voice", e);
        }
    }

    // InMemoryMultipartFile 클래스 추가
    private static class InMemoryMultipartFile implements MultipartFile {
        private final String name;
        private final byte[] content;
        private final String contentType;

        public InMemoryMultipartFile(String name, byte[] content, String contentType) {
            this.name = name;
            this.content = content;
            this.contentType = contentType;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return name;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(content);
            }
        }
    }
}




//package com.example.backend.utils;
//
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import kong.unirest.HttpResponse;
//import kong.unirest.Unirest;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.ByteArrayInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class VoiceUtil {
//
//    private final S3Util s3Util;
//
//    public void generateVoice(String text, String voiceId) {
//        String modelId = "eleven_multilingual_v2"; // 적합한 모델 ID로 교체
////        String languageCode = "ko"; // 한국어 언어 코드
//
//        // 요청 본문에 model_id와 language_code 추가
//        String requestBody = "{\n" +
//                "  \"text\": \"" + text + "\",\n" +
//                "  \"voice_settings\": {\n" +
//                "    \"stability\": 0.1,\n" +
//                "    \"similarity_boost\": 0.3,\n" +
//                "    \"style\": 0.2\n" +
//                "  },\n" +
//                "  \"model_id\": \"" + modelId + "\"" +  // model_id를 본문에 포함
//                "}"; // 마지막에 쉼표를 제거
//
//
//        // POST 요청 (voice ID를 포함하여 정확한 경로 사용)
//        // ZcZcEsYxXAIc3nNSev1H
//        // 내 id : JIjbXTZpmyJ2xQda07lI
//        // 오빠 id : uK4FsNMYnpgUHjQKUZwq
//        try {
//            // POST 요청
//            HttpResponse<byte[]> response = Unirest.post("https://api.elevenlabs.io/v1/text-to-speech/" + userVoiceId)
//                    .header("xi-api-key", "sk_dd3e66d094ebadc941c704104af955edffa33f77acebad6c")  // 실제 API 키로 교체
//                    .header("Content-Type", "application/json")
//                    .body(requestBody)
//                    .asBytes();
//
//            if (response.getStatus() == 200) {
//                // S3에 업로드할 파일 이름 생성
//                String fileName = "voice_" + System.currentTimeMillis() + ".mp3";
//                ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBody());
//
//                ObjectMetadata metadata = new ObjectMetadata();
//                metadata.setContentLength(response.getBody().length);
//                metadata.setContentType("audio/mpeg");
//
//                // S3에 업로드하고 URL 반환
//                return s3Util.upload("voice", inputStream, metadata, fileName);
//            } else {
//                throw new RuntimeException("음성 생성 실패: " + response.getStatus() + " - " + new String(response.getBody()));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to generate voice", e);
//        }
//    }
//
//
////    public String addVoice(String name, MultipartFile[] files) throws IOException {
////        // Unirest로 multipart/form-data 요청을 보냄
////        var request = Unirest.post("https://api.elevenlabs.io/v1/voices/add")
////                .header("xi-api-key", "sk_dd3e66d094ebadc941c704104af955edffa33f77acebad6c")
////                .field("name", name)  // name 필드 추가
////                .field("remove_background_noise", "true");  // remove_background_noise 필드를 고정값 false로 추가
////
////        // 파일 부분 추가
////        for (MultipartFile file : files) {
////            request.field("files", file.getBytes(), file.getOriginalFilename());  // 파일 추가
////        }
////
////        // 요청을 보낸 후 응답을 문자열로 받음
////        HttpResponse<String> response = request.asString(); // 요청을 실행하고 응답을 받음
////
////        return response.getBody(); // 응답의 본문 반환
////    }
//}
