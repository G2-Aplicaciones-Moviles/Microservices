package pe.edu.upc.goals_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.actuate.autoconfigure.audit.AuditEventsEndpointAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {AuditEventsEndpointAutoConfiguration.class})
@EnableJpaAuditing
@EnableFeignClients
public class GoalsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoalsServiceApplication.class, args);
	}

}
