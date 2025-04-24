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

@Configuration
@EnableWebSecurity  //tells to spring to use this config x all 'web security' requests 
@EnableMethodSecurity  //x use @PreAuthorize
public class SecurityConfiguration {
    
    @Bean
    @SuppressWarnings("removal")  //to avoid deprecation's errors or settings not perfectly right
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()

            .requestMatchers("/projects/create", "/projects/edit/**").hasAuthority("Admin")
            .requestMatchers("/users/create", "/users/store", "/security/sign-in", "/css/**", "/js/**").permitAll()
            .requestMatchers("/", "/home").authenticated()
            //.requestMatchers("/**").permitAll()  overwrite on all rules!
            .anyRequest().permitAll()

            .and().formLogin().loginPage("/security/sign-in").loginProcessingUrl("/sign-in").permitAll()  //!!!set custom form + use custom POST url (x sign-in submit)(managed auto by spring security)
            .and().logout()
                    .logoutUrl("/sign-out")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            .and().exceptionHandling();


        return http.build();
    }

    @Bean
    @SuppressWarnings("deprecation")  //to avoid deprecation's errors or settings not perfectly right
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean   
    public CustomUserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder  passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
