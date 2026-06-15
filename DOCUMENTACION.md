# Documentacion Academica y Tecnica

## Caratula

**Institucion:** Tecnicatura Universitaria en Programacion a Distancia  
**Materia:** Programacion 2  
**Proyecto:** Food Store - Sistema de Gestion de Pedidos  
**Integrantes:** Lopez, Moron y Menendez  
**Fecha:** Junio 2026

## Indice

1. Introduccion
2. Marco teorico
3. Arquitectura del sistema
4. Modelo de dominio
5. Base de datos
6. Funcionamiento del sistema
7. Validaciones y manejo de errores
8. Decisiones tecnicas
9. Dificultades y soluciones
10. Pruebas realizadas
11. Pendientes antes de la entrega
12. Bibliografia y enlaces

## 1. Introduccion

Food Store es una aplicacion de consola desarrollada en Java para gestionar pedidos de comida. El sistema permite administrar categorias, productos, usuarios y pedidos mediante menus interactivos.

El objetivo principal del proyecto es aplicar Programacion Orientada a Objetos, separacion por capas, persistencia con JDBC y uso de una base de datos relacional MySQL.

## 2. Marco teorico

### Java

Java es un lenguaje orientado a objetos, tipado y multiplataforma. En este proyecto se utiliza para modelar entidades del dominio, servicios, acceso a datos y menus de consola.

### Programacion Orientada a Objetos

El proyecto aplica encapsulamiento, herencia, constructores, enums, interfaces y sobrescritura de metodos. Las entidades heredan de una clase abstracta `Base`, que contiene atributos comunes como `id`, `eliminado` y `createdAt`.

### JDBC

JDBC permite conectar Java con una base de datos relacional. Se utiliza para ejecutar sentencias SQL mediante `PreparedStatement`, leer resultados con `ResultSet` y separar el acceso a datos dentro de clases DAO.

### MySQL

MySQL es el motor de base de datos utilizado para almacenar categorias, productos, usuarios, pedidos y detalles de pedidos.

### Patron DAO

El patron DAO separa la logica de persistencia del resto de la aplicacion. Las clases DAO contienen las consultas SQL y evitan que el menu o los servicios dependan directamente de la base de datos.

## 3. Arquitectura del sistema

El proyecto esta organizado en capas:

- `Menu`: gestiona la interaccion con el usuario por consola.
- `Service`: contiene validaciones y reglas de negocio.
- `DAO`: contiene la persistencia con JDBC y SQL.
- `Entities`: contiene el modelo de dominio.
- `Enums`: define valores fijos del sistema.
- `Exceptions`: contiene excepciones propias del dominio.
- `Config`: centraliza la conexion a la base de datos.

Esta separacion permite que el codigo sea mas mantenible, testeable y facil de corregir.

## 4. Modelo de dominio

### Base

Clase abstracta heredada por todas las entidades principales. Define:

- `id`
- `eliminado`
- `createdAt`
- `equals()`
- `hashCode()`

### Categoria

Representa una categoria de productos. Contiene nombre y descripcion.

### Producto

Representa un producto del catalogo. Contiene nombre, precio, descripcion, stock, imagen, disponibilidad y una categoria asociada.

### Usuario

Representa un usuario del sistema. Contiene nombre, apellido, mail, celular, contrasenia y rol.

### Pedido

Representa una compra realizada por un usuario. Contiene fecha, estado, total, forma de pago, usuario asociado y una lista de detalles. Implementa la interfaz `Calculable`.

### DetallePedido

Representa cada item incluido en un pedido. Contiene cantidad, subtotal y producto asociado.

## 5. Base de datos

El archivo `schema.sql` crea la base `pedidos_db` y las siguientes tablas:

- `categoria`
- `producto`
- `usuario`
- `pedido`
- `detalle_pedido`

Todas las tablas principales incluyen el campo `eliminado`, usado para baja logica. Esto permite ocultar registros sin borrarlos fisicamente, conservando historial.

## 6. Funcionamiento del sistema

Al iniciar, el sistema muestra el menu principal:

```text
=== SISTEMA DE PEDIDOS (FOOD STORE) ===
1. Categorias
2. Productos
3. Usuarios
4. Pedidos
0. Salir
```

Cada opcion abre un submenu con operaciones de listado, creacion, edicion y eliminacion logica.

## 7. Validaciones y manejo de errores

El sistema contempla validaciones basicas:

- No permitir precios negativos.
- No permitir stock negativo.
- No permitir cantidades menores o iguales a cero en detalles de pedido.
- No permitir usuarios con mail vacio.
- Validar mail duplicado.
- Informar cuando un ID no existe.
- Capturar errores de formato numerico en el menu.

Las excepciones propias ayudan a expresar errores del dominio de forma clara:

- `EntidadNoEncontradaException`
- `MailDuplicadoException`
- `StockInvalidoException`

## 8. Decisiones tecnicas

- Se eligio una aplicacion de consola porque la consigna no requiere frontend ni API REST.
- Se uso una clase `Base` para evitar duplicar atributos comunes.
- Se separaron menus, servicios, entidades y DAO para respetar responsabilidades.
- Se uso baja logica para no romper relaciones historicas entre productos, usuarios y pedidos.
- Se centralizo la conexion en `DatabaseConnection`.

## 9. Dificultades y soluciones

Una dificultad principal fue separar correctamente responsabilidades entre capas. Para resolverlo, se definio que el menu solo interactua con el usuario, el servicio valida reglas de negocio y el DAO concentra las consultas SQL.

Otra dificultad fue mantener consistencia entre el modelo orientado a objetos y la base de datos. Para esto se creo `schema.sql` con claves primarias, claves foraneas y datos iniciales.

## 10. Pruebas realizadas

Pruebas basicas sugeridas:

1. Ejecutar `schema.sql`.
2. Ejecutar `config.TestConnection`.
3. Iniciar `Main`.
4. Listar categorias iniciales.
5. Crear una categoria nueva.
6. Editar la categoria creada.
7. Eliminar logicamente la categoria.
8. Verificar que no aparezca en el listado.
9. Crear usuarios, productos y pedidos desde sus menus.
10. Verificar mensajes de error con IDs inexistentes y valores numericos invalidos.

## 11. Pendientes antes de la entrega

- Completar persistencia JDBC para producto, usuario, pedido y detalle.
- Implementar transacciones al crear pedidos con detalles.
- Agregar UML actualizado.
- Agregar capturas del sistema funcionando.
- Agregar enlace publico al video demostrativo.
- Exportar este documento a PDF.

## 12. Bibliografia y enlaces

- Documentacion oficial de Java: https://docs.oracle.com/en/java/
- Documentacion JDBC: https://docs.oracle.com/javase/tutorial/jdbc/
- Documentacion MySQL: https://dev.mysql.com/doc/
- Repositorio del proyecto: agregar enlace publico.
- Video demostrativo: agregar enlace publico.
