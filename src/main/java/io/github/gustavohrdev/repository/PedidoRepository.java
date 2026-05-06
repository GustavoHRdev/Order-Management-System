package io.github.gustavohrdev.repository;

import io.github.gustavohrdev.model.ItemPedido;
import io.github.gustavohrdev.model.Pedido;
import io.github.gustavohrdev.model.StatusPedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository {

    void salvar(Pedido pedido);

    List<Pedido> listar();

    Optional<Pedido> buscarPorId(int id);

    boolean estaVazio();

    void adicionarItem(int pedidoId, ItemPedido itemPedido);

    void atualizarStatus(int pedidoId, StatusPedido statusPedido);

    void removerEntregues();
}

