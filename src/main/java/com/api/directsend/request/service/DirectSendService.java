package com.api.directsend.request.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

public class DirectSendService {

    private static final String API_URL = "https://directsend.co.kr/index.php/api_v2/sms_change_word";

    public static boolean sendNowSms(String receiver, String sender, String title, String message, String apiId, String apiKey) {
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

            System.out.println("Sending JSON: " + urlParameters);

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
            System.out.println("Response Code: " + responseCode);
            if (responseCode == 200) {
                result = true;
            } else {
                System.out.println("Failed with HTTP error code: " + responseCode);
            }

            // 응답을 읽어서 출력
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println("Response: " + response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return result;
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
