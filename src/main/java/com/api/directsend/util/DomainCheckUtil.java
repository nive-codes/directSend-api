package com.api.directsend.util;

import java.util.Collections;
import java.util.List;

/*허용할 도메인 목록*/
public class DomainCheckUtil {

    /*list 내부 값 변경 안되는 unmodifiableList로 생성*/
    public static final List<String> ALLOWED_DOMAINS = Collections.unmodifiableList(
            List.of("https://dalseoppg.com", "https://allowed-domain2.com")
    );

//    ALLOWED_DOMAINS.forEach(domain -> System.out.println("Allowed domain: " + domain));
//    ALLOWED_DOMAINS.stream()
//            .filter(domain -> domain.contains("allowed"))  // 필터링 조건
//            .forEach(domain -> System.out.println("Filtered domain: " + domain));  // 필터링된 리스트 출력
}
