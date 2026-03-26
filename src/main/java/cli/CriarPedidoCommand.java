package cli;

import model.Cliente;
import model.Produto;
import service.ClienteService;
import service.PedidoService;
import service.ProdutoService;

import java.util.List;
import java.util.Scanner;

public class CriarPedidoCommand implements Command {

    private final Scanner scanner;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    public CriarPedidoCommand(Scanner scanner,
                              ClienteService clienteService,
                              ProdutoService produtoService,
                              PedidoService pedidoService) {
        this.scanner = scanner;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
    }

    @Override
    public void execute() {
        try {
            List<Cliente> clientes = clienteService.listarClientes();

            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente cadastrado.");
                return;
            }

            System.out.println("\nClientes:");
            for (int i = 0; i < clientes.size(); i++) {
                System.out.println(i + " - " + clientes.get(i).getNome());
            }

            int clienteIndex = scanner.nextInt();
            scanner.nextLine();

            List<Produto> produtos = produtoService.listarProdutos();

            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto cadastrado.");
                return;
            }

            System.out.println("\nProdutos:");
            for (int i = 0; i < produtos.size(); i++) {
                System.out.println(i + " - " + produtos.get(i).getNome());
            }

            pedidoService.criarPedido(clienteIndex);
            int pedidoIndex = pedidoService.listarPedidos().size() - 1;

            int produtoIndex = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Digite a quantidade:");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            pedidoService.adicionarItem(pedidoIndex, produtoIndex, quantidade);

            System.out.println("Pedido criado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            scanner.nextLine();
        }
    }
}
