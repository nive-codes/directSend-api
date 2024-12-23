package com.api.directsend.request.handler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.api.directsend.request.service.DirectSendRequest;
import com.api.directsend.request.service.DirectSendService;
import com.api.directsend.util.DomainCheckUtil;
import com.api.directsend.util.DomainInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.request.InvalidRequestStateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

@Slf4j
public class DirectSendHandler implements RequestHandler<APIGatewayProxyRequestEvent, ResponseEntity<?>> {

    @Override
    public ResponseEntity<?> handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {


        String domain = DomainCheckUtil.extractDomain(apiGatewayProxyRequestEvent);      /*요청한 도메인 확인*/


        if(domain == null){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Domain is null Invalid domain"));
        }

        DomainInfo domainInfo = DomainCheckUtil.getDomainInfo(domain);

        if (domainInfo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Invalid domain"));
        }

        // 요청 본문(body) 추출
        String body = apiGatewayProxyRequestEvent.getBody();

        // JSON 파싱을 위한 ObjectMapper
        ObjectMapper directSendObjectMapper = new ObjectMapper();

        // 본문에서 DirectSendRequest 데이터 파싱
        DirectSendRequest request = null;
        try {
            request = directSendObjectMapper.readValue(body, DirectSendRequest.class);
        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "DirectSend API Error - Status: JsonProcessingException"));
        } catch (Exception e){
            context.getLogger().log("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An unexpected error occurred"));
        }

//
//        request.setApiId(domainInfo.getApiId());
//        request.setApiKey(domainInfo.getApiKey());
//        request.setSender(domainInfo.getSender());


        try {
            // 받아온 요청 값 검증
            if (request == null || request.getMobile() == null || request.getMessage() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Invalid request data"));
            }

            // 요청 값 추출
            /*검증 이후 DirectSendService.sendNowSms(request)로 발송하도록 수정*/
            String receiver = request.getMobile();
            String sender = request.getSender();
            String message = request.getMessage();
            String title = request.getTitle(); // 필요 시 추가
            String apiId = request.getApiId();
            String apiKey = request.getApiKey();

            log.debug("Receiver JSON: {} : "+ receiver);
            log.debug("Sender: {} : " +sender);
            log.debug("Message: {} : "+message);
            log.debug("Title: {} : "+title);
            log.debug("API ID: {} : "+ apiId);
            log.debug("API Key: {} : "+ apiKey);


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

}
