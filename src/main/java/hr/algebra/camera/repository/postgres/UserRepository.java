package hr.algebra.camera.repository.postgres;

import hr.algebra.camera.exception.DatabaseOperationException;
import hr.algebra.camera.model.User;
import hr.algebra.camera.model.enums.UserRole;
import hr.algebra.camera.repository.IUserRepository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IUserRepository {
    private static final String FIND_BY_USERNAME = "SELECT * FROM get_user_by_username(?)";
    private static final String REGISTER_USER = "CALL register_user(?,?,?)";
    private static final int INSERT_ID_INDEX = 3;

    @Override
    public List<User> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findById(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int save(User user) {
        if (user.getRole() == UserRole.ADMIN) {
            throw new UnsupportedOperationException("Admins are inserted via create_admin, which does not return an ID in procedures.sql");
        }
        try (Connection connection = PostgresConnection.getInstance().getConnection();
             CallableStatement statement = connection.prepareCall(REGISTER_USER)) {

            CallableStatementFactory.bindParams(statement,
                    user.getName(),
                    user.getHashedPassword()
            );
            statement.setNull(INSERT_ID_INDEX, Types.INTEGER);
            statement.registerOutParameter(INSERT_ID_INDEX, Types.INTEGER);
            statement.execute();
            return statement.getInt(INSERT_ID_INDEX);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save user", e);
        }
    }

    @Override
    public void update(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection connection = PostgresConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME)) {

            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapRow(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find user by username: " + username, e);
        }
    }

    private User mapRow(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password_hash"),
                UserRole.valueOf(resultSet.getString("user_role"))
        );
    }

}
