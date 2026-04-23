package repository;

import model.Pedido;
import model.StatusPedido;

import java.util.ArrayList;
import java.util.List;

public class InMemoryPedidoRepository implements PedidoRepository {

    private final List<Pedido> pedidos = new ArrayList<>();

    @Override
    public void salvar(Pedido pedido) {
        pedidos.add(pedido);
    }

    @Override
    public List<Pedido> listar() {
        return new ArrayList<>(pedidos);
    }

    @Override
    public Pedido buscarPorId(int id) {
        for (Pedido pedido : pedidos) {
            if (pedido.getId() == id) {
                return pedido;
            }
        }

        throw new IllegalArgumentException("Pedido inválido!");
    }

    @Override
    public boolean estaVazio() {
        return pedidos.isEmpty();
    }

    @Override
    public void removerEntregues() {
        pedidos.removeIf(pedido -> pedido.getStatus() == StatusPedido.ENTREGUE);
    }
}
