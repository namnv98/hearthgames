package com.hearthgames.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthgames.server.game.parse.domain.CardCollection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.io.IOException;

@SpringBootApplication
@EnableOAuth2Client
public class HearthGamesServerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HearthGamesServerApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HearthGamesServerApplication.class, args);
    }

    @Bean
    public CardCollection cardSets() throws IOException {
        return new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("AllSets.json"), CardCollection.class);
    }
}