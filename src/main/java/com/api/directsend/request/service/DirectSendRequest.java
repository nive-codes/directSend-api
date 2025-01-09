package com.api.directsend.request.service;


import lombok.Getter;
import lombok.Setter;

/*다이렉트샌드로 요청할 객체 domain*/
@Getter
@Setter
public class DirectSendRequest {
    private String mobile;
    private String message;
    private String apiId;
    private String apiKey;
    private String sender;
    private String title;



}
