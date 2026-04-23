package cli;

import repository.ClienteRepository;
import repository.PedidoRepository;
import repository.ProdutoRepository;
import service.ClienteService;
import service.PedidoService;
import service.ProdutoService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {

    private final InputReader inputReader;
    private final Map<Integer, MenuItem> menu = new LinkedHashMap<>();
    private boolean running = true;

    public ConsoleApp(Scanner scanner) {
        this.inputReader = new InputReader(scanner);

        ClienteRepository clienteRepository = new ClienteRepository();
        ProdutoRepository produtoRepository = new ProdutoRepository();
        PedidoRepository pedidoRepository = new PedidoRepository();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository);
        PedidoService pedidoService = new PedidoService(
                pedidoRepository,
                clienteRepository,
                produtoRepository
        );

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
        menu.put(7, new MenuItem("Sair",
                new SairCommand(() -> running = false)));
    }

    public void run() {
        while (running) {
            printMenu();
            int opcao = readOption();

            MenuItem item = menu.get(opcao);
            if (item == null) {
                System.out.println("Opção inválida!");
                continue;
            }

            item.getCommand().execute();
        }
    }

    private void printMenu() {
        System.out.println();
        for (Map.Entry<Integer, MenuItem> entry : menu.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue().getLabel());
        }
        System.out.print("Escolha uma opção: ");
    }

    private int readOption() {
        return inputReader.readInt(null);
    }
}
