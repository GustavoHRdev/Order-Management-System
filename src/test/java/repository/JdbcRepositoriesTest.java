package repository;

import database.ConnectionFactory;
import database.DatabaseInitializer;
import database.H2ConnectionFactory;
import model.Cliente;
import model.Pedido;
import model.Produto;
import model.StatusPedido;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JdbcRepositoriesTest {

    @Test
    void jdbcRepositoriesDevemPersistirPedidoComItens() {
        ConnectionFactory connectionFactory = createIsolatedConnectionFactory();
        new DatabaseInitializer(connectionFactory).initialize();

        ClienteRepository clienteRepository = new JdbcClienteRepository(connectionFactory);
        ProdutoRepository produtoRepository = new JdbcProdutoRepository(connectionFactory);
        PedidoRepository pedidoRepository = new JdbcPedidoRepository(connectionFactory);

        Cliente cliente = new Cliente("Ana", "ana@example.com");
        Produto produto = new Produto("Mouse", 50.0);
        clienteRepository.salvar(cliente);
        produtoRepository.salvar(produto);

        Pedido pedido = new Pedido(cliente);
        pedidoRepository.salvar(pedido);
        pedidoRepository.adicionarItem(pedido.getId(), new model.ItemPedido(produto, 2));
        pedidoRepository.atualizarStatus(pedido.getId(), StatusPedido.PROCESSANDO);

        Pedido pedidoSalvo = pedidoRepository.buscarPorId(pedido.getId()).orElseThrow();

        assertEquals(StatusPedido.PROCESSANDO, pedidoSalvo.getStatus());
        assertEquals(1, pedidoSalvo.getItens().size());
        assertEquals(100.0, pedidoSalvo.calcularTotal(), 0.0001);
    }

    @Test
    void jdbcRepositoriesDevemRemoverPedidosEntregues() {
        ConnectionFactory connectionFactory = createIsolatedConnectionFactory();
        new DatabaseInitializer(connectionFactory).initialize();

        ClienteRepository clienteRepository = new JdbcClienteRepository(connectionFactory);
        PedidoRepository pedidoRepository = new JdbcPedidoRepository(connectionFactory);

        Cliente cliente = new Cliente("Ana", "ana@example.com");
        clienteRepository.salvar(cliente);

        Pedido pedido = new Pedido(cliente);
        pedidoRepository.salvar(pedido);
        pedidoRepository.atualizarStatus(pedido.getId(), StatusPedido.ENTREGUE);

        assertFalse(pedidoRepository.estaVazio());
        pedidoRepository.removerEntregues();
        assertTrue(pedidoRepository.estaVazio());
    }

    private ConnectionFactory createIsolatedConnectionFactory() {
        String jdbcUrl = "jdbc:h2:mem:test_" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1";
        return new H2ConnectionFactory(jdbcUrl, "sa", "");
    }
}
