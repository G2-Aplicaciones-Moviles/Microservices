package pe.edu.upc.recommendations_service.recommendations.application.internal.commandservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.recommendations_service.recommendations.domain.model.aggregates.Recommendation;
import pe.edu.upc.recommendations_service.recommendations.domain.model.entities.RecommendationTemplate;
import pe.edu.upc.recommendations_service.recommendations.domain.model.valueobjects.RecommendationStatus;
import pe.edu.upc.recommendations_service.recommendations.domain.model.valueobjects.UserId;
import pe.edu.upc.recommendations_service.recommendations.domain.services.RecommendationTemplateService;
import pe.edu.upc.recommendations_service.recommendations.infrastructure.persistence.jpa.repositories.RecommendationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RecommendationAssignmentService implements pe.edu.upc.recommendations_service.recommendations.domain.services.RecommendationAssignmentService {

    @Autowired
    private RecommendationTemplateService templateService;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Override
    public List<Recommendation> autoAssignRecommendations(Long userId) {
        // 1. Obtener templates disponibles
        List<RecommendationTemplate> allTemplates = templateService.getAllTemplates();

        if (allTemplates.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Seleccionar 1 template aleatorio
        Random random = new Random(userId);
        RecommendationTemplate selectedTemplate = allTemplates.get(random.nextInt(allTemplates.size()));

        // 3. Buscar recommendations BASE (sin asignar) de ese template
        List<Recommendation> baseRecommendations = recommendationRepository
                .findByTemplateAndUserIdIsNull(selectedTemplate);

        if (baseRecommendations.isEmpty()) {
            return new ArrayList<>();
        }

        // 4. Tomar máximo 3 recommendations aleatorias
        Collections.shuffle(baseRecommendations, random);
        int assignCount = Math.min(3, baseRecommendations.size());

        // 5. CREAR COPIAS para el usuario (NO modificar las originales)
        List<Recommendation> userRecommendations = baseRecommendations.stream()
                .limit(assignCount)
                .map(base -> createCopyForUser(base, userId))
                .collect(Collectors.toList());

        return recommendationRepository.saveAll(userRecommendations);
    }

    @Override
    public Recommendation assignSingleRecommendation(Long userId, Long templateId) {
        RecommendationTemplate template = templateService.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        // Buscar una recommendation BASE disponible del template
        Recommendation baseRecommendation = recommendationRepository
                .findFirstByTemplateAndUserIdIsNull(template)
                .orElseThrow(() -> new RuntimeException("No hay recommendations disponibles para este template"));

        // CREAR COPIA para el usuario
        Recommendation userRecommendation = createCopyForUser(baseRecommendation, userId);
        return recommendationRepository.save(userRecommendation);
    }

    // MÉTODO CLAVE: Crear copia para usuario específico
    private Recommendation createCopyForUser(Recommendation base, Long userId) {
        // Opción 1: Usar el constructor que ya tienes (SIN asignar usuario)
        Recommendation copy = new Recommendation(
                base.getTemplate(),
                base.getReason(),
                base.getNotes(),
                base.getTimeOfDay(),
                base.getScore(),
                RecommendationStatus.ACTIVE
        );

        // Luego asignar el usuario manualmente
        copy.setUserId(new UserId(userId));
        copy.setAssignedAt(LocalDateTime.now());

        return copy;

        // Opción 2: Si la anterior no funciona, usar este enfoque
        /*
        Recommendation copy = new Recommendation();
        // NO usar setId() - dejarlo que Hibernate maneje el ID automáticamente
        copy.setUserId(new UserId(userId));
        copy.setTemplate(base.getTemplate());
        copy.setReason(base.getReason());
        copy.setNotes(base.getNotes());
        copy.setTimeOfDay(base.getTimeOfDay());
        copy.setScore(base.getScore());
        copy.setStatus(RecommendationStatus.ACTIVE);
        copy.setAssignedAt(LocalDateTime.now());
        return copy;
        */
    }
}