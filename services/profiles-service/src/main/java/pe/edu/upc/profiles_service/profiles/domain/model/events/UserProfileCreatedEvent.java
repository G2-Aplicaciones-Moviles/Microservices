package pe.edu.upc.profiles_service.profiles.domain.model.events;

/**
 * Event published when a user profile is created.
 * Used to trigger post-creation actions like creating tracking goal.
 */
public class UserProfileCreatedEvent {
    private final Long userId;
    private final Integer profileId;

    public UserProfileCreatedEvent(Long userId, Integer profileId) {
        this.userId = userId;
        this.profileId = profileId;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getProfileId() {
        return profileId;
    }
}

