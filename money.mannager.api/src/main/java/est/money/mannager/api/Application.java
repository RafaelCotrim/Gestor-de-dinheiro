package est.money.mannager.api;

import est.money.mannager.api.models.User;
import est.money.mannager.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@SpringBootApplication
@RestController
public class Application {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		Base64.Decoder encoder = Base64.getDecoder();
		return args -> {
			userRepository.save(new User(
					"Admin",
					"admin@gmail.com",
					encoder.decode("xEJZNyhLw7d0P2XW3veJmA==".getBytes()),
					encoder.decode("+yYGSymR1yG2yMqznOKcTA==".getBytes()),
					true)
			);

			userRepository.save(new User(
					"User",
					"user@gmail.com",
					encoder.decode("k7rhJIbaKwobNMRoftbYhA==".getBytes()),
					encoder.decode("0q1yuPp/SOwut2UMQzoQuQ==".getBytes()),
					false)
			);

		};
	}

}
