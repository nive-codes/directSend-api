package com.api.directsend;

import com.api.directsend.request.handler.DirectSendHandler;
import com.api.directsend.request.service.DirectSendRequest;
import org.junit.jupiter.api.Test;
import com.amazonaws.services.lambda.runtime.Context;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class LambdaHandlerTest {

    @Test
    public void testHandleRequest() {
        // 핸들러 인스턴스 생성
        DirectSendHandler directSendHandler = new DirectSendHandler();

        // Mockito로 Context 객체 모킹
        Context mockContext = mock(Context.class);

        // 테스트용 DirectSendRequest 객체 생성
        DirectSendRequest request = new DirectSendRequest();
        request.setMobile("01011112222");
        request.setMessage("test message");
        request.setSender("0212341234");
        request.setApiId("gucf1232024");
        request.setApiKey("123");

        // 핸들러 호출
        ResponseEntity<?> result = directSendHandler.handleRequest(request, mockContext);
//        System.out.println("result : " + result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        // 결과 검증
//        assertTrue(result);  // 정상적으로 문자 발송이 이루어졌다면 true가 반환되야 함
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

        // Create handler
        DirectSendHandler directSendHandler = new DirectSendHandler();

        // When
        ResponseEntity<?> result = directSendHandler.handleRequest(request, mockContext);

        // Then
        // 검증: 상태 코드가 400 BAD_REQUEST인지 확인
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        // 검증: 응답 본문에 "DirectSend API Error" 메시지가 포함되는지 확인
        assertTrue(result.getBody().toString().contains("DirectSend API Error"));
    }
}
