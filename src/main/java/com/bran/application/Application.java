package com.bran.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.bran.controllers.service.StreamMerger;
import com.bran.controllers.service.StreamMergerImpl;
import com.bran.stream.datasource.PelotonDataSource;
import com.bran.stream.datasource.StreamDataSource;

@SpringBootApplication
@ComponentScan("com.bran.controllers")
public class Application {
	
	@Bean
	public StreamMerger streamMerger(){
		return new StreamMergerImpl(streamDataSource());
	}
	
	@Bean
	public StreamDataSource<Integer> streamDataSource(){
		return new PelotonDataSource();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
