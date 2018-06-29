package com.jason.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradeSystemApplication //extends SpringBootServletInitializer
{

	/*@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(TradeSystemApplication.class);
	}*/

	public static void main(String[] args) {
		SpringApplication.run(TradeSystemApplication.class, args);
	}
}
