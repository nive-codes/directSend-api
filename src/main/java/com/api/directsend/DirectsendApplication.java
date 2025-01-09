package com.api.directsend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*lambda에서는 springboot가 실행되는게 아님.*/
@SpringBootApplication
public class DirectsendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DirectsendApplication.class, args);
	}

}
