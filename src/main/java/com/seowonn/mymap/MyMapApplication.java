package com.seowonn.mymap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MyMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyMapApplication.class, args);
	}

}
