package api.dto.response;

import model.Pedido;

import java.util.List;

public record PedidoResponse(int id,
                             int clienteId,
                             String clienteNome,
                             String status,
                             List<ItemPedidoResponse> itens,
                             double total) {

    public static PedidoResponse from(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getStatus().name(),
                pedido.getItens().stream().map(ItemPedidoResponse::from).toList(),
                pedido.calcularTotal()
        );
    }
}
