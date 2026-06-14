package hr.algebra.camera;

import hr.algebra.camera.repository.postgres.BrandRepository;
import hr.algebra.camera.repository.postgres.PostgresConnection;
import hr.algebra.camera.utils.ConfigurationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CameraApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //TEST
        try {
            hr.algebra.camera.utils.ConfigurationManager.load();
            hr.algebra.camera.repository.postgres.PostgresConnection.getInstance().getConnection();
            System.out.println("--- TESTING REPOSITORIES ---");
            // 1. Test Brand
            hr.algebra.camera.repository.postgres.BrandRepository brandRepo = new hr.algebra.camera.repository.postgres.BrandRepository();
            hr.algebra.camera.model.Brand newBrand = new hr.algebra.camera.model.Brand(0, "Canon", "Japan");
            int brandId = brandRepo.save(newBrand);
            System.out.println("Saved Brand ID: " + brandId);

            // Fetch brand back so it has the actual DB ID before linking it to the Camera
            hr.algebra.camera.model.Brand savedBrand = brandRepo.findById(brandId).orElse(newBrand);
            // 2. Test Lens
            hr.algebra.camera.repository.postgres.LensRepository lensRepo = new hr.algebra.camera.repository.postgres.LensRepository();
            hr.algebra.camera.model.Lens newLens = new hr.algebra.camera.model.Lens(0, "Canon EF 50mm f/1.8", 50, 1.8, "EF-Mount", 125.00);
            int lensId = lensRepo.save(newLens);
            System.out.println("Saved Lens ID: " + lensId);
            // 3. Test Camera
            hr.algebra.camera.repository.postgres.CameraRepository cameraRepo = new hr.algebra.camera.repository.postgres.CameraRepository(brandRepo);
            hr.algebra.camera.model.Camera newCamera = new hr.algebra.camera.model.Camera(
                    0, "Canon EOS 5D Mark IV", 2016, 30.4, "Full Frame", "100-32000",
                    7, 2499.00, null, savedBrand,
                    hr.algebra.camera.model.enums.CameraType.DSLR,
                    hr.algebra.camera.model.enums.Purpose.PORTRAIT,
                    new java.util.ArrayList<>()
            );
            int cameraId = cameraRepo.save(newCamera);
            System.out.println("Saved Camera ID: " + cameraId);
            // Attach lens to camera (Tests the many-to-many relationship)
            cameraRepo.attachLens(cameraId, lensId);
            System.out.println("Attached Lens " + lensId + " to Camera " + cameraId);
            // 4. Test User
            hr.algebra.camera.repository.postgres.UserRepository userRepo = new hr.algebra.camera.repository.postgres.UserRepository();
            // Remember that save() uses register_user which forces the 'USER' role in the DB
            hr.algebra.camera.model.User newUser = new hr.algebra.camera.model.User(0, "test_user_1", "hashed_pwd_123", hr.algebra.camera.model.enums.UserRole.USER);
            int userId = userRepo.save(newUser);
            System.out.println("Saved User ID: " + userId);
            // 5. Fetch and print all
            System.out.println("\n--- FETCHING DATA FROM DB ---");
            brandRepo.findAll().forEach(System.out::println);
            lensRepo.findAll().forEach(System.out::println);
            cameraRepo.findAll().forEach(System.out::println);
            userRepo.findByUsername("test_user_1").ifPresent(System.out::println);

            System.out.println("Lenses for camera " + cameraId + ": " + cameraRepo.findLensesForCamera(cameraId));
        } catch (Exception e) {
            System.err.println("Database test failed:");
            e.printStackTrace();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(CameraApplication.class.getResource("view/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), ConfigurationManager.getWindowWidth(), ConfigurationManager.getWindowHeight());
        stage.setTitle(ConfigurationManager.getWindowTitle());
        stage.setScene(scene);
        stage.show();
    }
}
