package repository;

import model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class InMemoryClienteRepository implements ClienteRepository {

    private final List<Cliente> clientes = new ArrayList<>();

    @Override
    public void salvar(Cliente cliente) {
        clientes.add(cliente);
    }

    @Override
    public List<Cliente> listar() {
        return new ArrayList<>(clientes);
    }

    @Override
    public Cliente buscarPorId(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }

        throw new IllegalArgumentException("Cliente inválido!");
    }

    @Override
    public boolean estaVazio() {
        return clientes.isEmpty();
    }
}
