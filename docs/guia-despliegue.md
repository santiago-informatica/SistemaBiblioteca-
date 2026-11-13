# Guía de despliegue reproducible

## 1. Preparar entorno
Instalar JDK 21, IntelliJ IDEA, Maven y Docker Desktop. Clonar el repositorio y abrirlo como proyecto Maven.

## 2. Configurar variables
Copiar `.env.example` a `.env`. Para ejecutar desde IntelliJ, configurar `DB_URL`, `DB_USER`, `DB_PASSWORD` y `APP_ADMIN_PASSWORD` en Run > Edit Configurations > Environment variables. Java no carga `.env` automáticamente.

## 3. Iniciar MySQL local
```powershell
docker compose up -d
docker compose ps
docker logs sistemabiblioteca-db --tail 50
```
Conexión local: puerto publicado 3307, esquema `biblioteca`, usuario `biblioteca`. Credenciales de ejemplo únicamente para desarrollo.

## 4. Ejecutar aplicación
Esperar a que MySQL acepte conexiones. Ejecutar `uy.utu.biblioteca.Main`. En el primer arranque se crea un usuario administrador con contraseña inicial tomada de `APP_ADMIN_PASSWORD` (hash BCrypt). Acceder y crear un usuario personal; cambiar el acceso inicial antes de publicar o utilizar información real.

## 5. Verificar funcionalidades
- Login como administrador.
- Crear/editar/buscar/eliminar un libro y un alumno.
- Crear un préstamo y verificar que disminuye el stock.
- Devolverlo y verificar que aumenta el stock.
- Generar reporte por libro.
- Abrir pestaña Estado y comprobar MySQL.
- Crear usuario EMPLEADO y verificar que no aparece la pestaña Usuarios y que no puede eliminar libros/alumnos.

## 6. Base de datos Aiven
Crear una instancia MySQL gratuita si está disponible para la cuenta y región. Ejecutar `sql/01-crear-tablas.sql` y `sql/02-datos-prueba.sql` en la base remota. Configurar `DB_URL`, `DB_USER`, `DB_PASSWORD` con los datos de Aiven y SSL/certificado indicado por Aiven. No guardar credenciales en GitHub. Verificar creando un dato desde la aplicación y consultándolo con SELECT desde Aiven.

## 7. Imagen de contenedor
```powershell
docker build -t ghcr.io/TU_USUARIO/sistema-biblioteca:2.0.0 .
docker login ghcr.io -u TU_USUARIO_GITHUB
docker push ghcr.io/TU_USUARIO/sistema-biblioteca:2.0.0
```
El contenedor de escritorio requiere servidor gráfico para mostrar Swing. El uso previsto es ejecutar Swing localmente y conectarlo a Aiven.

## 8. Evidencias pendientes
Guardar capturas reales de `docker compose ps`, aplicación conectada, publicación GHCR, base Aiven y consulta SQL, y Kubernetes si se realiza el nivel 3. No inventar capturas ni resultados.
