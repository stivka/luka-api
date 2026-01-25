package ee.stivka.luka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * General Web MVC configuration for the application.
 * Currently handles CORS settings to allow the frontend to interact with the API.
 * This class can be extended with other WebMvcConfigurer methods (e.g., interceptors, resource handlers) in the future.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
      .allowedOrigins("http://localhost:3000", "https://haveyouseenluka.com", "https://www.haveyouseenluka.com")
      .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
      .allowedHeaders("*")
      .allowCredentials(true);
  }
}
