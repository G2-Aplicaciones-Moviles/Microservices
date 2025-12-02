package pe.edu.upc.recipes_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.actuate.autoconfigure.audit.AuditEventsEndpointAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {AuditEventsEndpointAutoConfiguration.class})
@EnableFeignClients
public class RecipesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipesServiceApplication.class, args);
	}

}
