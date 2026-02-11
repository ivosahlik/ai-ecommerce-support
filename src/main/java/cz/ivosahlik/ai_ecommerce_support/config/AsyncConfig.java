package cz.ivosahlik.ai_ecommerce_support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// @EnableAsync
@Configuration
public class AsyncConfig {

    @Bean
    public Executor asyncExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

/*    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }*/
}