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
                    String nomecliente = scanner.nextLine();
                    System.out.println("Insira seu email:");
                    String email = scanner.nextLine();

                    Cliente cliente = new Cliente(nomecliente, email);
                    clientes.add(cliente);

                    System.out.println("Cadastro de Cliente realizado!");
                    break;

                case 2: // cadastrar produto

                    break;

                case 3: // criar produto

                    break;

                case 4: // listar pedidos

                    break;

                case 5: // atualizar status

                    break;

                case 6: // listar clientes

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