package com.ljk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//@ComponentScan(basePackages="com.apache.service")
//@MapperScan(basePackages= {"com.ljk.pay.mapper"})
//@ComponentScan(basePackageClasses = PayController.class)

public class KakaoPayApp extends SpringBootServletInitializer{
	public static void main(String[] args) {
		SpringApplication.run(KakaoPayApp.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(KakaoPayApp.class);
	}
}
