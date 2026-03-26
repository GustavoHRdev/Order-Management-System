package repository;

import model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    private final List<Produto> produtos = new ArrayList<>();

    public void salvar(Produto produto) {
        produtos.add(produto);
    }

    public List<Produto> listar() {
        return new ArrayList<>(produtos);
    }

    public Produto buscarPorIndex(int index) {
        if (index < 0 || index >= produtos.size()) {
            throw new IllegalArgumentException("Produto inválido!");
        }
        return produtos.get(index);
    }

    public boolean estaVazio() {
        return produtos.isEmpty();
    }
}
