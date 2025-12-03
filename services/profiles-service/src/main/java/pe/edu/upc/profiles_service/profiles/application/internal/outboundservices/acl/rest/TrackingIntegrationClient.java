package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Feign Client for communication with tracking-service.
 */
@FeignClient(name = "tracking-service", path = "/api/v1/tracking-goals")
public interface TrackingIntegrationClient {

    /**
     * Creates a tracking goal from a user profile.
     *
     * @param userId the ID of the user
     * @return the ID of the created tracking goal
     */
    @PostMapping("/from-profile/{userId}")
    Long createTrackingGoalFromProfile(@PathVariable("userId") Long userId);
}

