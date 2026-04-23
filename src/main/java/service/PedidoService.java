package service;

import model.*;
import repository.*;
import java.util.List;

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

        if (clienteRepository.estaVazio()) {
            throw new IllegalStateException("Nenhum cliente cadastrado.");
        }

        Cliente cliente = clienteRepository.buscarPorId(clienteId);

        Pedido pedido = new Pedido(cliente);

        pedidoRepository.salvar(pedido);

        return pedido;
    }

    public void adicionarItem(int pedidoId, int produtoId, int quantidade) {

        if (pedidoRepository.estaVazio()) {
            throw new IllegalStateException("Nenhum pedido cadastrado.");
        }

        if (produtoRepository.estaVazio()) {
            throw new IllegalStateException("Nenhum produto cadastrado.");
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida.");
        }

        Pedido pedido = pedidoRepository.buscarPorId(pedidoId);
        if (pedido.getStatus() != StatusPedido.PENDENTE) {
            throw new IllegalStateException("Nao e permitido alterar itens de um pedido que nao esta pendente.");
        }

        Produto produto = produtoRepository.buscarPorId(produtoId);

        ItemPedido item = new ItemPedido(produto, quantidade);
        pedido.adicionarItem(item);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.listar();
    }
    public void atualizarStatus(int pedidoId, StatusPedido status) {

        if (pedidoRepository.estaVazio()) {
            throw new IllegalStateException("Nenhum pedido cadastrado.");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status inválido.");
        }

        Pedido pedido = pedidoRepository.buscarPorId(pedidoId);
        validarTransicaoStatus(pedido, status);

        pedido.atualizarStatus(status);
    }

    public void removerPedidosEntregues() {
        if (pedidoRepository.estaVazio()) {
            throw new IllegalStateException("Nenhum pedido cadastrado.");
        }
        pedidoRepository.removerEntregues();
    }

    private void validarTransicaoStatus(Pedido pedido, StatusPedido novoStatus) {
        StatusPedido statusAtual = pedido.getStatus();

        if (statusAtual == StatusPedido.ENTREGUE || statusAtual == StatusPedido.CANCELADO) {
            throw new IllegalStateException("Nao e permitido alterar um pedido finalizado.");
        }

        if (novoStatus == StatusPedido.PENDENTE && statusAtual != StatusPedido.PENDENTE) {
            throw new IllegalStateException("Nao e permitido retornar o pedido para pendente.");
        }

        if (novoStatus != StatusPedido.CANCELADO && !pedido.temItens()) {
            throw new IllegalStateException("Nao e permitido avancar o status de um pedido sem itens.");
        }

        if (statusAtual == StatusPedido.PENDENTE
                && novoStatus != StatusPedido.PROCESSANDO
                && novoStatus != StatusPedido.CANCELADO) {
            throw new IllegalStateException("Transicao de status invalida.");
        }

        if (statusAtual == StatusPedido.PROCESSANDO
                && novoStatus != StatusPedido.ENVIADO
                && novoStatus != StatusPedido.CANCELADO) {
            throw new IllegalStateException("Transicao de status invalida.");
        }

        if (statusAtual == StatusPedido.ENVIADO && novoStatus != StatusPedido.ENTREGUE) {
            throw new IllegalStateException("Transicao de status invalida.");
        }
    }
}
