package com.hearthlogs.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hearthlogs.web.domain.CardSets;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableWebMvc
@EnableSolrRepositories(basePackages={"com.hearthlogs.web.repository.solr"})
@EnableJpaRepositories(basePackages={"com.hearthlogs.web.repository.jpa"})
@EnableTransactionManagement
@ComponentScan("com.hearthlogs.web")
public class
WebServiceConfig {

    @Bean
    public CardSets cards() throws IOException {
        return new ObjectMapper().readValue(getClass().getClassLoader().getResourceAsStream("AllSets.json"), CardSets.class);
    }

    @Bean
    public SolrServer solrServer() {
        return new HttpSolrServer("http://127.0.0.1:9000/solr/hpt");
    }

    @Bean
    public SolrOperations solrTemplate() {
        return new SolrTemplate(solrServer());
    }

    @Bean
    public DataSource dataSource() {
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName("org.postgresql.ds.PGSimpleDataSource");
//        ds.setUrl("jdbc:postgresql://localhost/games");
//        ds.setUsername("hpt");
//        ds.setPassword("STP*,96w");
        HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(10);
        ds.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        ds.addDataSourceProperty("url", "jdbc:postgresql://127.0.0.1:9001/games");
        ds.addDataSourceProperty("user", "hpt");
        ds.addDataSourceProperty("password", "STP*,96w");
        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter hibernateJpa = new HibernateJpaVendorAdapter();
        hibernateJpa.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("com.hearthlogs.web.match");
        emf.setJpaVendorAdapter(hibernateJpa);
        emf.setJpaPropertyMap(Collections.singletonMap("javax.persistence.validation.mode", "none"));
        return emf;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager txnMgr = new JpaTransactionManager();
        txnMgr.setEntityManagerFactory(entityManagerFactory().getObject());
        return txnMgr;
    }


}