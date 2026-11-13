# Informe de seguridad — plantilla para completar con resultados reales

## Medidas implementadas en el código
- Contraseñas almacenadas mediante BCrypt (`UsuarioDAO`); nunca se guarda la contraseña en texto plano.
- Consultas parametrizadas mediante `PreparedStatement`.
- Credenciales de conexión leídas desde variables de entorno (`Database`).
- Usuario no root en la etapa final del Dockerfile.
- Validaciones de datos y excepción propia `ValidacionException`.

## Análisis exigidos
Completar esta tabla después de ejecutar cada herramienta. No declarar un análisis como realizado sin guardar su salida.

| Análisis | Herramienta | Fecha | Hallazgos reales | Corrección / estado |
|---|---|---|---|---|
| SCA | Dependabot u OWASP Dependency-Check | PENDIENTE | PENDIENTE | PENDIENTE |
| SAST | SonarLint / analizador de clase | PENDIENTE | PENDIENTE | PENDIENTE |
| Imagen | Trivy | PENDIENTE | PENDIENTE | PENDIENTE |
| DAST | OWASP ZAP (solo sistema propio) | PENDIENTE | PENDIENTE | PENDIENTE |

## Riesgos y tareas antes de producción
- Cambiar la contraseña temporal `admin` y crear usuarios individuales.
- Para Aiven, activar SSL y usar la configuración/certificado recomendado por el proveedor.
- No publicar `.env`, tokens, certificados privados ni credenciales en el repositorio o historial Git.
- Verificar permisos de los roles con pruebas; ampliar las restricciones de EMPLEADO si el docente requiere permisos más finos.
- Revisar vulnerabilidades reportadas por las herramientas y documentar su resolución.
