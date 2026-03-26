package repository;

import model.Pedido;
import model.StatusPedido;

import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {

    private final List<Pedido> pedidos = new ArrayList<>();

    public void salvar(Pedido pedido) {
        pedidos.add(pedido);
    }

    public List<Pedido> listar() {
        return new ArrayList<>(pedidos);
    }

    public Pedido buscarPorIndex(int index) {
        if (index < 0 || index >= pedidos.size()) {
            throw new IllegalArgumentException("Pedido inválido!");
        }
        return pedidos.get(index);
    }

    public boolean estaVazio() {
        return pedidos.isEmpty();
    }

    public void removerEntregues() {
        pedidos.removeIf(p -> p.getStatus() == StatusPedido.ENTREGUE);
    }
}
