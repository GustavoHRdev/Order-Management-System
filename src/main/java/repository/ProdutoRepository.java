package repository;

import model.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {

    void salvar(Produto produto);

    List<Produto> listar();

    Optional<Produto> buscarPorId(int id);

    boolean estaVazio();
}
