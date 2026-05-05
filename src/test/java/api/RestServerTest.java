package api;

import app.ApplicationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryClienteRepository;
import repository.InMemoryPedidoRepository;
import repository.InMemoryProdutoRepository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestServerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private RestServer restServer;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        ApplicationContext applicationContext = new ApplicationContext(
                new InMemoryClienteRepository(),
                new InMemoryProdutoRepository(),
                new InMemoryPedidoRepository()
        );

        restServer = new RestServer(applicationContext, 0);
        restServer.start();
        baseUrl = "http://localhost:" + restServer.getPort();
    }

    @AfterEach
    void tearDown() {
        restServer.stop();
    }

    @Test
    void postClientesDeveCriarClienteERetornar201() throws Exception {
        HttpResponse<String> response = sendJsonRequest(
                "POST",
                "/clientes",
                """
                {
                  "nome": "Ana",
                  "email": "ana@example.com"
                }
                """
        );

        JsonNode body = objectMapper.readTree(response.body());
        String location = response.headers().firstValue("Location").orElseThrow();

        assertEquals(201, response.statusCode());
        assertTrue(location.endsWith("/clientes/" + body.get("id").asInt()));
        assertEquals("Ana", body.get("nome").asText());
        assertEquals("ana@example.com", body.get("email").asText());
        assertTrue(body.get("id").asInt() > 0);
    }

    @Test
    void postPedidosDeveRetornar404QuandoClienteNaoExiste() throws Exception {
        createCliente("Ana", "ana@example.com");

        HttpResponse<String> response = sendJsonRequest(
                "POST",
                "/pedidos",
                """
                {
                  "clienteId": 999
                }
                """
        );

        JsonNode body = objectMapper.readTree(response.body());

        assertEquals(404, response.statusCode());
        assertEquals("Not Found", body.get("error").asText());
        assertEquals("Cliente inválido!", body.get("message").asText());
        assertEquals("/pedidos", body.get("path").asText());
    }

    @Test
    void patchStatusDeveAtualizarPedidoQuandoFluxoEhValido() throws Exception {
        int clienteId = createCliente("Ana", "ana@example.com");
        int produtoId = createProduto("Mouse", 50.0);
        int pedidoId = createPedido(clienteId);

        sendJsonRequest(
                "POST",
                "/pedidos/" + pedidoId + "/itens",
                """
                {
                  "produtoId": %d,
                  "quantidade": 2
                }
                """.formatted(produtoId)
        );

        HttpResponse<String> response = sendJsonRequest(
                "PATCH",
                "/pedidos/" + pedidoId + "/status",
                """
                {
                  "status": "PROCESSANDO"
                }
                """
        );

        JsonNode body = objectMapper.readTree(response.body());

        assertEquals(200, response.statusCode());
        assertEquals("PROCESSANDO", body.get("status").asText());
        assertEquals(100.0, body.get("total").asDouble(), 0.0001);
    }

    @Test
    void postClientesDeveRetornar400QuandoJsonEhInvalido() throws Exception {
        HttpResponse<String> response = sendJsonRequest(
                "POST",
                "/clientes",
                """
                {
                  "nome": "Ana",
                """
        );

        JsonNode body = objectMapper.readTree(response.body());

        assertEquals(400, response.statusCode());
        assertEquals("Bad Request", body.get("error").asText());
        assertEquals("JSON inválido.", body.get("message").asText());
    }

    @Test
    void getClientesComMetodoNaoPermitidoDeveRetornar405() throws Exception {
        HttpResponse<String> response = sendJsonRequest(
                "DELETE",
                "/clientes",
                ""
        );

        JsonNode body = objectMapper.readTree(response.body());

        assertEquals(405, response.statusCode());
        assertEquals("GET, POST", response.headers().firstValue("Allow").orElseThrow());
        assertEquals("Method Not Allowed", body.get("error").asText());
        assertEquals("Método não permitido.", body.get("message").asText());
    }

    @Test
    void patchStatusDeveRetornar400QuandoStatusEhInvalido() throws Exception {
        int clienteId = createCliente("Ana", "ana@example.com");
        int pedidoId = createPedido(clienteId);

        HttpResponse<String> response = sendJsonRequest(
                "PATCH",
                "/pedidos/" + pedidoId + "/status",
                """
                {
                  "status": "QUALQUER_COISA"
                }
                """
        );

        JsonNode body = objectMapper.readTree(response.body());

        assertEquals(400, response.statusCode());
        assertEquals("Bad Request", body.get("error").asText());
        assertEquals("Status inválido.", body.get("message").asText());
    }

    private int createCliente(String nome, String email) throws Exception {
        HttpResponse<String> response = sendJsonRequest(
                "POST",
                "/clientes",
                """
                {
                  "nome": "%s",
                  "email": "%s"
                }
                """.formatted(nome, email)
        );

        return objectMapper.readTree(response.body()).get("id").asInt();
    }

    private int createProduto(String nome, double preco) throws Exception {
        HttpResponse<String> response = sendJsonRequest(
                "POST",
                "/produtos",
                """
                {
                  "nome": "%s",
                  "preco": %s
                }
                """.formatted(nome, preco)
        );

        return objectMapper.readTree(response.body()).get("id").asInt();
    }

    private int createPedido(int clienteId) throws Exception {
        HttpResponse<String> response = sendJsonRequest(
                "POST",
                "/pedidos",
                """
                {
                  "clienteId": %d
                }
                """.formatted(clienteId)
        );

        return objectMapper.readTree(response.body()).get("id").asInt();
    }

    private HttpResponse<String> sendJsonRequest(String method, String path, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json")
                .method(method, HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
}
