package hr.algebra.camera.repository.postgres;

import hr.algebra.camera.exception.DatabaseOperationException;
import hr.algebra.camera.model.Lens;
import hr.algebra.camera.repository.interfaces.ILensRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LensRepository implements ILensRepository {
    private static final String FIND_ALL = "SELECT * FROM get_all_lenses()";
    private static final String FIND_BY_ID = "SELECT * FROM get_lens_by_id(?)";
    private static final String INSERT = "CALL insert_lens(?,?,?,?,?,?)";
    private static final String UPDATE = "CALL update_lens(?,?,?,?,?,?)";
    private static final String DELETE = "CALL delete_lens(?)";

    private static final int INSERT_ID_INDEX = 6;

    @Override
    public List<Lens> findAll() {
        List<Lens> lenses = new ArrayList<>();
        try (Connection connection = PostgresConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                lenses.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to load lenses", e);
        }
        return lenses;
    }

    @Override
    public Optional<Lens> findById(int id) {
        try (Connection connection = PostgresConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapRow(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to load lens id: " + id, e);
        }
    }

    @Override
    public int save(Lens lens) {
        try (Connection connection = PostgresConnection.getInstance().getConnection();
             CallableStatement statement = connection.prepareCall(INSERT)) {

            CallableStatementFactory.bindParams(statement,
                    lens.getName(),
                    lens.getFocalLength(),
                    BigDecimal.valueOf(lens.getAperture()),
                    lens.getMountType(),
                    BigDecimal.valueOf(lens.getPrice())
            );
            statement.setNull(INSERT_ID_INDEX, Types.INTEGER);
            statement.registerOutParameter(INSERT_ID_INDEX, Types.INTEGER);
            statement.execute();
            return statement.getInt(INSERT_ID_INDEX);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save lens", e);
        }
    }

    @Override
    public void update(Lens lens) {
        try (Connection connection = PostgresConnection.getInstance().getConnection();
             CallableStatement statement = connection.prepareCall(UPDATE)) {

            CallableStatementFactory.bindParams(statement,
                    lens.getId(),
                    lens.getName(),
                    lens.getFocalLength(),
                    BigDecimal.valueOf(lens.getAperture()),
                    lens.getMountType(),
                    BigDecimal.valueOf(lens.getPrice())
            );
            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update lens id: " + lens.getId(), e);
        }

    }

    @Override
    public void deleteById(int id) {
        try (Connection connection = PostgresConnection.getInstance().getConnection();
             CallableStatement statement = connection.prepareCall(DELETE)) {

            CallableStatementFactory.bindParams(statement, id);
            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete lens id: " + id, e);
        }
    }
    private Lens mapRow(ResultSet resultSet) throws SQLException {
        return new Lens(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("focal_length"),
                resultSet.getDouble("aperture"),
                resultSet.getString("mount_type"),
                resultSet.getDouble("price")
        );
    }
}
