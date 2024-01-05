-- Crear tabla usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    tipo_usuario VARCHAR(20),
    telefono VARCHAR(15),
    email VARCHAR(50)
    -- Otros campos según necesidad
);

-- Crear tabla proveedores
CREATE TABLE proveedores (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    contacto VARCHAR(50),
    telefono VARCHAR(15),
    email VARCHAR(100),
    direccion VARCHAR(255)
    -- Otros campos según necesidad
);

-- Crear tabla insumos
CREATE TABLE insumos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50),
    proveedor_id INT,
    precio FLOAT,
    cantidad INT
    -- Otros campos según necesidad
);

-- Crear tabla inventarios
CREATE TABLE inventarios (
    id SERIAL PRIMARY KEY,
    insumo_id INT,
    cantidad INT,
    fecha_movimiento VARCHAR(255),
    tipo_movimiento VARCHAR(10)
    -- Otros campos según necesidad
);

-- Crear tabla motorizados
CREATE TABLE motorizados (
    id SERIAL PRIMARY KEY,
    cliente_id INT,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    anio INT,
    placa VARCHAR(15),
    estado VARCHAR(20),
    FOREIGN KEY (cliente_id) REFERENCES usuarios(id)
    -- Otros campos según necesidad
);

-- Crear tabla servicios
CREATE TABLE servicios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50),
    descripcion VARCHAR(255),
    precio FLOAT
    -- Otros campos según necesidad
);

-- Crear tabla citas
CREATE TABLE citas (
    id SERIAL PRIMARY KEY,
    cliente_id INT,
    motorizado_id INT,
    fecha_hora VARCHAR(255),
    estado VARCHAR(20),
    monto_total FLOAT -- Nuevo campo para el monto total de la cita
    -- Otros campos según necesidad
);

-- Crear tabla citas_servicios para la relación muchos a muchos
CREATE TABLE citas_servicios (
    cita_id INT,
    servicio_id INT,
    cantidad INT, -- Nuevo campo para la cantidad de servicios
    PRIMARY KEY (cita_id, servicio_id),
    FOREIGN KEY (cita_id) REFERENCES citas(id),
    FOREIGN KEY (servicio_id) REFERENCES servicios(id)
);

-- Crear tabla citas_insumos para la relación muchos a muchos
CREATE TABLE citas_insumos (
    cita_id INT,
    insumo_id INT,
    cantidad INT, -- Nuevo campo para la cantidad de insumos
    PRIMARY KEY (cita_id, insumo_id),
    FOREIGN KEY (cita_id) REFERENCES citas(id),
    FOREIGN KEY (insumo_id) REFERENCES insumos(id)
);

-- Crear tabla pagos
CREATE TABLE pagos (
    id SERIAL PRIMARY KEY,
    cita_id INT,
    monto FLOAT,
    fecha_pago VARCHAR(255),
    metodo_pago VARCHAR(20)
    -- Otros campos según necesidad
);

-- Crear la tabla help con el campo ejemplo
CREATE TABLE help (
    id SERIAL PRIMARY KEY,
    cu VARCHAR(255),
    accion VARCHAR(255),
    parametros VARCHAR(255),
    ejemplo VARCHAR(255)
);


-- Agregar claves foráneas después de haber creado todas las tablas
ALTER TABLE insumos
ADD FOREIGN KEY (proveedor_id) REFERENCES proveedores(id);

ALTER TABLE inventarios
ADD FOREIGN KEY (insumo_id) REFERENCES insumos(id);

ALTER TABLE citas
ADD FOREIGN KEY (cliente_id) REFERENCES usuarios(id),
ADD FOREIGN KEY (motorizado_id) REFERENCES motorizados(id);

ALTER TABLE pagos
ADD FOREIGN KEY (cita_id) REFERENCES citas(id);



------------------------------------------ DATA ------------------------------------------------------------------------



-- USUARIOS --------------------------------------------------------------------


-- Drop the table if it exists
DELETE FROM usuarios;

-- Reiniciar la secuencia para que los IDs comiencen desde 1
ALTER SEQUENCE usuarios_id_seq RESTART WITH 1;

