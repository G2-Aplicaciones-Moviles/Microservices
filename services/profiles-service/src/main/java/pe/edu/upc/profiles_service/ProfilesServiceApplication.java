package pe.edu.upc.profiles_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest.IamIntegrationClient;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(clients = IamIntegrationClient.class)
public class ProfilesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfilesServiceApplication.class, args);
	}

}
