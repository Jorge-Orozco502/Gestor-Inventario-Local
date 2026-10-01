CREATE DATABASE IF NOT EXISTS inventario_vidrieria
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE inventario_vidrieria;

SET NAMES utf8mb4;

--  1. USUARIOS (Usuario, Administrador, Empleado)

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    tipo_usuario      ENUM('ADMINISTRADOR', 'EMPLEADO') NOT NULL,
    nombre_usuario    VARCHAR(50)  NOT NULL,
    contrasena_hash   VARCHAR(255) NOT NULL,
    nombre_completo   VARCHAR(150) NOT NULL,
    activo            BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_creacion    DATE         NOT NULL DEFAULT (CURRENT_DATE),

    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
    CONSTRAINT uq_usuario_nombre_usuario UNIQUE (nombre_usuario),
    -- Necesario para que empleado_accion pueda garantizar que solo apunta a EMPLEADOS
    CONSTRAINT uq_usuario_id_tipo UNIQUE (id_usuario, tipo_usuario)
) ENGINE = InnoDB;

-- Empleado.accionesPermitidas : List<String>
CREATE TABLE IF NOT EXISTS empleado_accion (
    id_usuario    INT UNSIGNED NOT NULL,
    tipo_usuario  ENUM('ADMINISTRADOR', 'EMPLEADO') NOT NULL DEFAULT 'EMPLEADO',
    accion        VARCHAR(100) NOT NULL,

    CONSTRAINT pk_empleado_accion PRIMARY KEY (id_usuario, accion),
    CONSTRAINT ck_empleado_accion_tipo CHECK (tipo_usuario = 'EMPLEADO'),
    CONSTRAINT fk_empleado_accion_usuario
        FOREIGN KEY (id_usuario, tipo_usuario)
        REFERENCES usuario (id_usuario, tipo_usuario)
) ENGINE = InnoDB;

--  2. PRODUCTOS (Producto, MateriaPrima, Herramienta, ProductoTerminado, PlanchaVidrio)

CREATE TABLE IF NOT EXISTS producto (
    codigo             VARCHAR(30)   NOT NULL,
    tipo_producto      ENUM('MATERIA_PRIMA', 'HERRAMIENTA', 'PRODUCTO_TERMINADO') NOT NULL,
    nombre             VARCHAR(150)  NOT NULL,
    categoria          VARCHAR(100)  NOT NULL,
    descripcion        VARCHAR(500)  NULL,
    unidad_medida      VARCHAR(30)   NOT NULL,
    precio_compra      DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    precio_venta       DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    existencia_actual  DECIMAL(14,3) NOT NULL DEFAULT 0,
    existencia_minima  DECIMAL(14,3) NOT NULL DEFAULT 0,
    estado             ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',

    CONSTRAINT pk_producto PRIMARY KEY (codigo),
    -- Necesario para que las tablas hijas puedan exigir un tipo de producto concreto
    CONSTRAINT uq_producto_codigo_tipo UNIQUE (codigo, tipo_producto),
    CONSTRAINT ck_producto_precio_compra      CHECK (precio_compra >= 0),
    CONSTRAINT ck_producto_precio_venta       CHECK (precio_venta >= 0),
    CONSTRAINT ck_producto_existencia_actual  CHECK (existencia_actual >= 0),
    CONSTRAINT ck_producto_existencia_minima  CHECK (existencia_minima >= 0),

    INDEX idx_producto_nombre    (nombre),
    INDEX idx_producto_categoria (categoria),
    INDEX idx_producto_tipo      (tipo_producto),
    INDEX idx_producto_estado    (estado)
) ENGINE = InnoDB;

