package pe.edu.upc.recommendations_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.actuate.autoconfigure.audit.AuditEventsEndpointAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {AuditEventsEndpointAutoConfiguration.class})
@EnableJpaAuditing
@EnableFeignClients(basePackages = "pe.edu.upc.recommendations_service.recommendations.application.internal.outboundservices.acl.rest")
public class RecommendationsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendationsServiceApplication.class, args);
    }

}
