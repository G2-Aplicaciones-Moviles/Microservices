package pe.edu.upc.recommendations_service.recommendations.interfaces.rest.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.recommendations_service.recommendations.domain.model.entities.RecommendationTemplate;
import pe.edu.upc.recommendations_service.recommendations.infrastructure.persistence.jpa.repositories.RecommendationTemplateRepository;
import pe.edu.upc.recommendations_service.recommendations.interfaces.rest.resources.CreateTemplateResource;

import java.net.URI;
import java.util.List;

@Tag(name = "Recommendation Template", description = "Recommendation Template Endpoints")
@RestController
@RequestMapping("/api/v1/recommendation-templates")
@RequiredArgsConstructor
public class RecommendationTemplateController {

    private final RecommendationTemplateRepository repository;

    @GetMapping
    public ResponseEntity<List<RecommendationTemplate>> getAllTemplates() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<RecommendationTemplate> createTemplate(@RequestBody CreateTemplateResource resource) {
        RecommendationTemplate template = new RecommendationTemplate(resource.title(), resource.content(), resource.category());
        RecommendationTemplate saved = repository.save(template);
        return ResponseEntity.created(URI.create("/api/v1/recommendation-templates/" + saved.getId())).body(saved);
    }
}