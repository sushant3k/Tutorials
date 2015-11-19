/**
 * Filename: SimpleWebSocketApplication.java
 * -----------------------------------------
 * Licensed: MIT License https://opensource.org/licenses/MIT
 */

package com.sushant;

/**
 * Spring Boot Initializer Class
 * Extends from SpringBootServletInitializer so as to enable
 * servlet based deployment. 
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SpringBootWebSecurityConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SpringBootWebSecurityConfiguration.class })
@ComponentScan(basePackages = { "com.sushant" })
public class SimpleWebSocketApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(SimpleWebSocketApplication.class, args);

    }
}
