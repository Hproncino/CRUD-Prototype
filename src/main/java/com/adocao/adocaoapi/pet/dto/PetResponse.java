package com.adocao.adocaoapi.pet.dto;

import java.time.Instant;

public record PetResponse(
        Long id,
        String name,
        Instant createdAt
) {
}

