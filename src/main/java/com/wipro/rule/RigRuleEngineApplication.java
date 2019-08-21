package com.wipro.rule;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;




@SpringBootApplication
@EnableScheduling
public class RigRuleEngineApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(RigRuleEngineApplication.class, args);
		new ServiceMongo().callTemplate();
	}

	
}
