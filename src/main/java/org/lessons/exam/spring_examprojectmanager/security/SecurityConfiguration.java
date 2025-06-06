package org.lessons.exam.spring_examprojectmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity  //tells to spring to use this config x all 'web security' requests 
@EnableMethodSecurity  //x use @PreAuthorize
public class SecurityConfiguration {
    
    @Bean
    @SuppressWarnings("removal")  //to avoid deprecation's errors or settings not perfectly right
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().and()  //!not disable() it because in layout.html in row 123 i use a csrf token!!
            .cors().and()  //Cross-Origin Resource Sharing x frontend
            .authorizeHttpRequests()
            //FINORA TUTTI POSSONO FARE TUTTO
            // .requestMatchers("/projects/create", "/projects/edit/**").hasAuthority("Admin")
            // .requestMatchers("/users/create", "/users/store", "/security/sign-in", "/css/**", "/js/**").permitAll()
            // .requestMatchers("/", "/home").authenticated()
            //.requestMatchers("/**").permitAll()  overwrite on all rules!
                .requestMatchers("/api/**").permitAll()  //disability authorization for all endpoints start with '/api/..'
                .anyRequest().permitAll()  //disability authorization for all requests
            .and()

            .formLogin()
                .loginPage("/security/sign-in")  //where custom form x log-in
                .loginProcessingUrl("/sign-in")  //endpoint post,managed by spring sec, x submit 
                .successHandler((req, res, auth) -> {  //what to do after success log-in
                    if(req.getRequestURI().equals("/api/auth/login")) {  //url react x log-in
                        res.setStatus(HttpServletResponse.SC_OK); //react receives 200OK ar result of his request
                    } else {
                        res.sendRedirect("/"); //redirect to home page
                    }
                })
                .permitAll()  //anyone can access to url "/security/sign-in"+form x log-in+any other thing in this .formLogin()    //!!!set custom form + use custom POST url (x sign-in submit)(managed auto by spring security)
            .and()

            .logout()
                .logoutUrl("/sign-out")  //endpoint to call x logout
                .logoutSuccessHandler((req, res, auth) -> {
                    if (req.getRequestURI().equals("/api/auth/logout")) {
                        res.setStatus(HttpServletResponse.SC_OK); //useful x react
                    } else {
                        res.sendRedirect("/"); //redirect to home page
                    }
                })
                //.logoutSuccessUrl("/")  //i am using logoutSuccessHandler x set custom logout url and where go after that
                .invalidateHttpSession(true)  //invalidate session x the user
                .deleteCookies("JSESSIONID")  //destroy JSESSIONID
                .permitAll()  //anyone can access to url "/sign-out"++any other thing in this .logout()
            .and()
            
            .exceptionHandling();  //abilitate custom error pages

        return http.build();
    }


    @Bean  //diventa cosi un Bean Spring, ora gestibile da Spring e facilmente iniettabile
    @SuppressWarnings("deprecation")  //to avoid deprecation's errors or settings not perfectly right
    DaoAuthenticationProvider daoAuthenticationProvider() {   //il nome del method è anche il nome del bean!
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean   
    public CustomUserDetailsService userDetailsService() {  //il nome del method è anche il nome del bean!
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
