package repository;

import model.Produto;

import java.util.ArrayList;
import java.util.List;

public class InMemoryProdutoRepository implements ProdutoRepository {

    private final List<Produto> produtos = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void salvar(Produto produto) {
        if (produto.getId() == 0) {
            produto.setId(nextId++);
        }
        produtos.add(produto);
    }

    @Override
    public List<Produto> listar() {
        return new ArrayList<>(produtos);
    }

    @Override
    public java.util.Optional<Produto> buscarPorId(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return java.util.Optional.of(produto);
            }
        }
        return java.util.Optional.empty();
    }

    @Override
    public boolean estaVazio() {
        return produtos.isEmpty();
    }
}
