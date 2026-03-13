import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<Cliente> clientes = new ArrayList<>();
        List<Produto> produtos = new ArrayList<>();
        List<Pedido> pedidos = new ArrayList<>();

        int opcao = 0;

        while (opcao != 7) {

            System.out.println("\n1 - Cadastrar cliente");
            System.out.println("2 - Cadastrar produto");
            System.out.println("3 - Criar pedido");
            System.out.println("4 - Listar pedidos");
            System.out.println("5 - Atualizar status do pedido");
            System.out.println("6 - Listar clientes");
            System.out.println("7 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {

                case 1: // cadastrar cliente
                    System.out.println("Digite o seu nome:");
                    String nomeCliente = scanner.nextLine();
                    System.out.println("Insira seu email:");
                    String email = scanner.nextLine();

                    Cliente cliente = new Cliente(nomeCliente, email);
                    clientes.add(cliente);

                    System.out.println("Cadastro de Cliente realizado!");
                    break;

                case 2: // cadastrar produto

                    System.out.println("Digite o nome do produto:");
                    String nomeProduto = scanner.nextLine();

                    System.out.println("Insira o preço do produto:");
                    double preco = scanner.nextDouble();
                    scanner.nextLine();

                    Produto produto = new Produto(nomeProduto, preco);
                    produtos.add(produto);
                    System.out.println("Cadastro de Produto realizado!");
                    break;

                case 3: // criar produto
                 if (clientes.isEmpty()) {
                     System.out.println("Nenhum cliente cadastrado.");
                        break;
                    }

                    if (produtos.isEmpty()) {
                        System.out.println("Nenhum produto cadastrado.");
                        break;
                    }

                    System.out.println("\nEscolha um cliente:");

                    for (int i = 0; i < clientes.size(); i++) {
                        System.out.println(i + " - " + clientes.get(i).getNome());
                    }

                    int clienteIndex = scanner.nextInt();
                    scanner.nextLine();

                    Cliente clienteSelecionado = clientes.get(clienteIndex);

                    Pedido pedido = new Pedido(clienteSelecionado);

                    System.out.println("\nEscolha um produto:");

                    for (int i = 0; i < produtos.size(); i++) {
                        System.out.println(i + " - " + produtos.get(i).getNome());
                    }

                    int produtoIndex = scanner.nextInt();
                    scanner.nextLine();

                    Produto produtoSelecionado = produtos.get(produtoIndex);

                    System.out.println("Digite a quantidade:");
                    int quantidade = scanner.nextInt();
                    scanner.nextLine();

                    ItemPedido item = new ItemPedido(produtoSelecionado, quantidade);

                    pedido.adicionarItem(item);

                    pedidos.add(pedido);

                    System.out.println("Pedido criado com sucesso!");

                    break;

                case 4: // listar pedidos

                    break;

                case 5: // atualizar status

                    break;

                case 6: // listar clientes
                if (clientes.isEmpty()) {
                    System.out.println("Nenhum cliente Registrado!");
                } else {
                    System.out.println("\nClientes cadastrados:");

                    for (Cliente clienteC : clientes) {
                        System.out.println(clienteC);
                    }
                }
                    break;

                case 7: // encerramento de sistema


                    System.out.println("Encerrando sistema...");
                    return;
                default:
                    System.out.println("Opção inválida!");
                    break;

            }

        }

        scanner.close();
    }
}