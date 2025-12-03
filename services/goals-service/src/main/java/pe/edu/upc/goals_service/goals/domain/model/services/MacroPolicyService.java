package pe.edu.upc.goals_service.goals.domain.model.services;

import org.springframework.stereotype.Service;
import pe.edu.upc.goals_service.goals.domain.model.valueobjects.DietPreset;

@Service
public class MacroPolicyService {
    public int[] macrosFor(DietPreset preset) {
        return switch (preset) {
            case OMNIVORE       -> new int[]{30, 40, 30};
            case VEGETARIAN     -> new int[]{25, 50, 25};
            case VEGAN          -> new int[]{20, 55, 25};
            case LOW_CARB       -> new int[]{30, 20, 50};
            case HIGH_PROTEIN   -> new int[]{40, 35, 25};
            case MEDITERRANEAN  -> new int[]{25, 45, 30};
        };
    }
}