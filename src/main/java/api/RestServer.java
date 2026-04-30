package api;

import api.dto.request.AdicionarItemRequest;
import api.dto.request.AtualizarStatusRequest;
import api.dto.request.CriarClienteRequest;
import api.dto.request.CriarPedidoRequest;
import api.dto.request.CriarProdutoRequest;
import api.dto.response.ApiErrorResponse;
import api.dto.response.ClienteResponse;
import api.dto.response.PedidoResponse;
import api.dto.response.ProdutoResponse;
import app.ApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Cliente;
import model.Pedido;
import model.Produto;
import model.StatusPedido;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class RestServer {

    private static final Set<String> NOT_FOUND_MESSAGES = Set.of(
            "Cliente inválido!",
            "Produto inválido!",
            "Pedido inválido!"
    );

    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;
    private final HttpServer httpServer;

    public RestServer(ApplicationContext applicationContext, int port) {
        try {
            this.applicationContext = applicationContext;
            this.objectMapper = new ObjectMapper().findAndRegisterModules();
            this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            registerContexts();
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao iniciar servidor HTTP.", e);
        }
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public int getPort() {
        return httpServer.getAddress().getPort();
    }

    private void registerContexts() {
        httpServer.createContext("/clientes", this::handleClientes);
        httpServer.createContext("/produtos", this::handleProdutos);
        httpServer.createContext("/pedidos", this::handlePedidos);
    }

    private void handleClientes(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("/clientes".equals(path) && "GET".equalsIgnoreCase(method)) {
                List<ClienteResponse> response = applicationContext.getClienteService()
                        .listarClientes()
                        .stream()
                        .map(ClienteResponse::from)
                        .toList();
                writeJson(exchange, 200, response);
                return;
            }

            if ("/clientes".equals(path) && "POST".equalsIgnoreCase(method)) {
                CriarClienteRequest request = readBody(exchange, CriarClienteRequest.class);
                Cliente cliente = applicationContext.getClienteService()
                        .cadastrarCliente(request.nome(), request.email());
                writeJson(exchange, 201, ClienteResponse.from(cliente));
                return;
            }

            if (path.startsWith("/clientes/") && "GET".equalsIgnoreCase(method)) {
                int clienteId = parseId(path, "/clientes/");
                Cliente cliente = applicationContext.getClienteService().buscarClientePorId(clienteId);
                writeJson(exchange, 200, ClienteResponse.from(cliente));
                return;
            }

            writeRouteNotFound(exchange);
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handleProdutos(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("/produtos".equals(path) && "GET".equalsIgnoreCase(method)) {
                List<ProdutoResponse> response = applicationContext.getProdutoService()
                        .listarProdutos()
                        .stream()
                        .map(ProdutoResponse::from)
                        .toList();
                writeJson(exchange, 200, response);
                return;
            }

            if ("/produtos".equals(path) && "POST".equalsIgnoreCase(method)) {
                CriarProdutoRequest request = readBody(exchange, CriarProdutoRequest.class);
                Produto produto = applicationContext.getProdutoService()
                        .cadastrarProduto(request.nome(), request.preco());
                writeJson(exchange, 201, ProdutoResponse.from(produto));
                return;
            }

            if (path.startsWith("/produtos/") && "GET".equalsIgnoreCase(method)) {
                int produtoId = parseId(path, "/produtos/");
                Produto produto = applicationContext.getProdutoService().buscarProdutoPorId(produtoId);
                writeJson(exchange, 200, ProdutoResponse.from(produto));
                return;
            }

            writeRouteNotFound(exchange);
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handlePedidos(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("/pedidos".equals(path) && "GET".equalsIgnoreCase(method)) {
                List<PedidoResponse> response = applicationContext.getPedidoService()
                        .listarPedidos()
                        .stream()
                        .map(PedidoResponse::from)
                        .toList();
                writeJson(exchange, 200, response);
                return;
            }

            if ("/pedidos".equals(path) && "POST".equalsIgnoreCase(method)) {
                CriarPedidoRequest request = readBody(exchange, CriarPedidoRequest.class);
                Pedido pedido = applicationContext.getPedidoService().criarPedido(request.clienteId());
                writeJson(exchange, 201, PedidoResponse.from(pedido));
                return;
            }

            if (path.matches("^/pedidos/\\d+$") && "GET".equalsIgnoreCase(method)) {
                int pedidoId = parseId(path, "/pedidos/");
                Pedido pedido = applicationContext.getPedidoService().buscarPedidoPorId(pedidoId);
                writeJson(exchange, 200, PedidoResponse.from(pedido));
                return;
            }

            if (path.matches("^/pedidos/\\d+/itens$") && "POST".equalsIgnoreCase(method)) {
                int pedidoId = parseId(path.substring(0, path.lastIndexOf("/itens")), "/pedidos/");
                AdicionarItemRequest request = readBody(exchange, AdicionarItemRequest.class);
                applicationContext.getPedidoService().adicionarItem(pedidoId, request.produtoId(), request.quantidade());
                Pedido pedido = applicationContext.getPedidoService().buscarPedidoPorId(pedidoId);
                writeJson(exchange, 200, PedidoResponse.from(pedido));
                return;
            }

            if (path.matches("^/pedidos/\\d+/status$") && "PATCH".equalsIgnoreCase(method)) {
                int pedidoId = parseId(path.substring(0, path.lastIndexOf("/status")), "/pedidos/");
                AtualizarStatusRequest request = readBody(exchange, AtualizarStatusRequest.class);
                applicationContext.getPedidoService().atualizarStatus(pedidoId, StatusPedido.valueOf(request.status()));
                Pedido pedido = applicationContext.getPedidoService().buscarPedidoPorId(pedidoId);
                writeJson(exchange, 200, PedidoResponse.from(pedido));
                return;
            }

            writeRouteNotFound(exchange);
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private int parseId(String path, String prefix) {
        try {
            return Integer.parseInt(path.substring(prefix.length()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID inválido.");
        }
    }

    private <T> T readBody(HttpExchange exchange, Class<T> type) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return objectMapper.readValue(inputStream, type);
        }
    }

    private void handleException(HttpExchange exchange, Exception exception) throws IOException {
        if (exception instanceof JsonProcessingException) {
            writeError(exchange, 400, "Bad Request", "JSON inválido.");
            return;
        }

        if (exception instanceof IllegalArgumentException || exception instanceof IllegalStateException) {
            int status = NOT_FOUND_MESSAGES.contains(exception.getMessage()) ? 404 : 400;
            String error = status == 404 ? "Not Found" : "Bad Request";
            writeError(exchange, status, error, exception.getMessage());
            return;
        }

        writeError(exchange, 500, "Internal Server Error", "Erro interno do servidor.");
    }

    private void writeRouteNotFound(HttpExchange exchange) throws IOException {
        writeError(exchange, 404, "Not Found", "Rota não encontrada.");
    }

    private void writeError(HttpExchange exchange, int statusCode, String error, String message) throws IOException {
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now().toString(),
                statusCode,
                error,
                message,
                exchange.getRequestURI().getPath()
        );
        writeJson(exchange, statusCode, response);
    }

    private void writeJson(HttpExchange exchange, int statusCode, Object body) throws IOException {
        byte[] response = objectMapper.writeValueAsString(body).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response);
        }
    }
}
