package com.hearthlogs.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthlogs.server.game.parse.domain.CardSets;
import com.hearthlogs.server.hearthpwn.CardLinks;
import org.apache.solr.client.solrj.SolrServer;
import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactory;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@SpringBootApplication
@EnableOAuth2Client
public class HearthlogsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HearthlogsServerApplication.class, args);
    }

    @Bean
    public SolrServer solrServer() throws IOException, SAXException, ParserConfigurationException {
        EmbeddedSolrServerFactory factory = new EmbeddedSolrServerFactory("C:\\solr\\example\\solr\\");
        return factory.getSolrServer();
    }

    @Bean
    public SolrOperations solrTemplate() throws ParserConfigurationException, SAXException, IOException {
        return new SolrTemplate(solrServer());
    }

    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
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