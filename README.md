# Sistema Biblioteca — Java 21 + Swing + MySQL

Proyecto de escritorio para Programación Avanzada. Esta versión amplía la base original: mantiene Java 21, Maven, Swing y MySQL; incorpora autenticación BCrypt, roles, CRUD, préstamos/devoluciones transaccionales, reportes, validaciones y documentación inicial.

> **Importante:** el código y los archivos de despliegue están preparados, pero deben probarse en la PC del grupo y completarse los pasos externos (Aiven, GitHub Container Registry, Kubernetes y análisis de seguridad). No presentar como evidencia una captura que todavía no se haya obtenido.

## Requisitos
- JDK 21
- IntelliJ IDEA
- Maven 3.9+ (o Maven Wrapper si se agrega)
- Docker Desktop con Docker Compose

## Inicio rápido en Windows
1. Descomprimir el ZIP y abrir la carpeta `SistemaBiblioteca_Final` en IntelliJ como proyecto Maven.
2. Copiar `.env.example` a `.env` (el archivo `.env` no se sube a GitHub).
3. En PowerShell, dentro de la carpeta:
   ```powershell
   docker compose up -d
   ```
4. En IntelliJ, configurar variables de entorno de la ejecución de `Main`:
   ```text
   DB_URL=jdbc:mysql://localhost:3307/biblioteca?useSSL=false&allowPublicKeyRetrieval=true
   DB_USER=biblioteca
   DB_PASSWORD=admin
   APP_ADMIN_PASSWORD=CAMBIAR_POR_UNA_CLAVE_SEGURA
   ```
   Java no carga `.env` automáticamente; por eso se configuran en IntelliJ. También se pueden definir en el entorno de Windows.
5. Esperar a que MySQL esté saludable y ejecutar `Main.java`.
6. Primer acceso: usuario `admin` y la contraseña indicada en `APP_ADMIN_PASSWORD`. El usuario inicial se crea al primer inicio con BCrypt. No hay una contraseña de aplicación fija en el código.

Si la base ya tenía un volumen de una instalación anterior, los scripts de `docker-entrypoint-initdb.d` no se ejecutan de nuevo. Para reiniciar desde cero (se pierden los datos del volumen): `docker compose down -v` y luego `docker compose up -d`.

## Funciones incluidas
- Login con BCrypt y roles `ADMIN` / `EMPLEADO`.
- Administrador: pestaña de usuarios para crear cuentas, asignar rol y activar/desactivar cuentas.
- Libros: alta, baja, modificación, consulta y filtros.
- Alumnos: alta, baja, modificación, consulta y filtros.
- Préstamos: alta, baja (solo ADMIN), consulta con JOIN y actualización mediante devolución.
- Préstamo y devolución modifican dos tablas en una transacción con `commit` / `rollback`.
- Reporte por libro con `COUNT`, `SUM` y `GROUP BY`.
- Validación de entradas, excepción propia `ValidacionException` y mensajes controlados.
- Pantalla para comprobar el estado de conexión MySQL.
- SQL con `PreparedStatement` en operaciones con parámetros.

## Requisitos del examen (ubicación orientativa)
| Requisito | Evidencia en el proyecto |
|---|---|
| R1 | `sql/01-crear-tablas.sql`: usuarios, alumnos, libros, prestamos y claves foráneas |
| R2 | `MySqlLibroDAO`, `AlumnoDAO`, `UsuarioDAO`, `PrestamoDAO` y pestañas Swing |
| R3 | `UsuarioDAO.autenticar`, BCrypt, `Usuario.rol`, pestaña Usuarios solo ADMIN |
| R4 | Modelo (`Libro`, `Alumno`, etc.), DAO, `ServicioBiblioteca`, `Main` |
| R5 | `Persona` → `Alumno`; `LibroDAO` con `MySqlLibroDAO` y `MemoryLibroDAO` |
| R6 | `PrestamoDAO.listar` usa JOIN; `reportePorLibro` usa COUNT/SUM/GROUP BY |
| R7 | `PrestamoDAO.crear` y `devolver` usan transacción, commit y rollback |
| R8 | `Validador`, `ValidacionException`, validaciones en `ServicioBiblioteca` |
| R9 | Consultas parametrizadas con `PreparedStatement` |
| R10 | `Database` lee `DB_URL`, `DB_USER`, `DB_PASSWORD` y `APP_ADMIN_PASSWORD` |
| R11 | `sql/02-datos-prueba.sql` incluye 20 libros y 20 alumnos |
| R12 | pestaña Estado y `Database.comprobar()` |

## Docker
`docker compose up -d` inicia MySQL con persistencia y healthcheck. El Dockerfile construye una imagen Java 21 para la aplicación. Como la aplicación es Swing (escritorio), normalmente se ejecuta en la PC y se conecta a MySQL; ejecutar una interfaz gráfica dentro de un contenedor requiere configurar un servidor gráfico/reenviar DISPLAY y no está automatizado aquí.

Para construir la imagen: `docker build -t sistema-biblioteca:2.0.0 .`

## Nube Aiven (proyecto de escritorio)
Crear MySQL en Aiven, ejecutar ambos scripts SQL contra esa instancia y configurar las variables de IntelliJ con el host, puerto, base, usuario y contraseña de Aiven. La URL JDBC debe incluir SSL según el certificado/configuración descargados de Aiven. No usar `useSSL=false` en la nube. La conexión TLS y la prueba de datos deben verificarse antes de documentarlas.

## Publicación GitHub / GHCR
1. Revisar `.gitignore`; confirmar que `.env` no esté versionado (`git status`).
2. Crear un Personal Access Token con permisos necesarios para publicar paquetes.
3. `docker login ghcr.io -u TU_USUARIO_GITHUB`
4. `docker build -t ghcr.io/TU_USUARIO/sistema-biblioteca:2.0.0 .`
5. `docker push ghcr.io/TU_USUARIO/sistema-biblioteca:2.0.0`

## Kubernetes
Los manifiestos de `k8s/` son plantillas para adaptar en el taller. Reemplazar los valores `CAMBIAR_*`, crear Secret desde la terminal (no subir secretos reales) y verificar el entorno. Para una app Swing, Kubernetes no proporciona por sí solo una pantalla gráfica; el escenario principal indicado por el enunciado para escritorio es ejecutar la aplicación local y conectarla a Aiven.

## Seguridad
Ver `docs/informe-seguridad.md`. Ejecutar Trivy sobre la imagen, revisar dependencias (Dependabot/OWASP Dependency-Check), análisis estático y, si corresponde, DAST únicamente contra el propio proyecto. Registrar fecha, herramienta, hallazgos y correcciones reales.

## Estructura
- `src/main/java`: aplicación Java.
- `sql/`: creación y datos de prueba.
- `docs/`: despliegue y seguridad.
- `k8s/`: ejemplos de manifiestos.
