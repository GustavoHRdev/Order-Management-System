package repository;

import model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository {

    void salvar(Pedido pedido);

    List<Pedido> listar();

    Optional<Pedido> buscarPorId(int id);

    boolean estaVazio();

    void adicionarItem(int pedidoId, model.ItemPedido itemPedido);

    void atualizarStatus(int pedidoId, model.StatusPedido statusPedido);

    void removerEntregues();
}
