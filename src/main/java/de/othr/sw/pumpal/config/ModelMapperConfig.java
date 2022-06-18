package de.othr.sw.pumpal.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ModelMapperConfig {

    @Bean
    @Scope(value = "singleton")
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
