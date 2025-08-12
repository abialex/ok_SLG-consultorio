# 🦷 SLG Consultorio - Sistema de Gestión Odontológica

## 📋 Descripción del Proyecto

**SLG Consultorio** es una aplicación de escritorio desarrollada en Java que proporciona una solución integral para la gestión de consultorios odontológicos. El sistema permite administrar pacientes, doctores, historias clínicas, presupuestos y citas de manera eficiente y profesional.

## ✨ Características Principales

### 🏥 Gestión de Pacientes
- Registro y modificación de información personal
- Historial médico completo
- Gestión de tratamientos y presupuestos
- Seguimiento de citas y consultas

### 👨‍⚕️ Administración de Doctores
- Perfiles profesionales
- Horarios de atención
- Especialidades y disponibilidad

### 💰 Gestión Financiera
- Presupuestos de tratamientos
- Control de pagos
- Generación de reportes financieros

### 📊 Reportes y Documentación
- Generación de historias clínicas en PDF
- Reportes de pacientes
- Estadísticas de consultorio

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 16+** - Lenguaje principal
- **JavaFX 17.0.1** - Framework de interfaz gráfica
- **Maven** - Gestión de dependencias y build
- **Hibernate 5.4.0** - ORM para persistencia de datos
- **JPA 2.2.1** - API de persistencia Java

### Base de Datos
- **PostgreSQL 42.3.1** - Sistema de gestión de base de datos

### Interfaz de Usuario
- **JFoenix 9.0.0** - Componentes Material Design para JavaFX
- **FontAwesomeFX 8.9** - Iconografía moderna
- **Bootstrap CSS** - Estilos responsivos

### Utilidades
- **iText 7.0.0** - Generación de documentos PDF
- **Retrofit 2.9.0** - Cliente HTTP para APIs REST
- **JSON** - Procesamiento de datos JSON
- **Gson** - Serialización/deserialización JSON

## 🏗️ Arquitectura del Proyecto

```
src/main/java/
├── controller/           # Controladores de la interfaz
├── controllerLogin/      # Autenticación y splash screen
├── Entidades/           # Modelos de datos (JPA entities)
├── Repository/          # Capa de acceso a datos
├── RepositoryInterface/ # Interfaces del repositorio
├── global/              # Configuraciones globales
├── Pdf/                 # Generación de documentos PDF
└── Util/                # Utilidades y helpers
```

### Patrones de Diseño Implementados
- **MVC (Model-View-Controller)** - Separación de responsabilidades
- **Repository Pattern** - Abstracción del acceso a datos
- **Dependency Injection** - Inyección de dependencias
- **Factory Pattern** - Creación de objetos


## 🔧 Configuración de Desarrollo

### Estructura del Proyecto
- **Maven** para gestión de dependencias
- **JavaFX Maven Plugin** para ejecución
- **Hibernate** configurado para desarrollo local

### Base de Datos
- Configuración JPA en `src/main/resources/META-INF/persistence.xml`
- Soporte para múltiples entornos (desarrollo, producción)

## 📈 Funcionalidades Destacadas

### 🔐 Sistema de Autenticación
- Login seguro con tokens CSRF
- Gestión de sesiones
- Control de acceso por roles

### 📋 Gestión de Historias Clínicas
- Registro completo de antecedentes médicos
- Seguimiento de tratamientos
- Generación automática de PDFs

### 💳 Sistema de Presupuestos
- Cálculo automático de costos
- Seguimiento de pagos
- Generación de facturas






*Desarrollado con ❤️ usando Java y JavaFX*
