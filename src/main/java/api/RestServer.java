package api;

import app.ApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Cliente;
import model.ItemPedido;
import model.Pedido;
import model.Produto;
import model.StatusPedido;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
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

            writeJson(exchange, 404, new ErrorResponse("Rota não encontrada."));
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

            writeJson(exchange, 404, new ErrorResponse("Rota não encontrada."));
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

            writeJson(exchange, 404, new ErrorResponse("Rota não encontrada."));
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
        if (exception instanceof IllegalArgumentException || exception instanceof IllegalStateException) {
            int status = NOT_FOUND_MESSAGES.contains(exception.getMessage()) ? 404 : 400;
            writeJson(exchange, status, new ErrorResponse(exception.getMessage()));
            return;
        }

        writeJson(exchange, 500, new ErrorResponse("Erro interno do servidor."));
    }

    private void writeJson(HttpExchange exchange, int statusCode, Object body) throws IOException {
        byte[] response = objectMapper.writeValueAsString(body).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response);
        }
    }

    public record CriarClienteRequest(String nome, String email) {
    }

    public record CriarProdutoRequest(String nome, double preco) {
    }

    public record CriarPedidoRequest(int clienteId) {
    }

    public record AdicionarItemRequest(int produtoId, int quantidade) {
    }

    public record AtualizarStatusRequest(String status) {
    }

    public record ErrorResponse(String message) {
    }

    public record ClienteResponse(int id, String nome, String email) {
        public static ClienteResponse from(Cliente cliente) {
            return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail());
        }
    }

    public record ProdutoResponse(int id, String nome, double preco) {
        public static ProdutoResponse from(Produto produto) {
            return new ProdutoResponse(produto.getId(), produto.getNome(), produto.getPreco());
        }
    }

    public record ItemPedidoResponse(int produtoId, String produtoNome, double precoUnitario, int quantidade, double subtotal) {
        public static ItemPedidoResponse from(ItemPedido itemPedido) {
            return new ItemPedidoResponse(
                    itemPedido.getProduto().getId(),
                    itemPedido.getProduto().getNome(),
                    itemPedido.getProduto().getPreco(),
                    itemPedido.getQuantidade(),
                    itemPedido.calcularSubtotal()
            );
        }
    }

    public record PedidoResponse(int id,
                                 int clienteId,
                                 String clienteNome,
                                 String status,
                                 List<ItemPedidoResponse> itens,
                                 double total) {
        public static PedidoResponse from(Pedido pedido) {
            return new PedidoResponse(
                    pedido.getId(),
                    pedido.getCliente().getId(),
                    pedido.getCliente().getNome(),
                    pedido.getStatus().name(),
                    pedido.getItens().stream().map(ItemPedidoResponse::from).toList(),
                    pedido.calcularTotal()
            );
        }
    }
}
