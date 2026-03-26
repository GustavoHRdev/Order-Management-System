package service;

import model.Pedido;
import model.StatusPedido;
import org.junit.jupiter.api.Test;
import repository.ClienteRepository;
import repository.PedidoRepository;
import repository.ProdutoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PedidoServiceTest {

    @Test
    void criarPedidoDeveFalharSemClientes() {
        PedidoService service = new PedidoService(
                new PedidoRepository(),
                new ClienteRepository(),
                new ProdutoRepository()
        );

        assertThrows(IllegalStateException.class, () -> service.criarPedido(0));
    }

    @Test
    void criarPedidoDeveSalvarPedido() {
        ClienteRepository clienteRepository = new ClienteRepository();
        ProdutoRepository produtoRepository = new ProdutoRepository();
        PedidoRepository pedidoRepository = new PedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        clienteService.cadastrarCliente("Ana", "ana@example.com");

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);

        service.criarPedido(0);

        List<Pedido> pedidos = service.listarPedidos();
        assertEquals(1, pedidos.size());
    }

    @Test
    void adicionarItemDeveAtualizarTotal() {
        ClienteRepository clienteRepository = new ClienteRepository();
        ProdutoRepository produtoRepository = new ProdutoRepository();
        PedidoRepository pedidoRepository = new PedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository);

        clienteService.cadastrarCliente("Ana", "ana@example.com");
        produtoService.cadastrarProduto("Mouse", 50.0);

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
        service.criarPedido(0);

        service.adicionarItem(0, 0, 2);

        Pedido pedido = service.listarPedidos().get(0);
        assertEquals(100.0, pedido.calcularTotal(), 0.0001);
    }

    @Test
    void adicionarItemDeveFalharComQuantidadeInvalida() {
        ClienteRepository clienteRepository = new ClienteRepository();
        ProdutoRepository produtoRepository = new ProdutoRepository();
        PedidoRepository pedidoRepository = new PedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository);

        clienteService.cadastrarCliente("Ana", "ana@example.com");
        produtoService.cadastrarProduto("Mouse", 50.0);

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
        service.criarPedido(0);

        assertThrows(IllegalArgumentException.class, () -> service.adicionarItem(0, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> service.adicionarItem(0, 0, -1));
    }

    @Test
    void atualizarStatusDeveFalharComStatusNulo() {
        ClienteRepository clienteRepository = new ClienteRepository();
        ProdutoRepository produtoRepository = new ProdutoRepository();
        PedidoRepository pedidoRepository = new PedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        clienteService.cadastrarCliente("Ana", "ana@example.com");

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
        service.criarPedido(0);

        assertThrows(IllegalArgumentException.class, () -> service.atualizarStatus(0, null));
    }

    @Test
    void removerPedidosEntreguesDeveRemoverApenasEntregues() {
        ClienteRepository clienteRepository = new ClienteRepository();
        ProdutoRepository produtoRepository = new ProdutoRepository();
        PedidoRepository pedidoRepository = new PedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        clienteService.cadastrarCliente("Ana", "ana@example.com");

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);

        service.criarPedido(0);
        service.criarPedido(0);

        service.atualizarStatus(0, StatusPedido.ENTREGUE);
        service.removerPedidosEntregues();

        assertEquals(1, service.listarPedidos().size());
    }
}
