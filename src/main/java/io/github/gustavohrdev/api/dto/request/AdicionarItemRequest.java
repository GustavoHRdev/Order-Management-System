package io.github.gustavohrdev.api.dto.request;

import jakarta.validation.constraints.Positive;

public record AdicionarItemRequest(
        @Positive(message = "produtoId: deve ser maior que zero.")
        int produtoId,
        @Positive(message = "quantidade: deve ser maior que zero.")
        int quantidade
) {
}

