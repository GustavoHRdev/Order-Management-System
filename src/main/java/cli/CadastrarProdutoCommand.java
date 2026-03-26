package cli;

import service.ProdutoService;

import java.util.Scanner;

public class CadastrarProdutoCommand implements Command {

    private final Scanner scanner;
    private final ProdutoService produtoService;

    public CadastrarProdutoCommand(Scanner scanner, ProdutoService produtoService) {
        this.scanner = scanner;
        this.produtoService = produtoService;
    }

    @Override
    public void execute() {
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
    }
}
