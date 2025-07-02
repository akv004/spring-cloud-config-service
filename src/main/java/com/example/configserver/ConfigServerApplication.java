package com.example.configserver;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    private static final Logger log = LoggerFactory.getLogger(ConfigServerApplication.class);

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logStartupDetails() {
        log.debug("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));
        log.debug("Datasource URL: {}", env.getProperty("spring.datasource.url"));
        log.debug("Key Vault endpoint: {}", env.getProperty("AZURE_KEY_VAULT_ENDPOINT"));
    }
}
