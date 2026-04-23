package app;

import database.ConnectionFactory;
import database.DatabaseInitializer;
import database.H2ConnectionFactory;
import repository.ClienteRepository;
import repository.JdbcClienteRepository;
import repository.JdbcPedidoRepository;
import repository.JdbcProdutoRepository;
import repository.PedidoRepository;
import repository.ProdutoRepository;
import service.ClienteService;
import service.PedidoService;
import service.ProdutoService;

public class ApplicationContext {

    private final ClienteService clienteService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    public ApplicationContext(ClienteRepository clienteRepository,
                              ProdutoRepository produtoRepository,
                              PedidoRepository pedidoRepository) {
        this.clienteService = new ClienteService(clienteRepository);
        this.produtoService = new ProdutoService(produtoRepository);
        this.pedidoService = new PedidoService(pedidoRepository, clienteRepository, produtoRepository);
    }

    public static ApplicationContext createDefault() {
        ConnectionFactory connectionFactory = new H2ConnectionFactory(
                "jdbc:h2:./data/order-management-system;AUTO_SERVER=TRUE",
                "sa",
                ""
        );

        new DatabaseInitializer(connectionFactory).initialize();

        ClienteRepository clienteRepository = new JdbcClienteRepository(connectionFactory);
        ProdutoRepository produtoRepository = new JdbcProdutoRepository(connectionFactory);
        PedidoRepository pedidoRepository = new JdbcPedidoRepository(connectionFactory);

        return new ApplicationContext(clienteRepository, produtoRepository, pedidoRepository);
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public ProdutoService getProdutoService() {
        return produtoService;
    }

    public PedidoService getPedidoService() {
        return pedidoService;
    }
}
