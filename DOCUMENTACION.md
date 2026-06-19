# Documentación Académica y Técnica

## Food Store — Sistema de Gestión de Pedidos

**Institución:** Universidad Tecnológica Nacional (UTN) — Facultad Regional Mendoza<br>
**Carrera:** Tecnicatura Universitaria en Programación<br>
**Materia:** Programación II<br>
**Integrantes:** Federico López, Nicolás Morón y Ezequiel Menéndez<br>
**Fecha de entrega:** 18 de junio de 2026<br>
**Repositorio:** https://github.com/EzequielMenendez/TPI-Programacion2-Lopez-Moron-Menendez

## Índice

1. Resumen y objetivos
2. Alcance funcional
3. Marco teórico
4. Arquitectura y organización
5. Modelo de dominio
6. Persistencia y base de datos
7. Flujo de ejecución
8. Validaciones y manejo de errores
9. Decisiones técnicas
10. Dificultades y soluciones
11. Pruebas y verificación
12. Limitaciones y mejoras
13. Instalación y ejecución
14. Conclusión
15. Bibliografía y enlaces

## 1. Resumen y objetivos

Food Store es una aplicación de consola desarrollada en Java para gestionar la operación básica de un local gastronómico. Permite administrar categorías, productos, usuarios y pedidos persistidos en una base de datos MySQL.

El objetivo académico es integrar Programación Orientada a Objetos, colecciones, interfaces, herencia, enumeraciones, excepciones, arquitectura por capas, acceso a datos con JDBC y modelado relacional.

## 2. Alcance funcional

El menú principal permite acceder a cuatro módulos:

- **Categorías:** listar, crear, editar y eliminar lógicamente.
- **Productos:** listar, crear, editar y eliminar, manteniendo la relación con una categoría.
- **Usuarios:** listar, crear, editar y eliminar, con validación de correo único.
- **Pedidos:** listar, crear, agregar productos, actualizar estado y forma de pago, y eliminar lógicamente.

El sistema es monousuario y se ejecuta por consola. No incluye interfaz gráfica, API web ni autenticación.

## 3. Marco teórico

### Programación Orientada a Objetos

Las entidades encapsulan estado y comportamiento. `Base` centraliza los atributos comunes; `Pedido` implementa la interfaz `Calculable`; los enums limitan los valores posibles de estado, forma de pago y rol.

### JDBC

JDBC es la API estándar de Java para comunicarse con bases de datos relacionales. El proyecto utiliza `DriverManager`, `Connection`, `PreparedStatement` y `ResultSet`. Las consultas parametrizadas reducen errores de formato y riesgos de inyección SQL.

### Base de datos relacional

MySQL almacena los datos en tablas vinculadas mediante claves foráneas. La estructura preserva la relación entre productos y categorías, pedidos y usuarios, y detalles con pedidos y productos.

### Maven

Maven define la versión de Java, resuelve MySQL Connector/J y ejecuta el ciclo de compilación y empaquetado a partir de `pom.xml`.

### Patrón DAO y arquitectura por capas

El patrón DAO concentra el SQL y el mapeo de registros. Los servicios aplican reglas de negocio y los menús se ocupan de la interacción por consola. Esta separación reduce el acoplamiento y facilita el mantenimiento.

## 4. Arquitectura y organización

El flujo general es:

```text
Usuario
  ↓
Main y clases Menu*
  ↓
Clases *Service
  ↓
Clases CRUD* (DAO)
  ↓
DatabaseConnection / JDBC
  ↓
MySQL
```

Responsabilidades:

| Capa | Responsabilidad | Clases principales |
|---|---|---|
| Presentación | Menús, lectura y mensajes | `Main`, `MenuCategoria`, `MenuProducto`, `MenuUsuario`, `MenuPedido` |
| Servicio | Validaciones y casos de uso | `CategoriaService`, `ProductoService`, `UsuarioService`, `PedidoService` |
| Persistencia | SQL, consultas y mapeo | `CRUDCategoria`, `CRUDProducto`, `CRUDUsuario`, `CRUDPedido` |
| Dominio | Entidades y comportamiento | `Categoria`, `Producto`, `Usuario`, `Pedido`, `DetallePedido` |
| Configuración | Apertura de conexiones | `DatabaseConnection`, `TestConnection` |

