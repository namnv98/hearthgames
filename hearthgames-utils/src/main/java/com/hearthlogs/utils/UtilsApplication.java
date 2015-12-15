package com.hearthlogs.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthlogs.utils.hearthpwn.CardSets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class UtilsApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UtilsApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(UtilsApplication.class, args);
    }

    @Bean
    public CardSets cardSets() throws IOException {
        return new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("AllSets.json"), CardSets.class);
    }
}
