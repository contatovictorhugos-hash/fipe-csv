package br.com.fipe_csv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FipeCsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(FipeCsvApplication.class, args);
	}

}
