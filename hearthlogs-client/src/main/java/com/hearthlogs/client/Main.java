package com.hearthlogs.client;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import com.hearthlogs.client.log.LogConfigAnalyzerConfigurer;
import com.hearthlogs.client.log.LogManager;
import com.hearthlogs.client.ui.SystemTraySupportedJFrame;
import com.hearthlogs.client.ui.TextAreaOutputStream;
import com.hearthlogs.client.config.ApplicationConfiguration;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws Exception {

        JFrame frame = new SystemTraySupportedJFrame("Hearth Logs");

        JTextArea consoleTextArea = new JTextArea(10, 60);

        JScrollPane scrollPane = new JScrollPane(consoleTextArea);
        scrollPane.setBounds(0, 0, 710, 220);
        frame.add(scrollPane);
        frame.setSize(735, 220);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        initLogger(consoleTextArea);

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        LogConfigAnalyzerConfigurer logConfigAnalyzerConfigurer = context.getBean(LogConfigAnalyzerConfigurer.class);
        logConfigAnalyzerConfigurer.configure();

        LogManager logManager = context.getBean(LogManager.class);
        logManager.start();


    }

    private static void initLogger(JTextArea consoleTextArea) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{HH:mm:ss} %level - %msg%n");
        encoder.start();

        OutputStreamAppender<ILoggingEvent> appender= new OutputStreamAppender<>();
        appender.setName( "OutputStream Appender" );
        appender.setContext(context);
        appender.setEncoder(encoder);
        appender.setOutputStream(new TextAreaOutputStream(consoleTextArea));

        appender.start();

        Logger log = context.getLogger(Logger.ROOT_LOGGER_NAME);
        log.setLevel(Level.ERROR);
        log.setAdditive(false);
        log.addAppender(appender);

        log = context.getLogger("com.hearthlogs.client");
        log.setLevel(Level.INFO);
        log.setAdditive(false);
        log.addAppender(appender);
    }
}