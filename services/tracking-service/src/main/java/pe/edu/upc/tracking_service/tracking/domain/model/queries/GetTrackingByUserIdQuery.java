package pe.edu.upc.tracking_service.tracking.domain.model.queries;

import pe.edu.upc.tracking_service.tracking.domain.model.valueobjects.UserId;

public record GetTrackingByUserIdQuery(UserId userId) {
}