## 5. Modelo de dominio

- **Base:** clase abstracta con `id`, `eliminado`, `createdAt`, `equals()` y `hashCode()`.
- **Categoria:** nombre y descripción.
- **Producto:** nombre, precio, descripción, stock, imagen, disponibilidad y categoría.
- **Usuario:** nombre, apellido, correo, celular, contraseña y rol.
- **Pedido:** fecha, estado, total, forma de pago, usuario y lista de detalles.
- **DetallePedido:** cantidad, subtotal y producto.
- **Calculable:** contrato utilizado por `Pedido` para recalcular el total.

Relaciones principales:

```text
Categoria 1 ─── N Producto
Usuario   1 ─── N Pedido
Pedido    1 ─── N DetallePedido
Producto  1 ─── N DetallePedido
```

## 6. Persistencia y base de datos

La aplicación utiliza una base de datos MySQL ya creada y configurada para el proyecto. No se requiere preparar una base local en el entorno habitual del equipo. El archivo `schema.sql` documenta la estructura relacional y permite reproducirla en otro entorno si fuera necesario.

| Tabla | Propósito | Relaciones |
|---|---|---|
| `categoria` | Clasificar productos | Referenciada por `producto` |
| `producto` | Mantener catálogo y stock | FK a `categoria` |
| `usuario` | Registrar clientes y administradores | Referenciada por `pedido` |
| `pedido` | Cabecera de la compra | FK a `usuario` |
| `detalle_pedido` | Ítems y cantidades | FK a `pedido` y `producto` |

Todas las tablas incluyen `id`, `eliminado` y `created_at`. La baja lógica conserva el historial y evita borrar físicamente registros relacionados.

Los DAO abren conexiones con `try-with-resources`, utilizan `PreparedStatement`, recuperan claves generadas y reconstruyen objetos a partir de `ResultSet`.

## 7. Flujo de ejecución

1. `Main` crea un único `Scanner` y los servicios.
2. El usuario selecciona un módulo.
3. El menú solicita y convierte los datos ingresados.
4. El servicio valida las reglas de negocio.
5. El DAO ejecuta la operación SQL correspondiente.
6. El resultado o error se informa por consola.

Ejemplo de creación de producto:

```text
MenuProducto
  → busca la categoría seleccionada
  → ProductoService valida nombre, precio y stock
  → CRUDProducto ejecuta INSERT
  → MySQL genera el ID
  → el sistema informa el resultado
```

Al crear un pedido se persiste primero la cabecera y luego cada detalle seleccionado. El objeto `Pedido` recalcula su total en memoria al agregar productos.

## 8. Validaciones y manejo de errores

Validaciones implementadas:

- Precio y stock no negativos.
- Cantidad de detalle mayor que cero.
- Cantidad solicitada no superior al stock.
- Correo obligatorio y no duplicado.
- Existencia de entidades antes de editar o eliminar.
- Conversión controlada de datos numéricos ingresados por consola.
- Confirmación antes de eliminar.

Excepciones de dominio:

- `EntidadNoEncontradaException`
- `MailDuplicadoException`
- `StockInvalidoException`

Los errores SQL se capturan en la capa de servicio y se muestran con un mensaje contextual. Los recursos JDBC se cierran automáticamente.

## 9. Decisiones técnicas

- **Aplicación de consola:** suficiente para la consigna y permite concentrar el trabajo en POO y persistencia.
- **Capas separadas:** evita mezclar interacción, reglas y SQL.
- **Clase `Base`:** elimina duplicación de identificador, fecha y baja lógica.
- **Enums:** impiden estados, roles y medios de pago inválidos.
- **DAO específico por agregado:** mantiene consultas agrupadas por responsabilidad.
- **Baja lógica:** conserva referencias históricas.
- **Consultas preparadas:** separan SQL de parámetros.
- **MySQL y JDBC:** proporcionan persistencia relacional sin incorporar un ORM innecesario.

