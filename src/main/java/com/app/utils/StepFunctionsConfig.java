package com.app.utils;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sfn.SfnClient;
import org.springframework.context.annotation.Bean;

@Configuration
public class StepFunctionsConfig {

    @Bean
    public SfnClient sfnClient() {
        return SfnClient.builder()
                .region(Region.SA_EAST_1)
                .build();
    }
}