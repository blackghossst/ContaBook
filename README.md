# 🧮 ContaBook - Sistema de Contabilidad Empresarial

![Java](https://img.shields.io/badge/Java-11%2B-orange)
![Swing](https://img.shields.io/badge/GUI-Swing-blue)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-336791)
![License](https://img.shields.io/badge/License-MIT-green)

Sistema de escritorio para la gestión integral de registros contables empresariales. Permite a usuarios, contadores y administradores llevar un control detallado de transacciones financieras, generar reportes, gestionar cuentas contables y administrar usuarios.

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
- [Seguridad](#-seguridad)
- [Solución de Problemas](#-solución-de-problemas)
- [Autor](#-autor)

---

## ✨ Características

### 🔐 Autenticación y Seguridad
- ✅ Sistema de login con encriptación SHA-256
- ✅ Registro de nuevos usuarios con validación
- ✅ Control de acceso basado en roles (Usuario, Contador, Admin)
- ✅ Validación de contraseña para operaciones críticas
- ✅ Arquitectura MVC con LoginController

### 💰 Gestión de Partidas Contables
- ✅ Agregar partidas de Ingreso y Gasto
- ✅ Editar partidas existentes (con permisos)
- ✅ Eliminar partidas con confirmación por contraseña
- ✅ Adjuntar documentos de comprobación (PDF, JPG, PNG, DOC)
- ✅ Visualización de documentos adjuntos
- ✅ Selección de cuentas desde base de datos
- ✅ Subcategorías opcionales
- ✅ Actualización automática de saldos de cuentas

### 📊 Reportes Financieros
- ✅ **Balance General**: Activos, Pasivos y Patrimonio
- ✅ **Libro Mayor**: Registro completo de débitos y créditos
- ✅ Exportación a PDF profesional con iText
- ✅ Marca de agua con información del usuario generador
- ✅ Resumen en tiempo real de ingresos, gastos y balance

### 📅 Filtros y Períodos
- ✅ Filtrado por rango de fechas personalizado
- ✅ Filtrado por últimos N días (1-365)
- ✅ Filtrado por año específico
- ✅ Filtrado por tipo (Ingreso/Gasto)
- ✅ Vista de todas las transacciones
- ✅ Resumen financiero del período filtrado

### 💳 Gestión de Cuentas Contables
- ✅ Crear y visualizar cuentas T
- ✅ Registro automático de movimientos por cuenta
- ✅ Cálculo dinámico de saldos
- ✅ Visualización de débitos y créditos
- ✅ Actualización automática al agregar transacciones

### 👥 Gestión de Usuarios (Solo Admin)
- ✅ CRUD completo de usuarios
- ✅ Asignación de roles
- ✅ Edición de usuarios con validación
- ✅ Eliminación con confirmación por contraseña
- ✅ Control de permisos según rol

### 🎨 Interfaz Gráfica
- ✅ Diseño moderno con paleta verde pastel (#8CA08C, #C8DCB4)
- ✅ Tipografía: Segoe UI
- ✅ Efectos hover en botones
- ✅ Formularios en ventanas modales (JDialog)
- ✅ Tabla interactiva con íconos Unicode
- ✅ Pantalla de portada profesional
- ✅ Ventanas maximizadas adaptables

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

- **PostgreSQL JDBC Driver**: `postgresql-42.7.7.jar`
  - [Descargar aquí](https://jdbc.postgresql.org/download/)
  
- **iText PDF**: `itextpdf-5.5.13.3.jar`
  - [Descargar aquí](https://github.com/itext/itextpdf)
  
- **JCalendar**: `jcalendar-tz-1.3.3-4.jar`
  - [Descargar aquí](https://toedter.com/jcalendar/)

---

## 📥 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/blackghossst/ContaBook.git
cd ContaBook
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
    rol VARCHAR(50) NOT NULL CHECK (rol IN ('Usuario', 'Contador', 'Admin', 'Administrador')),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de transacciones
CREATE TABLE transacciones (
    idtransaccion SERIAL PRIMARY KEY,
    fecha VARCHAR(20) NOT NULL,
    referencia VARCHAR(100),
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('Ingreso', 'Gasto')),
    categoria VARCHAR(100),
    descripcion TEXT,
    monto DECIMAL(15, 2) NOT NULL CHECK (monto > 0),
    usuario VARCHAR(200),
    documento BYTEA,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de cuentas contables
CREATE TABLE cuentas (
    idcuenta SERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    saldo DECIMAL(15, 2) DEFAULT 0.00
);

-- Índices para optimización
CREATE INDEX idx_tipo ON transacciones(tipo);
CREATE INDEX idx_fecha ON transacciones(fecha);
CREATE INDEX idx_usuario_tabla ON transacciones(usuario);
CREATE INDEX idx_usuario_login ON usuario(usuario);
CREATE INDEX idx_categoria ON transacciones(categoria);
CREATE INDEX idx_fecha_registro ON transacciones(fecha_registro);
```

### 5. Insertar Usuario Administrador Inicial

```sql
-- Contraseña: admin123 (encriptada con SHA-256)
INSERT INTO usuario (nombre, apellido, usuario, contraseña, rol) 
VALUES ('Admin', 'Sistema', 'admin', 
        '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 
        'Admin');

-- Usuarios de prueba (contraseña: admin123)
INSERT INTO usuario (nombre, apellido, usuario, contraseña, rol) VALUES
('Juan', 'Pérez', 'jperez', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Usuario'),
('María', 'García', 'mgarcia', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Contador');

-- Cuentas contables de ejemplo
INSERT INTO cuentas (nombre, saldo) VALUES
('Caja', 0.00),
('Bancos', 0.00),
('Inventario', 0.00),
('Proveedores', 0.00),
('Ventas', 0.00),
('Gastos Operativos', 0.00);
```

### 6. Configurar Conexión en el Proyecto

Edita el archivo `conexion/Conexion.java`:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/Contabook";
private static final String USER = "postgres";  // Tu usuario de PostgreSQL
private static final String PASSWORD = "tu_contraseña";  // Tu contraseña
```

### 7. Agregar las Dependencias

**En NetBeans:**
1. Click derecho en **Dependencies**
2. **Add JAR/Folder**
3. Selecciona los JARs:
   - `postgresql-42.7.7.jar`
   - `itextpdf-5.5.13.3.jar`
   - `jcalendar-tz-1.3.3-4.jar`

**En IntelliJ IDEA:**
1. File → Project Structure → Libraries
2. Click en `+` → Java
3. Selecciona todos los JARs

### 8. Compilar y Ejecutar

Ejecuta la clase principal: `vistas.PortadaContaBook`

O desde terminal:
```bash
java -cp .:lib/* vistas.PortadaContaBook
```

---

## 📁 Estructura del Proyecto

```
ContaBook/ [main]
├── Source Packages
│   ├── Controlador/
│   │   ├── LoginController.java        # Controlador MVC del login
│   │   └── RegistroControlador.java    # Controlador del registro
│   │
│   ├── com.mycompany.contabook/
│   │   └── ContaBook.java              # Clase principal del proyecto
│   │
│   ├── conexion/
│   │   └── Conexion.java               # Gestión de conexión a BD y encriptación SHA-256
│   │
│   ├── models/
│   │   └── [Modelos de datos]          # Clases de modelo (si aplica)
│   │
│   └── vistas/
│       ├── FormularioPartida.java      # Formulario modal de transacciones (CRUD)
│       ├── GeneradorPDF.java           # Generador de reportes PDF (Balance/Libro Mayor)
│       ├── LoginVista.java             # Vista de login con arquitectura MVC
│       ├── Periodos.java               # Módulo de filtros y períodos contables
│       ├── PortadaContaBook.java       # Pantalla de bienvenida del sistema
│       ├── Principal.java              # Dashboard principal y navegación
│       ├── Registro.java               # Formulario de registro de usuarios
│       ├── Usuarios.java               # Gestión completa de usuarios (Admin)
│       └── login.java                  # Vista alternativa de login
│
├── Test Packages/
│   └── [Pruebas unitarias]
│
├── Dependencies/
│   ├── itextpdf-5.5.13.3.jar          # Generación de PDFs
│   ├── jcalendar-tz-1.3.3-4.jar       # Selector de fechas (JDateChooser)
│   └── postgresql-42.7.7.jar          # Driver JDBC PostgreSQL
│
├── Runtime Dependencies/
├── Java Dependencies/
└── Project Files/
```

---

## 🗄️ Configuración de Base de Datos

### Tabla: `usuario`

| Campo | Tipo | Restricción | Descripción |
|-------|------|-------------|-------------|
| idusuario | SERIAL | PRIMARY KEY | ID único autoincremental |
| nombre | VARCHAR(100) | NOT NULL | Nombre del usuario |
| apellido | VARCHAR(100) | NOT NULL | Apellido del usuario |
| usuario | VARCHAR(50) | UNIQUE, NOT NULL | Nombre de usuario único |
| contraseña | VARCHAR(255) | NOT NULL | Contraseña encriptada SHA-256 |
| rol | VARCHAR(50) | NOT NULL, CHECK | Usuario/Contador/Admin |
| fecha_creacion | TIMESTAMP | DEFAULT NOW() | Fecha de registro |

### Tabla: `transacciones`

| Campo | Tipo | Restricción | Descripción |
|-------|------|-------------|-------------|
| idtransaccion | SERIAL | PRIMARY KEY | ID único autoincremental |
| fecha | VARCHAR(20) | NOT NULL | Fecha formato DD/MM/YYYY |
| referencia | VARCHAR(100) | NULL | Número de referencia |
| tipo | VARCHAR(20) | NOT NULL, CHECK | 'Ingreso' o 'Gasto' |
| categoria | VARCHAR(100) | NULL | Cuenta contable seleccionada |
| descripcion | TEXT | NULL | Descripción detallada |
| monto | DECIMAL(15,2) | NOT NULL, CHECK > 0 | Monto en US$ |
| usuario | VARCHAR(200) | NULL | Usuario que registró |
| documento | BYTEA | NULL | Archivo adjunto en binario |
| fecha_registro | TIMESTAMP | DEFAULT NOW() | Timestamp de creación |

### Tabla: `cuentas`

| Campo | Tipo | Restricción | Descripción |
|-------|------|-------------|-------------|
| idcuenta | SERIAL | PRIMARY KEY | ID único |
| nombre | VARCHAR(100) | UNIQUE, NOT NULL | Nombre de la cuenta |
| saldo | DECIMAL(15,2) | DEFAULT 0.00 | Saldo actual |

### Diagrama Entidad-Relación

```
┌─────────────────────┐         ┌──────────────────────┐
│      USUARIO        │         │    TRANSACCIONES     │
├─────────────────────┤         ├──────────────────────┤
│ idusuario (PK)      │         │ idtransaccion (PK)   │
│ nombre              │    1    │ fecha                │
│ apellido            │────┐    │ referencia           │
│ usuario (UNIQUE)    │    │    │ tipo                 │
│ contraseña          │    │    │ categoria (FK)       │
│ rol                 │    └───→│ usuario              │
│ fecha_creacion      │         │ descripcion          │
└─────────────────────┘         │ monto                │
                                │ documento            │
                                │ fecha_registro       │
       ┌────────────────────────┴──────────────────────┘
       │
       │ N:1
       ▼
┌──────────────────────┐
│       CUENTAS        │
├──────────────────────┤
│ idcuenta (PK)        │
│ nombre (UNIQUE)      │
│ saldo                │
└──────────────────────┘
```

---

## 🚀 Uso del Sistema

### 1️⃣ Inicio de Sesión

1. Ejecuta `PortadaContaBook.java`
2. Click en **"INGRESAR AL SISTEMA"**
3. Ingresa credenciales:
   - **Usuario**: `admin`
   - **Contraseña**: `admin123`
4. Click en **"Iniciar Sesión"** o presiona Enter

### 2️⃣ Agregar una Partida Contable

1. En el Dashboard, click en **"+ Nueva Partida Contable"**
2. Se abrirá el formulario modal (`FormularioPartida.java`)
3. Completa los campos:
   - **Fecha**: Se autocompleta con fecha actual
   - **Referencia**: Opcional (ej: #001, FAC-2024-001)
   - **Tipo**: Selecciona "Ingreso" o "Gasto"
   - **Cuenta**: Selecciona de la lista desplegable (desde BD)
   - **Subcategoría**: Opcional (se concatena con la cuenta)
   - **Descripción**: Detalle de la transacción
   - **Monto (US$)**: Cantidad (solo números positivos)
   - **Documento**: Opcional (PDF, JPG, PNG, DOC máx 5MB)
4. Click en **"💾 Guardar Partida"**

### 3️⃣ Editar una Partida

1. En la tabla, click en **✏️** (Editar)
2. Solo **Contador** y **Admin** pueden editar
3. Se abrirá el formulario con datos precargados
4. Modifica los campos necesarios
5. Click en **"💾 Actualizar Partida"**
6. El saldo de la cuenta se actualiza automáticamente

### 4️⃣ Eliminar una Partida

1. En la tabla, click en **🗑️** (Eliminar)
2. Ingresa tu contraseña para confirmar
3. Confirma la eliminación
4. La partida será eliminada permanentemente

### 5️⃣ Ver Documento Adjunto

1. En la tabla, click en **📄** (Documento)
2. El documento se abrirá con el visor predeterminado
3. Si no hay documento, aparece: "Esta transacción no tiene documento adjunto"

### 6️⃣ Filtrar por Períodos

1. Click en pestaña **"Períodos"**
2. Selecciona tipo de filtro:
   - Ver todas las transacciones
   - Filtrar por rango de fechas (JDateChooser)
   - Filtrar por últimos N días (1-365)
   - Filtrar por año (últimos 10 años)
   - Filtrar por tipo (Ingreso/Gasto/Todos)
3. Click en **"🔍 Aplicar Filtro"**
4. Ver resumen con totales actualizados

### 7️⃣ Generar Reportes

1. Click en pestaña **"Reportes"**
2. Selecciona:
   - **Balance General**: Activos, Pasivos, Patrimonio
   - **Libro Mayor**: Débitos, Créditos, Saldo acumulado
3. Click en **"📄 Descargar PDF"**
4. Elige ubicación y nombre del archivo
5. El PDF se genera con marca de agua y timestamp

### 8️⃣ Gestionar Cuentas Contables

1. Click en pestaña **"Cuentas"**
2. **Agregar nueva cuenta**:
   - Escribe nombre de la cuenta
   - Click en "Agregar Cuenta"
3. **Ver Cuentas T**:
   - Visualiza movimientos (débitos/créditos)
   - Ve el saldo actual de cada cuenta
4. Las cuentas se actualizan automáticamente al registrar transacciones

### 9️⃣ Gestionar Usuarios (Solo Admin)

1. Click en pestaña **"Usuarios"**
2. **Agregar usuario**:
   - Click en "+ Nuevo Usuario"
   - Completa formulario
   - Asigna rol (Usuario/Contador/Admin)
3. **Editar usuario**:
   - Click en ✏️
   - Solo Admin puede cambiar roles
4. **Eliminar usuario**:
   - Click en 🗑️
   - Confirma con contraseña

---

## 👥 Roles y Permisos

| Funcionalidad | Usuario | Contador | Admin |
|--------------|---------|----------|-------|
| **Agregar partidas** | ✅ Sí | ✅ Sí | ✅ Sí |
| **Editar partidas** | ❌ No | ✅ Sí | ✅ Sí |
| **Eliminar partidas** | ❌ No | ✅ Sí (con contraseña) | ✅ Sí (con contraseña) |
| **Ver Dashboard** | ✅ Sí | ✅ Sí | ✅ Sí |
| **Períodos** | ❌ No | ✅ Sí | ✅ Sí |
| **Reportes** | ❌ No | ✅ Sí | ✅ Sí |
| **Cuentas** | ❌ No | ✅ Sí | ✅ Sí |
| **Gestionar usuarios** | ❌ No | ❌ No | ✅ Sí |

---

## 📸 Capturas de Pantalla

### Portada de Bienvenida
![Portada](https://github.com/user-attachments/assets/d3750d94-6178-47da-9d36-6a2f69e9035f)

### Login
![Login](https://github.com/user-attachments/assets/81aa6317-aee9-4f89-9c87-5b685e60e4ed)

### Dashboard Principal
![Dashboard](https://github.com/user-attachments/assets/e7f4abe9-619e-49bc-8170-fe20011805f3)

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Uso |
|------------|---------|-----|
| **Java** | 11+ | Lenguaje de programación principal |
| **Swing** | Built-in | Framework de interfaz gráfica (JFrame, JDialog, JTable) |
| **PostgreSQL** | 13+ | Sistema de gestión de base de datos relacional |
| **JDBC** | 42.7.7 | Conector Java-PostgreSQL |
| **iText** | 5.5.13.3 | Generación de documentos PDF |
| **JCalendar** | 1.3.3-4 | Selector de fechas (JDateChooser) |
| **SHA-256** | Built-in | Encriptación de contraseñas (MessageDigest) |

### Características de Java Utilizadas
- ✅ **POO**: Herencia, Encapsulación, Polimorfismo
- ✅ **JDBC**: PreparedStatement, ResultSet, Conexiones
- ✅ **Java Swing**: JFrame, JDialog, JTable, JDateChooser
- ✅ **Event Listeners**: ActionListener, MouseAdapter
- ✅ **File I/O**: FileInputStream, FileOutputStream, ByteArrays
- ✅ **Exception Handling**: try-catch-finally, SQLException
- ✅ **Encriptación**: MessageDigest (SHA-256)
- ✅ **MVC**: Separación de Controlador y Vista

---

## 🔒 Seguridad

### Medidas Implementadas

1. **Encriptación de Contraseñas**
   - Algoritmo: SHA-256
   - Método: `Conexion.encriptarPassword()`
   - No se almacenan contraseñas en texto plano

2. **Validación de Permisos**
   - Control de acceso basado en roles
   - Verificación en cada operación crítica
   - Botones deshabilitados según permisos

3. **Confirmación de Eliminación**
   - Doble confirmación con JOptionPane
   - Requiere contraseña del usuario actual
   - Validación contra la base de datos

4. **Prepared Statements**
   - Prevención de SQL Injection
   - Parametrización de todas las consultas
   - Uso de placeholders (?) en queries

5. **Validación de Datos**
   - Campos obligatorios verificados
   - Montos solo positivos
   - Tipos de transacción restringidos (CHECK)

---

## 🐛 Solución de Problemas

### Error: "Driver not found"

**Causa**: El driver de PostgreSQL no está agregado al proyecto.

**Solución**:
```bash
# Descargar el driver
wget https://jdbc.postgresql.org/download/postgresql-42.7.7.jar

# Agregarlo a Dependencies en el IDE
# O agregarlo al classpath:
java -cp .:postgresql-42.7.7.jar vistas.PortadaContaBook
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
sudo netstat -plunt | grep postgres
```

### Error: "Error al cargar cuentas"

**Causa**: La tabla `cuentas` no existe.

**Solución**:
```sql
-- Crear tabla de cuentas
CREATE TABLE cuentas (
    idcuenta SERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    saldo DECIMAL(15, 2) DEFAULT 0.00
);

-- Insertar cuentas iniciales
INSERT INTO cuentas (nombre) VALUES
('Caja'), ('Bancos'), ('Inventario'),
('Proveedores'), ('Ventas'), ('Gastos Operativos');
```

### Error al generar PDF

**Causa**: La librería iText no está en el classpath.

**Solución**:
1. Verificar que `itextpdf-5.5.13.3.jar` esté en Dependencies
2. Limpiar y reconstruir el proyecto
3. Verificar permisos de escritura en la carpeta de destino

### Error: "Usuario o contraseña incorrectos"

**Causa**: Credenciales incorrectas o usuario no existe.

**Solución**:
1. Verifica que el usuario existe: `SELECT * FROM usuario WHERE usuario = 'admin';`
2. Usa credenciales por defecto: `admin` / `admin123`
3. Resetear contraseña:
```sql
UPDATE usuario 
SET contraseña = '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9' 
WHERE usuario = 'admin';
```

---
## 👨‍💻 Autor

**Nemma**
- GitHub: [@blackghossst](https://github.com/blackghossst)
- Email: nemmanuel2001@gmail.com

---

**⭐ Proyecto 2025 - Sistemas Contables ⭐**

Si este proyecto te fue útil, considera darle una estrella en GitHub ⭐

---

**Universidad de El Salvador**  
**Facultad de Ingeniería y Arquitectura**  
**Escuela de Ingeniería de Sistemas Informáticos**

</div>
