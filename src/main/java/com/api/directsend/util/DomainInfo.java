package com.api.directsend.util;

// DomainInfo.java
public class DomainInfo {
    private String apiKey;
    private String apiId;

    public DomainInfo(String apiKey, String apiId) {
        this.apiKey = apiKey;
        this.apiId = apiId;
    }

    // Getter, Setter methods
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }
}
