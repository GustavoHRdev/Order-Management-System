package repository;

import model.Produto;

import java.util.ArrayList;
import java.util.List;

public class InMemoryProdutoRepository implements ProdutoRepository {

    private final List<Produto> produtos = new ArrayList<>();

    @Override
    public void salvar(Produto produto) {
        produtos.add(produto);
    }

    @Override
    public List<Produto> listar() {
        return new ArrayList<>(produtos);
    }

    @Override
    public Produto buscarPorId(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }

        throw new IllegalArgumentException("Produto inválido!");
    }

    @Override
    public boolean estaVazio() {
        return produtos.isEmpty();
    }
}
