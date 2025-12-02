package pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.upc.mealplan_service.mealplan.application.internal.outboundservices.acl.rest.resource.TrackingResource;

/**
 * Feign Client for communication with tracking-service.
 */
@FeignClient(name = "tracking-service", path = "/api/v1/tracking")
public interface TrackingIntegrationClient {

    /**
     * Retrieves tracking information by user ID from tracking-service.
     *
     * @param userId the user ID
     * @return the tracking resource
     * @throws feign.FeignException.NotFound if tracking is not found
     */
    @GetMapping("/user/{userId}")
    TrackingResource getTrackingByUserId(@PathVariable("userId") Long userId);
}
