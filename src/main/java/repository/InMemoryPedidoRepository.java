package repository;

import model.Pedido;
import model.ItemPedido;
import model.StatusPedido;

import java.util.ArrayList;
import java.util.List;

public class InMemoryPedidoRepository implements PedidoRepository {

    private final List<Pedido> pedidos = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void salvar(Pedido pedido) {
        if (pedido.getId() == 0) {
            pedido.setId(nextId++);
        }
        pedidos.add(pedido);
    }

    @Override
    public List<Pedido> listar() {
        return new ArrayList<>(pedidos);
    }

    @Override
    public java.util.Optional<Pedido> buscarPorId(int id) {
        for (Pedido pedido : pedidos) {
            if (pedido.getId() == id) {
                return java.util.Optional.of(pedido);
            }
        }
        return java.util.Optional.empty();
    }

    @Override
    public boolean estaVazio() {
        return pedidos.isEmpty();
    }

    @Override
    public void adicionarItem(int pedidoId, ItemPedido itemPedido) {
        Pedido pedido = buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido inválido!"));
        pedido.adicionarItem(itemPedido);
    }

    @Override
    public void atualizarStatus(int pedidoId, StatusPedido statusPedido) {
        Pedido pedido = buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido inválido!"));
        pedido.atualizarStatus(statusPedido);
    }

    @Override
    public void removerEntregues() {
        pedidos.removeIf(pedido -> pedido.getStatus() == StatusPedido.ENTREGUE);
    }
}
