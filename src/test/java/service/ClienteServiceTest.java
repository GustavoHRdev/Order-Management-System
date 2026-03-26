package service;

import model.Cliente;
import org.junit.jupiter.api.Test;
import repository.ClienteRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClienteServiceTest {

    @Test
    void cadastrarClienteDeveSalvarClienteValido() {
        ClienteRepository repository = new ClienteRepository();
        ClienteService service = new ClienteService(repository);

        service.cadastrarCliente("Ana", "ana@example.com");

        List<Cliente> clientes = service.listarClientes();
        assertEquals(1, clientes.size());
        assertEquals("Ana", clientes.get(0).getNome());
        assertEquals("ana@example.com", clientes.get(0).getEmail());
    }

    @Test
    void cadastrarClienteDeveFalharComNomeInvalido() {
        ClienteRepository repository = new ClienteRepository();
        ClienteService service = new ClienteService(repository);

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrarCliente(" ", "ana@example.com"));
    }

    @Test
    void cadastrarClienteDeveFalharComEmailInvalido() {
        ClienteRepository repository = new ClienteRepository();
        ClienteService service = new ClienteService(repository);

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrarCliente("Ana", "email-invalido"));
    }
}
