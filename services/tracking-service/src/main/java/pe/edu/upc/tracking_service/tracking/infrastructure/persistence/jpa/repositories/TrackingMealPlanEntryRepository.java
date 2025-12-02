package pe.edu.upc.tracking_service.tracking.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.TrackingMealPlanEntry;

import java.util.List;

@Repository
public interface TrackingMealPlanEntryRepository extends JpaRepository<TrackingMealPlanEntry, Long> {

    List<TrackingMealPlanEntry> findAllByTracking_Id(Long trackingId);
}
