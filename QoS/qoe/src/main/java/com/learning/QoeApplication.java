package com.learning;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SpringBootWebSecurityConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.util.FileSystemUtils;

@SpringBootApplication(exclude={SpringBootWebSecurityConfiguration.class})
@ComponentScan(basePackages={"com.learning"})
@EnableMongoRepositories(basePackages="com.learning.qoe.repositories")
@EnableJms
public class QoeApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        
        FileSystemUtils.deleteRecursively(new File("activemq-data"));
        SpringApplication.run(QoeApplication.class, args);
    }
    
    
    
}
