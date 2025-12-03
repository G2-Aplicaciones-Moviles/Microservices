package pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects;

public record UserProfileId(int userProfileId) {
    public UserProfileId {
        if (userProfileId < 0) {
            throw new IllegalArgumentException("Profile profile ID cannot be negative");
        }
    }

    public UserProfileId() {
        this(0);
    }
}
