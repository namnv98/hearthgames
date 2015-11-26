package com.hearthlogs.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthlogs.server.game.parse.domain.CardSets;
import com.hearthlogs.server.hearthpwn.CardLinks;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.io.IOException;

@SpringBootApplication
@EnableOAuth2Client
public class HearthlogsServerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HearthlogsServerApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HearthlogsServerApplication.class, args);
    }

    @Bean
    public CardSets cardSets() throws IOException {
        return new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("AllSets.json"), CardSets.class);
    }

    @Bean
    public CardLinks cards() throws IOException {
        return new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("HearthPwn.json"), CardLinks.class);
    }
}