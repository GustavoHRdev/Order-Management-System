package api;

import app.OrderManagementApplication;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OrderManagementApplication.class, properties = {
        "spring.datasource.url=jdbc:h2:mem:api-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@AutoConfigureMockMvc
public class RestServerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM itens_pedido");
        jdbcTemplate.execute("DELETE FROM pedidos");
        jdbcTemplate.execute("DELETE FROM produtos");
        jdbcTemplate.execute("DELETE FROM clientes");
    }

    @Test
    void postClientesDeveCriarClienteERetornar201() throws Exception {
        var result = mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Ana",
                                  "email": "ana@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode body = objectMapper.readTree(response);
        String location = result.getResponse().getHeader("Location");

        assertEquals("Ana", body.get("nome").asText());
        assertEquals("ana@example.com", body.get("email").asText());
        assertTrue(body.get("id").asInt() > 0);
        assertTrue(location.endsWith("/clientes/" + body.get("id").asInt()));
    }

    @Test
    void postPedidosDeveRetornar404QuandoClienteNaoExiste() throws Exception {
        createCliente("Ana", "ana@example.com");

        String response = mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clienteId": 999
                                }
                                """))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode body = objectMapper.readTree(response);

        assertEquals("Not Found", body.get("error").asText());
        assertEquals("Cliente inválido!", body.get("message").asText());
        assertEquals("/pedidos", body.get("path").asText());
    }

    @Test
    void patchStatusDeveAtualizarPedidoQuandoFluxoEhValido() throws Exception {
        int clienteId = createCliente("Ana", "ana@example.com");
        int produtoId = createProduto("Mouse", 50.0);
        int pedidoId = createPedido(clienteId);

        mockMvc.perform(post("/pedidos/" + pedidoId + "/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "produtoId": %d,
                                  "quantidade": 2
                                }
                                """.formatted(produtoId)))
                .andExpect(status().isOk());

        String response = mockMvc.perform(patch("/pedidos/" + pedidoId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "PROCESSANDO"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode body = objectMapper.readTree(response);

        assertEquals("PROCESSANDO", body.get("status").asText());
        assertEquals(100.0, body.get("total").asDouble(), 0.0001);
    }

    @Test
    void postClientesDeveRetornar400QuandoJsonEhInvalido() throws Exception {
        String response = mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Ana",
                                """))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode body = objectMapper.readTree(response);

        assertEquals("Bad Request", body.get("error").asText());
        assertEquals("JSON inválido.", body.get("message").asText());
    }

    @Test
    void getClientesComMetodoNaoPermitidoDeveRetornar405() throws Exception {
        String response = mockMvc.perform(delete("/clientes"))
                .andExpect(status().isMethodNotAllowed())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode body = objectMapper.readTree(response);

        String allowHeader = mockMvc.perform(delete("/clientes"))
                .andReturn()
                .getResponse()
                .getHeader("Allow");
        assertTrue(allowHeader.contains("GET"));
        assertTrue(allowHeader.contains("POST"));
        assertEquals("Method Not Allowed", body.get("error").asText());
        assertEquals("Método não permitido.", body.get("message").asText());
    }

    @Test
    void patchStatusDeveRetornar400QuandoStatusEhInvalido() throws Exception {
        int clienteId = createCliente("Ana", "ana@example.com");
        int pedidoId = createPedido(clienteId);

        String response = mockMvc.perform(patch("/pedidos/" + pedidoId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "QUALQUER_COISA"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode body = objectMapper.readTree(response);

        assertEquals("Bad Request", body.get("error").asText());
        assertEquals("Status inválido.", body.get("message").asText());
    }

    @Test
    void postClientesDeveRetornar400QuandoPayloadNaoPassaNaValidacao() throws Exception {
        String response = mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "",
                                  "email": "invalido"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode body = objectMapper.readTree(response);

        assertEquals("Bad Request", body.get("error").asText());
        assertEquals("nome: não deve estar em branco.", body.get("message").asText());
    }

    private int createCliente(String nome, String email) throws Exception {
        String response = mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "%s",
                                  "email": "%s"
                                }
                                """.formatted(nome, email)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asInt();
    }

    private int createProduto(String nome, double preco) throws Exception {
        String response = mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "%s",
                                  "preco": %s
                                }
                                """.formatted(nome, preco)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asInt();
    }

    private int createPedido(int clienteId) throws Exception {
        String response = mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clienteId": %d
                                }
                                """.formatted(clienteId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asInt();
    }
}
