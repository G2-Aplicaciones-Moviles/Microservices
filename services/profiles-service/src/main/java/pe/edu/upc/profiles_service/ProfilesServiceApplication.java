package pe.edu.upc.profiles_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.actuate.autoconfigure.audit.AuditEventsEndpointAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {AuditEventsEndpointAutoConfiguration.class})
@EnableJpaAuditing
@EnableFeignClients(basePackages = "pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest")
public class ProfilesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfilesServiceApplication.class, args);
	}

}
