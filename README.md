Prueba Técnica - Gestión de Pólizas (Java Spring Boot)
Este proyecto es una solución backend desarrollada como prueba técnica, enfocada en la gestión de pólizas de seguros y riesgos asociados. El sistema implementa una arquitectura limpia, buenas prácticas de desarrollo, persistencia en memoria (H2) y una capa de seguridad para proteger los endpoints.

🚀 ¿Qué hace este proyecto?
El sistema permite gestionar el ciclo de vida de pólizas de seguros (Individuales y Colectivas) y sus riesgos asociados.

Gestión de Pólizas: Listado, renovación (con cálculo de IPC) y cancelación.

Gestión de Riesgos: Registro y cancelación de riesgos asociados a las pólizas.

Validaciones de Negocio: Restricciones estrictas (ej. pólizas individuales solo permiten un riesgo).

Integración: Notificación asíncrona simulada hacia un sistema CORE legado mediante un mock.

Seguridad: Protección de endpoints mediante validación de API Key (x-api-key).

🛠️ Tecnologías
Java 17

Spring Boot 3.2.5

JPA / Hibernate

H2 Database (In-Memory)

OpenAPI / Swagger (Documentación)

Lombok

📥 Cómo clonar y ejecutar el proyecto
Clonar el repositorio:

Bash
git clone https://github.com/joas9212/PruebaTecnicaJesus
Importar en tu IDE:

Abre Eclipse/IntelliJ.

Selecciona File > Import > Maven > Existing Maven Projects.

Selecciona la carpeta donde clonaste el proyecto.

Ejecutar:

Busca la clase principal PolizasApplication.java (en src/main/java/com/pruebatecnica/seguros).

Haz clic derecho y selecciona Run As > Spring Boot App.

Prueba:

La base de datos H2 se precarga automáticamente con datos al iniciar.

📖 Documentación (Swagger UI)
Puedes explorar y probar los endpoints interactivamente en:
👉 http://localhost:8080/swagger-ui/index.html

Nota: Para realizar peticiones desde Swagger, presiona el botón "Authorize" en la parte superior derecha e ingresa la API Key: 123456.

📮 Pruebas con Postman
Para consumir el API fuera de Swagger, debes incluir siempre el siguiente header:

Key: x-api-key

Value: 123456

Ejemplo de petición: Agregar un riesgo
Método: POST

URL: http://localhost:8080/polizas/2/riesgos

Body (JSON):

JSON
{
  "descripcion": "Riesgo de incendio comercial"
}
Ejemplo de petición: Listar pólizas
Método: GET

URL: http://localhost:8080/polizas

🏛️ Arquitectura de la Solución
DTOs: Utilizados para el contrato de datos (transporte) desacoplado de las entidades de base de datos.

Servicios: Contienen la lógica de negocio y las reglas de dominio.

Filtros: Capa de seguridad implementada con un OncePerRequestFilter.

DataInitializer: Componente de ciclo de vida para asegurar que el entorno de pruebas siempre tenga datos listos al arrancar.

Desarrollado como prueba técnica senior.
