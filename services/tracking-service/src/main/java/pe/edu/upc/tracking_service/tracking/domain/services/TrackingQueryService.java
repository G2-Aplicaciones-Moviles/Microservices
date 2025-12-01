package pe.edu.upc.tracking_service.tracking.domain.services;

import pe.edu.upc.center.jameoFit.tracking.domain.model.Entities.MacronutrientValues;
import pe.edu.upc.center.jameoFit.tracking.domain.model.Entities.TrackingMealPlanEntry;
import pe.edu.upc.center.jameoFit.tracking.domain.model.aggregates.Tracking;
import pe.edu.upc.center.jameoFit.tracking.domain.model.queries.GetAllMealsQuery;
import pe.edu.upc.center.jameoFit.tracking.domain.model.queries.GetConsumedMacrosQuery;
import pe.edu.upc.center.jameoFit.tracking.domain.model.queries.GetTrackingByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface TrackingQueryService {
    List<TrackingMealPlanEntry> handle(GetAllMealsQuery query);
    Optional<Tracking> handle(GetTrackingByUserIdQuery query);
    Optional<MacronutrientValues> handle(GetConsumedMacrosQuery query);
}
