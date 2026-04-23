package repository;

import model.Pedido;

import java.util.List;

public interface PedidoRepository {

    void salvar(Pedido pedido);

    List<Pedido> listar();

    Pedido buscarPorId(int id);

    boolean estaVazio();

    void removerEntregues();
}
