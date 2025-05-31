-- Crear base de datos
CREATE DATABASE IF NOT EXISTS asturifyangeldb;
USE asturifyangeldb;

-- Tabla: usuarios
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    tipo_usuario ENUM('INVITADO', 'REGISTRADO', 'ADMINISTRADOR') NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL
);

-- Tabla: eventos
CREATE TABLE eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    ubicacion VARCHAR(255) NOT NULL,
    descripcion TEXT,
    capacidad INT NOT NULL,
    tipo_evento ENUM('DEPORTIVO', 'CULTURAL', 'RECREATIVO', 'GASTRONOMICO', 'TURISTICO') NOT NULL,
    hora TIME NOT NULL,
    fecha DATE NOT NULL
);

-- Tabla: reservas
CREATE TABLE reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_reserva DATETIME NOT NULL,
    cantidad_plazas INT NOT NULL,
    usuario_id BIGINT NOT NULL,
    evento_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE CASCADE
);

-- Tabla: resenas
CREATE TABLE resenas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    calificacion INT NOT NULL,
    comentario TEXT,
    fecha_resena DATE NOT NULL,
    usuario_id BIGINT NOT NULL,
    evento_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE CASCADE
);

-- Tabla: notificaciones
CREATE TABLE notificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensaje TEXT NOT NULL,
    fecha_envio DATETIME NOT NULL,
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
