package com.midtier.bonmunch.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageSourceConfiguration {

    @Bean
    public MessageSource emtErrorMessageSource() {
        ReloadableResourceBundleMessageSource emtErrorMessageSource = new ReloadableResourceBundleMessageSource();
        emtErrorMessageSource.setBasename("classpath:/messages/validationerrors");
        return emtErrorMessageSource;
    }

    @Bean
    public MessageSource errorMessageSource() {
        ReloadableResourceBundleMessageSource errorMessageSource = new ReloadableResourceBundleMessageSource();
        errorMessageSource.setBasename("classpath:/messages/errors");
        return errorMessageSource;
    }
}
