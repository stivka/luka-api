package ee.stivka.luka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application.
 *
 * This class defines the {@link SecurityFilterChain} which:
 * 1. Categorizes endpoints into Public and Private.
 * 2. Disables CSRF (common for stateless APIs or specific frontend requirements).
 * 3. Enables Basic Authentication for administrative access (e.g., via Swagger).
 *
 * Security is enforced at two levels:
 * - Filter Chain level: URLs are matched and required to be authenticated or permitted.
 * - Method level: {@code @EnableMethodSecurity} allows using {@code @PreAuthorize} on controller methods
 *   as an additional layer of protection.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // This is the key that unlocks @PreAuthorize
public class SecurityConfig {

  /**
   * Public SSE stream must not be affected by HTTP Basic challenges.
   * If the browser has cached/invalid basic-auth creds for localhost:8080,
   * requests that include an Authorization header can end up as 401 even if the URL is permitAll.
   * This dedicated chain disables httpBasic entirely for the stream.
   */
  @Bean
  @Order(1)
  public SecurityFilterChain sseSecurityFilterChain(HttpSecurity http) throws Exception {
    http
      .securityMatcher("/api/stream", "/api/stream/**")
      .cors(Customizer.withDefaults())
      .csrf(AbstractHttpConfigurer::disable)
      .logout(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
      .httpBasic(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .cors(Customizer.withDefaults())
      .csrf(csrf -> csrf.disable())
      .logout(logout -> logout.disable()) // Disable default logout since we are stateless
      .authorizeHttpRequests(auth -> auth
        // 1. PUBLIC Endpoints (Accessible by anyone)
        .requestMatchers(
          "/swagger-ui/**",
          "/swagger-ui.html",
          "/v3/api-docs/**",
          "/webjars/**",
          "/hello",
          "/api/visitor-count",
          "/api/guestbook/**",
          "/api/users/**"
        ).permitAll()

        // 2. PRIVATE Endpoints (Admin/Authenticated only)
        // Transmission endpoints are explicitly secured here
        .requestMatchers("/api/transmission/**").authenticated()

        // 3. Safety Net
        .anyRequest().authenticated()
      )
      // httpBasic enables "Basic Authentication" using an Authorization header:
      // "Authorization: Basic <base64(username:password)>"
      // This is used by Swagger and other clients to authenticate admin requests.
      .httpBasic(Customizer.withDefaults());

    return http.build();
  }
}
