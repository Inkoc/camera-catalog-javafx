package hr.algebra.camera.controller;

import hr.algebra.camera.repository.interfaces.IBrandRepository;
import hr.algebra.camera.repository.interfaces.ICameraRepository;
import hr.algebra.camera.repository.interfaces.ILensRepository;
import hr.algebra.camera.repository.interfaces.IUserRepository;
import hr.algebra.camera.repository.postgres.BrandRepository;
import hr.algebra.camera.repository.postgres.CameraRepository;
import hr.algebra.camera.repository.postgres.LensRepository;
import hr.algebra.camera.repository.postgres.UserRepository;
import hr.algebra.camera.service.*;
import hr.algebra.camera.service.interfaces.*;
import javafx.util.Callback;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ControllerFactory implements Callback<Class<?>, Object> {

    private final Map<Class<?>, Object> dependencies = new HashMap<>();

    public ControllerFactory() {
        IBrandRepository brandRepository = new BrandRepository();
        ILensRepository lensRepository = new LensRepository();
        ICameraRepository cameraRepository = new CameraRepository(brandRepository);
        IUserRepository userRepository = new UserRepository();

        IAuthService authService = new AuthService(userRepository);
        ICameraService cameraService = new CameraService(cameraRepository);
        ILensService lensService = new LensService(lensRepository);
        IBrandService brandService = new BrandService(brandRepository);
        IDataImportService importService = new DataImportService(cameraService, brandService);
        IXmlExportService exportService = new XmlExportService(cameraService);

        dependencies.put(IAuthService.class, authService);
        dependencies.put(ICameraService.class, cameraService);
        dependencies.put(ILensService.class, lensService);
        dependencies.put(IBrandService.class, brandService);
        dependencies.put(IDataImportService.class, importService);
        dependencies.put(IXmlExportService.class, exportService);
    }

    @Override
    public Object call(Class<?> type) {
        try {
            Constructor<?>[] constructors = type.getConstructors();
            if (constructors.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            }

            Constructor<?> targetConstructor = constructors[0];
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() > targetConstructor.getParameterCount()) {
                    targetConstructor = constructor;
                }
            }

            Class<?>[] parameterTypes = targetConstructor.getParameterTypes();
            Object[] initArgs = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Object dependency = dependencies.get(parameterTypes[i]);
                if (dependency != null) {
                    initArgs[i] = dependency;
                } else {
                    initArgs[i] = null;
                }
            }

            return targetConstructor.newInstance(initArgs);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate controller: " + type.getName(), e);
        }
    }
}
