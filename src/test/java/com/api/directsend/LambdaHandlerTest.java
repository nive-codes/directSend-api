package com.api.directsend;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.api.directsend.request.handler.DirectSendHandler;
import com.api.directsend.request.service.DirectSendRequest;
import org.junit.jupiter.api.Test;
import com.amazonaws.services.lambda.runtime.Context;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LambdaHandlerTest {

    @Test
    public void testHandleRequest() {
        // 핸들러 인스턴스 생성
        DirectSendHandler directSendHandler = new DirectSendHandler();

        // Mockito로 Context 객체 모킹
        Context mockContext = mock(Context.class);

        // APIGatewayProxyRequestEvent 객체 생성
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();

        // 테스트용 JSON 데이터 설정
        String body = "{"
                + "\"mobile\":\"01011112222\","
                + "\"message\":\"test message\","
                + "\"sender\":\"0212341234\","
                + "\"apiId\":\"gucf1232024\","
                + "\"apiKey\":\"123\""
                + "}";

        requestEvent.setBody(body); // 요청 본문(body) 설정

        // 핸들러 호출
        ResponseEntity<?> result = directSendHandler.handleRequest(requestEvent, mockContext);

        // 결과 검증
        assertNotNull(result);  // 결과가 null이 아니어야 한다.
        assertEquals(HttpStatus.OK, result.getStatusCode());  // 응답 상태 코드가 200이어야 한다.

        // 추가 검증: 메시지가 성공적으로 전송되었는지 확인
        assertTrue(result.getBody().toString().contains("Message sent successfully"));
    }

    @Test
    public void testHandleRequest_Failure() {
        // Given
        DirectSendRequest request = new DirectSendRequest();
        request.setMobile("01011112222");
        request.setMessage("test message");
        request.setSender("0212341234");
        request.setApiId("gucf1232024");
        request.setApiKey("123");

        // Mock Context
        Context mockContext = Mockito.mock(Context.class);

        // APIGatewayProxyRequestEvent 객체 생성
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();

        // 테스트용 JSON 데이터 설정
        String body = "{"
                + "\"mobile\":\"01011112222\","
                + "\"message\":\"test message\","
                + "\"sender\":\"0212341234\","
                + "\"apiId\":\"gucf1232024\","
                + "\"apiKey\":\"123\""
                + "}";

        requestEvent.setBody(body); // 요청 본문(body) 설정

        // Create handler
        DirectSendHandler directSendHandler = new DirectSendHandler();

        // When
        ResponseEntity<?> result = directSendHandler.handleRequest(requestEvent, mockContext);

        // Then
        // 검증: 상태 코드가 400 BAD_REQUEST인지 확인
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        // 검증: 응답 본문에 "DirectSend API Error" 메시지가 포함되는지 확인
        assertTrue(result.getBody().toString().contains("DirectSend API Error"));
    }
}
