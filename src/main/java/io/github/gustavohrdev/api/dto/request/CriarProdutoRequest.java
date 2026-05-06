package io.github.gustavohrdev.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record CriarProdutoRequest(
        @NotBlank(message = "nome: não deve estar em branco.")
        String nome,
        @DecimalMin(value = "0.01", message = "preco: deve ser maior que zero.")
        double preco
) {
}

