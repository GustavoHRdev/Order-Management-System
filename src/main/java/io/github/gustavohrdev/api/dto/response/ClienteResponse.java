package io.github.gustavohrdev.api.dto.response;

import io.github.gustavohrdev.model.Cliente;

public record ClienteResponse(int id, String nome, String email) {

    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }
}

