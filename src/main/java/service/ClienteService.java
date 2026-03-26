package service;

import model.Cliente;
import repository.ClienteRepository;

import java.util.List;

public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public void cadastrarCliente(String nome, String email) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }

        if (!emailValido(email)) {
            throw new IllegalArgumentException("Email inválido.");
        }

        Cliente cliente = new Cliente(nome, email);
        repository.salvar(cliente);
    }

    public List<Cliente> listarClientes() {
        return repository.listar();
    }

    private boolean emailValido(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}