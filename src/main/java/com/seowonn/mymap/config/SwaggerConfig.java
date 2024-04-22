package com.seowonn.mymap.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(title = "마이맵: 지역별 기록하고 싶은 나의 픽 장소",
    description = "지역별로 인상적인 나만의 장소를 기록하고 공유하는 곳입니다 :)",
    version = "1.0")
)

@Configuration
public class SwaggerConfig {

    private OpenAPI openAPI(){
      return new OpenAPI();
    }

}
