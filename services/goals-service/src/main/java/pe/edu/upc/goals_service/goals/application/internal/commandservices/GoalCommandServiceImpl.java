package pe.edu.upc.goals_service.goals.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.upc.goals_service.goals.domain.model.aggregates.Goal;
import pe.edu.upc.goals_service.goals.domain.model.commands.UpdateDietTypeCommand;
import pe.edu.upc.goals_service.goals.domain.model.commands.UpdateGoalCaloriesCommand;
import pe.edu.upc.goals_service.goals.domain.model.services.MacroPolicyService;
import pe.edu.upc.goals_service.goals.domain.model.valueobjects.DietPreset;
import pe.edu.upc.goals_service.goals.domain.model.valueobjects.UserId;
import pe.edu.upc.goals_service.goals.domain.services.GoalCommandService;
import pe.edu.upc.goals_service.goals.infrastructure.persistence.jpa.repositories.GoalRepository;

@Service
@Transactional
public class GoalCommandServiceImpl implements GoalCommandService {

    private final GoalRepository repository;
    private final MacroPolicyService macroPolicy;

    public GoalCommandServiceImpl(GoalRepository repository, MacroPolicyService macroPolicy) {
        this.repository = repository;
        this.macroPolicy = macroPolicy;
    }

    @Override
    public Goal handle(UpdateGoalCaloriesCommand command) {
        if (command.userId() == null || command.userId() <= 0) throw new IllegalArgumentException("Invalid userId");
        if (command.targetWeightKg() == null || command.targetWeightKg() <= 0)
            throw new IllegalArgumentException("Invalid targetWeightKg");

        var goal = repository.findByUserId_Value(command.userId())
                .orElseGet(() -> new Goal(new UserId(command.userId()),
                        command.objective(), command.targetWeightKg(), command.pace(), DietPreset.OMNIVORE));

        goal.updateGoalCalories(command.objective(), command.targetWeightKg(), command.pace());
        // recalcular macros sugeridos del preset actual por si hace falta mostrar
        int[] m = macroPolicy.macrosFor(goal.getDietPreset());
        goal.updateDietPreset(goal.getDietPreset(), m[0], m[1], m[2]);
        return repository.save(goal);
    }

    @Override
    public Goal handle(UpdateDietTypeCommand command) {
        if (command.userId() == null || command.userId() <= 0) throw new IllegalArgumentException("Invalid userId");

        var goal = repository.findByUserId_Value(command.userId())
                .orElseThrow(() -> new IllegalStateException("TrackingGoal not found for userId=" + command.userId()));

        int[] m = macroPolicy.macrosFor(command.preset());
        goal.updateDietPreset(command.preset(), m[0], m[1], m[2]);
        return repository.save(goal);
    }
}
