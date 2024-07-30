package ee.stivka.luka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LukaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LukaApplication.class, args);
    }
}
