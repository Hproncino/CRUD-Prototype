package com.adocao.adocaoapi.pet.dto;

import jakarta.validation.constraints.NotBlank;

public record PetUpdateRequest(
        @NotBlank String name
) {
}

