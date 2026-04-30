package api.dto.response;

import model.Cliente;

public record ClienteResponse(int id, String nome, String email) {

    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }
}
