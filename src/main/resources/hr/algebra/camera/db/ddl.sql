DROP TABLE IF EXISTS camera_lens CASCADE;
DROP TABLE IF EXISTS camera CASCADE;
DROP TABLE IF EXISTS lens CASCADE;
DROP TABLE IF EXISTS brand CASCADE;
DROP TABLE IF EXISTS app_user CASCADE;

CREATE TABLE brand (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       country_of_origin VARCHAR(100)
);

CREATE TABLE lens (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      focal_length INT NOT NULL,
                      aperture NUMERIC(3, 1) NOT NULL,
                      mount_type VARCHAR(50),
                      price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE camera (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        release_year INT NOT NULL,
                        megapixels NUMERIC(5, 2) NOT NULL,
                        sensor_type VARCHAR(50),
                        iso_range VARCHAR(50),
                        max_shooting_speed INT,
                        price NUMERIC(10, 2) NOT NULL,
                        image_path VARCHAR(255),

                        camera_type VARCHAR(50) NOT NULL,  -- Enums saved as plain text (Java will parse them using CameraType.valueOf())
                        purpose VARCHAR(50) NOT NULL,

                        brand_id INT,
                        CONSTRAINT fk_brand FOREIGN KEY(brand_id) REFERENCES brand(id) ON DELETE SET NULL
);

CREATE TABLE camera_lens (
                             camera_id INT REFERENCES camera(id) ON DELETE CASCADE,
                             lens_id INT REFERENCES lens(id) ON DELETE CASCADE,
                             PRIMARY KEY (camera_id, lens_id)
);

CREATE TABLE app_user (
                          id SERIAL PRIMARY KEY,
                          username VARCHAR(50) UNIQUE NOT NULL,
                          password_hash VARCHAR(255) NOT NULL,
                          user_role VARCHAR(20) NOT NULL
);