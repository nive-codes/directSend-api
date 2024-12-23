package com.api.directsend.request.function;

import com.api.directsend.request.service.DirectSendRequest;
import com.api.directsend.request.service.DirectSendService;
import org.springframework.stereotype.Component;

/*함수형 프로그래밍 제공 java-8*/
import java.util.function.Function;

@Component  /*bean 등록*/
public class DirectSendFunction implements Function<DirectSendRequest, Boolean> {

    @Override
    public Boolean apply(DirectSendRequest request){/*상속받은 인터페이스에 따라 return 객체가 다름*/
        // 받아온 DirectSendRequest에서 값 추출
        String receiver = request.getMobile();
        String message = request.getMessage();
        String sender = request.getSender(); // 송신자 번호는 환경 변수나 설정으로 대체 가능
        String title = "";  // 제목도 필요한 경우 추가
        String apiKey =request.getApiKey();
        String apiId = request.getApiId();

        // 기존 sendNowSms 메서드를 호출하여 API 요청을 보냄
        return DirectSendService.sendNowSms(receiver, sender, title, message, apiId, apiKey);
    }

}
