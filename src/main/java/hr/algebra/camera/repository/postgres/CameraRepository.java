package hr.algebra.camera.repository.postgres;

import hr.algebra.camera.exception.DatabaseOperationException;
import hr.algebra.camera.model.Brand;
import hr.algebra.camera.model.Camera;
import hr.algebra.camera.model.Lens;
import hr.algebra.camera.model.enums.CameraType;
import hr.algebra.camera.model.enums.Purpose;
import hr.algebra.camera.repository.interfaces.ICameraRepository;
import hr.algebra.camera.repository.interfaces.IRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CameraRepository implements ICameraRepository {

    private static final String FIND_ALL = "SELECT * FROM get_all_cameras()";
    private static final String FIND_BY_ID = "SELECT * FROM get_camera_by_id(?)";
    private static final String LENSES_FOR = "SELECT * FROM get_lenses_for_camera(?)";
    private static final String INSERT = "CALL insert_camera(?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE = "CALL update_camera(?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DELETE = "CALL delete_camera(?)";
    private static final String ATTACH_LENS = "CALL attach_lens_to_camera(?,?)";
    private static final String DETACH_LENS = "CALL detach_lens_from_camera(?,?)";
    private static final String CLEAR_ALL = "CALL clear_all_data()";

    private static final int INSERT_ID_INDEX = 12;

    private final IRepository<Brand> brandIRepository;

    public CameraRepository(IRepository<Brand> brandIRepository) {
        this.brandIRepository = brandIRepository;
    }

    @Override
    public List<Camera> findAll() {
        Map<Integer, Brand> brandsById = loadBrandsById();
        List<Camera> cameras = new ArrayList<>();

        try (Connection connection = PostgresConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                cameras.add(mapRow(resultSet, brandsById));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to load cameras", e);
        }
        return cameras;
    }


    @Override
    public Optional<Camera> findById(int id) {
        Map<Integer, Brand> brandsById = loadBrandsById();
        try (Connection connection = PostgresConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapRow(resultSet, brandsById)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to load camera id: " + id, e);
        }
    }

    @Override
    public int save(Camera camera) {
        try (Connection connection = PostgresConnection.getInstance().getConnection(); CallableStatement statement = connection.prepareCall(INSERT)) {

            CallableStatementFactory.bindParams(statement,
                    camera.getName(), camera.getReleaseYear(), BigDecimal.valueOf(camera.getMegapixels()),
                    camera.getSensorType(), camera.getIsoRange(), camera.getMaxShootingSpeed(),
                    BigDecimal.valueOf(camera.getPrice()), camera.getImagePath(), camera.getCameraType().name(),
                    camera.getPurpose().name(), brandIdOf(camera));

            statement.setNull(INSERT_ID_INDEX, Types.INTEGER);
            statement.registerOutParameter(INSERT_ID_INDEX, Types.INTEGER);
            statement.execute();

            return statement.getInt(INSERT_ID_INDEX);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save camera", e);
        }
    }

    @Override
    public void update(Camera camera) {
        try (Connection connection = PostgresConnection.getInstance().getConnection(); CallableStatement statement = connection.prepareCall(UPDATE)) {
            CallableStatementFactory.bindParams(statement,
                    camera.getId(), camera.getName(), camera.getReleaseYear(), BigDecimal.valueOf(camera.getMegapixels()),
                    camera.getSensorType(), camera.getIsoRange(), camera.getMaxShootingSpeed(),
                    BigDecimal.valueOf(camera.getPrice()), camera.getImagePath(), camera.getCameraType().name(),
                    camera.getPurpose().name(), brandIdOf(camera));

            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update camera id: " + camera.getId(), e);
        }
    }

    @Override
    public void deleteById(int id) {
        execProcedure(DELETE, "Failed to delete camera id=" + id, id);
    }
    @Override
    public void attachLens(int cameraId, int lensId) {
        execProcedure(ATTACH_LENS, "Failed to attach lens " + lensId + " to camera " + cameraId, cameraId, lensId);
    }

    @Override
    public void detachLens(int cameraId, int lensId) {
        execProcedure(DETACH_LENS, "Failed to detach lens " + lensId + " from camera " + cameraId, cameraId, lensId);
    }

    @Override
    public List<Lens> findLensesForCamera(int cameraId) {
        List<Lens> lenses = new ArrayList<>();
        try (Connection connection = PostgresConnection.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(LENSES_FOR)) {
            statement.setInt(1, cameraId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    lenses.add(mapLens(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to load lenses for camera " + cameraId, e);
        }
        return lenses;
    }

    @Override
    public void clearAllData() {
        execProcedure(CLEAR_ALL, "Failed to clear catalog data");
    }

    private void execProcedure(String call, String errorMessage, Object... params) {
        try (Connection connection = PostgresConnection.getInstance().getConnection(); CallableStatement statement = connection.prepareCall(call)) {
            CallableStatementFactory.bindParams(statement, params);
            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseOperationException(errorMessage, e);
        }
    }

    private Map<Integer, Brand> loadBrandsById() {
        return brandIRepository.findAll().stream().collect(Collectors.toMap(Brand::getId, Function.identity()));
    }

    private Camera mapRow(ResultSet resultSet, Map<Integer, Brand> brandsById) throws SQLException {
        Integer brandId = resultSet.getObject("brand_id", Integer.class);
        Brand brand = (brandId == null) ? null : brandsById.get(brandId);
        return new Camera(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("release_year"),
                resultSet.getDouble("megapixels"),
                resultSet.getString("sensor_type"),
                resultSet.getString("iso_range"),
                resultSet.getInt("max_shooting_speed"),
                resultSet.getDouble("price"),
                resultSet.getString("image_path"),
                brand,
                CameraType.valueOf(resultSet.getString("camera_type")),
                Purpose.valueOf(resultSet.getString("purpose"))
        );
    }


    private Integer brandIdOf(Camera camera) {
        return camera.getBrand() == null ? null : camera.getBrand().getId();
    }

    private Lens mapLens(ResultSet resultSet) throws SQLException {
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
