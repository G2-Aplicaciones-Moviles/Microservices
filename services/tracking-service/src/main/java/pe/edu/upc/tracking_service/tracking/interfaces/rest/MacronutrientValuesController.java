package pe.edu.upc.tracking_service.tracking.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.tracking_service.tracking.domain.model.queries.GetConsumedMacrosQuery;
import pe.edu.upc.tracking_service.tracking.domain.services.MacronutrientValuesQueryService;
import pe.edu.upc.tracking_service.tracking.domain.services.TrackingQueryService;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.resources.MacronutrientValuesResource;
import pe.edu.upc.tracking_service.tracking.interfaces.rest.transform.MacronutrientValuesResourceFromEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/macronutrients", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Macronutrient Values", description = "Macronutrient Values Management Endpoints")
public class MacronutrientValuesController {

    private final MacronutrientValuesQueryService macronutrientValuesQueryService;
    private final TrackingQueryService trackingQueryService;

    public MacronutrientValuesController(MacronutrientValuesQueryService macronutrientValuesQueryService
            , TrackingQueryService trackingQueryService) {
        this.macronutrientValuesQueryService = macronutrientValuesQueryService;
        this.trackingQueryService = trackingQueryService;
    }

    @GetMapping("/consumed/tracking/{trackingId}")
    public ResponseEntity<MacronutrientValuesResource> getConsumedMacrosByTrackingId(@PathVariable Long trackingId) {
        var query = new GetConsumedMacrosQuery(trackingId);
        var optionalMacros = trackingQueryService.handle(query);

        if (optionalMacros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resource = MacronutrientValuesResourceFromEntityAssembler.toResource(optionalMacros.get());
        return ResponseEntity.ok(resource);
    }
}