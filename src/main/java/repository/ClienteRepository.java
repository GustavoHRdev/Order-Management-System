package repository;

import model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    void salvar(Cliente cliente);

    List<Cliente> listar();

    Optional<Cliente> buscarPorId(int id);

    boolean estaVazio();
}
