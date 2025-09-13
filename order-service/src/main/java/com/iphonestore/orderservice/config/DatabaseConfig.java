package main.java.com.iphonestore.orderservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.iphonestore.orderservice.repository")
@EnableTransactionManagement
public class DatabaseConfig {
}