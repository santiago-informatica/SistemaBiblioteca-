# Sistema de Biblioteca — base inicial

Proyecto Java 21 + Swing + MySQL para IntelliJ. Esta entrega es un **esqueleto inicial**, no el sistema completo ni una compilación probada.

## Preparación
1. Instalar JDK 21, IntelliJ y Docker.
2. Copiar `.env.example` a `.env` y cambiar las contraseñas.
3. Ejecutar `docker compose up -d` desde la carpeta del proyecto.
4. Configurar en IntelliJ las variables `DB_URL`, `DB_USER` y `DB_PASSWORD` (Java no lee `.env` automáticamente).
5. Ejecutar `Main.java`.

## Incluido ahora
- Base MySQL con tablas `usuarios`, `alumnos`, `libros` y `prestamos`.
- 20 libros y 20 alumnos de ejemplo.
- Capa DAO para libros, interfaz `LibroDAO`, `PreparedStatement` y filtro por título/autor.
- Ventana Swing inicial para listar libros.
- Herencia básica `Persona` → `Alumno`.
- Docker Compose para la base de datos.

## Falta implementar antes de entregar
Login con BCrypt y roles reales; CRUD completo de alumnos; gestión de préstamos y devoluciones con transacción commit/rollback; reportes con JOIN, COUNT, SUM, AVG y GROUP BY; validaciones y excepción propia; segunda implementación de interfaz/polimorfismo; pantalla de estado de conexión; configuración completa y pruebas.

## Importante
No subir `.env` a GitHub. El examen requiere R1–R10 obligatorios: esta base todavía NO los cumple todos. Hay que continuar desarrollándola y probarla en IntelliJ y MySQL antes de presentarla.
