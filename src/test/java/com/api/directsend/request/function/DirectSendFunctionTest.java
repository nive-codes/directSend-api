package com.api.directsend.request.function;

import com.api.directsend.request.service.DirectSendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class DirectSendFunctionTest {

    @Test
    public void testSendNowSMS(){
        // 테스트할 파라미터 설정
        String receiver = "01012341234";
        String sender = "0212341234";
        String title = "";
        String message = "This is test message.";
        String apiId = "test";  // 실제 API ID를 사용해야 합니다.
        String apiKey = "Test";  // 실제 API Key를 사용해야 합니다.

        // 서비스 메서드 호출
        boolean result = DirectSendService.sendNowSms(receiver, sender, title, message, apiId, apiKey);
        System.out.println(result);
        // 결과 검증
        assertTrue(result, "SMS 전송에 실패했습니다.");
    }



    @Test
    void  convertToReceiverArray() {
        String mobile = "01012341234";
        String expectedReceiverJson = "[{\"mobile\":\"01012341234\"}]";  // 예상 결과
        String receiverJson = "";
        try {
            // 배열 형태로 mobile을 포함한 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();
            receiverJson = objectMapper.writeValueAsString(Collections.singletonList(Collections.singletonMap("mobile", mobile)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 결과 비교
        assertEquals(expectedReceiverJson, receiverJson, "The receiver JSON should match the expected format.");
    }

}