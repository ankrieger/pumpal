package de.othr.sw.pumpal.config;


import de.othr.sw.pumpal.PumpalApplication;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class LoggingConfig {

    //logger for service and controller operations
    @Bean(name="customApplicationLogger")
    @Scope("prototype")
    @Primary
//    @Qualifier("applicationLogger")
    public Logger createLogger(InjectionPoint injectionPoint) {
                                            //injectionPoint.getMethodParameter().getContainingClass() ausprobieren
        Class<?> loggerClass = injectionPoint.getMember().getDeclaringClass();
        Logger logger = LoggerFactory.getLogger(loggerClass);
        return logger;
    }

    //logger for REST api
    @Bean(name="customRestLogger")
    @Scope("prototype")
//    @Qualifier("customRestLogger")
    public Logger createLogger() {
        return LoggerFactory.getLogger("RestLogger");
    }

}
