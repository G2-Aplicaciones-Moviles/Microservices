package pe.edu.upc.profiles_service.profiles.application.internal.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.ExternalTrackingService;
import pe.edu.upc.profiles_service.profiles.domain.model.events.UserProfileCreatedEvent;

/**
 * Event handler for UserProfileCreated events.
 * Creates tracking goal after profile creation transaction commits.
 */
@Component
public class UserProfileCreatedEventHandler {

    private final ExternalTrackingService externalTrackingService;

    public UserProfileCreatedEventHandler(ExternalTrackingService externalTrackingService) {
        this.externalTrackingService = externalTrackingService;
    }

    /**
     * Handles UserProfileCreatedEvent after transaction commits.
     * Creates tracking goal asynchronously to avoid blocking profile creation.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleUserProfileCreated(UserProfileCreatedEvent event) {
        System.out.println(">>> [Event Handler] UserProfile created event received for userId: " + event.getUserId());

        try {
            Thread.sleep(500);

            System.out.println(">>> [Event Handler] Attempting to create TrackingGoal for userId: " + event.getUserId());
            Long trackingGoalId = externalTrackingService.createTrackingGoalForUser(event.getUserId());

            if (trackingGoalId != null) {
                System.out.println(">>> [Event Handler] SUCCESS: TrackingGoal created with ID: " + trackingGoalId + " for userId: " + event.getUserId());
            } else {
                System.err.println(">>> [Event Handler] WARNING: TrackingGoal creation returned null for userId: " + event.getUserId());
            }
        } catch (InterruptedException e) {
            System.err.println(">>> [Event Handler] Sleep interrupted for userId: " + event.getUserId());
            Thread.currentThread().interrupt();
        } catch (feign.FeignException e) {
            System.err.println(">>> [Event Handler] ERROR calling Tracking Service for userId " + event.getUserId() +
                             ": [" + e.status() + "] " + e.getMessage());
            System.err.println(">>> [Event Handler] Response body: " + e.contentUTF8());
        } catch (Exception e) {
            System.err.println(">>> [Event Handler] UNEXPECTED ERROR creating TrackingGoal for userId " + event.getUserId() +
                             ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}

