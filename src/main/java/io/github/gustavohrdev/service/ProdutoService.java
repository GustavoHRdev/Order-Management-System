package io.github.gustavohrdev.service;

import io.github.gustavohrdev.exception.InvalidProductNameException;
import io.github.gustavohrdev.exception.InvalidProductPriceException;
import io.github.gustavohrdev.exception.ProdutoNotFoundException;
import io.github.gustavohrdev.model.Produto;
import org.springframework.stereotype.Service;
import io.github.gustavohrdev.repository.ProdutoRepository;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto cadastrarProduto(String nome, double preco) {

        if (nome == null || nome.isBlank()) {
            throw new InvalidProductNameException();
        }

        if (preco <= 0) {
            throw new InvalidProductPriceException();
        }

        Produto produto = new Produto(nome, preco);
        repository.salvar(produto);
        return produto;
    }

    public List<Produto> listarProdutos() {
        return repository.listar();
    }

    public Produto buscarProdutoPorId(int id) {
        return repository.buscarPorId(id)
                .orElseThrow(ProdutoNotFoundException::new);
    }
}

