package io.github.gustavohrdev.service;

import io.github.gustavohrdev.exception.InvalidClientNameException;
import io.github.gustavohrdev.exception.InvalidEmailException;
import io.github.gustavohrdev.model.Cliente;
import org.junit.jupiter.api.Test;
import io.github.gustavohrdev.repository.ClienteRepository;
import io.github.gustavohrdev.repository.InMemoryClienteRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClienteServiceTest {

    @Test
    void cadastrarClienteDeveSalvarClienteValido() {
        ClienteRepository repository = new InMemoryClienteRepository();
        ClienteService service = new ClienteService(repository);

        service.cadastrarCliente("Ana", "ana@example.com");

        List<Cliente> clientes = service.listarClientes();
        assertEquals(1, clientes.size());
        assertEquals("Ana", clientes.get(0).getNome());
        assertEquals("ana@example.com", clientes.get(0).getEmail());
    }

    @Test
    void cadastrarClienteDeveFalharComNomeInvalido() {
        ClienteRepository repository = new InMemoryClienteRepository();
        ClienteService service = new ClienteService(repository);

        assertThrows(InvalidClientNameException.class,
                () -> service.cadastrarCliente(" ", "ana@example.com"));
    }

    @Test
    void cadastrarClienteDeveFalharComEmailInvalido() {
        ClienteRepository repository = new InMemoryClienteRepository();
        ClienteService service = new ClienteService(repository);

        assertThrows(InvalidEmailException.class,
                () -> service.cadastrarCliente("Ana", "email-invalido"));
    }
}

