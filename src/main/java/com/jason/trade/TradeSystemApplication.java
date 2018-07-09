package com.jason.trade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("com.jason.trade.mapper")//将项目中对应的mapper类的路径加进来就可以了
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
