package cli;

import model.Cliente;
import model.Pedido;
import model.Produto;
import service.ClienteService;
import service.PedidoService;
import service.ProdutoService;

import java.util.List;

public class CriarPedidoCommand implements Command {

    private final InputReader inputReader;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    public CriarPedidoCommand(InputReader inputReader,
                              ClienteService clienteService,
                              ProdutoService produtoService,
                              PedidoService pedidoService) {
        this.inputReader = inputReader;
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
            for (Cliente cliente : clientes) {
                System.out.println(cliente.getId() + " - " + cliente.getNome());
            }

            int clienteId = inputReader.readInt("Digite o ID do cliente:");

            List<Produto> produtos = produtoService.listarProdutos();

            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto cadastrado.");
                return;
            }

            System.out.println("\nProdutos:");
            for (Produto produto : produtos) {
                System.out.println(produto.getId() + " - " + produto.getNome());
            }

            Pedido pedido = pedidoService.criarPedido(clienteId);

            int produtoId = inputReader.readInt("Digite o ID do produto:");

            int quantidade = inputReader.readInt("Digite a quantidade:");

            pedidoService.adicionarItem(pedido.getId(), produtoId, quantidade);

            System.out.println("Pedido criado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
