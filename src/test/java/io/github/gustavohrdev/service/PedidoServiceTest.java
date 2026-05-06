package io.github.gustavohrdev.service;

import io.github.gustavohrdev.exception.ClienteNotFoundException;
import io.github.gustavohrdev.exception.InvalidOrderQuantityException;
import io.github.gustavohrdev.exception.InvalidOrderStatusException;
import io.github.gustavohrdev.exception.InvalidOrderStatusTransitionException;
import io.github.gustavohrdev.exception.OrderItemModificationNotAllowedException;
import io.github.gustavohrdev.exception.OrderWithoutItemsStatusTransitionException;
import io.github.gustavohrdev.exception.ProdutoNotFoundException;
import io.github.gustavohrdev.model.Pedido;
import io.github.gustavohrdev.model.StatusPedido;
import org.junit.jupiter.api.Test;
import io.github.gustavohrdev.repository.ClienteRepository;
import io.github.gustavohrdev.repository.InMemoryClienteRepository;
import io.github.gustavohrdev.repository.InMemoryPedidoRepository;
import io.github.gustavohrdev.repository.InMemoryProdutoRepository;
import io.github.gustavohrdev.repository.PedidoRepository;
import io.github.gustavohrdev.repository.ProdutoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PedidoServiceTest {

    @Test
    void criarPedidoDeveFalharSemClientes() {
        PedidoService service = new PedidoService(
                new InMemoryPedidoRepository(),
                new InMemoryClienteRepository(),
                new InMemoryProdutoRepository()
        );

        assertThrows(ClienteNotFoundException.class, () -> service.criarPedido(1));
    }

    @Test
    void criarPedidoDeveSalvarPedido() {
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        clienteService.cadastrarCliente("Ana", "ana@example.com");
        int clienteId = clienteService.listarClientes().get(0).getId();

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);

        service.criarPedido(clienteId);

        List<Pedido> pedidos = service.listarPedidos();
        assertEquals(1, pedidos.size());
    }

    @Test
    void criarPedidoDeveFalharQuandoClienteIdNaoExiste() {
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        clienteService.cadastrarCliente("Ana", "ana@example.com");

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);

        assertThrows(ClienteNotFoundException.class, () -> service.criarPedido(999));
    }

    @Test
    void adicionarItemDeveAtualizarTotal() {
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository);

        clienteService.cadastrarCliente("Ana", "ana@example.com");
        produtoService.cadastrarProduto("Mouse", 50.0);
        int clienteId = clienteService.listarClientes().get(0).getId();
        int produtoId = produtoService.listarProdutos().get(0).getId();

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
        Pedido pedidoCriado = service.criarPedido(clienteId);

        service.adicionarItem(pedidoCriado.getId(), produtoId, 2);

        Pedido pedido = service.listarPedidos().get(0);
        assertEquals(100.0, pedido.calcularTotal(), 0.0001);
    }

    @Test
    void adicionarItemDeveFalharComQuantidadeInvalida() {
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository);

        clienteService.cadastrarCliente("Ana", "ana@example.com");
        produtoService.cadastrarProduto("Mouse", 50.0);
        int clienteId = clienteService.listarClientes().get(0).getId();
        int produtoId = produtoService.listarProdutos().get(0).getId();

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
        Pedido pedidoCriado = service.criarPedido(clienteId);

        assertThrows(InvalidOrderQuantityException.class, () -> service.adicionarItem(pedidoCriado.getId(), produtoId, 0));
        assertThrows(InvalidOrderQuantityException.class, () -> service.adicionarItem(pedidoCriado.getId(), produtoId, -1));
    }

    @Test
    void adicionarItemDeveFalharQuandoProdutoIdNaoExiste() {
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository);

        clienteService.cadastrarCliente("Ana", "ana@example.com");
        produtoService.cadastrarProduto("Mouse", 50.0);
        int clienteId = clienteService.listarClientes().get(0).getId();

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
        Pedido pedidoCriado = service.criarPedido(clienteId);

        assertThrows(ProdutoNotFoundException.class,
                () -> service.adicionarItem(pedidoCriado.getId(), 999, 1));
    }

    @Test
    void atualizarStatusDeveFalharComStatusNulo() {
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        clienteService.cadastrarCliente("Ana", "ana@example.com");
        int clienteId = clienteService.listarClientes().get(0).getId();

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
        Pedido pedidoCriado = service.criarPedido(clienteId);

        assertThrows(InvalidOrderStatusException.class, () -> service.atualizarStatus(pedidoCriado.getId(), null));
    }

    @Test
    void atualizarStatusDeveFalharQuandoPedidoNaoTemItens() {
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        clienteService.cadastrarCliente("Ana", "ana@example.com");
        int clienteId = clienteService.listarClientes().get(0).getId();

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
        Pedido pedidoCriado = service.criarPedido(clienteId);

        assertThrows(OrderWithoutItemsStatusTransitionException.class,
                () -> service.atualizarStatus(pedidoCriado.getId(), StatusPedido.PROCESSANDO));
    }

    @Test
    void atualizarStatusDevePermitirFluxoValido() {
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository);

        clienteService.cadastrarCliente("Ana", "ana@example.com");
        produtoService.cadastrarProduto("Mouse", 50.0);
        int clienteId = clienteService.listarClientes().get(0).getId();
        int produtoId = produtoService.listarProdutos().get(0).getId();

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
        Pedido pedidoCriado = service.criarPedido(clienteId);
        service.adicionarItem(pedidoCriado.getId(), produtoId, 1);

        service.atualizarStatus(pedidoCriado.getId(), StatusPedido.PROCESSANDO);
        service.atualizarStatus(pedidoCriado.getId(), StatusPedido.ENVIADO);
        service.atualizarStatus(pedidoCriado.getId(), StatusPedido.ENTREGUE);

        Pedido pedidoAtualizado = service.listarPedidos().get(0);
        assertEquals(StatusPedido.ENTREGUE, pedidoAtualizado.getStatus());
    }

    @Test
    void atualizarStatusDeveFalharComTransicaoInvalida() {
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository);

        clienteService.cadastrarCliente("Ana", "ana@example.com");
        produtoService.cadastrarProduto("Mouse", 50.0);
        int clienteId = clienteService.listarClientes().get(0).getId();
        int produtoId = produtoService.listarProdutos().get(0).getId();

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
        Pedido pedidoCriado = service.criarPedido(clienteId);
        service.adicionarItem(pedidoCriado.getId(), produtoId, 1);

        assertThrows(InvalidOrderStatusTransitionException.class,
                () -> service.atualizarStatus(pedidoCriado.getId(), StatusPedido.ENTREGUE));
    }

    @Test
    void adicionarItemDeveFalharQuandoPedidoNaoEstaPendente() {
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository);

        clienteService.cadastrarCliente("Ana", "ana@example.com");
        produtoService.cadastrarProduto("Mouse", 50.0);
        int clienteId = clienteService.listarClientes().get(0).getId();
        int produtoId = produtoService.listarProdutos().get(0).getId();

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
        Pedido pedidoCriado = service.criarPedido(clienteId);
        service.adicionarItem(pedidoCriado.getId(), produtoId, 1);
        service.atualizarStatus(pedidoCriado.getId(), StatusPedido.PROCESSANDO);

        assertThrows(OrderItemModificationNotAllowedException.class,
                () -> service.adicionarItem(pedidoCriado.getId(), produtoId, 1));
    }

    @Test
    void removerPedidosEntreguesDeveRemoverApenasEntregues() {
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository);
        clienteService.cadastrarCliente("Ana", "ana@example.com");
        produtoService.cadastrarProduto("Mouse", 50.0);
        int clienteId = clienteService.listarClientes().get(0).getId();
        int produtoId = produtoService.listarProdutos().get(0).getId();

        PedidoService service = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);

        Pedido pedidoEntregue = service.criarPedido(clienteId);
        Pedido outroPedido = service.criarPedido(clienteId);

        service.adicionarItem(pedidoEntregue.getId(), produtoId, 1);
        service.adicionarItem(outroPedido.getId(), produtoId, 1);

        service.atualizarStatus(pedidoEntregue.getId(), StatusPedido.PROCESSANDO);
        service.atualizarStatus(pedidoEntregue.getId(), StatusPedido.ENVIADO);
        service.atualizarStatus(pedidoEntregue.getId(), StatusPedido.ENTREGUE);
        service.removerPedidosEntregues();

        assertEquals(1, service.listarPedidos().size());
    }
}

