package pe.edu.upc.profiles_service.profiles.application.internal.outboundservices;

public interface ExternalUserService {
    boolean userExists(Long userId);
}