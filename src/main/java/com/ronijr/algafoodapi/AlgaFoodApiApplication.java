package com.ronijr.algafoodapi;

import com.ronijr.algafoodapi.config.io.Base64ProtocolResolver;
import com.ronijr.algafoodapi.infrastructure.repository.CustomJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class AlgaFoodApiApplication {
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		var app = new SpringApplication(AlgaFoodApiApplication.class);
		app.addListeners(new Base64ProtocolResolver());
		app.run(args);
	}
}