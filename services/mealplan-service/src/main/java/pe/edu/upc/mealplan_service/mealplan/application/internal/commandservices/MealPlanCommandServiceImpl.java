package pe.edu.upc.mealplan_service.mealplan.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.mealplan_service.mealplan.domain.model.aggregates.MealPlan;
import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.CreateMealPlanCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.DeleteMealPlanCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.commands.UpdateMealPlanCommand;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanEntry;
import pe.edu.upc.mealplan_service.mealplan.domain.model.entities.MealPlanTag;
import pe.edu.upc.mealplan_service.mealplan.domain.model.valueobjects.MealPlanMacros;
import pe.edu.upc.mealplan_service.mealplan.domain.services.MealPlanCommandService;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanRepository;
import pe.edu.upc.mealplan_service.mealplan.infrastructure.persistence.jpa.repositories.MealPlanTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MealPlanCommandServiceImpl implements MealPlanCommandService {
    private final MealPlanRepository mealPlanRepository;
    private final MealPlanTypeRepository mealPlanTypeRepository;

    public MealPlanCommandServiceImpl(MealPlanRepository mealPlanRepository, MealPlanTypeRepository mealPlanTypeRepository) {
        this.mealPlanRepository = mealPlanRepository;
        this.mealPlanTypeRepository = mealPlanTypeRepository;
    }

    @Override
    public Optional<MealPlan> handle(CreateMealPlanCommand command) {
        var mealPlan = new MealPlan(command);

        List<MealPlanTag> tagEntities = command.tags().stream()
                .map(tagStr -> {
                    var tag = new MealPlanTag(tagStr);
                    tag.setMealPlan(mealPlan);
                    return tag;
                }).toList();

        // add through VO
        mealPlan.getTags().addItems(tagEntities);

        try{
            this.mealPlanRepository.save(mealPlan);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving meal plan: " + e.getMessage());
        }
        return mealPlan.getId() > 0 ? Optional.of(mealPlan) : Optional.empty();
    }

    @Override
    public void handle(DeleteMealPlanCommand command) {
        if(command == null || command.mealPlanId() <= 0) {
            throw new IllegalArgumentException("MealPlan ID not found");
        }
        var optionalMealPlan = this.mealPlanRepository.findById(command.mealPlanId());
        this.mealPlanRepository.deleteById(optionalMealPlan.get().getId());

    }

    @Override
    public Optional<MealPlan> handle(UpdateMealPlanCommand command) {
        var mealPlanOptional = mealPlanRepository.findById(command.id());
        if (mealPlanOptional.isEmpty()) return Optional.empty();

        var mealPlan = mealPlanOptional.get();

        // Update meal plan details
        mealPlan.setName(command.name());
        mealPlan.setDescription(command.description());
        mealPlan.setMacros(new MealPlanMacros(
                command.macros().getCalories(), command.macros().getCarbs(), command.macros().getProteins(), command.macros().getFats()
        ));
        mealPlan.setCategory(command.category());
        mealPlan.setIsCurrent(command.isCurrent());

        //Update entries
        List<MealPlanEntry> newEntries = new ArrayList<>();
        for (var entryCommand : command.entries()) {
            var mealPlanType = mealPlanTypeRepository.findById(entryCommand.mealPlanTypeId())
                    .orElseThrow(() -> new RuntimeException("MealPlanType not found"));

            var entry = new MealPlanEntry(
                    entryCommand.recipeId(),
                    mealPlanType,
                    entryCommand.day(),
                    mealPlan
            );
            newEntries.add(entry);
        }
        mealPlan.getEntries().replaceWith(newEntries);

        // Update tags
        List<MealPlanTag> newTags = command.tags().stream()
                .map(tagStr -> new MealPlanTag(tagStr, mealPlan)) // crea tags
                .toList();
        mealPlan.getTags().replaceWith(newTags);

        //persist changes
        var updatedMealPlan = mealPlanRepository.save(mealPlan);

        return Optional.of(updatedMealPlan);
    }
}
