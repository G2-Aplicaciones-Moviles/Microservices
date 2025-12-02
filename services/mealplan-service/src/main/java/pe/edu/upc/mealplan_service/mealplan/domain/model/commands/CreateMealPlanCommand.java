package pe.edu.upc.mealplan_service.mealplan.domain.model.commands;

import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanMacros;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.UserProfileId;

import java.util.List;
import java.util.Objects;

public record CreateMealPlanCommand(
        String name,
        String description,
        MealPlanMacros macros,
        UserProfileId profileId,
        String category,
        Boolean isCurrent,
        List<String> tags,
        Integer createdByNutritionistId
) {

    // build from entity
    public static Builder builderFrom(MealPlan mp) {
        Builder b = new Builder();
        b.name = mp.getName();
        b.description = mp.getDescription();
        b.macros = mp.getMacros();
        b.profileId = mp.getProfileId();
        b.category = mp.getCategory();
        b.isCurrent = mp.getIsCurrent();
        b.tags = mp.getTags() != null ?
                mp.getTags().getMealPlanTags().stream().map(t -> t.getTag()).toList()
                : List.of();

        b.createdByNutritionistId = mp.getCreatedByNutritionistId();
        return b;
    }

    public static class Builder {
        private String name;
        private String description;
        private MealPlanMacros macros;
        private UserProfileId profileId;
        private String category;
        private Boolean isCurrent;
        private List<String> tags;
        private Integer createdByNutritionistId;

        public Builder name(String name) { this.name = name; return this;}
        public Builder description(String d) { this.description = d; return this;}
        public Builder macros(MealPlanMacros m) { this.macros = m; return this;}
        public Builder profileId(Integer userProfileId) {
            this.profileId = userProfileId == null ? null : new UserProfileId(userProfileId);
            return this;
        }
        public Builder profileId(UserProfileId p) { this.profileId = p; return this;}
        public Builder category(String c) { this.category = c; return this;}
        public Builder isCurrent(Boolean cur) { this.isCurrent = cur; return this;}
        public Builder tags(List<String> t) { this.tags = t; return this;}

        /** NUEVO: acepta Long */
        public Builder createdByNutritionistId(Long id) {
            this.createdByNutritionistId = (id == null ? null : id.intValue());
            return this;
        }

        public Builder createdByNutritionistId(Integer id) {
            this.createdByNutritionistId = id;
            return this;
        }

        public CreateMealPlanCommand build() {
            return new CreateMealPlanCommand(
                    Objects.requireNonNullElse(name, ""),
                    Objects.requireNonNullElse(description, ""),
                    Objects.requireNonNullElse(macros, new MealPlanMacros(0.0,0.0,0.0,0.0)),
                    profileId,
                    Objects.requireNonNullElse(category, ""),
                    Objects.requireNonNullElse(isCurrent, false),
                    Objects.requireNonNullElse(tags, List.of()),
                    createdByNutritionistId
            );
        }
    }
}
