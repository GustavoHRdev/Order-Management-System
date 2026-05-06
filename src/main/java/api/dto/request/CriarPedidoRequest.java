package api.dto.request;

import jakarta.validation.constraints.Positive;

public record CriarPedidoRequest(
        @Positive(message = "clienteId: deve ser maior que zero.")
        int clienteId
) {
}
