package be.ecam.biblioweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "le nom est obligatoire") String name,
        @NotBlank(message = "le prenom est obligatoire") String surname,
        @NotBlank(message = "l'email est obligatoire") @Email(message = "email invalide") String email
) {
}
