package ee.stivka.luka;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LukaApplication {

	public static void main(String[] args) {
		TimeZone timeZone = TimeZone.getDefault();
		System.out.println("Current Timezone: " + timeZone.getID());
		SpringApplication.run(LukaApplication.class, args);
	}
}
