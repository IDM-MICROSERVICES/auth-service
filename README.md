## 🔧 Configuración Principal

Asegúrate de configurar las siguientes propiedades en tu `application.properties` o `application.yml` antes de ejecutar:

* **Puerto por defecto:** `8080` (Configurable).
* **Base de Datos:** Requiere una instancia de PostgreSQL activa.
* **JWT Secret:** Se debe definir una clave secreta para la firma de los tokens.

## 🛠️ Instalación y Uso

1. **Clonación:**
   ```bash
   git clone [https://github.com/IDM-MICROSERVICES/auth-service.git](https://github.com/IDM-MICROSERVICES/auth-service.git)

2. **Compilación:**
   mvn clean install

3. **Ejecución:**
   mvn spring-boot:run

**Endpoints Base**

POST /auth/login: Autenticación de usuario y retorno de Token.

POST /auth/register: Creación de nuevos usuarios con cifrado de contraseñas.
