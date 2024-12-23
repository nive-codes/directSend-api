package com.api.directsend.request.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DirectSendService {

    private static final String API_URL = "https://directsend.co.kr/index.php/api_v2/sms_change_word";

    public static String sendNowSms(String receiver, String sender, String title, String message, String apiId, String apiKey) {
        boolean result = false;
        try {
            URL obj = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // POST 메서드 설정
            con.setRequestMethod("POST");

            // 필요한 헤더 설정
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setRequestProperty("Accept", "application/json");

            receiver = convertToReceiverArray(receiver);

            // JSON 형식의 요청 데이터 생성
            String urlParameters = "{" +
                    "\"title\":\"" + title + "\", " +
                    "\"message\":\"" + message + "\", " +
                    "\"sender\":\"" + sender + "\", " +
                    "\"username\":\"" + apiId + "\", " +
                    "\"receiver\":" + receiver + ", " +
                    "\"key\":\"" + apiKey + "\", " +
                    "\"type\":\"java\"" +
                    "}";

            log.debug("Sending JSON: " + urlParameters);

            // SNI 관련 설정
            System.setProperty("jsse.enableSNIExtension", "false");

            // 요청 본문에 데이터를 쓰기 위해 OutputStream을 열고 데이터를 작성
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(urlParameters.getBytes("UTF-8"));
                wr.flush();
            }

            // 서버로부터의 응답 코드 확인
            int responseCode = con.getResponseCode();
            log.debug("Response Code: " + responseCode);
            String response;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    responseCode == 200 ? con.getInputStream() : con.getErrorStream(), StandardCharsets.UTF_8))) {
                response = br.lines().collect(Collectors.joining("\n"));
            }
            // 로그 출력
            log.debug("Response Code: " + responseCode);

            // 결과 반환
            if (responseCode == 200) {
                log.debug("test com in");
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> responseMap = objectMapper.readValue(response, new TypeReference<>() {});
//                log.debug("Response Data: " + responseMap.toString());
                String status = responseMap.get("status");
                if ("0".equals(status)) {
                    return "{\"status\":0,\"msg\":\"Message sent successfully\"}";
                } else {
                    return "{\"status\":" + status + ",\"msg\":\"Failed to send message: " + responseMap.get("msg") + "\"}";
                }

            } else {
                return "{\"status\":" + responseCode + ",\"msg\":\"Failed to send message\"}"; // 실패 시 JSON 반환
            }



        } catch (IOException e) {
            e.printStackTrace();
            return "{\"status\":500,\"msg\":\"" + e.getMessage() + "\"}"; // 예외 상황 JSON 반환
        }
    }


    private static String convertToReceiverArray(String mobile) {
        // JSON 배열 형식으로 변환
        String receiverJson = "";
        try {
            // 배열 형태로 mobile을 포함한 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();
            receiverJson = objectMapper.writeValueAsString(Collections.singletonList(Collections.singletonMap("mobile", mobile)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiverJson;
    }
}
