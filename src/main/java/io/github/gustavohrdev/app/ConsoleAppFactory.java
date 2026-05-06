package io.github.gustavohrdev.app;

import io.github.gustavohrdev.cli.*;
import org.springframework.stereotype.Component;
import io.github.gustavohrdev.service.ClienteService;
import io.github.gustavohrdev.service.PedidoService;
import io.github.gustavohrdev.service.ProdutoService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class ConsoleAppFactory {

    private final ClienteService clienteService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    public ConsoleAppFactory(ClienteService clienteService,
                             ProdutoService produtoService,
                             PedidoService pedidoService) {
        this.clienteService = clienteService;
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
    }

    public ConsoleApp create(Scanner scanner) {
        InputReader inputReader = new InputReader(scanner);

        Map<Integer, MenuItem> menu = new LinkedHashMap<>();
        ConsoleApp app = new ConsoleApp(inputReader, menu);

        menu.put(1, new MenuItem("Cadastrar cliente",
                new CadastrarClienteCommand(inputReader, clienteService)));
        menu.put(2, new MenuItem("Cadastrar produto",
                new CadastrarProdutoCommand(inputReader, produtoService)));
        menu.put(3, new MenuItem("Criar pedido",
                new CriarPedidoCommand(
                        inputReader,
                        clienteService,
                        produtoService,
                        pedidoService
                )));
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

