CREATE DATABASE IF NOT EXISTS pedidos_db;
USE pedidos_db;

-- Tabla Categoria
CREATE TABLE IF NOT EXISTS categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
    );

-- Tabla Producto
CREATE TABLE IF NOT EXISTS producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    nombre VARCHAR(150) NOT NULL,
    precio DOUBLE NOT NULL,
    descripcion VARCHAR(255),
    stock INT NOT NULL,
    imagen VARCHAR(255),
    disponible BOOLEAN DEFAULT TRUE,
    categoria_id BIGINT,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id)
    );

-- Tabla Usuario
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    mail VARCHAR(150) NOT NULL UNIQUE,
    celular VARCHAR(50),
    contraseña VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'USUARIO') NOT NULL
    );

-- Tabla Pedido
CREATE TABLE IF NOT EXISTS pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha DATE NOT NULL,
    estado ENUM('PENDIENTE', 'CONFIRMADO', 'TERMINADO', 'CANCELADO') NOT NULL,
    total DOUBLE NOT NULL DEFAULT 0.0,
    forma_pago ENUM('TARJETA', 'TRANSFERENCIA', 'EFECTIVO') NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
    );

-- Tabla DetallePedido
CREATE TABLE IF NOT EXISTS detalle_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    cantidad INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto(id)
    );

-- Datos de prueba
INSERT INTO categoria (nombre, descripcion) VALUES
    ('Hamburguesas', 'Hamburguesas caseras con papas'),
    ('Bebidas', 'Gaseosas, aguas y cervezas');

INSERT INTO producto (nombre, precio, descripcion, stock, imagen, disponible, categoria_id) VALUES
    ('Cheeseburger', 6500.00, 'Hamburguesa con doble queso cheddar', 50, 'cheeseburger.jpg', TRUE, 1),
    ('Coca Cola 500ml', 1500.00, 'Gaseosa linea Coca Cola', 100, 'coca.jpg', TRUE, 2);

INSERT INTO usuario (nombre, apellido, mail, celular, contraseña, rol) VALUES
    ('Admin', 'Principal', 'admin@foodstore.com', '2615555555', 'admin123', 'ADMIN'),
    ('Juan', 'Perez', 'juan.perez@email.com', '2614444444', 'user123', 'USUARIO');