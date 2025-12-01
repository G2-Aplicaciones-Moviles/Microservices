package pe.edu.upc.profiles_service.profiles.domain.model.events;

import lombok.Getter;

@Getter
public class UserProfileCreatedEvent extends DomainEvent {

    private final Long userProfileId;
    private final Long userId;

    public UserProfileCreatedEvent(Number userProfileId, Long userId) {
        super("profiles.userprofile.created");
        this.userProfileId = userProfileId.longValue();
        this.userId = userId;
    }
}
