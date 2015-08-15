package com.hearthlogs.client.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Configuration
@ComponentScan("com.hearthlogs.client")
@PropertySource("classpath:hearthlogs-client.properties")
public class ApplicationConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

    @Autowired
    Environment env;

    @Bean
    public ApplicationProperties properties() {
        ApplicationProperties properties = new ApplicationProperties();
        properties.setUploadUrl(env.getProperty("upload.url"));

        String name = env.getProperty("os.name");
        String userHome = env.getProperty("user.home");
        if (name.toLowerCase().startsWith("windows")) {
            File logConfigFile = new File(userHome + "\\AppData\\Local\\Blizzard\\Hearthstone\\log.config");
            properties.setLogConfigFile(logConfigFile);
            logger.info("log config: " + properties.getLogConfigFile().getAbsolutePath());

            String root = userHome.substring(0, userHome.indexOf(File.separator));
            File logFile = new File(root+"\\Program Files (x86)\\Hearthstone\\Hearthstone_Data\\output_log.txt");
            properties.setLogFile(logFile);
            logger.info("log: " + properties.getLogFile());

        } else if (name.toLowerCase().startsWith("mac")) {
            File logConfigFile = new File(userHome + "/Library/Preferences/Blizzard/Hearthstone/log.config");
            properties.setLogConfigFile(logConfigFile);
            logger.info("log.config: " + properties.getLogConfigFile().getAbsolutePath());

            File logFile = new File(userHome+"/Library/Logs/Unity/Player.log");
            properties.setLogFile(logFile);
            logger.info("log: " + properties.getLogFile());
        }
        return properties;
    }

    @Bean
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enableDefaultTyping();
        return mapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rest = new RestTemplate();
        rest.getMessageConverters().add(0, mappingJacksonHttpMessageConverter());
        return rest;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper());
        return converter;
    }
}