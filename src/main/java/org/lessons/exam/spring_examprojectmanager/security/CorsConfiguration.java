package org.lessons.exam.spring_examprojectmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  //bc contains @beans of configuration
public class CorsConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {  //bean type WebMvcConfigurer(interface) x custom behavior of MVC(Model-View-Controller)
        return new WebMvcConfigurer() {  //anonymous class inline that implements WebMvcConfigurer
            @Override  //ovverride method della classe anonima
            public void addCorsMappings(CorsRegistry registry) {  //add new rules(w override) x CORS(Cross-Origin Resource Sharing)
                 //spring chiama this durante la fase di configurazione, e poi crea CorsRegistry e lo pass qui come param
                registry.addMapping("/**")  //apply to all endpoints start with '/..'
                        .allowedOrigins("http://localhost:5173")  //allowed origin (localhost front-end)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  //option method: se vuoi fare methodi no get/post, browser invia Option x sapere se si puo fare
                        .allowCredentials(true);  //allow cookie/tokens
            }  //.addMApping return type CorsRegistration that contains allowedOrigins allowedMethods allowCredentials ect
        };
    }
}
