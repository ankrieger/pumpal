package de.othr.sw.pumpal.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class PumpalSecurityUtilities {
    @Value("#{environment.user_password_salt}")
    //@Value("${application-config.user-password-salt}")
    //private String salt = "Super-streng-geh€1m";
    private String salt;

    @Bean
    @Scope(value = "singleton")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(15, new SecureRandom(salt.getBytes()));
    }
}
