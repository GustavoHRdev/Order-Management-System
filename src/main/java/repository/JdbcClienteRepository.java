package repository;

import model.Cliente;
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
public class JdbcClienteRepository implements ClienteRepository {

    private final DataSource dataSource;

    public JdbcClienteRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, email) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getEmail());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    cliente.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao salvar cliente.", e);
        }
    }

    @Override
    public List<Cliente> listar() {
        String sql = "SELECT id, nome, email FROM clientes ORDER BY id";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                clientes.add(mapCliente(resultSet));
            }
            return clientes;
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao listar clientes.", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(int id) {
        String sql = "SELECT id, nome, email FROM clientes WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapCliente(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao buscar cliente.", e);
        }
    }

    @Override
    public boolean estaVazio() {
        String sql = "SELECT 1 FROM clientes FETCH FIRST ROW ONLY";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return !resultSet.next();
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao verificar clientes.", e);
        }
    }

    private Cliente mapCliente(ResultSet resultSet) throws SQLException {
        return new Cliente(
                resultSet.getInt("id"),
                resultSet.getString("nome"),
                resultSet.getString("email")
        );
    }
}
