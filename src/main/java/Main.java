import model.*;
import repository.*;
import service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            // Repositories
            ClienteRepository clienteRepository = new ClienteRepository();
            ProdutoRepository produtoRepository = new ProdutoRepository();
            PedidoRepository pedidoRepository = new PedidoRepository();

            // Services
            ClienteService clienteService = new ClienteService(clienteRepository);
            ProdutoService produtoService = new ProdutoService(produtoRepository);
            PedidoService pedidoService = new PedidoService(
                    pedidoRepository,
                    clienteRepository,
                    produtoRepository
            );

            Map<Integer, Runnable> actions = new HashMap<>();
            boolean[] running = {true};

            actions.put(1, () -> { // cadastrar cliente
                try {
                    System.out.println("Digite o nome:");
                    String nome = scanner.nextLine();

                    System.out.println("Digite o email:");
                    String email = scanner.nextLine();

                    clienteService.cadastrarCliente(nome, email);
                    System.out.println("Cliente cadastrado com sucesso!");

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });

            actions.put(2, () -> { // cadastrar produto
                try {
                    System.out.println("Digite o nome do produto:");
                    String nomeProduto = scanner.nextLine();

                    System.out.println("Digite o preço:");
                    double preco = scanner.nextDouble();
                    scanner.nextLine();

                    produtoService.cadastrarProduto(nomeProduto, preco);
                    System.out.println("Produto cadastrado com sucesso!");

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    scanner.nextLine();
                }
            });

            actions.put(3, () -> { // criar pedido
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
            });

            actions.put(4, () -> { // listar pedidos
                List<Pedido> pedidos = pedidoService.listarPedidos();

                if (pedidos.isEmpty()) {
                    System.out.println("Nenhum pedido registrado.");
                } else {
                    System.out.println("\nPedidos:");
                    for (Pedido pedido : pedidos) {
                        System.out.println(pedido);
                    }
                }
            });

            actions.put(5, () -> { // atualizar status
                try {
                    List<Pedido> pedidosLista = pedidoService.listarPedidos();

                    if (pedidosLista.isEmpty()) {
                        System.out.println("Nenhum pedido registrado.");
                        return;
                    }

                    System.out.println("\nPedidos:");
                    for (int i = 0; i < pedidosLista.size(); i++) {
                        System.out.println(i + " - " + pedidosLista.get(i));
                    }

                    int pedidoIndex = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("\nEscolha o status:");
                    System.out.println("1 - PENDENTE");
                    System.out.println("2 - PROCESSANDO");
                    System.out.println("3 - ENVIADO");
                    System.out.println("4 - ENTREGUE");
                    System.out.println("5 - CANCELADO");

                    int statusOpcao = scanner.nextInt();
                    scanner.nextLine();

                    Map<Integer, StatusPedido> statusMap = new HashMap<>();
                    statusMap.put(1, StatusPedido.PENDENTE);
                    statusMap.put(2, StatusPedido.PROCESSANDO);
                    statusMap.put(3, StatusPedido.ENVIADO);
                    statusMap.put(4, StatusPedido.ENTREGUE);
                    statusMap.put(5, StatusPedido.CANCELADO);

                    StatusPedido status = statusMap.get(statusOpcao);
                    if (status == null) {
                        System.out.println("Status inválido!");
                        return;
                    }

                    pedidoService.atualizarStatus(pedidoIndex, status);
                    System.out.println("Status atualizado com sucesso!");

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    scanner.nextLine();
                }
            });

            actions.put(6, () -> { // listar clientes
                List<Cliente> listaClientes = clienteService.listarClientes();

                if (listaClientes.isEmpty()) {
                    System.out.println("Nenhum cliente cadastrado.");
                } else {
                    System.out.println("\nClientes:");
                    for (Cliente cliente : listaClientes) {
                        System.out.println(cliente);
                    }
                }
            });

            actions.put(7, () -> {
                System.out.println("Encerrando sistema...");
                running[0] = false;
            });

            while (running[0]) {

                System.out.println("\n1 - Cadastrar cliente");
                System.out.println("2 - Cadastrar produto");
                System.out.println("3 - Criar pedido");
                System.out.println("4 - Listar pedidos");
                System.out.println("5 - Atualizar status do pedido");
                System.out.println("6 - Listar clientes");
                System.out.println("7 - Sair");
                System.out.print("Escolha uma opção: ");

                try {
                    int opcao = scanner.nextInt();
                    scanner.nextLine();

                    Runnable action = actions.get(opcao);
                    if (action == null) {
                        System.out.println("Opção inválida!");
                        continue;
                    }

                    action.run();
                } catch (Exception e) {
                    System.out.println("Entrada inválida!");
                    scanner.nextLine();
                }
            }
        }
    }
}
