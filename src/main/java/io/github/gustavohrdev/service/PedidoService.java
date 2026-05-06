package io.github.gustavohrdev.service;

import io.github.gustavohrdev.exception.ClienteNotFoundException;
import io.github.gustavohrdev.exception.InvalidOrderQuantityException;
import io.github.gustavohrdev.exception.InvalidOrderStatusException;
import io.github.gustavohrdev.exception.InvalidOrderStatusTransitionException;
import io.github.gustavohrdev.exception.OrderAlreadyFinalizedException;
import io.github.gustavohrdev.exception.OrderItemModificationNotAllowedException;
import io.github.gustavohrdev.exception.OrderStatusRegressionNotAllowedException;
import io.github.gustavohrdev.exception.OrderWithoutItemsStatusTransitionException;
import io.github.gustavohrdev.exception.PedidoNotFoundException;
import io.github.gustavohrdev.exception.ProdutoNotFoundException;
import io.github.gustavohrdev.model.*;
import org.springframework.stereotype.Service;
import io.github.gustavohrdev.repository.*;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;


    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         ProdutoRepository produtoRepository) {


        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    public Pedido criarPedido(int clienteId) {
        Cliente cliente = clienteRepository.buscarPorId(clienteId)
                .orElseThrow(ClienteNotFoundException::new);

        Pedido pedido = new Pedido(cliente);

        pedidoRepository.salvar(pedido);

        return pedido;
    }

    public void adicionarItem(int pedidoId, int produtoId, int quantidade) {
        if (quantidade <= 0) {
            throw new InvalidOrderQuantityException();
        }

        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(PedidoNotFoundException::new);
        if (pedido.getStatus() != StatusPedido.PENDENTE) {
            throw new OrderItemModificationNotAllowedException();
        }

        Produto produto = produtoRepository.buscarPorId(produtoId)
                .orElseThrow(ProdutoNotFoundException::new);

        ItemPedido item = new ItemPedido(produto, quantidade);
        pedidoRepository.adicionarItem(pedidoId, item);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.listar();
    }

    public Pedido buscarPedidoPorId(int pedidoId) {
        return pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(PedidoNotFoundException::new);
    }

    public void atualizarStatus(int pedidoId, StatusPedido status) {
        if (status == null) {
            throw new InvalidOrderStatusException();
        }

        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(PedidoNotFoundException::new);
        validarTransicaoStatus(pedido, status);

        pedidoRepository.atualizarStatus(pedidoId, status);
    }

    public void removerPedidosEntregues() {
        pedidoRepository.removerEntregues();
    }

    private void validarTransicaoStatus(Pedido pedido, StatusPedido novoStatus) {
        StatusPedido statusAtual = pedido.getStatus();

        if (statusAtual == StatusPedido.ENTREGUE || statusAtual == StatusPedido.CANCELADO) {
            throw new OrderAlreadyFinalizedException();
        }

        if (novoStatus == StatusPedido.PENDENTE && statusAtual != StatusPedido.PENDENTE) {
            throw new OrderStatusRegressionNotAllowedException();
        }

        if (novoStatus != StatusPedido.CANCELADO && !pedido.temItens()) {
            throw new OrderWithoutItemsStatusTransitionException();
        }

        if (statusAtual == StatusPedido.PENDENTE
                && novoStatus != StatusPedido.PROCESSANDO
                && novoStatus != StatusPedido.CANCELADO) {
            throw new InvalidOrderStatusTransitionException();
        }

        if (statusAtual == StatusPedido.PROCESSANDO
                && novoStatus != StatusPedido.ENVIADO
                && novoStatus != StatusPedido.CANCELADO) {
            throw new InvalidOrderStatusTransitionException();
        }

        if (statusAtual == StatusPedido.ENVIADO && novoStatus != StatusPedido.ENTREGUE) {
            throw new InvalidOrderStatusTransitionException();
        }
    }
}

