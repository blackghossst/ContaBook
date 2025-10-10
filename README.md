# 🧮 ContaBook - Sistema de Contabilidad Empresarial
Sistema de registro y gestión de libros de cuentas desarrollado en Java con interfaz gráfica Swing y base de datos PostgreSQL.

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Requisitos](#-requisitos)
- [Instalación](#-instalación)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Configuración de Base de Datos](#-configuración-de-base-de-datos)
- [Uso del Sistema](#-uso-del-sistema)
- [Roles y Permisos](#-roles-y-permisos)
- [Capturas de Pantalla](#-capturas-de-pantalla)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Autor](#-autor)

---

## ✨ Características

### 🔐 Autenticación y Seguridad
- ✅ Sistema de login con encriptación SHA-256
- ✅ Registro de nuevos usuarios
- ✅ Control de acceso basado en roles (Usuario, Contador, Admin)
- ✅ Validación de contraseña para operaciones críticas

### 📊 Gestión de Partidas Contables
- ✅ Agregar partidas de Ingreso y Gasto
- ✅ Editar partidas existentes
- ✅ Eliminar partidas (con confirmación por contraseña)
- ✅ Adjuntar documentos de comprobación (PDF, imágenes, documentos)
- ✅ Visualización de documentos adjuntos

### 💰 Panel Financiero
- ✅ Resumen en tiempo real de:
  - Total de Ingresos
  - Total de Gastos
  - Balance actual
- ✅ Contador de transacciones
- ✅ Actualización automática al agregar/editar/eliminar

### 🎨 Interfaz Gráfica
- ✅ Diseño moderno y limpio
- ✅ Paleta de colores verde suave
- ✅ Efectos hover en botones
- ✅ Formularios en ventanas modales
- ✅ Tabla interactiva de transacciones

---

## 🔧 Requisitos

### Software Necesario

| Software | Versión Mínima | Propósito |
|----------|----------------|-----------|
| Java JDK | 11+ | Entorno de desarrollo |
| PostgreSQL | 13+ | Base de datos |
| NetBeans/IntelliJ/Eclipse | Cualquiera | IDE (opcional) |
| pgAdmin | 4+ | Administración de BD (opcional) |

### Dependencias

- **PostgreSQL JDBC Driver**: `postgresql-42.7.1.jar`
  - [Descargar aquí](https://jdbc.postgresql.org/download/)

---

## 📥 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/blackghossst/ContaBook.git
cd contabook
```

### 2. Configurar PostgreSQL

Asegúrate de tener PostgreSQL instalado y corriendo:

```bash
# Verificar estado de PostgreSQL
sudo service postgresql status

# Iniciar PostgreSQL (si está detenido)
sudo service postgresql start
```

### 3. Crear la Base de Datos

Ejecuta los siguientes comandos en PostgreSQL:

```sql
-- Conectarse a PostgreSQL
psql -U postgres

-- Crear la base de datos
CREATE DATABASE Contabook;

-- Conectarse a la base de datos
\c Contabook
```

### 4. Crear las Tablas

```sql
-- Tabla de usuarios
CREATE TABLE usuario (
    idusuario SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL
);

-- Tabla de transacciones
CREATE TABLE transacciones (
    idtransaccion SERIAL PRIMARY KEY,
    fecha VARCHAR(20) NOT NULL,
    referencia VARCHAR(100),
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('Ingreso', 'Gasto')),
    categoria VARCHAR(100),
    descripcion TEXT,
    monto DECIMAL(10, 2) NOT NULL,
    usuario VARCHAR(200),
    documento BYTEA,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para optimización
CREATE INDEX idx_tipo ON transacciones(tipo);
CREATE INDEX idx_fecha ON transacciones(fecha);
CREATE INDEX idx_usuario_tabla ON transacciones(usuario);
CREATE INDEX idx_usuario_login ON usuario(usuario);
```

### 5. Insertar Usuario Administrador Inicial

```sql
-- Contraseña: admin123 (encriptada con SHA-256)
INSERT INTO usuario (nombre, apellido, usuario, contraseña, rol) 
VALUES ('Admin', 'Sistema', 'admin', 
        '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 
        'Admin');
```

### 6. Configurar Conexión en el Proyecto

Edita el archivo `conexion/Conexion.java`:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/Contabook";
private static final String USER = "postgres";  // Tu usuario de PostgreSQL
private static final String PASSWORD = "tu_contraseña";  // Tu contraseña
```

### 7. Agregar el Driver PostgreSQL

**En NetBeans:**
1. Click derecho en **Libraries/Dependencies**
2. **Add JAR/Folder**
3. Selecciona `postgresql-42.7.1.jar`

**En IntelliJ IDEA:**
1. File → Project Structure → Libraries
2. Click en `+` → Java
3. Selecciona `postgresql-42.7.1.jar`

### 8. Compilar y Ejecutar

```bash
# Compilar
javac -cp .:postgresql-42.7.1.jar vistas/*.java conexion/*.java

# Ejecutar
java -cp .:postgresql-42.7.1.jar vistas.PortadaContaBook
```

O simplemente ejecuta desde tu IDE:
- **Clase principal**: `vistas.PortadaContaBook`

---

## 📁 Estructura del Proyecto

```
ContaBook/
├── src/
│   ├── vistas/
│   │   ├── PortadaContaBook.java      # Pantalla de bienvenida
│   │   ├── login.java                  # Sistema de autenticación
│   │   ├── Registro.java               # Registro de usuarios
│   │   ├── Principal.java              # Dashboard principal
│   │   └── FormularioPartida.java      # Formulario de partidas
│   │
│   └── conexion/
│       └── Conexion.java               # Gestión de conexión a BD
│
├── lib/
│   └── postgresql-42.7.1.jar           # Driver de PostgreSQL
│
├── README.md                            # Este archivo
└── LICENSE                              # Licencia del proyecto
```

---

## 🗄️ Configuración de Base de Datos

### Tabla: `usuario`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `idusuario` | SERIAL | ID único del usuario |
| `nombre` | VARCHAR(100) | Nombre del usuario |
| `apellido` | VARCHAR(100) | Apellido del usuario |
| `usuario` | VARCHAR(50) | Nombre de usuario (único) |
| `contraseña` | VARCHAR(255) | Contraseña encriptada (SHA-256) |
| `rol` | VARCHAR(50) | Rol del usuario (Usuario/Contador/Admin) |

### Tabla: `transacciones`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `idtransaccion` | SERIAL | ID único de la transacción |
| `fecha` | VARCHAR(20) | Fecha de la transacción |
| `referencia` | VARCHAR(100) | Referencia o número de documento |
| `tipo` | VARCHAR(20) | Tipo (Ingreso o Gasto) |
| `categoria` | VARCHAR(100) | Categoría de la transacción |
| `descripcion` | TEXT | Descripción detallada |
| `monto` | DECIMAL(10,2) | Monto de la transacción |
| `usuario` | VARCHAR(200) | Usuario que creó la transacción |
| `documento` | BYTEA | Archivo adjunto (opcional) |
| `fecha_registro` | TIMESTAMP | Fecha y hora de registro |

---

## 🚀 Uso del Sistema

### 1️⃣ Inicio de Sesión

1. Ejecuta la aplicación
2. Se abrirá la **Portada de ContaBook**
3. Click en **"INGRESAR AL SISTEMA"**
4. Ingresa tus credenciales:
   - **Usuario**: `admin`
   - **Contraseña**: `admin123`
5. Click en **"Iniciar Sesión"**

### 2️⃣ Registrar Nuevo Usuario

1. En el login, click en **"Regístrate aquí"**
2. Completa el formulario:
   - Nombre
   - Apellido
   - Usuario (único)
   - Rol (Usuario/Contador/Admin)
   - Contraseña
   - Confirmar contraseña
3. Click en **"Registrarse"**

### 3️⃣ Agregar una Partida Contable

1. En el Dashboard, click en **"+ Nueva Partida Contable"**
2. Se abrirá el formulario modal
3. Completa los campos:
   - **Fecha**: Automática o manual
   - **Tipo**: Ingreso o Gasto
   - **Categoría**: Ej. Ventas, Servicios, Materia Prima
   - **Descripción**: Detalle de la transacción
   - **Monto**: Cantidad en dólares
   - **Documento** (opcional): Adjuntar comprobante
4. Click en **"💾 Guardar Partida"**

### 4️⃣ Editar una Partida

1. En la tabla, click en el icono **✏️** (Editar)
2. Los datos se cargarán en el formulario
3. Modifica los campos necesarios
4. Click en **"💾 Actualizar Partida"**

### 5️⃣ Eliminar una Partida

1. En la tabla, click en el icono **🗑️** (Eliminar)
2. Ingresa tu contraseña para confirmar
3. Confirma la eliminación
4. La partida será eliminada permanentemente

### 6️⃣ Ver Documento Adjunto

1. En la tabla, click en el icono **📄** (si existe documento)
2. El documento se abrirá automáticamente

---

## 👥 Roles y Permisos

### 🟢 Usuario (Usuario Normal)

| Permiso | Acceso |
|---------|--------|
| Ver Dashboard | ✅ Sí |
| Agregar Partidas | ✅ Sí |
| Editar Partidas | ❌ No |
| Eliminar Partidas | ❌ No |
| Ver Períodos | ❌ No |
| Ver Reportes | ❌ No |
| Gestionar Usuarios | ❌ No |

### 🟡 Contador

| Permiso | Acceso |
|---------|--------|
| Ver Dashboard | ✅ Sí |
| Agregar Partidas | ✅ Sí |
| Editar Partidas | ✅ Sí |
| Eliminar Partidas | ✅ Sí (con contraseña) |
| Ver Períodos | ✅ Sí |
| Ver Reportes | ✅ Sí |
| Gestionar Usuarios | ❌ No |

### 🔴 Administrador (Admin)

| Permiso | Acceso |
|---------|--------|
| Ver Dashboard | ✅ Sí |
| Agregar Partidas | ✅ Sí |
| Editar Partidas | ✅ Sí |
| Eliminar Partidas | ✅ Sí (con contraseña) |
| Ver Períodos | ✅ Sí |
| Ver Reportes | ✅ Sí |
| Gestionar Usuarios | ✅ Sí |

---

## 📸 Capturas de Pantalla

### Portada de Bienvenida
<img width="1231" height="865" alt="image" src="https://github.com/user-attachments/assets/713ff6e6-fe3a-4184-aa0f-f0e5d75cfabb" />


### Login
<img width="983" height="743" alt="image" src="https://github.com/user-attachments/assets/7d2b7009-724d-4285-b692-64b466eb7297" />


### Dashboard Principal
<img width="1919" height="1019" alt="image" src="https://github.com/user-attachments/assets/1697e73a-cf9a-4aa7-bcb7-99852888cb48" />


## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Uso |
|------------|---------|-----|
| **Java** | 11+ | Lenguaje de programación principal |
| **Swing** | Built-in | Framework de interfaz gráfica |
| **PostgreSQL** | 13+ | Sistema de gestión de base de datos |
| **JDBC** | 42.7.1 | Conector Java-PostgreSQL |
| **SHA-256** | Built-in | Encriptación de contraseñas |

### Características de Java Utilizadas
- ✅ POO (Programación Orientada a Objetos)
- ✅ JDBC para conexión a BD
- ✅ Java Swing para GUI
- ✅ Event Listeners
- ✅ File I/O para documentos
- ✅ Exception Handling
- ✅ Encriptación con MessageDigest

---

## 🔒 Seguridad

### Medidas Implementadas

1. **Encriptación de Contraseñas**
   - Algoritmo: SHA-256
   - No se almacenan contraseñas en texto plano

2. **Validación de Permisos**
   - Control de acceso basado en roles
   - Verificación en cada operación crítica

3. **Confirmación de Eliminación**
   - Doble confirmación
   - Requiere contraseña del usuario

4. **Prepared Statements**
   - Prevención de SQL Injection
   - Parametrización de consultas

---

## 🐛 Solución de Problemas

### Error: "Driver not found"

**Causa**: El driver de PostgreSQL no está agregado al proyecto.

**Solución**:
```bash
# Descargar el driver
wget https://jdbc.postgresql.org/download/postgresql-42.7.1.jar

# Agregar al classpath
java -cp .:postgresql-42.7.1.jar vistas.PortadaContaBook
```

### Error: "Connection refused"

**Causa**: PostgreSQL no está corriendo o la configuración es incorrecta.

**Solución**:
```bash
# Verificar estado
sudo service postgresql status

# Iniciar PostgreSQL
sudo service postgresql start

# Verificar puerto (debe ser 5432)
psql -U postgres -c "SHOW port;"
```

### Error: "Usuario o contraseña incorrectos"

**Causa**: Credenciales incorrectas o usuario no existe.

**Solución**:
1. Verifica que el usuario existe en la BD
2. Usa las credenciales por defecto: `admin` / `admin123`
3. Si olvidaste la contraseña, inserta un nuevo usuario admin

---

## 📝 Roadmap

### Versión Actual: 1.0.0
- ✅ Sistema de autenticación
- ✅ CRUD de partidas contables
- ✅ Adjuntar documentos
- ✅ Resumen financiero en tiempo real

## 👨‍💻 Autor

**Nestor Mendoza**
- GitHub: [@nemma](https://github.com/nemma)
- Email: nemmanuel2001@gmail.com

- **Melida Fuentes**
- Github:
- Email: fm21015@ues.edu.sv

---
<div align="center">

**⭐ Si te gustó este proyecto, dale una estrella en GitHub ⭐**

Hecho con ❤️ por nemma

</div>
