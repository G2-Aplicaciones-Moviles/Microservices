package pe.edu.upc.mealplan_service.mealplan.application.internal.eventhandlers;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.SeedMealPlanTypesCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanTypeCommandService;

import java.sql.Timestamp;

@Service
public class MealPlanReadyEventHandler {
    private final MealPlanTypeCommandService mealPlanTypeCommandService;
    public MealPlanReadyEventHandler(MealPlanTypeCommandService mealPlanTypeCommandService) {
        this.mealPlanTypeCommandService = mealPlanTypeCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event){
        var applicationName = event.getApplicationContext().getId();

        mealPlanTypeCommandService.handle(new SeedMealPlanTypesCommand());
    }

    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