-- Create or replace the stored procedure
CREATE OR REPLACE PROCEDURE insertar_usuarios()
AS
$$
DECLARE
    i INT := 1;
    nombres_usuarios VARCHAR[] := ARRAY['Juan', 'María', 'Luis', 'Ana', 'Carlos', 'Elena', 'Javier', 'Carmen', 'Pedro', 'Isabel', 'David', 'Laura', 'Francisco', 'Sara', 'Manuel', 'Raquel', 'Miguel', 'Natalia', 'Antonio', 'Beatriz'];
    apellidos_usuarios VARCHAR[] := ARRAY['Gomez', 'Lopez', 'Perez', 'Rodriguez', 'Martinez', 'Fernandez', 'Gonzalez', 'Sanchez', 'Alvarez', 'Romero', 'Serrano', 'Hernandez', 'Diaz', 'Jimenez', 'Ruiz', 'Moreno', 'Molina', 'Navarro', 'Cruz', 'Vazquez'];
    dominios_correo VARCHAR[] := ARRAY['outlook.com', 'gmail.com', 'hotmail.com', 'yahoo.com', 'aol.com', 'icloud.com'];
    correo VARCHAR;
BEGIN
    -- Insertar 300 registros ficticios en la tabla usuarios
    WHILE i <= 300 LOOP
        -- Generar correo electrónico incluso si uno de los campos es nulo
        correo := LOWER(
            COALESCE(nombres_usuarios[i % 21] || '', 'usuario') ||
            COALESCE('_' || apellidos_usuarios[i % 21] || '', '') ||
            '_' || TRUNC(random() * 1000)::TEXT || -- Agregar número aleatorio
            '@' || dominios_correo[i % 6]
        );

        INSERT INTO usuarios(nombre, apellido, tipo_usuario, telefono, email)
        VALUES (
            COALESCE(nombres_usuarios[i % 21], 'usuario'), -- Manejar nulos
            COALESCE(apellidos_usuarios[i % 21], 'apellido'), -- Manejar nulos
            'Cliente',
            TRUNC(random() * 1000000000)::TEXT,
            correo
        );

        i := i + 1;
    END LOOP;
END;
$$
LANGUAGE plpgsql;

-- Call the stored procedure
CALL insertar_usuarios();

-- Select and display the records from the usuarios table
SELECT * FROM usuarios;

-- PROVEEDORES -----------------------------------------------------------------


-- Eliminar registros existentes en la tabla proveedores
DELETE FROM proveedores;

-- Reiniciar la secuencia para que los IDs comiencen desde 1
ALTER SEQUENCE proveedores_id_seq RESTART WITH 1;

-- Insertar registros ficticios en la tabla proveedores con dominio de correo Gmail
INSERT INTO proveedores (nombre, contacto, telefono, email, direccion)
VALUES
    ('Autopiezas Rodríguez', 'Juan Rodríguez', TRUNC(random() * 1000000000)::TEXT, 'juan.rodriguez@gmail.com', '123 Calle Principal, CiudadA'),
    ('Neumáticos Veloz', 'María Velázquez', TRUNC(random() * 1000000000)::TEXT, 'maria.velazquez@gmail.com', '456 Avenida Central, CiudadB'),
    ('Baterías Potencia', 'Carlos Baterista', TRUNC(random() * 1000000000)::TEXT, 'carlos.baterista@gmail.com', '789 Plaza Mayor, CiudadC'),
    ('Aceites Motorizados', 'Ana Aceitera', TRUNC(random() * 1000000000)::TEXT, 'ana.aceitera@gmail.com', '101 Paseo del Sol, CiudadD'),
    ('Sistemas de Escape', 'Luis Escapista', TRUNC(random() * 1000000000)::TEXT, 'luis.escapista@gmail.com', '202 Rincón del Parque, CiudadE');

-- Seleccionar y mostrar los registros de la tabla proveedores
SELECT * FROM proveedores;



-- INSUMOS ---------------------------------------------------------------------



-- Eliminar registros existentes en la tabla insumos
DELETE FROM insumos;

-- Reiniciar la secuencia para que los IDs comiencen desde 1
ALTER SEQUENCE insumos_id_seq RESTART WITH 1;

-- Insertar registros ficticios en la tabla insumos
INSERT INTO insumos (nombre, proveedor_id, precio, cantidad) VALUES
    ('Filtro de Aceite', 1, 15.99, 100),
    ('Pastillas de Freno', 2, 25.50, 50),
    ('Bujías de Encendido', 3, 8.75, 80),
    ('Aceite de Motor Sintético', 4, 32.99, 120),
    ('Neumáticos Radiales', 5, 89.95, 40),
    ('Batería de Arranque', 1, 75.00, 30),
    ('Escape Deportivo', 2, 120.50, 15),
    ('Filtro de Aire De Alto Rendimiento', 3, 18.75, 60),
    ('Aceite de Transmisión', 4, 45.99, 25),
    ('Llantas Todo Terreno', 5, 110.25, 20);

