package pe.edu.upc.iam_service.iam.interfaces.rest.resources;

import java.util.List;

public record SignUpResource(String email, String password, String firstName, String lastName, String phone, List<String> roles) {
}
