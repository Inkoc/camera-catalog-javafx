-- ============ BRANDS ============
INSERT INTO brand (name, country_of_origin) VALUES
                                                ('Canon',     'Japan'),
                                                ('Nikon',     'Japan'),
                                                ('Sony',      'Japan'),
                                                ('Fujifilm',  'Japan'),
                                                ('Panasonic', 'Japan'),
                                                ('GoPro',     'USA');

-- ============ LENSES ============
INSERT INTO lens (name, focal_length, aperture, mount_type, price) VALUES
                                                                       ('Canon RF 50mm f/1.8',         50,  1.8, 'RF', 199.00),
                                                                       ('Canon EF 24-70mm f/2.8',      70,  2.8, 'EF', 1899.00),
                                                                       ('Sony FE 85mm f/1.4',          85,  1.4, 'E',  1799.00),
                                                                       ('Sigma 150-600mm f/6.3',       600, 6.3, 'E',  1499.00),
                                                                       ('Nikon Z 24-120mm f/4',        120, 4.0, 'Z',  1099.00),
                                                                       ('Fujifilm XF 35mm f/1.4',      35,  1.4, 'X',  599.00),
                                                                       ('Panasonic Lumix 12-60mm f/3.5', 60, 3.5, 'L', 999.00);

-- ============ CAMERAS ============
INSERT INTO camera
(name, release_year, megapixels, sensor_type, iso_range, max_shooting_speed,
 price, image_path, camera_type, purpose, brand_id)
VALUES
    ('Canon EOS R5',    2020, 45.0,  'Full-Frame CMOS',      '100-51200', 20, 3899.00, NULL, 'MIRRORLESS',    'WILDLIFE',     (SELECT id FROM brand WHERE name='Canon')),
    ('Canon EOS 90D',   2019, 32.5,  'APS-C CMOS',           '100-25600', 10, 1199.00, NULL, 'DSLR',          'SPORTS',       (SELECT id FROM brand WHERE name='Canon')),
    ('Nikon Z6 II',     2020, 24.5,  'Full-Frame BSI CMOS',  '100-51200', 14, 1999.00, NULL, 'MIRRORLESS',    'LANDSCAPE',    (SELECT id FROM brand WHERE name='Nikon')),
    ('Nikon D850',      2017, 45.7,  'Full-Frame BSI CMOS',  '64-25600',   9, 2999.00, NULL, 'DSLR',          'LANDSCAPE',    (SELECT id FROM brand WHERE name='Nikon')),
    ('Sony A7 IV',      2021, 33.0,  'Full-Frame Exmor R',   '100-51200', 10, 2499.00, NULL, 'MIRRORLESS',    'PORTRAIT',     (SELECT id FROM brand WHERE name='Sony')),
    ('Sony ZV-1',       2020, 20.1,  '1-inch Exmor RS',      '125-12800', 24,  749.00, NULL, 'COMPACT',       'VLOGGING',     (SELECT id FROM brand WHERE name='Sony')),
    ('Fujifilm X100V',  2020, 26.1,  'APS-C X-Trans',        '160-12800', 11, 1399.00, NULL, 'COMPACT',       'STREET_PHOTO', (SELECT id FROM brand WHERE name='Fujifilm')),
    ('Fujifilm GFX 100S',2021,102.0, 'Medium Format CMOS',   '100-12800',  5, 5999.00, NULL, 'MEDIUM_FORMAT', 'LANDSCAPE',    (SELECT id FROM brand WHERE name='Fujifilm')),
    ('Panasonic GH6',   2022, 25.2,  'Micro Four Thirds',    '100-25600', 75, 2199.00, NULL, 'MIRRORLESS',    'VLOGGING',     (SELECT id FROM brand WHERE name='Panasonic')),
    ('GoPro HERO11',    2022, 27.0,  '1/1.9-inch CMOS',      '100-6400',  30,  499.00, NULL, 'ACTION_CAMERA', 'SPORTS',       (SELECT id FROM brand WHERE name='GoPro'));

-- ============ CAMERA <-> LENS LINKS ============
INSERT INTO camera_lens (camera_id, lens_id)
SELECT c.id, l.id
FROM (VALUES
          ('Canon EOS R5',  'Canon RF 50mm f/1.8'),
          ('Canon EOS 90D', 'Canon EF 24-70mm f/2.8'),
          ('Sony A7 IV',    'Sony FE 85mm f/1.4'),
          ('Sony A7 IV',    'Sigma 150-600mm f/6.3'),
          ('Nikon Z6 II',   'Nikon Z 24-120mm f/4'),
          ('Panasonic GH6', 'Panasonic Lumix 12-60mm f/3.5')
     ) AS pair(cam, lens)
         JOIN camera c ON c.name = pair.cam
         JOIN lens   l ON l.name = pair.lens;
