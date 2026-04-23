package repository;

import model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    private  final List<Cliente> clientes = new ArrayList<>();

    public void salvar(Cliente cliente){
        clientes.add(cliente);
    }
    public List<Cliente> listar(){
        return new ArrayList<>(clientes);
    }

    public Cliente buscarPorId(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }

        throw new IllegalArgumentException("Cliente inválido!");
    }

    public boolean estaVazio() {
        return clientes.isEmpty();
    }
}
