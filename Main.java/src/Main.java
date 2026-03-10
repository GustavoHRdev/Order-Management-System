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

            if (opcao == 1) {

                System.out.print("Digite o nome do cliente: ");
                String nome = scanner.nextLine();

                System.out.println("Digite o email do cliente: ");
                String email = scanner.nextLine();

                Cliente cliente = new Cliente(nome, email);
                clientes.add(cliente);
                System.out.println("Cliente cadastrado com sucesso!");
            }
            

        }

        scanner.close();
    }
}