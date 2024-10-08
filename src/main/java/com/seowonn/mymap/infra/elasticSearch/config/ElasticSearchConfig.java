package com.seowonn.mymap.infra.elasticSearch.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

  @Value("${spring.data.elasticsearch.url}")
  private String elasticUrl;

  @Override
  public RestHighLevelClient elasticsearchClient() {

    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo(elasticUrl)
        .build();
    return RestClients.create(clientConfiguration).rest();
  }
}
