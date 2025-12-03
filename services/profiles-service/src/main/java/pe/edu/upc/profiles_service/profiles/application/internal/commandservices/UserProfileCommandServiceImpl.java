package pe.edu.upc.profiles_service.profiles.application.internal.commandservices;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.ExternalUserService;
import pe.edu.upc.profiles_service.profiles.application.internal.outboundservices.acl.ExternalTrackingService;
import pe.edu.upc.profiles_service.profiles.domain.model.aggregates.UserProfile;
import pe.edu.upc.profiles_service.profiles.domain.model.commands.CreateUserProfileCommand;
import pe.edu.upc.profiles_service.profiles.domain.model.commands.DeleteUserProfileCommand;
import pe.edu.upc.profiles_service.profiles.domain.model.commands.UpdateUserProfileCommand;
import pe.edu.upc.profiles_service.profiles.domain.model.events.UserProfileCreatedEvent;
import pe.edu.upc.profiles_service.profiles.domain.services.UserProfileCommandService;
import pe.edu.upc.profiles_service.profiles.infrastructure.persistence.jpa.repositories.*;

import java.util.Optional;

@Service
public class UserProfileCommandServiceImpl implements UserProfileCommandService {

    private final UserProfileRepository userProfileRepository;
    private final ActivityLevelRepository activityLevelRepository;
    private final ObjectiveRepository objectiveRepository;
    private final AllergyRepository allergyRepository;
    private final ExternalUserService externalUserService;
    private final ExternalTrackingService externalTrackingService;
    private final ApplicationEventPublisher eventPublisher;

    public UserProfileCommandServiceImpl(UserProfileRepository userProfileRepository,
                                         ActivityLevelRepository activityLevelRepository,
                                         ObjectiveRepository objectiveRepository,
                                         AllergyRepository allergyRepository,
                                         ExternalUserService externalUserService,
                                         ExternalTrackingService externalTrackingService,
                                         ApplicationEventPublisher eventPublisher) {
        this.userProfileRepository = userProfileRepository;
        this.activityLevelRepository = activityLevelRepository;
        this.objectiveRepository = objectiveRepository;
        this.allergyRepository = allergyRepository;
        this.externalUserService = externalUserService;
        this.externalTrackingService = externalTrackingService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public int handle(CreateUserProfileCommand command) {
        if (command.userId() == null) throw new IllegalArgumentException("userId is required");

        if (!externalUserService.userExists(command.userId()))
            throw new IllegalArgumentException("User with ID " + command.userId() + " does not exist in IAM");

        if (userProfileRepository.existsByUserId(command.userId()))
            throw new IllegalArgumentException("UserProfile already exists for userId " + command.userId());

        var activityLevel = activityLevelRepository.findById(command.activityLevelId())
                .orElseThrow(() -> new IllegalArgumentException("Activity level not found"));

        var objective = objectiveRepository.findById(command.objectiveId())
                .orElseThrow(() -> new IllegalArgumentException("Objective not found"));

        var userProfile = new UserProfile(command, activityLevel, objective);

        if (command.allergyIds() != null && !command.allergyIds().isEmpty()) {
            var allergies = allergyRepository.findAllById(command.allergyIds());
            allergies.forEach(userProfile::addAllergy);
        }

        var savedProfile = userProfileRepository.save(userProfile);

        System.out.println(">>> [Profiles] UserProfile created for userId: " + command.userId() + ", profileId: " + savedProfile.getId());

        eventPublisher.publishEvent(new UserProfileCreatedEvent(command.userId(), savedProfile.getId().intValue()));
        System.out.println(">>> [Profiles] UserProfileCreatedEvent published for userId: " + command.userId());

        return savedProfile.getId().intValue();
    }


    @Override
    public Optional<UserProfile> handle(UpdateUserProfileCommand command) {
        var profile = userProfileRepository.findById(command.userProfileId())
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        var activityLevel = activityLevelRepository.findById(command.activityLevelId())
                .orElseThrow(() -> new IllegalArgumentException("Activity level not found"));

        var objective = objectiveRepository.findById(command.objectiveId())
                .orElseThrow(() -> new IllegalArgumentException("Objective not found"));

        profile.updateProfile(command.gender(), command.height(), command.weight(),
                activityLevel, objective, command.userScore(), command.birthDate());

        var updatedProfile = userProfileRepository.save(profile);
        return Optional.of(updatedProfile);
    }

    @Override
    public void handle(DeleteUserProfileCommand command) {
        if (!userProfileRepository.existsById(command.userProfileId()))
            throw new IllegalArgumentException("Profile not found");
        userProfileRepository.deleteById(command.userProfileId());
    }
}
