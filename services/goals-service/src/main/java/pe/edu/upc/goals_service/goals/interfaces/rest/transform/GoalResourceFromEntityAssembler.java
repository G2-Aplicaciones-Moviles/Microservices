package pe.edu.upc.goals_service.goals.interfaces.rest.transform;

import pe.edu.upc.goals_service.goals.domain.model.aggregates.Goal;
import pe.edu.upc.goals_service.goals.interfaces.rest.resources.GoalResource;

public class GoalResourceFromEntityAssembler {
    public static GoalResource toResourceFromEntity(Goal e) {
        return new GoalResource(
                e.getUserId().getValue(),
                e.getObjective(),
                e.getTargetWeightKg(),
                e.getPace(),
                e.getDietPreset(),
                e.getProteinPct(),
                e.getCarbsPct(),
                e.getFatPct()
        );
    }
}