-- Seleccionar y mostrar los registros de la tabla insumos
SELECT * FROM insumos;




-- INVENTARIOS -----------------------------------------------------------------



-- Eliminar registros existentes en la tabla inventarios
DELETE FROM inventarios;

-- Reiniciar la secuencia para que los IDs comiencen desde 1
ALTER SEQUENCE inventarios_id_seq RESTART WITH 1;

-- Insertar registros ficticios en la tabla inventarios con la misma cantidad de entradas y salidas
INSERT INTO inventarios (insumo_id, cantidad, fecha_movimiento, tipo_movimiento)
SELECT 
    insumo_id,
    cantidad,
    fecha_movimiento,
    tipo_movimiento
FROM (
    SELECT 
        id AS insumo_id,
        ROUND(random() * 3)::INT * 10 + 10 AS cantidad,
        CURRENT_DATE - ROUND(random() * 30)::INT AS fecha_movimiento,
        CASE WHEN random() < 0.5 THEN 'Entrada' ELSE 'Salida' END AS tipo_movimiento
    FROM insumos
    CROSS JOIN generate_series(1, 5)  -- Repetir para tener aproximadamente 50 registros
) AS subquery;

-- Seleccionar y mostrar los registros de la tabla inventarios
SELECT * FROM inventarios;


-- MOTORIZADOS -----------------------------------------------------------------

-- Eliminar registros existentes en la tabla motorizados
DELETE FROM motorizados;

-- Reiniciar la secuencia para que los IDs comiencen desde 1
ALTER SEQUENCE motorizados_id_seq RESTART WITH 1;

-- Insertar registros ficticios en la tabla motorizados
INSERT INTO motorizados (cliente_id, marca, modelo, anio, placa, estado)
SELECT
    u.id AS cliente_id,
    CASE
        WHEN random() < 0.4 THEN 'Toyota'
        WHEN random() < 0.7 THEN 'Honda'
        WHEN random() < 0.9 THEN 'Ford'
        ELSE 'Chevrolet'
    END AS marca,
    CASE
        WHEN random() < 0.2 THEN 'Civic'
        WHEN random() < 0.4 THEN 'Camry'
        WHEN random() < 0.6 THEN 'Accord'
        WHEN random() < 0.8 THEN 'Fusion'
        ELSE 'Malibu'
    END AS modelo,
    (ROUND(random() * 10)::INT + 2010) AS anio,
    'ABC' || (ROUND(random() * 1000)::INT + 100)::TEXT AS placa,
    CASE WHEN random() < 0.8 THEN 'Activo' ELSE 'Inactivo' END AS estado
FROM usuarios u
CROSS JOIN generate_series(1, 3)  -- Aproximadamente 3 motorizados por usuario
ORDER BY u.id;

-- Seleccionar y mostrar los registros de la tabla motorizados
SELECT * FROM motorizados;




-- SERVICIOS -------------------------------------------------------------------



-- Eliminar registros existentes en la tabla servicios
DELETE FROM servicios;

-- Reiniciar la secuencia para que los IDs comiencen desde 1
ALTER SEQUENCE servicios_id_seq RESTART WITH 1;

-- Insertar registros ficticios en la tabla servicios
INSERT INTO servicios (nombre, descripcion, precio)
VALUES
    ('Cambio de Aceite', 'Incluye cambio de aceite y filtro', 39.99),
    ('Revisión de Frenos', 'Inspección y ajuste de frenos', 49.50),
    ('Alineación y Balanceo', 'Alineación y balanceo de ruedas', 59.75),
    ('Cambio de Batería', 'Reemplazo de batería de arranque', 89.00),
    ('Reparación de Escape', 'Reparación de sistema de escape', 79.50),
    ('Cambio de Neumáticos', 'Incluye cambio de neumáticos', 129.95),
    ('Revisión de Motor', 'Inspección detallada del motor', 69.99),
    ('Lavado Exterior e Interior', 'Lavado completo del vehículo', 29.95),
    ('Cambio de Filtro de Aire', 'Reemplazo del filtro de aire', 19.75),
    ('Recarga de Aire Acondicionado', 'Recarga y revisión del sistema', 59.00);

