package be.ecam.biblioweb.dto;

import jakarta.validation.constraints.NotBlank;

public record LivreRequest(
        @NotBlank(message = "le titre est obligatoire") String title,
        @NotBlank(message = "l'auteur est obligatoire") String author
) {
}
