package com.chason.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(prefix = "rwe", name = "spring-session-open", havingValue = "true")
public class SpringSessionConfig {

}
