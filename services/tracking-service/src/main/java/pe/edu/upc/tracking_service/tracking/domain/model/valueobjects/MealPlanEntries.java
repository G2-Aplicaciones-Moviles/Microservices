package pe.edu.upc.tracking_service.tracking.domain.model.valueobjects;



import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;
import pe.edu.upc.tracking_service.tracking.domain.model.Entities.TrackingMealPlanEntry;

import java.util.ArrayList;
import java.util.List;

@ToString
@Embeddable
public class MealPlanEntries {

  @Getter
  @OneToMany(mappedBy = "tracking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<TrackingMealPlanEntry> mealPlanEntries = new ArrayList<>();

  public MealPlanEntries() {
    this.mealPlanEntries = new ArrayList<>();
  }

  // Todos tus métodos existentes se quedan igual
  public void addEntry(TrackingMealPlanEntry entry) {
    this.mealPlanEntries.add(entry);
  }

  public void addEntries(List<TrackingMealPlanEntry> entries) {
    this.mealPlanEntries.addAll(entries);
  }

  public boolean removeEntryById(Long entryId) {
    return mealPlanEntries.removeIf(e -> e.getId().equals(entryId));
  }
  public void clearEntries() {
    this.mealPlanEntries.clear();
  }
}
