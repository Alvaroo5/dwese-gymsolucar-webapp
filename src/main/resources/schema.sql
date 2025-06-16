CREATE TABLE IF NOT EXISTS usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
        email VARCHAR(50) UNIQUE NOT NULL,
        username VARCHAR(30) UNIQUE NOT NULL,
        password VARCHAR(255) NOT NULL,
        nombre VARCHAR(50) NOT NULL,
        apellidos VARCHAR(50) NOT NULL,
        fecha_nacimiento DATE,
        telefono VARCHAR(20),
        estado_cuenta BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS usuario_roles (
    id_usuario INT NOT NULL,
    id_rol INT NOT NULL,
    PRIMARY KEY (id_usuario, id_rol),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (id_rol) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS clases (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre_clase VARCHAR(100) NOT NULL,
    descripcion TEXT,
    dia_semana VARCHAR(15),
    hora_inicio VARCHAR(20),
    hora_fin VARCHAR(20),
    aforo_maximo INT
);

CREATE TABLE IF NOT EXISTS reservas_clase (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT,
    id_clase INT,
    fecha_reserva DATE,
    asistencia_confirmada BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (id_clase) REFERENCES clases(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS grupos_musculares (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS ejercicios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    imagen_url VARCHAR(255),
    id_grupo INT,
    FOREIGN KEY (id_grupo) REFERENCES grupos_musculares(id)
);

CREATE TABLE IF NOT EXISTS progreso (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT,
    actividad VARCHAR(100),
    fecha DATE,
    puntos INT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE
);

DROP VIEW IF EXISTS vista_ranking;
CREATE VIEW vista_ranking AS
SELECT id_usuario, SUM(puntos) AS puntos_totales
FROM progreso
GROUP BY id_usuario;