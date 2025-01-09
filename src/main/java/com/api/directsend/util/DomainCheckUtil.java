package com.api.directsend.util;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import java.util.HashMap;
import java.util.Map;

// DomainCheckUtil.java
/*domaininfo에 값을 지정, static final을 통한 불변성 보장*/
public class DomainCheckUtil {

    private static final Map<String, DomainInfo> domainInfoMap = new HashMap<>();

    static {
        // 도메인별 API Key, API ID 설정
        domainInfoMap.put("dalseoppg.com", new DomainInfo("exampleApiKey", "exampleApiId",""));
        domainInfoMap.put("lms.dgmirae.or.kr", new DomainInfo("testApiKey", "testApiId",""));
        domainInfoMap.put("garts.kr", new DomainInfo("testApiKey", "testApiId",""));
        domainInfoMap.put("dmtravel.kr", new DomainInfo("testApiKey", "testApiId",""));
    }


    public static DomainInfo getDomainInfo(String domain) {
        return domainInfoMap.get(domain);
    }


    // APIGatewayProxyRequestEvent에서 도메인 추출
    /*도메인 별 요청 값을 처리하도록 api key, api id, sender 전화번호를 선택하도록 처리*/
    public static String extractDomain(APIGatewayProxyRequestEvent request) {
        String host = "";
        try{
            host = request.getHeaders().get("Host");
        }catch (NullPointerException e){
            e.getMessage();
            host = null;
        }
        if (host != null) {
            return host.split(":")[0];  // 포트번호를 제외한 도메인만 추출
        }
        return null;  // 도메인 추출 실패시 null 반환
    }
}

