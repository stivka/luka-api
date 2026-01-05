package ee.stivka.luka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for easier API testing
            .authorizeHttpRequests(auth -> auth
                // Secure Swagger endpoints
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").authenticated()
                // Everything else (the actual site) is public
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults()); // Use basic browser popup for login
        
        return http.build();
    }
}
