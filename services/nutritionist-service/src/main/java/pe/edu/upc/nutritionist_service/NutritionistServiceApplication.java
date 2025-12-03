package pe.edu.upc.nutritionist_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.actuate.autoconfigure.audit.AuditEventsEndpointAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {AuditEventsEndpointAutoConfiguration.class})
@EnableJpaAuditing
@EnableFeignClients(basePackages = "pe.edu.upc.nutritionist_service.nutritionist.application.internal.outboundservices.acl.rest")
public class NutritionistServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NutritionistServiceApplication.class, args);
    }

}
