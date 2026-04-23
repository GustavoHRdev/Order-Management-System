package repository;

import model.Produto;

import java.util.List;

public interface ProdutoRepository {

    void salvar(Produto produto);

    List<Produto> listar();

    Produto buscarPorId(int id);

    boolean estaVazio();
}
