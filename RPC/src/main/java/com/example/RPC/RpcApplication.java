package com.example.RPC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan("com.example")
public class RpcApplication {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(RpcApplication.class);
	}
}
