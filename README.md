# Food Store — Sistema de Gestión de Pedidos

Trabajo Práctico Integrador de **Programación II**. Aplicación de consola desarrollada en Java para administrar categorías, productos, usuarios y pedidos de un local gastronómico.

## Tecnologías

- Java 21
- Apache Maven
- MySQL
- JDBC con MySQL Connector/J
- Programación Orientada a Objetos
- Arquitectura por capas y patrón DAO

## Requisitos

- JDK 21 o superior
- Maven 3.9 o superior
- Acceso a la base de datos MySQL configurada

## Base de datos

La aplicación utiliza una base de datos MySQL ya creada y configurada para el proyecto. No requiere instalar ni preparar una base local para su ejecución en el entorno del equipo.

La conexión se encuentra centralizada en `src/config/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/pedidos_db";
private static final String USER = "TU_USUARIO";
private static final String PASSWORD = "TU_CONTRASENA";
```

El archivo [`schema.sql`](schema.sql) se conserva como referencia de la estructura relacional y para reproducir la base en otro entorno si fuera necesario.

## Compilación y ejecución

Compilar y descargar dependencias:

```bash
mvn clean package
mvn dependency:copy-dependencies -DoutputDirectory=target/dependency
```

Ejecutar en Windows:

```powershell
java -cp "target/classes;target/dependency/*" Main
```

Ejecutar en Linux/macOS:

```bash
java -cp "target/classes:target/dependency/*" Main
```

También se puede ejecutar la clase `Main` directamente desde IntelliJ IDEA. Para comprobar solamente la conexión, ejecutar `config.TestConnection`.

## Funcionalidades

- Gestión CRUD de categorías.
- Gestión CRUD de productos asociados a categorías.
- Gestión CRUD de usuarios con control de correo duplicado.
- Creación, consulta, actualización y baja lógica de pedidos.
- Registro de detalles de pedido y cálculo del total en memoria.
- Validación de precios, stock, cantidades e identificadores.
- Persistencia mediante consultas parametrizadas con `PreparedStatement`.

Las eliminaciones son lógicas: los registros se conservan con `eliminado = true` y dejan de aparecer en los listados.

## Arquitectura

```text
Main y Menús
    ↓
Servicios
    ↓
DAO / JDBC
    ↓
MySQL
```

```text
src/
├── Main.java
├── MenuCategoria.java
├── MenuProducto.java
├── MenuUsuario.java
├── MenuPedido.java
├── config/       # Conexión y prueba de conexión
├── dao/          # Consultas SQL y mapeo relacional
├── entities/     # Modelo de dominio
├── enums/        # Estado, FormaPago y Rol
├── exceptions/   # Excepciones del dominio
└── service/      # Casos de uso y validaciones
```

## Modelo de datos

- `categoria`: clasificación de productos.
- `producto`: catálogo, precio, stock y categoría.
- `usuario`: datos personales, credenciales y rol.
- `pedido`: fecha, estado, medio de pago, total y usuario.
- `detalle_pedido`: productos y cantidades de cada pedido.

## Reglas de negocio implementadas

- El precio y el stock no pueden ser negativos.
- La cantidad de un detalle debe ser mayor que cero.
- No se puede solicitar más cantidad que el stock disponible.
- El correo del usuario es obligatorio y único entre usuarios activos.
- Las búsquedas y actualizaciones ignoran registros eliminados.
- El total del pedido se calcula a partir de sus detalles.

## Estado y limitaciones conocidas

El proyecto compila correctamente con Maven y los DAO de las cuatro áreas utilizan JDBC. Antes de considerar completa la persistencia de pedidos se recomienda:

- Ejecutar pedido, detalles, actualización del total y descuento de stock dentro de una única transacción.
- Actualizar en la base el total calculado al agregar detalles.
- Corregir la reconstrucción del subtotal al cargar detalles de un pedido.
- Incorporar pruebas automatizadas.
- Mover las credenciales de conexión a variables de entorno o configuración externa.

## Documentación

- [Documentación académica y técnica](DOCUMENTACION.md)
- [Informe final en PDF](docs/Entrega_Final_Food_Store_UTN.pdf)
- [Repositorio GitHub](https://github.com/EzequielMenendez/TPI-Programacion2-Lopez-Moron-Menendez)
