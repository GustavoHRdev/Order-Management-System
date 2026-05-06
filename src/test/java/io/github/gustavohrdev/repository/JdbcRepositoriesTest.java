package io.github.gustavohrdev.repository;

import io.github.gustavohrdev.model.Cliente;
import io.github.gustavohrdev.model.ItemPedido;
import io.github.gustavohrdev.model.Pedido;
import io.github.gustavohrdev.model.Produto;
import io.github.gustavohrdev.model.StatusPedido;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JdbcRepositoriesTest {

    @Test
    void jdbcRepositoriesDevemPersistirPedidoComItens() {
        DataSource dataSource = createIsolatedDataSource();
        initializeSchema(dataSource);

        ClienteRepository clienteRepository = new JdbcClienteRepository(dataSource);
        ProdutoRepository produtoRepository = new JdbcProdutoRepository(dataSource);
        PedidoRepository pedidoRepository = new JdbcPedidoRepository(dataSource);

        Cliente cliente = new Cliente("Ana", "ana@example.com");
        Produto produto = new Produto("Mouse", 50.0);
        clienteRepository.salvar(cliente);
        produtoRepository.salvar(produto);

        Pedido pedido = new Pedido(cliente);
        pedidoRepository.salvar(pedido);
        pedidoRepository.adicionarItem(pedido.getId(), new ItemPedido(produto, 2));
        pedidoRepository.atualizarStatus(pedido.getId(), StatusPedido.PROCESSANDO);

        Pedido pedidoSalvo = pedidoRepository.buscarPorId(pedido.getId()).orElseThrow();

        assertEquals(StatusPedido.PROCESSANDO, pedidoSalvo.getStatus());
        assertEquals(1, pedidoSalvo.getItens().size());
        assertEquals(100.0, pedidoSalvo.calcularTotal(), 0.0001);
    }

    @Test
    void jdbcRepositoriesDevemRemoverPedidosEntregues() {
        DataSource dataSource = createIsolatedDataSource();
        initializeSchema(dataSource);

        ClienteRepository clienteRepository = new JdbcClienteRepository(dataSource);
        PedidoRepository pedidoRepository = new JdbcPedidoRepository(dataSource);

        Cliente cliente = new Cliente("Ana", "ana@example.com");
        clienteRepository.salvar(cliente);

        Pedido pedido = new Pedido(cliente);
        pedidoRepository.salvar(pedido);
        pedidoRepository.atualizarStatus(pedido.getId(), StatusPedido.ENTREGUE);

        assertFalse(pedidoRepository.estaVazio());
        pedidoRepository.removerEntregues();
        assertTrue(pedidoRepository.estaVazio());
    }

    private DataSource createIsolatedDataSource() {
        String jdbcUrl = "jdbc:h2:mem:test_" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1";
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    private void initializeSchema(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);
    }
}

