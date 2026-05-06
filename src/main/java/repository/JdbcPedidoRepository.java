package repository;

import model.Cliente;
import model.ItemPedido;
import model.Pedido;
import model.Produto;
import model.StatusPedido;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class JdbcPedidoRepository implements PedidoRepository {

    private final DataSource dataSource;

    public JdbcPedidoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvar(Pedido pedido) {
        String sql = "INSERT INTO pedidos (cliente_id, status) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, pedido.getCliente().getId());
            statement.setString(2, pedido.getStatus().name());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    pedido.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao salvar pedido.", e);
        }
    }

    @Override
    public List<Pedido> listar() {
        String sql = """
                SELECT p.id AS pedido_id, p.status, c.id AS cliente_id, c.nome AS cliente_nome, c.email
                FROM pedidos p
                JOIN clientes c ON c.id = p.cliente_id
                ORDER BY p.id
                """;

        List<Pedido> pedidos = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                pedidos.add(mapPedidoBase(connection, resultSet));
            }
            return pedidos;
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao listar pedidos.", e);
        }
    }

    @Override
    public Optional<Pedido> buscarPorId(int id) {
        String sql = """
                SELECT p.id AS pedido_id, p.status, c.id AS cliente_id, c.nome AS cliente_nome, c.email
                FROM pedidos p
                JOIN clientes c ON c.id = p.cliente_id
                WHERE p.id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapPedidoBase(connection, resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao buscar pedido.", e);
        }
    }

    @Override
    public boolean estaVazio() {
        String sql = "SELECT 1 FROM pedidos FETCH FIRST ROW ONLY";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return !resultSet.next();
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao verificar pedidos.", e);
        }
    }

    @Override
    public void adicionarItem(int pedidoId, ItemPedido itemPedido) {
        String sql = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pedidoId);
            statement.setInt(2, itemPedido.getProduto().getId());
            statement.setInt(3, itemPedido.getQuantidade());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao adicionar item no pedido.", e);
        }
    }

    @Override
    public void atualizarStatus(int pedidoId, StatusPedido statusPedido) {
        String sql = "UPDATE pedidos SET status = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, statusPedido.name());
            statement.setInt(2, pedidoId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao atualizar status do pedido.", e);
        }
    }

    @Override
    public void removerEntregues() {
        String sql = "DELETE FROM pedidos WHERE status = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, StatusPedido.ENTREGUE.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao remover pedidos entregues.", e);
        }
    }

    private Pedido mapPedidoBase(Connection connection, ResultSet resultSet) throws SQLException {
        int pedidoId = resultSet.getInt("pedido_id");
        Cliente cliente = new Cliente(
                resultSet.getInt("cliente_id"),
                resultSet.getString("cliente_nome"),
                resultSet.getString("email")
        );

        return new Pedido(
                pedidoId,
                cliente,
                carregarItens(connection, pedidoId),
                StatusPedido.valueOf(resultSet.getString("status"))
        );
    }

    private List<ItemPedido> carregarItens(Connection connection, int pedidoId) throws SQLException {
        String sql = """
                SELECT pr.id, pr.nome, pr.preco, ip.quantidade
                FROM itens_pedido ip
                JOIN produtos pr ON pr.id = ip.produto_id
                WHERE ip.pedido_id = ?
                ORDER BY ip.id
                """;

        List<ItemPedido> itens = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pedidoId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Produto produto = new Produto(
                            resultSet.getInt("id"),
                            resultSet.getString("nome"),
                            resultSet.getDouble("preco")
                    );
                    itens.add(new ItemPedido(produto, resultSet.getInt("quantidade")));
                }
            }
        }
        return itens;
    }
}
