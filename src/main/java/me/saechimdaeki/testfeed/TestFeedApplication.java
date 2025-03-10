package me.saechimdaeki.testfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TestFeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestFeedApplication.class, args);
	}

}
