package com.api.directsend.request.handler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.api.directsend.request.service.DirectSendRequest;
import com.api.directsend.request.service.DirectSendService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.request.InvalidRequestStateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
public class DirectSendHandler implements RequestHandler<DirectSendRequest, ResponseEntity<?>> {

    @Override
    public ResponseEntity<?> handleRequest(DirectSendRequest request, Context context) {
        try {
            // 받아온 요청 값 검증
            if (request == null || request.getMobile() == null || request.getMessage() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Invalid request data"));
            }

            // 요청 값 추출
            String receiver = request.getMobile();
            String sender = request.getSender();
            String message = request.getMessage();
            String title = ""; // 필요 시 추가
            String apiId = request.getApiId();
            String apiKey = request.getApiKey();

            log.debug("Receiver JSON: {} "+ receiver);
            log.debug("Sender: {}" +sender);
            log.debug("Message: {}"+message);
            log.debug("Title: {}"+title);
            log.debug("API ID: {}"+ apiId);
            log.debug("API Key: {}"+ apiKey);


            // 문자 발송 API 호출
            String response = DirectSendService.sendNowSms(receiver, sender, title, message, apiId, apiKey);
            log.debug("DirectSendHandler.handleRequest RESPONSE DATA -> "+response);

            // JSON 응답 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response);

            int status = jsonResponse.get("status").asInt();
            String msg = jsonResponse.get("msg").asText();


            if (status == 0) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Message sent successfully: " + msg));
            } else {
                // 상태 코드와 메시지 매핑
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "DirectSend API Error - Status: " + status + ", Message: " + msg));
            }
//
        } catch (InvalidRequestStateException e) {
            // 요청 데이터 오류
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Invalid request: " + e.getMessage()));
        } catch (Exception e) {
            // 예외 발생 시 로그 및 오류 응답 반환
            context.getLogger().log("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An unexpected error occurred"));
        }
    }

    // 전화번호를 JSON 배열 형식으로 변환하는 메서드
    private String convertToReceiverArray(String mobile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String[] mobiles = mobile.split(",");
            return objectMapper.writeValueAsString(
                    Arrays.stream(mobiles)
                            .map(m -> Collections.singletonMap("mobile", m.trim()))
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse receiver JSON", e);
        }
    }
}
