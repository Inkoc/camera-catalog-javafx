CREATE OR REPLACE PROCEDURE insert_camera(
    p_name VARCHAR, p_release_year INT, p_megapixels NUMERIC,
    p_sensor_type VARCHAR, p_iso_range VARCHAR, p_max_shooting_speed INT,
    p_price NUMERIC, p_image_path VARCHAR, p_camera_type VARCHAR,
    p_purpose VARCHAR, p_brand_id INT,
    INOUT p_id INT DEFAULT NULL
)
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO camera (
        name, release_year, megapixels, sensor_type, iso_range,
        max_shooting_speed, price, image_path, camera_type, purpose, brand_id
    ) VALUES (
                 p_name, p_release_year, p_megapixels, p_sensor_type, p_iso_range,
                 p_max_shooting_speed, p_price, p_image_path, p_camera_type, p_purpose, p_brand_id
             )
    RETURNING id INTO p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE update_camera(
    p_id INT, p_name VARCHAR, p_release_year INT, p_megapixels NUMERIC,
    p_sensor_type VARCHAR, p_iso_range VARCHAR, p_max_shooting_speed INT,
    p_price NUMERIC, p_image_path VARCHAR, p_camera_type VARCHAR,
    p_purpose VARCHAR, p_brand_id INT
)
    LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE camera SET
        name               = p_name,
        release_year       = p_release_year,
        megapixels         = p_megapixels,
        sensor_type        = p_sensor_type,
        iso_range          = p_iso_range,
        max_shooting_speed = p_max_shooting_speed,
        price              = p_price,
        image_path         = p_image_path,
        camera_type        = p_camera_type,
        purpose            = p_purpose,
        brand_id           = p_brand_id
    WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE delete_camera(p_id INT)
    LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM camera WHERE id = p_id;
END;
$$;


CREATE OR REPLACE FUNCTION get_all_cameras()
    RETURNS SETOF camera
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY SELECT * FROM camera ORDER BY name;
END;
$$;


CREATE OR REPLACE FUNCTION get_camera_by_id(p_id INT)
    RETURNS SETOF camera
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY SELECT * FROM camera WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE attach_lens_to_camera(p_camera_id INT, p_lens_id INT)
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO camera_lens (camera_id, lens_id)
    VALUES (p_camera_id, p_lens_id)
    ON CONFLICT DO NOTHING;
END;
$$;


CREATE OR REPLACE PROCEDURE detach_lens_from_camera(p_camera_id INT, p_lens_id INT)
    LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM camera_lens
    WHERE camera_id = p_camera_id AND lens_id = p_lens_id;
END;
$$;


CREATE OR REPLACE FUNCTION get_lenses_for_camera(p_camera_id INT)
    RETURNS SETOF lens
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        SELECT l.*
        FROM lens l
        JOIN camera_lens cl ON cl.lens_id = l.id
        WHERE cl.camera_id = p_camera_id
        ORDER BY l.name;
END;
$$;


CREATE OR REPLACE PROCEDURE insert_lens(
    p_name VARCHAR, p_focal_length INT, p_aperture NUMERIC,
    p_mount_type VARCHAR, p_price NUMERIC,
    INOUT p_id INT DEFAULT NULL
)
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO lens (name, focal_length, aperture, mount_type, price)
    VALUES (p_name, p_focal_length, p_aperture, p_mount_type, p_price)
    RETURNING id INTO p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE update_lens(
    p_id INT, p_name VARCHAR, p_focal_length INT, p_aperture NUMERIC,
    p_mount_type VARCHAR, p_price NUMERIC
)
    LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE lens SET
        name         = p_name,
        focal_length = p_focal_length,
        aperture     = p_aperture,
        mount_type   = p_mount_type,
        price        = p_price
    WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE delete_lens(p_id INT)
    LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM lens WHERE id = p_id;
END;
$$;


CREATE OR REPLACE FUNCTION get_all_lenses()
    RETURNS SETOF lens
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY SELECT * FROM lens ORDER BY name;
END;
$$;


CREATE OR REPLACE FUNCTION get_lens_by_id(p_id INT)
    RETURNS SETOF lens
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY SELECT * FROM lens WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE insert_brand(
    p_name VARCHAR, p_country_of_origin VARCHAR,
    INOUT p_id INT DEFAULT NULL
)
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO brand (name, country_of_origin)
    VALUES (p_name, p_country_of_origin)
    RETURNING id INTO p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE update_brand(
    p_id INT, p_name VARCHAR, p_country_of_origin VARCHAR
)
    LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE brand SET
        name              = p_name,
        country_of_origin = p_country_of_origin
    WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE delete_brand(p_id INT)
    LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM brand WHERE id = p_id;
END;
$$;


CREATE OR REPLACE FUNCTION get_all_brands()
    RETURNS SETOF brand
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY SELECT * FROM brand ORDER BY name;
END;
$$;


CREATE OR REPLACE FUNCTION get_brand_by_id(p_id INT)
    RETURNS SETOF brand
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY SELECT * FROM brand WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE create_admin(
    p_username VARCHAR, p_password_hash VARCHAR
)
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO app_user (username, password_hash, user_role)
    VALUES (p_username, p_password_hash, 'ADMIN')
    ON CONFLICT (username) DO NOTHING;
END;
$$;


CREATE OR REPLACE PROCEDURE register_user(
    p_username VARCHAR, p_password_hash VARCHAR,
    INOUT p_id INT DEFAULT NULL
)
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO app_user (username, password_hash, user_role)
    VALUES (p_username, p_password_hash, 'USER')
    RETURNING id INTO p_id;
END;
$$;


CREATE OR REPLACE FUNCTION get_user_by_username(p_username VARCHAR)
    RETURNS SETOF app_user
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY SELECT * FROM app_user WHERE username = p_username;
END;
$$;

CREATE OR REPLACE PROCEDURE clear_all_data()
    LANGUAGE plpgsql
AS $$
BEGIN
    TRUNCATE camera_lens, camera, lens, brand RESTART IDENTITY CASCADE;
END;
$$;
