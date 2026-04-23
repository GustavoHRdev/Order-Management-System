package app;

import cli.*;
import repository.ClienteRepository;
import repository.InMemoryClienteRepository;
import repository.InMemoryPedidoRepository;
import repository.InMemoryProdutoRepository;
import repository.PedidoRepository;
import repository.ProdutoRepository;
import service.ClienteService;
import service.PedidoService;
import service.ProdutoService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleAppFactory {

    public ConsoleApp create(Scanner scanner) {
        InputReader inputReader = new InputReader(scanner);

        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository);
        PedidoService pedidoService = new PedidoService(
                pedidoRepository,
                clienteRepository,
                produtoRepository
        );

        Map<Integer, MenuItem> menu = new LinkedHashMap<>();
        ConsoleApp app = new ConsoleApp(inputReader, menu);

        menu.put(1, new MenuItem("Cadastrar cliente",
                new CadastrarClienteCommand(inputReader, clienteService)));
        menu.put(2, new MenuItem("Cadastrar produto",
                new CadastrarProdutoCommand(inputReader, produtoService)));
        menu.put(3, new MenuItem("Criar pedido",
                new CriarPedidoCommand(inputReader, clienteService, produtoService, pedidoService)));
        menu.put(4, new MenuItem("Listar pedidos",
                new ListarPedidosCommand(pedidoService)));
        menu.put(5, new MenuItem("Atualizar status do pedido",
                new AtualizarStatusCommand(inputReader, pedidoService)));
        menu.put(6, new MenuItem("Listar clientes",
                new ListarClientesCommand(clienteService)));
        menu.put(7, new MenuItem("Sair", new SairCommand(app::stop)));

        return app;
    }
}
