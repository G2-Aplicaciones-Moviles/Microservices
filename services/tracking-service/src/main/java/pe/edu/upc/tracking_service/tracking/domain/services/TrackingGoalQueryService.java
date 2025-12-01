package pe.edu.upc.tracking_service.tracking.domain.services;

import pe.edu.upc.center.jameoFit.tracking.domain.model.Entities.MacronutrientValues;
import pe.edu.upc.center.jameoFit.tracking.domain.model.Entities.TrackingGoal;
import pe.edu.upc.center.jameoFit.tracking.domain.model.queries.GetTargetMacronutrientsQuery;
import pe.edu.upc.center.jameoFit.tracking.domain.model.queries.GetTrackingGoalByUserIdQuery;

import java.util.Optional;

public interface TrackingGoalQueryService {
    Optional<TrackingGoal> handle(GetTrackingGoalByUserIdQuery query);
    Optional<MacronutrientValues> handle(GetTargetMacronutrientsQuery query);
}
