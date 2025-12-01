package pe.edu.upc.mealplan_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(basePackages = "pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest")
public class MealplanServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealplanServiceApplication.class, args);
    }

}