-- PlanchaVidrio hereda de MateriaPrima: solo puede colgar de productos MATERIA_PRIMA
CREATE TABLE IF NOT EXISTS plancha_vidrio (
    codigo         VARCHAR(30) NOT NULL,
    tipo_producto  ENUM('MATERIA_PRIMA', 'HERRAMIENTA', 'PRODUCTO_TERMINADO')
                   NOT NULL DEFAULT 'MATERIA_PRIMA',
    tipo_vidrio    ENUM('CLARO', 'BRONCE', 'GRIS', 'NEGRO',
                        'REFLECTIVO_BRONCE', 'REFLECTIVO_AZUL',
                        'ESMERILADO', 'LAMINADO', 'TEMPLADO') NOT NULL,
    espesor_mm     INT UNSIGNED NOT NULL,
    ancho_mm       INT UNSIGNED NOT NULL,
    largo_mm       INT UNSIGNED NOT NULL,

    CONSTRAINT pk_plancha_vidrio PRIMARY KEY (codigo),
    CONSTRAINT ck_plancha_vidrio_tipo     CHECK (tipo_producto = 'MATERIA_PRIMA'),
    CONSTRAINT ck_plancha_vidrio_espesor  CHECK (espesor_mm > 0),
    CONSTRAINT ck_plancha_vidrio_ancho    CHECK (ancho_mm > 0),
    CONSTRAINT ck_plancha_vidrio_largo    CHECK (largo_mm > 0),
    CONSTRAINT fk_plancha_vidrio_producto
        FOREIGN KEY (codigo, tipo_producto)
        REFERENCES producto (codigo, tipo_producto)
) ENGINE = InnoDB;

--  3. PROVEEDORES Y CLIENTES

