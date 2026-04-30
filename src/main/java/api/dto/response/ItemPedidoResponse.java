package api.dto.response;

import model.ItemPedido;

public record ItemPedidoResponse(int produtoId, String produtoNome, double precoUnitario, int quantidade, double subtotal) {

    public static ItemPedidoResponse from(ItemPedido itemPedido) {
        return new ItemPedidoResponse(
                itemPedido.getProduto().getId(),
                itemPedido.getProduto().getNome(),
                itemPedido.getProduto().getPreco(),
                itemPedido.getQuantidade(),
                itemPedido.calcularSubtotal()
        );
    }
}
