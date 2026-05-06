package io.github.gustavohrdev.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CriarClienteRequest(
        @NotBlank(message = "nome: não deve estar em branco.")
        String nome,
        @NotBlank(message = "email: não deve estar em branco.")
        @Email(message = "email: deve ser um email válido.")
        String email
) {
}

