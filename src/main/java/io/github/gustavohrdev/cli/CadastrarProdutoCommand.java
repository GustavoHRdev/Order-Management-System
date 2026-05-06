package io.github.gustavohrdev.cli;

import io.github.gustavohrdev.service.ProdutoService;

public class CadastrarProdutoCommand implements Command {

    private final InputReader inputReader;
    private final ProdutoService produtoService;

    public CadastrarProdutoCommand(InputReader inputReader, ProdutoService produtoService) {
        this.inputReader = inputReader;
        this.produtoService = produtoService;
    }

    @Override
    public void execute() {
        try {
            String nomeProduto = inputReader.readLine("Digite o nome do produto:");
            double preco = inputReader.readDouble("Digite o preço:");

            produtoService.cadastrarProduto(nomeProduto, preco);
            System.out.println("Produto cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