## 10. Dificultades y soluciones

### Correspondencia entre objetos y tablas

Los resultados de consultas con relaciones deben reconstruir objetos asociados. Se resolvió mediante `JOIN` y mapeo explícito en los DAO.

### Identificadores generados

Las entidades necesitan el ID asignado por MySQL. Los `INSERT` usan `Statement.RETURN_GENERATED_KEYS`.

### Registros eliminados

Para conservar historial se adoptó baja lógica. Los listados y búsquedas filtran `eliminado = FALSE`.

### Consistencia de pedidos

Un pedido involucra cabecera, detalles, total y stock. El código ya separa estas operaciones, pero todavía requiere una transacción única para garantizar atomicidad ante errores.

### Separación de responsabilidades

Los menús delegan validaciones a servicios y SQL a DAO, evitando que una misma clase concentre todo el comportamiento.

## 11. Pruebas y verificación

Verificaciones realizadas el 18 de junio de 2026:

- `mvn clean package`: **BUILD SUCCESS**.
- Compilación de 28 archivos fuente con destino Java 21.
- Generación de `target/mi-proyecto-1.0-SNAPSHOT.jar`.
- Ejecución de `config.TestConnection`: conexión MySQL establecida.
- Ejecución de `Main`: menú principal y submenús operativos.

La conexión JDBC con la base configurada fue verificada correctamente. Actualmente no existen pruebas automatizadas en `src/test`.

## 12. Limitaciones y mejoras

Limitaciones verificadas en el código actual:

1. La creación del pedido y sus detalles no utiliza una transacción común.
2. El total calculado al agregar detalles no se actualiza en la tabla `pedido`.
3. Al reconstruir detalles, el subtotal guardado se pasa como si fuera precio unitario y puede recalcular un total incorrecto.
4. El stock se valida, pero no se descuenta al confirmar el detalle.
5. Las credenciales están definidas dentro del código fuente.
6. `DatabaseConnection` devuelve `null` ante un fallo, lo que puede producir errores secundarios.
7. No hay pruebas unitarias ni de integración automatizadas.

Mejoras prioritarias:

- Incorporar transacciones JDBC con `commit` y `rollback`.
- Actualizar total y stock dentro de la misma transacción.
- Extraer configuración a variables de entorno.
- Agregar pruebas de servicios y DAO.
- Usar `BigDecimal` para importes monetarios.

## 13. Instalación y ejecución

1. Instalar JDK 21 y Maven.
2. Clonar el repositorio.
3. Contar con acceso a la base de datos MySQL configurada para el proyecto.
4. Verificar los datos de conexión en `DatabaseConnection`.
5. Compilar:

```bash
mvn clean package
mvn dependency:copy-dependencies -DoutputDirectory=target/dependency
```

6. Ejecutar en Windows:

```powershell
java -cp "target/classes;target/dependency/*" Main
```

## 14. Conclusión

Food Store cumple el objetivo de integrar POO, arquitectura por capas, JDBC y una base de datos relacional en una aplicación de consola. La división entre menús, servicios, DAO y entidades es clara y reutilizable. Las operaciones CRUD principales están implementadas y el proyecto compila correctamente.

El principal trabajo técnico pendiente se concentra en asegurar la consistencia transaccional de pedidos, total y stock, además de incorporar pruebas automatizadas y configuración externa.

## 15. Bibliografía y enlaces

- Oracle. Java SE 21 Documentation: https://docs.oracle.com/en/java/javase/21/
- Oracle. JDBC Basics: https://docs.oracle.com/javase/tutorial/jdbc/basics/
- Apache Software Foundation. Maven Documentation: https://maven.apache.org/guides/
- Oracle. MySQL Reference Manual: https://dev.mysql.com/doc/
- Oracle. MySQL Connector/J Developer Guide: https://dev.mysql.com/doc/connector-j/en/
- Repositorio del proyecto: https://github.com/EzequielMenendez/TPI-Programacion2-Lopez-Moron-Menendez
