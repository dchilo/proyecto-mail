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




--new inventario ---------------------------

-- Eliminar registros existentes en la tabla inventarios
DELETE FROM inventarios;

-- Reiniciar la secuencia para que los IDs comiencen desde 1
ALTER SEQUENCE inventarios_id_seq RESTART WITH 1;

-- Insertar registros ficticios en la tabla inventarios con la misma cantidad de entradas y salidas
INSERT INTO inventarios (insumo_id, cantidad, fecha_movimiento, tipo_movimiento)
SELECT 
    insumo_id,
    cantidad,
    TO_CHAR(fecha_movimiento, 'YYYY-MM-DD') AS fecha_movimiento,
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

--

-- Reiniciar las secuencias de ID para todas las tablas
SELECT setval('citas_id_seq', 1, false);
DELETE FROM CITAS
-- Insertar datos en la tabla citas
INSERT INTO citas (cliente_id, motorizado_id, fecha_hora, estado, monto_total)
VALUES
    (1, 1, '2023-12-03 04:45:00', 'Confirmada', 55.98),
    (1, 2, '2023-12-04 06:15:00', 'Confirmada', 144.99),
    (1, 3, '2023-12-05 07:30:00', 'Confirmada', 164.00),
    (2, 4, '2023-12-06 08:45:00', 'Confirmada', 309.85),
    (2, 5, '2023-12-07 10:00:00', 'Confirmada', 249.99),
    (2, 6, '2023-12-10 13:45:00', 'Confirmada', 277.75),
    (3, 7, '2023-12-01 02:00:00', 'Pendiente', 0.00),
    (3, 8, '2023-12-02 03:30:00', 'Pendiente', 0.00),
    (3, 9, '2023-12-08 11:15:00', 'Pendiente', 0.00),
    (4, 10, '2023-12-09 12:30:00', 'Pendiente', 0.00);
	
	select * from citas

-- Insertar datos en la tabla citas_insumos
INSERT INTO citas_insumos (cita_id, insumo_id, cantidad)
VALUES
    (6, 5, 2),
    (3, 1, 1),
    (4, 2, 1),
    (7, 4, 1),
    (5, 6, 1),
    (10, 7, 1),
    (7, 8, 1),
    (10, 8, 1);

-- Insertar datos en la tabla citas_servicios
INSERT INTO citas_servicios (cita_id, servicio_id, cantidad)
VALUES
    (8, 1, 2),
    (8, 2, 2),
    (3, 1, 1),
    (4, 2, 1),
    (5, 4, 1),
    (10, 5, 1),
    (6, 6, 1),
    (4, 7, 1),
    (7, 7, 1),
    (10, 10, 1);
	
-- Insertar datos en la tabla pagos
INSERT INTO pagos (cita_id, monto, fecha_pago, metodo_pago)
VALUES
    (3, 55.98, '2023-12-03', 'Tarjeta'),
    (4, 144.99, '2023-12-03', 'Tarjeta'),
    (5, 164.00, '2023-12-03', 'Tarjeta'),
    (6, 309.85, '2023-12-03', 'Tarjeta'),
    (7, 249.99, '2023-12-03', 'Tarjeta'),
    (10, 277.75, '2023-12-03', 'Tarjeta');