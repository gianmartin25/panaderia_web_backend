-- Insertar en la tabla CargoEmpleado
INSERT INTO cargos_empleado ( nombre) VALUES ( 'Vendedor');
INSERT INTO cargos_empleado ( nombre) VALUES ( 'Jefe de Almacén');
INSERT INTO cargos_empleado ( nombre) VALUES ( 'Cajero');
INSERT INTO cargos_empleado ( nombre) VALUES ( 'Gerente');
-- Agrega más cargos según sea necesario

-- Insertar en la tabla TipoDocumento
INSERT INTO tipos_documento ( nombre) VALUES ( 'DNI');
INSERT INTO tipos_documento ( nombre) VALUES ( 'Passport');
INSERT INTO tipos_documento ( nombre) VALUES ( 'RUC');
-- Agrega más tipos de documentos según sea necesario

INSERT INTO tipos_cliente ( nombre) VALUES ('persona');
INSERT INTO tipos_cliente ( nombre) VALUES ( 'empresa');



-- Insertar en la tabla Documento
INSERT INTO documentos ( numero, tipo_documento_id) VALUES ('12345678', 1);
INSERT INTO documentos ( numero, tipo_documento_id) VALUES ( '87654321', 2);
INSERT INTO documentos (numero, tipo_documento_id) VALUES ( '13579246', 1);
INSERT INTO documentos ( numero, tipo_documento_id) VALUES ( '64257931', 2);
INSERT INTO documentos ( numero, tipo_documento_id) VALUES ( '98765432', 1);
INSERT INTO documentos ( numero, tipo_documento_id) VALUES ( '23456789', 2);
INSERT INTO documentos ( numero, tipo_documento_id) VALUES ( '98765432', 1);
INSERT INTO documentos ( numero, tipo_documento_id) VALUES ( '23456789', 2);
INSERT INTO documentos ( numero, tipo_documento_id) VALUES ( '98765432', 1);
INSERT INTO documentos ( numero, tipo_documento_id) VALUES ( '23456789', 2);
-- Agrega más documentos según sea necesario

-- Insertar en la tabla Persona
INSERT INTO personas ( apellidos, fecha_nacimiento,documento_id) VALUES ( 'Perez', '1990-01-01',1);
INSERT INTO personas (apellidos, fecha_nacimiento,documento_id) VALUES ( 'Gomez', '1991-02-02',2);
INSERT INTO personas (apellidos, fecha_nacimiento,documento_id) VALUES ( 'Rodriguez', '1992-03-03',3);
INSERT INTO personas ( apellidos, fecha_nacimiento,documento_id) VALUES ('Fernandez', '1993-04-04',4);
INSERT INTO personas ( apellidos, fecha_nacimiento,documento_id) VALUES ( 'Lopez', '1994-05-05',5);
INSERT INTO personas ( apellidos, fecha_nacimiento,documento_id) VALUES ('Torres', '1995-06-06',6);
INSERT INTO personas (apellidos, fecha_nacimiento,documento_id) VALUES ( 'Garcia', '1996-07-07',7);
INSERT INTO personas ( apellidos, fecha_nacimiento,documento_id) VALUES ( 'Sanchez', '1997-08-08',8);
INSERT INTO personas ( apellidos, fecha_nacimiento,documento_id) VALUES ( 'Ramirez', '1998-09-09',9);
INSERT INTO personas ( apellidos, fecha_nacimiento,documento_id) VALUES ( 'Vargas', '1999-10-10',10);
-- Agrega más personas según sea necesario
-- Insertar en la tabla Empleado
INSERT INTO empleados (persona_id, cargo_empleado_id, fecha_contratacion,nombres,email) VALUES (1, 1, '2020-01-01','Juan','juan@google.com');
INSERT INTO empleados ( persona_id, cargo_empleado_id, fecha_contratacion,nombres,email) VALUES ( 2, 2, '2020-02-02','Ana', 'ana@google.com');
INSERT INTO empleados ( persona_id, cargo_empleado_id, fecha_contratacion,nombres,email) VALUES ( 3, 1, '2020-03-03','Luis','luis@google.com');
INSERT INTO empleados ( persona_id, cargo_empleado_id, fecha_contratacion,nombres,email) VALUES ( 4, 2, '2020-04-04','Maria','maria@google.com');
INSERT INTO empleados (persona_id, cargo_empleado_id, fecha_contratacion,nombres,email) VALUES ( 5, 1, '2020-05-05','Carlos','carlos@google.com');
INSERT INTO empleados ( persona_id, cargo_empleado_id, fecha_contratacion,nombres,email) VALUES ( 6, 2, '2020-06-06','David','david@google.com');
INSERT INTO empleados ( persona_id, cargo_empleado_id, fecha_contratacion,nombres,email) VALUES ( 7, 1, '2020-07-07','Eva','eva@googe.com');
INSERT INTO empleados ( persona_id, cargo_empleado_id, fecha_contratacion,nombres,email) VALUES ( 8, 2, '2020-08-08','Frank','frank@google.com');
INSERT INTO empleados ( persona_id, cargo_empleado_id, fecha_contratacion,nombres,email) VALUES (9, 1, '2020-09-09','Gina','gina@google.com');
INSERT INTO empleados ( persona_id, cargo_empleado_id, fecha_contratacion,nombres,email) VALUES (10, 2, '2020-10-10','Hugo','hugo@google.com');
-- Agrega más empleados según sea necesario

INSERT INTO categorias (nombre) VALUES ('Todo');
INSERT INTO categorias (nombre) VALUES ('Pasteles');
INSERT INTO categorias (nombre) VALUES ('Bebidas');
INSERT INTO categorias (nombre) VALUES ('Postres');
INSERT INTO categorias (nombre) VALUES ('Panes');

INSERT INTO proveedores (nombre, direccion, telefono) VALUES ('Proveedor 1', 'Direccion 1', '12345678');
INSERT INTO proveedores (nombre, direccion, telefono) VALUES ('Proveedor 2', 'Direccion 2', '87654321');


INSERT INTO transportistas(empleado_id,licencia,vehiculo) VALUES (1,'123456','Auto');
INSERT INTO  metodo_pago (nombre) VALUES ('Tarjeta');

INSERT INTO tipos_comprobante (nombre) VALUES ('Factura');
INSERT INTO tipos_comprobante (nombre) VALUES ('Boleta');

INSERT  INTO tipos_movimiento (nombre) VALUES ('INGRESO');
INSERT  INTO tipos_movimiento (nombre) VALUES ('SALIDA');

INSERT INTO estado_orden (nombre) VALUES ('Pendiente');
INSERT INTO estado_orden (nombre) VALUES ('En Almacen');
INSERT INTO estado_orden (nombre) VALUES ('En Camino');
INSERT INTO estado_orden (nombre) VALUES ('Entregado');
INSERT INTO estado_orden (nombre) VALUES ('Anulado');
