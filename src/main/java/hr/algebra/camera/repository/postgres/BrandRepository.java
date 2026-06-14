package hr.algebra.camera.repository.postgres;

import hr.algebra.camera.exception.DatabaseOperationException;
import hr.algebra.camera.model.Brand;
import hr.algebra.camera.repository.IBrandRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BrandRepository implements IBrandRepository {
    private static final String FIND_ALL = "SELECT * FROM get_all_brands()";
    private static final String FIND_BY_ID = "SELECT * FROM get_brand_by_id(?)";
    private static final String INSERT = "CALL insert_brand(?,?,?)";
    private static final String UPDATE = "CALL update_brand(?,?,?)";
    private static final String DELETE = "CALL delete_brand(?)";

    private static final int INSERT_ID_INDEX = 3;

    @Override
    public List<Brand> findAll() {
        List<Brand> brands = new ArrayList<>();

        try (Connection connection = PostgresConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                brands.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to load brands", e);
        }
        return brands;
    }

    @Override
    public Optional<Brand> findById(int id) {
        try (Connection connection = PostgresConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapRow(resultSet)) : Optional.empty();
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to load brand id: " + id, e);
        }
    }

    @Override
    public int save(Brand brand) {
        try (Connection connection = PostgresConnection.getInstance().getConnection(); CallableStatement statement = connection.prepareCall(INSERT)) {
            CallableStatementFactory.bindParams(statement, brand.getName(), brand.getCountry());

            statement.setNull(INSERT_ID_INDEX, Types.INTEGER);
            statement.registerOutParameter(INSERT_ID_INDEX, Types.INTEGER);
            statement.execute();

            return statement.getInt(INSERT_ID_INDEX);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save brand", e);
        }
    }

    @Override
    public void update(Brand brand) {
        try (Connection connection = PostgresConnection.getInstance().getConnection(); CallableStatement statement = connection.prepareCall(UPDATE)) {
            CallableStatementFactory.bindParams(statement, brand.getId(), brand.getName(), brand.getCountry());
            statement.execute();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update brand id: " + brand.getId(), e);
        }
    }

    @Override
    public void deleteById(int id) {
        try (Connection connection = PostgresConnection.getInstance().getConnection(); CallableStatement statement = connection.prepareCall(DELETE)) {
            CallableStatementFactory.bindParams(statement, id);
            statement.execute();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete brand id: " + id, e);
        }
    }

    private Brand mapRow(ResultSet resultSet) throws SQLException {
        return new Brand(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("country_of_origin")
        );
    }
}