-- Seleccionar y mostrar los registros de la tabla servicios
SELECT * FROM servicios;


--CITAS NO FUNCIONAAAAAAAAAAAAAAAAAAAAAAAAAAAA --------------------------------------------

-- Reiniciar las secuencias de ID para todas las tablas
SELECT setval('citas_id_seq', 1, false);
SELECT setval('pagos_id_seq', 1, false);

-- Eliminar registros existentes en las tablas pagos, citas_insumos, citas_servicios, y citas
DELETE FROM pagos;
DELETE FROM citas_insumos;
DELETE FROM citas_servicios;
DELETE FROM citas;

-- Insertar datos de citas
INSERT INTO citas (cliente_id, motorizado_id, fecha_hora, estado, monto_total)
VALUES
    (1, 101, '2023-12-01 10:00:00', 'Pendiente', 0.00),
    (2, 102, '2023-12-02 11:30:00', 'Pendiente', 0.00),
    (3, 103, '2023-12-03 12:45:00', 'Confirmada', 0.00),
    (4, 104, '2023-12-04 14:15:00', 'Confirmada', 0.00),
    (5, 105, '2023-12-05 15:30:00', 'Confirmada', 0.00),
    (6, 106, '2023-12-06 16:45:00', 'Confirmada', 0.00),
    (7, 107, '2023-12-07 18:00:00', 'Confirmada', 0.00),
    (8, 108, '2023-12-08 19:15:00', 'Pendiente', 0.00),
    (9, 109, '2023-12-09 20:30:00', 'Pendiente', 0.00),
    (10, 110, '2023-12-10 21:45:00', 'Confirmada', 0.00);

-- Insertar datos de servicios e insumos solo para las citas confirmadas
INSERT INTO citas_servicios (cita_id, servicio_id, cantidad)
SELECT c.id, s.id, CASE WHEN c.estado = 'Confirmada' THEN 1 ELSE 0 END
FROM citas c
JOIN servicios s ON s.id BETWEEN 1 AND 10 -- IDs de servicios que deseas asignar, variando del 1 al 10
WHERE c.estado = 'Confirmada';

INSERT INTO citas_insumos (cita_id, insumo_id, cantidad)
SELECT c.id, i.id, CASE WHEN c.estado = 'Confirmada' THEN 1 ELSE 0 END
FROM citas c
JOIN insumos i ON i.id BETWEEN 1 AND 10 -- IDs de insumos que deseas asignar, variando del 1 al 10
WHERE c.estado = 'Confirmada';

-- Actualizar el monto total solo para las citas confirmadas
UPDATE citas
SET monto_total = (
        SELECT COALESCE(SUM(servicios.precio * citas_servicios.cantidad), 0) +
               COALESCE(SUM(insumos.precio * citas_insumos.cantidad), 0)
        FROM citas_servicios
                 LEFT JOIN servicios ON citas_servicios.servicio_id = servicios.id
                 LEFT JOIN citas_insumos ON citas_servicios.cita_id = citas_insumos.cita_id
                     AND citas_servicios.servicio_id = citas_insumos.insumo_id
                 LEFT JOIN insumos ON citas_insumos.insumo_id = insumos.id
        WHERE citas.id = citas_servicios.cita_id
    )
WHERE estado = 'Confirmada';

-- Insertar datos de pagos solo para las citas confirmadas
INSERT INTO pagos (cita_id, monto, fecha_pago, metodo_pago)
SELECT id, monto_total, '2023-12-03', 'Tarjeta'
FROM citas
WHERE estado = 'Confirmada';

-- Repetir el proceso para las siguientes citas (3 al 10)
-- Añadir más datos según sea necesario

-- Verificar los datos en la tabla pagos
SELECT * FROM citas;



-- HELP ------------------------------------------------------------------------

-- Insertar datos para Gestionar Usuarios (CU1)

