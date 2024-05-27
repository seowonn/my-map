package com.seowonn.mymap;

import com.seowonn.mymap.infra.elasticSearch.repository.VisitLogSearchRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = VisitLogSearchRepository.class))
@SpringBootApplication
public class MyMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyMapApplication.class, args);
	}

}
