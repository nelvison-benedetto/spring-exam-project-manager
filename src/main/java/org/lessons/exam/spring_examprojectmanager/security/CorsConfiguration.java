package org.lessons.exam.spring_examprojectmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  //x set configurations x spring
public class CorsConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {  //bean type WebMvcConfigurer x custom behavior of MVC(Model-View-Controller)
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {  //add nw rules(override if same name) x CORS(Cross-Origin Resource Sharing)
                registry.addMapping("/**")  //apply to all endpoints start with '/..'
                        .allowedOrigins("http://localhost:5173")  //allowed origin (localhost front-end)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);  //allow cookie/tokens
            }
        };
    }
}
