package service;

import model.Produto;
import repository.ProdutoRepository;

import java.util.List;

public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public void cadastrarProduto(String nome, double preco) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do produto inválido.");
        }

        if (preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero.");
        }

        Produto produto = new Produto(nome, preco);
        repository.salvar(produto);
    }

    public List<Produto> listarProdutos() {
        return repository.listar();
    }
}