INSERT INTO help (cu, accion, parametros, ejemplo)
VALUES
    ('CU1: Gestionar Usuarios', 'Listar Usuarios', 'usuario mostrar', 'usuario mostrar'),
    ('CU1: Gestionar Usuarios', 'Registrar Usuario', 'usuario agregar [nombre; apellido; tipo_usuario; telefono; email]', 'usuario agregar [Juan; Pérez; Administrador; 123456789; juan@example.com]'),
    ('CU1: Gestionar Usuarios', 'Editar Usuario', 'usuario agregar [usuario_id; nombre; apellido; tipo_usuario; telefono; email]', 'usuario agregar [1; Juan; Pérez; Administrador; 123456789; juan@example.com]'),
    ('CU1: Gestionar Usuarios', 'Eliminar Usuario', 'usuario eliminar [usuario_id]', 'usuario eliminar [1]'),
    ('CU1: Gestionar Usuarios', 'Ver Usuario', 'usuario mostrar [usuario_id]', 'usuario mostrar [1]');

-- Insertar datos para Gestionar Insumos (CU2)
INSERT INTO help (cu, accion, parametros, ejemplo)
VALUES
    ('CU2: Gestionar Insumos', 'Listar Insumos', 'insumo mostrar', 'insumo mostrar'),
    ('CU2: Gestionar Insumos', 'Registrar Insumo', 'insumo agregar [proveedor_id; nombre; modelo; anio; placa; estado]', 'insumo agregar [1; Computadora; ModeloX; 2023; ABC123; Activo]'),
    ('CU2: Gestionar Insumos', 'Editar Insumo', 'insumo editar [insumo_id; proveedor_id; nombre; modelo; anio; placa; estado]', 'insumo editar [1; 1; Computadora; ModeloY; 2023; XYZ789; Inactivo]'),
    ('CU2: Gestionar Insumos', 'Eliminar Insumo', 'insumo eliminar [insumo_id]', 'insumo eliminar [1]'),
    ('CU2: Gestionar Insumos', 'Ver Insumo', 'insumo mostrar [insumo_id]', 'insumo mostrar [1]');

    -- Insertar datos para Gestionar Inventarios (CU3)
INSERT INTO help (cu, accion, parametros, ejemplo)
VALUES
    ('CU3: Gestionar Inventarios', 'Listar Inventarios', 'inventario mostrar', 'inventario mostrar'),
    ('CU3: Gestionar Inventarios', 'Registrar Inventario', 'inventario agregar [insumo_id; cantidad; fecha_movimiento; tipo_movimiento]', 'inventario agregar [1; 10; 2023-01-01; Entrada]'),
    ('CU3: Gestionar Inventarios', 'Editar Inventario', 'inventario modificar [inventario_id; insumo_id; cantidad; fecha_movimiento; tipo_movimiento]', 'inventario modificar [1; 1; 5; 2023-01-05; Salida]'),
    ('CU3: Gestionar Inventarios', 'Eliminar Inventario', 'inventario eliminar [inventario_id]', 'inventario eliminar [1]'),
    ('CU3: Gestionar Inventarios', 'Ver Inventario', 'inventario mostrar [inventario_id]', 'inventario mostrar [1]'),
    ('CU3: Gestionar Inventarios', 'Reporte de Inventario', 'inventario reporte', 'inventario reporte');

-- Insertar datos para Gestionar Motorizado (CU4)
INSERT INTO help (cu, accion, parametros, ejemplo)
VALUES
    ('CU4: Gestionar Motorizado', 'Listar Motorizado', 'motorizado mostrar', 'motorizado mostrar'),
    ('CU4: Gestionar Motorizado', 'Registrar Motorizado', 'motorizado agregar [cliente_id; marca; modelo; año; placa; estado]', 'motorizado agregar [1; Toyota; Camry; 2022; ABC123; Activo]'),
    ('CU4: Gestionar Motorizado', 'Editar Motorizado', 'motorizado modificar [motorizado_id; cliente_id; marca; modelo; año; placa; estado]', 'motorizado modificar [1; 1; Toyota; Corolla; 2022; XYZ789; Inactivo]'),
    ('CU4: Gestionar Motorizado', 'Eliminar Motorizado', 'motorizado eliminar [motorizado_id]', 'motorizado eliminar [1]'),
    ('CU4: Gestionar Motorizado', 'Ver Motorizado', 'motorizado mostrar [motorizado_id]', 'motorizado mostrar [1]'),
    ('CU4: Gestionar Motorizado', 'Reporte de Motorizado', 'motorizado reporte', 'motorizado reporte');

