
package com.example.logichain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LogichainApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogichainApplication.class, args);
    }
}
