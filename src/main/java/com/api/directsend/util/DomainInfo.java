package com.api.directsend.util;

import lombok.Getter;
import lombok.Setter;

// DomainInfo.java
@Getter
@Setter
public class DomainInfo {
    private String apiKey;
    private String apiId;
    private String sender;

    public DomainInfo(String apiKey, String apiId, String sender) {
        this.apiKey = apiKey;
        this.apiId = apiId;
        this.sender = sender;
    }

}