-- Insertar datos para Gestionar Servicios (CU5)
INSERT INTO help (cu, accion, parametros, ejemplo)
VALUES
    ('CU5: Gestionar Servicios', 'Listar Servicios', 'servicio mostrar', 'servicio mostrar'),
    ('CU5: Gestionar Servicios', 'Registrar Servicio', 'servicio agregar [nombre; descripcion; precio]', 'servicio agregar [Limpieza; Servicio de limpieza; 50.00]'),
    ('CU5: Gestionar Servicios', 'Editar Servicio', 'servicio modificar [servicio_id; nombre; descripcion; precio]', 'servicio modificar [1; Limpieza; Servicio de limpieza profunda; 60.00]'),
    ('CU5: Gestionar Servicios', 'Eliminar Servicio', 'servicio eliminar [servicio_id]', 'servicio eliminar [1]'),
    ('CU5: Gestionar Servicios', 'Ver Servicio', 'servicio mostrar [servicio_id]', 'servicio mostrar [1]'),
    ('CU5: Gestionar Servicios', 'Reporte de Servicio', 'servicio reporte', 'servicio reporte');

-- Insertar datos para Gestionar Citas (CU6)
INSERT INTO help (cu, accion, parametros, ejemplo)
VALUES
    ('CU6: Gestionar Citas', 'Listar Citas', 'cita mostrar', 'cita mostrar'),
    ('CU6: Gestionar Citas', 'Registrar Cita', 'cita agregar [cliente_id; motorizado_id; fecha_hora; estado; monto_total]', 'cita agregar [1; 1; 2023-01-10 10:00; Pendiente; 100.00]'),
    ('CU6: Gestionar Citas', 'Editar Cita', 'cita modificar [cita_id; cliente_id; motorizado_id; fecha_hora; estado; monto_total]', 'cita modificar [1; 1; 1; 2023-01-15 14:30; Confirmada; 120.00]'),
    ('CU6: Gestionar Citas', 'Eliminar Cita', 'cita eliminar [cita_id]', 'cita eliminar [1]'),
    ('CU6: Gestionar Citas', 'Ver Cita', 'cita mostrar [cita_id]', 'cita mostrar [1]'),
    ('CU6: Gestionar Citas', 'Reporte de Cita', 'cita reporte', 'cita reporte');

-- Insertar datos para Gestionar Pagos (CU7)
INSERT INTO help (cu, accion, parametros, ejemplo)
VALUES
    ('CU7: Gestionar Pagos', 'Listar Pagos', 'pago mostrar', 'pago mostrar'),
    ('CU7: Gestionar Pagos', 'Registrar Pago', 'pago agregar [cita_id; monto; fecha_pago; metodo_pago]', 'pago agregar [1; 80.00; 2023-01-20; Tarjeta]'),
    ('CU7: Gestionar Pagos', 'Editar Pago', 'pago modificar [pago_id; cita_id; monto; fecha_pago; metodo_pago]', 'pago modificar [1; 1; 90.00; 2023-01-25; Efectivo]'),
    ('CU7: Gestionar Pagos', 'Eliminar Pago', 'pago eliminar [pago_id]', 'pago eliminar [1]'),
    ('CU7: Gestionar Pagos', 'Ver Pago', 'pago mostrar [pago_id]', 'pago mostrar [1]'),
    ('CU7: Gestionar Pagos', 'Reporte de Pago', 'pago reporte', 'pago reporte');

-- Insertar datos para Reportes y Estadísticas (CU8)
INSERT INTO help (cu, accion, parametros, ejemplo)
VALUES
    ('CU8: Reportes y Estadísticas', 'Reporte de Usuario', 'usuario reporte', 'usuario reporte'),
    ('CU8: Reportes y Estadísticas', 'Reporte de Insumos', 'insumo reporte', 'insumo reporte'),
    ('CU8: Reportes y Estadísticas', 'Reporte de Inventario', 'inventario reporte', 'inventario reporte'),
    ('CU8: Reportes y Estadísticas', 'Reporte de Motorizado', 'motorizado reporte', 'motorizado reporte'),
    ('CU8: Reportes y Estadísticas', 'Reporte de Servicio', 'servicio reporte', 'servicio reporte'),
    ('CU8: Reportes y Estadísticas', 'Reporte de Cita', 'cita reporte', 'cita reporte'),
    ('CU8: Reportes y Estadísticas', 'Reporte de Pago', 'pago reporte', 'pago reporte');