CREATE TABLE IF NOT EXISTS proveedor (
    codigo              VARCHAR(30)  NOT NULL,
    nombre_empresa      VARCHAR(150) NOT NULL,
    nit                 VARCHAR(20)  NOT NULL,
    telefono            VARCHAR(30)  NULL,
    direccion           VARCHAR(255) NULL,
    correo_electronico  VARCHAR(150) NULL,

    CONSTRAINT pk_proveedor PRIMARY KEY (codigo),
    CONSTRAINT uq_proveedor_nit UNIQUE (nit),
    INDEX idx_proveedor_nombre (nombre_empresa)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS cliente (
    codigo     VARCHAR(30)  NOT NULL,
    nombre     VARCHAR(150) NOT NULL,
    nit_cf     VARCHAR(20)  NOT NULL,   -- NIT del cliente, o 'CF' (Consumidor Final)
    telefono   VARCHAR(30)  NULL,
    direccion  VARCHAR(255) NULL,

    CONSTRAINT pk_cliente PRIMARY KEY (codigo),
    INDEX idx_cliente_nombre (nombre),
    INDEX idx_cliente_nit_cf (nit_cf)
) ENGINE = InnoDB;

--  4. COMPRAS (Compra, DetalleCompra)

CREATE TABLE IF NOT EXISTS compra (
    numero_compra           VARCHAR(30)   NOT NULL,
    fecha                   DATE          NOT NULL,
    codigo_proveedor        VARCHAR(30)   NOT NULL,
    id_usuario_responsable  INT UNSIGNED  NOT NULL,
    forma_pago              ENUM('EFECTIVO', 'TARJETA', 'TRANSFERENCIA', 'CREDITO') NOT NULL,
    total                   DECIMAL(14,2) NOT NULL DEFAULT 0.00,

    CONSTRAINT pk_compra PRIMARY KEY (numero_compra),
    CONSTRAINT ck_compra_total CHECK (total >= 0),
    CONSTRAINT fk_compra_proveedor
        FOREIGN KEY (codigo_proveedor) REFERENCES proveedor (codigo),
    CONSTRAINT fk_compra_usuario
        FOREIGN KEY (id_usuario_responsable) REFERENCES usuario (id_usuario),

    INDEX idx_compra_fecha     (fecha),
    INDEX idx_compra_proveedor (codigo_proveedor),
    INDEX idx_compra_usuario   (id_usuario_responsable)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS detalle_compra (
    id_detalle_compra  INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    numero_compra      VARCHAR(30)   NOT NULL,
    codigo_producto    VARCHAR(30)   NOT NULL,
    cantidad           DECIMAL(14,3) NOT NULL,
    costo_unitario     DECIMAL(12,2) NOT NULL,
    subtotal           DECIMAL(14,2) NOT NULL,

    CONSTRAINT pk_detalle_compra PRIMARY KEY (id_detalle_compra),
    CONSTRAINT ck_detalle_compra_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_detalle_compra_costo    CHECK (costo_unitario >= 0),
    CONSTRAINT ck_detalle_compra_subtotal CHECK (subtotal >= 0),
    CONSTRAINT fk_detalle_compra_compra
        FOREIGN KEY (numero_compra) REFERENCES compra (numero_compra)
        ON DELETE CASCADE,
    CONSTRAINT fk_detalle_compra_producto
        FOREIGN KEY (codigo_producto) REFERENCES producto (codigo),

    INDEX idx_detalle_compra_compra   (numero_compra),
    INDEX idx_detalle_compra_producto (codigo_producto)
) ENGINE = InnoDB;

--  5. VENTAS (Venta, DetalleVenta)

CREATE TABLE IF NOT EXISTS venta (
    numero_venta            VARCHAR(30)   NOT NULL,
    fecha                   DATE          NOT NULL,
    codigo_cliente          VARCHAR(30)   NOT NULL,
    id_usuario_responsable  INT UNSIGNED  NOT NULL,
    forma_pago              ENUM('EFECTIVO', 'TARJETA', 'TRANSFERENCIA', 'CREDITO') NOT NULL,
    subtotal_general        DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    total_descuentos        DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    total_iva               DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    total_final             DECIMAL(14,2) NOT NULL DEFAULT 0.00,

    CONSTRAINT pk_venta PRIMARY KEY (numero_venta),
    CONSTRAINT ck_venta_subtotal   CHECK (subtotal_general >= 0),
    CONSTRAINT ck_venta_descuentos CHECK (total_descuentos >= 0),
    CONSTRAINT ck_venta_iva        CHECK (total_iva >= 0),
    CONSTRAINT ck_venta_total      CHECK (total_final >= 0),
    CONSTRAINT fk_venta_cliente
        FOREIGN KEY (codigo_cliente) REFERENCES cliente (codigo),
    CONSTRAINT fk_venta_usuario
        FOREIGN KEY (id_usuario_responsable) REFERENCES usuario (id_usuario),

    INDEX idx_venta_fecha   (fecha),
    INDEX idx_venta_cliente (codigo_cliente),
    INDEX idx_venta_usuario (id_usuario_responsable)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS detalle_venta (
    id_detalle_venta     INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    numero_venta         VARCHAR(30)   NOT NULL,
    codigo_producto      VARCHAR(30)   NOT NULL,
    cantidad_solicitada  DECIMAL(14,3) NOT NULL,
    precio_unitario      DECIMAL(12,2) NOT NULL,
    subtotal             DECIMAL(14,2) NOT NULL,
    iva_linea            DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    total_linea          DECIMAL(14,2) NOT NULL,

    CONSTRAINT pk_detalle_venta PRIMARY KEY (id_detalle_venta),
    CONSTRAINT ck_detalle_venta_cantidad CHECK (cantidad_solicitada > 0),
    CONSTRAINT ck_detalle_venta_precio   CHECK (precio_unitario >= 0),
    CONSTRAINT ck_detalle_venta_subtotal CHECK (subtotal >= 0),
    CONSTRAINT ck_detalle_venta_iva      CHECK (iva_linea >= 0),
    CONSTRAINT ck_detalle_venta_total    CHECK (total_linea >= 0),
    CONSTRAINT fk_detalle_venta_venta
        FOREIGN KEY (numero_venta) REFERENCES venta (numero_venta)
        ON DELETE CASCADE,
    CONSTRAINT fk_detalle_venta_producto
        FOREIGN KEY (codigo_producto) REFERENCES producto (codigo),

    INDEX idx_detalle_venta_venta    (numero_venta),
    INDEX idx_detalle_venta_producto (codigo_producto)
) ENGINE = InnoDB;

--  6. FABRICACIÓN (TrabajoFabricacion, MaterialUtilizado)

-- El producto fabricado debe ser de tipo PRODUCTO_TERMINADO
CREATE TABLE IF NOT EXISTS trabajo_fabricacion (
    numero_trabajo                VARCHAR(30)  NOT NULL,
    fecha                         DATE         NOT NULL,
    descripcion                   VARCHAR(500) NULL,
    codigo_producto_terminado     VARCHAR(30)  NOT NULL,
    tipo_producto                 ENUM('MATERIA_PRIMA', 'HERRAMIENTA', 'PRODUCTO_TERMINADO')
                                  NOT NULL DEFAULT 'PRODUCTO_TERMINADO',
    id_usuario_responsable        INT UNSIGNED NOT NULL,

    CONSTRAINT pk_trabajo_fabricacion PRIMARY KEY (numero_trabajo),
    CONSTRAINT ck_trabajo_fabricacion_tipo CHECK (tipo_producto = 'PRODUCTO_TERMINADO'),
    CONSTRAINT fk_trabajo_producto_terminado
        FOREIGN KEY (codigo_producto_terminado, tipo_producto)
        REFERENCES producto (codigo, tipo_producto),
    CONSTRAINT fk_trabajo_usuario
        FOREIGN KEY (id_usuario_responsable) REFERENCES usuario (id_usuario),

    INDEX idx_trabajo_fecha    (fecha),
    INDEX idx_trabajo_producto (codigo_producto_terminado),
    INDEX idx_trabajo_usuario  (id_usuario_responsable)
) ENGINE = InnoDB;

-- El material utilizado debe ser de tipo MATERIA_PRIMA
CREATE TABLE IF NOT EXISTS material_utilizado (
    id_material_utilizado  INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    numero_trabajo         VARCHAR(30)   NOT NULL,
    codigo_materia_prima   VARCHAR(30)   NOT NULL,
    tipo_producto          ENUM('MATERIA_PRIMA', 'HERRAMIENTA', 'PRODUCTO_TERMINADO')
                           NOT NULL DEFAULT 'MATERIA_PRIMA',
    cantidad_utilizada     DECIMAL(14,3) NOT NULL,

    CONSTRAINT pk_material_utilizado PRIMARY KEY (id_material_utilizado),
    CONSTRAINT ck_material_utilizado_tipo     CHECK (tipo_producto = 'MATERIA_PRIMA'),
    CONSTRAINT ck_material_utilizado_cantidad CHECK (cantidad_utilizada > 0),
    CONSTRAINT fk_material_utilizado_trabajo
        FOREIGN KEY (numero_trabajo) REFERENCES trabajo_fabricacion (numero_trabajo)
        ON DELETE CASCADE,
    CONSTRAINT fk_material_utilizado_materia_prima
        FOREIGN KEY (codigo_materia_prima, tipo_producto)
        REFERENCES producto (codigo, tipo_producto),

    INDEX idx_material_trabajo (numero_trabajo),
    INDEX idx_material_materia (codigo_materia_prima)
) ENGINE = InnoDB;

--  7. MOVIMIENTOS DE INVENTARIO (MovimientoInventario)
--     Generados por Compra, Venta y TrabajoFabricacion ("genera"). El documento origen se identifica con referencia_documento  (número de compra / venta / trabajo), sin FK porque puede apuntar a tablas distintas.

CREATE TABLE IF NOT EXISTS movimiento_inventario (
    id_movimiento         INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    fecha                 DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    codigo_producto       VARCHAR(30)   NOT NULL,
    tipo_movimiento       ENUM('ENTRADA_COMPRA', 'SALIDA_VENTA',
                               'ENTRADA_AJUSTE', 'SALIDA_AJUSTE',
                               'SALIDA_FABRICACION') NOT NULL,
    cantidad              DECIMAL(14,3) NOT NULL,
    existencia_anterior   DECIMAL(14,3) NOT NULL,
    existencia_nueva      DECIMAL(14,3) NOT NULL,
    id_usuario            INT UNSIGNED  NOT NULL,
    referencia_documento  VARCHAR(50)   NULL,
    motivo                VARCHAR(255)  NULL,

    CONSTRAINT pk_movimiento_inventario PRIMARY KEY (id_movimiento),
    CONSTRAINT ck_movimiento_cantidad          CHECK (cantidad > 0),
    CONSTRAINT ck_movimiento_existencia_ant    CHECK (existencia_anterior >= 0),
    CONSTRAINT ck_movimiento_existencia_nueva  CHECK (existencia_nueva >= 0),
    CONSTRAINT fk_movimiento_producto
        FOREIGN KEY (codigo_producto) REFERENCES producto (codigo),
    CONSTRAINT fk_movimiento_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),

    INDEX idx_movimiento_fecha      (fecha),
    INDEX idx_movimiento_producto   (codigo_producto, fecha),
    INDEX idx_movimiento_tipo       (tipo_movimiento),
    INDEX idx_movimiento_usuario    (id_usuario),
    INDEX idx_movimiento_referencia (referencia_documento)
) ENGINE = InnoDB;

--  8. DATOS INICIALES NECESARIOS

-- Administrador inicial.

INSERT INTO usuario (tipo_usuario, nombre_usuario, contrasena_hash, nombre_completo)
SELECT 'ADMINISTRADOR', 'admin', 'CAMBIAR_HASH_ANTES_DE_USAR', 'Administrador del Sistema'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE nombre_usuario = 'admin');

-- 8.2 Cliente genérico "Consumidor Final" para ventas sin NIT.
INSERT INTO cliente (codigo, nombre, nit_cf, telefono, direccion)
SELECT 'CF', 'Consumidor Final', 'CF', NULL, 'Ciudad'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE codigo = 'CF');