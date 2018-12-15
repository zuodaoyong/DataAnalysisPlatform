package com.zdy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@SpringBootApplication
@EnableWebMvc
public class LogCollectApplication {

	@RequestMapping("/test")
	public String test(){
		return "1";
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(LogCollectApplication.class, args);
	}
}
