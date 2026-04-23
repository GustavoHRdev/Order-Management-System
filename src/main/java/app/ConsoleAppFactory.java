package app;

import cli.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleAppFactory {

    public ConsoleApp create(ApplicationContext applicationContext, Scanner scanner) {
        InputReader inputReader = new InputReader(scanner);

        Map<Integer, MenuItem> menu = new LinkedHashMap<>();
        ConsoleApp app = new ConsoleApp(inputReader, menu);

        menu.put(1, new MenuItem("Cadastrar cliente",
                new CadastrarClienteCommand(inputReader, applicationContext.getClienteService())));
        menu.put(2, new MenuItem("Cadastrar produto",
                new CadastrarProdutoCommand(inputReader, applicationContext.getProdutoService())));
        menu.put(3, new MenuItem("Criar pedido",
                new CriarPedidoCommand(
                        inputReader,
                        applicationContext.getClienteService(),
                        applicationContext.getProdutoService(),
                        applicationContext.getPedidoService()
                )));
        menu.put(4, new MenuItem("Listar pedidos",
                new ListarPedidosCommand(applicationContext.getPedidoService())));
        menu.put(5, new MenuItem("Atualizar status do pedido",
                new AtualizarStatusCommand(inputReader, applicationContext.getPedidoService())));
        menu.put(6, new MenuItem("Listar clientes",
                new ListarClientesCommand(applicationContext.getClienteService())));
        menu.put(7, new MenuItem("Sair", new SairCommand(app::stop)));

        return app;
    }
}
