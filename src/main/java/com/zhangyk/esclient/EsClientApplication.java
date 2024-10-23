package com.zhangyk.esclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.zhangyk.esclient.repo.jpa")
@EnableElasticsearchRepositories(basePackages = "com.zhangyk.esclient.repo.es")
public class EsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsClientApplication.class, args);
    }

}
