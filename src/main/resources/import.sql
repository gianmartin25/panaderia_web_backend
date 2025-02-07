-- Insertar en la tabla CargoEmpleado
INSERT INTO cargos_empleado (id, nombre) VALUES (1, 'Vendedor');
INSERT INTO cargos_empleado (id, nombre) VALUES (2, 'Jefe de Almacén');
INSERT INTO cargos_empleado (id, nombre) VALUES (3, 'Cajero');
INSERT INTO cargos_empleado (id, nombre) VALUES (4, 'Gerente');
-- Agrega más cargos según sea necesario

-- Insertar en la tabla TipoDocumento
INSERT INTO tipos_documento (id, nombre) VALUES (1, 'DNI');
INSERT INTO tipos_documento (id, nombre) VALUES (2, 'Passport');
INSERT INTO tipos_documento (id, nombre) VALUES (3, 'RUC');
-- Agrega más tipos de documentos según sea necesario

INSERT INTO tipos_cliente (id, nombre) VALUES (1, 'persona');
INSERT INTO tipos_cliente (id, nombre) VALUES (2, 'empresa');



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
INSERT INTO empleados (persona_id, id_cargo_empleado, fecha_contratacion,nombres) VALUES (1, 1, '2020-01-01','Juan');
INSERT INTO empleados ( persona_id, id_cargo_empleado, fecha_contratacion,nombres) VALUES ( 2, 2, '2020-02-02','Ana');
INSERT INTO empleados ( persona_id, id_cargo_empleado, fecha_contratacion,nombres) VALUES ( 3, 1, '2020-03-03','Luis');
INSERT INTO empleados ( persona_id, id_cargo_empleado, fecha_contratacion,nombres) VALUES ( 4, 2, '2020-04-04','Maria');
INSERT INTO empleados (persona_id, id_cargo_empleado, fecha_contratacion,nombres) VALUES ( 5, 1, '2020-05-05','Carlos');
INSERT INTO empleados ( persona_id, id_cargo_empleado, fecha_contratacion,nombres) VALUES ( 6, 2, '2020-06-06','David');
INSERT INTO empleados ( persona_id, id_cargo_empleado, fecha_contratacion,nombres) VALUES ( 7, 1, '2020-07-07','Eva');
INSERT INTO empleados ( persona_id, id_cargo_empleado, fecha_contratacion,nombres) VALUES ( 8, 2, '2020-08-08','Frank');
INSERT INTO empleados ( persona_id, id_cargo_empleado, fecha_contratacion,nombres) VALUES (9, 1, '2020-09-09','Gina');
INSERT INTO empleados ( persona_id, id_cargo_empleado, fecha_contratacion,nombres) VALUES (10, 2, '2020-10-10','Hugo');
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
