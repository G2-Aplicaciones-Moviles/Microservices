package pe.edu.upc.tracking_service.tracking.application.internal.outboundservices.acl.rest.resource;

public record RecipeResource(
        int id,
        String title,
        String description,
        Long categoryId,
        String videoURL,
        String imageURL
) {
}

