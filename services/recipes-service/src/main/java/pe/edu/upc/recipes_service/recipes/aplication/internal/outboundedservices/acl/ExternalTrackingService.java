package pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl;

import feign.FeignException;
import org.springframework.stereotype.Service;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.TrackingIntegrationClient;
import pe.edu.upc.recipes_service.recipes.aplication.internal.outboundedservices.acl.rest.resource.MacronutrientValuesResource;

import java.util.List;
import java.util.Optional;

/**
 * Service to communicate with tracking-service via Feign Client.
 */
@Service
public class ExternalTrackingService {

    private final TrackingIntegrationClient trackingClient;

    public ExternalTrackingService(TrackingIntegrationClient trackingClient) {
        this.trackingClient = trackingClient;
    }

    public boolean existsMacronutrientValuesById(Long macronutrientValuesId) {
        try {
            trackingClient.getMacronutrientValuesById(macronutrientValuesId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error checking macronutrient values existence: " + e.getMessage());
            return false;
        }
    }

    public Optional<MacronutrientValuesResource> getMacronutrientValuesById(Long macronutrientValuesId) {
        try {
            MacronutrientValuesResource macros = trackingClient.getMacronutrientValuesById(macronutrientValuesId);
            return Optional.of(macros);
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error fetching macronutrient values: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void validateMacronutrientValuesExists(Long macronutrientValuesId) {
        if (!existsMacronutrientValuesById(macronutrientValuesId)) {
            throw new IllegalArgumentException("MacronutrientValues not found with ID: " + macronutrientValuesId);
        }
    }

    public List<MacronutrientValuesResource> getAllMacronutrientValues() {
        try {
            return trackingClient.getAllMacronutrientValues();
        } catch (Exception e) {
            System.err.println("Error fetching all macronutrient values: " + e.getMessage());
            return List.of();
        }
    }
}

