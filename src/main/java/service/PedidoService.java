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

    public Pedido criarPedido(int clienteIndex) {

        if (clienteRepository.estaVazio()) {
            throw new IllegalStateException("Nenhum cliente cadastrado.");
        }

        Cliente cliente = clienteRepository.buscarPorIndex(clienteIndex);

        Pedido pedido = new Pedido(cliente);

        pedidoRepository.salvar(pedido);

        return pedido;
    }

    public void adicionarItem(int pedidoIndex, int produtoIndex, int quantidade) {

        if (pedidoRepository.estaVazio()) {
            throw new IllegalStateException("Nenhum pedido cadastrado.");
        }

        if (produtoRepository.estaVazio()) {
            throw new IllegalStateException("Nenhum produto cadastrado.");
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida.");
        }

        Pedido pedido = pedidoRepository.buscarPorIndex(pedidoIndex);
        Produto produto = produtoRepository.buscarPorIndex(produtoIndex);

        ItemPedido item = new ItemPedido(produto, quantidade);
        pedido.adicionarItem(item);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.listar();
    }
    public void atualizarStatus(int pedidoIndex, StatusPedido status) {

        if (pedidoRepository.estaVazio()) {
            throw new IllegalStateException("Nenhum pedido cadastrado.");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status inválido.");
        }

        Pedido pedido = pedidoRepository.buscarPorIndex(pedidoIndex);

        pedido.atualizarStatus(status);
    }
    public void removerPedidosEntregues() {
        if (pedidoRepository.estaVazio()) {
            throw new IllegalStateException("Nenhum pedido cadastrado.");
        }
        pedidoRepository.removerEntregues();
    }
}
