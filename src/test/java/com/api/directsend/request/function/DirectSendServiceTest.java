package com.api.directsend.request.function;

import com.api.directsend.request.service.DirectSendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class DirectSendServiceTest {

    @Test
    public void testSendNowSMS(){
//        // 테스트할 파라미터 설정
//        String receiver = "01012341234";
//        String sender = "01012341234";
//        String title = "";
//        String message = "Thisisatestmessage.";
//        String apiId = "test";  // 실제 API ID를 사용해야 합니다.
//        String apiKey = "test";  // 실제 API Key를 사용해야 합니다.
//
//        // 서비스 메서드 호출
//        String result = DirectSendService.sendNowSms(receiver, sender, title, message, apiId, apiKey);
////        System.out.println(result);
//        // 결과 검증
//        String expectedResult = "{\"status\":0,\"msg\":\"Message sent successfully\"}";  // 예상 결과
//        assertEquals(expectedResult, result);

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