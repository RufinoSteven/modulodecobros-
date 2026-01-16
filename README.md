# 🏦 - Framework de Automatización de Pruebas

Un framework completo de automatización de pruebas para el sistema **ModuloCobros** (Módulo de Cobros) de Banreservas. Este proyecto implementa pruebas automatizadas utilizando Selenium WebDriver, Cucumber BDD y JUnit 5, siguiendo el patrón de diseño Page Object Model (POM).

## 📋 Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Características](#-características)
- [Stack Tecnológico](#-stack-tecnológico)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Configuración](#-configuración)
- [Guía de Ejecución](#-guía-de-ejecución)
- [Uso de Tags en Features](#-uso-de-tags-en-features)
- [Módulos de Prueba](#-módulos-de-prueba)
- [Reportes](#-reportes)
- [Utilidades](#-utilidades)
- [Contribución](#-contribución)
- [Autores](#-autores)

## 🎯 Descripción General

Este framework de automatización de pruebas proporciona capacidades de testing end-to-end para el sistema financiero ModuloCobros, cubriendo procesos de negocio críticos incluyendo:

- **Gestión de Cobros** (Collections Management)
- **Procesamiento de Embargos** (Garnishment Processing)
- **Gestión de Préstamos** (Loan Management)
- **Procesamiento por Lotes** (Batch Processing - transacciones ACH)
- **Gestión de Clientes** (Client Management)
- **Mantenimiento de Cuentas** (Account Maintenance)
- **Autenticación y Autorización** (Authentication & Authorization)

El framework está construido pensando en mantenibilidad, escalabilidad y confiabilidad, utilizando las mejores prácticas de la industria para automatización de pruebas.

## ✨ Características

- 🧪 **Testing BDD**: Desarrollo dirigido por comportamiento basado en Cucumber para escenarios de prueba legibles
- 🌐 **Soporte Multi-Navegador**: Chrome, Firefox, Edge y Safari
- 🔄 **Soporte Multi-Ambiente**: Ambientes QA1, UAT y DEV
- 📊 **Reportes Completos**: ExtentReports con salida HTML y XML
- 💾 **Integración con Base de Datos**: Conexiones directas a base de datos para validación de datos
- 📁 **Manejo de Archivos SFTP**: Carga y procesamiento automatizado de archivos
- 📸 **Captura de Screenshots**: Captura automática de screenshots en fallos de pruebas
- ⚡ **Ejecución Paralela**: Soporte para ejecución paralela de pruebas
- 🤖 **Modo Headless**: Soporte para pipelines CI/CD con ejecución de navegador sin interfaz gráfica
- 🏗️ **Page Object Model**: Objetos de página mantenibles y reutilizables

## 🛠 Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 21 | Lenguaje de programación |
| **Maven** | 3.x | Gestión de build y dependencias |
| **Selenium WebDriver** | 4.35.0 | Automatización web |
| **Cucumber** | 7.27.0 | Framework BDD |
| **JUnit 5** | 5.13.4 | Framework de ejecución de pruebas |
| **ExtentReports** | 5.1.2 | Reportes de pruebas |
| **WebDriverManager** | 6.2.0 | Gestión automática de drivers |
| **Lombok** | 1.18.38 | Generación de código |
| **Log4j** | 2.25.1 | Logging |
| **JSch** | 0.1.55 | Operaciones SFTP |
| **AssertJ** | 3.27.4 | Aserciones |

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado lo siguiente:

- ☕ **Java Development Kit (JDK) 21** o superior
- 📦 **Maven 3.6+**
- 🔀 **Git** para control de versiones
- 💻 **IDE** (IntelliJ IDEA, Eclipse o VS Code) con soporte para Java
- 🌐 **Drivers de navegador** (gestionados automáticamente por WebDriverManager)
- 🔐 **Acceso a ambientes de prueba de Banreservas** (QA1/UAT)
- 🗄️ **Credenciales de base de datos** para ConnexusDB
- 📤 **Credenciales SFTP** para carga de archivos

## 🚀 Instalación y Configuración

### 1️⃣ Clonar el Repositorio

```bash
git clone https://gitlab.com/tu-grupo/tu-proyecto.git
cd tu-proyecto
```

### 2️⃣ Configurar Maven

Asegúrate de que Maven esté configurado para acceder al repositorio Maven de GitLab:

```xml
<repositories>
    <repository>
        <id>gitlab-maven</id>
        <url>https://gitlab.example.com/api/v4/groups/87721015/-/packages/maven</url>
    </repository>
</repositories>
```

## 🔍 Búsqueda de Documentos y Archivos

Esta sección explica cómo buscar y acceder a documentos y archivos ubicados en diferentes carpetas dentro del proyecto.
[Configuración conexión a la base de datos.pdf](manuales/Configuraci%C3%B3n%20conexi%C3%B3n%20a%20la%20base%20de%20datos.pdf)
### 3️⃣ Instalar Dependencias

```bash
mvn clean install
```

### 4️⃣ Configurar Credenciales de Prueba

Edita el archivo `src/test/resources/userConfig.properties`:

```properties
signature.username='tu_usuario'
signature.password='tu_contraseña'
```

### 5️⃣ Verificar Configuración de Base de Datos

Actualiza `src/main/java/dbConnections/DBConfig.java` si es necesario:

```java
public String server = "0.0.0.0";
public int port = 0;
public String username = "tu_usuario_sftp";
public String password = "tu_contraseña_sftp";
```
**CONFIGURACION FUERA DEL REPOSITORIO**
Para realizar la configuracion fuera del repositorio. Lo recomendable es crear un archivo
`local.properties` ignorado por Git para almacenar esta información.

```properties
# Propiedades para la conexión a la Base de Datos
db.server=0.0.0.0
db.port=000
db.sid=CONNEXUS
db.username=tu_usuario_db
db.password=tu_contraseña_db

# Propiedades para la conexión SFTP
sftp.server=0.0.0.0
sftp.port=00
sftp.username=tu_usuario_sftp
sftp.password=tu_contraseña_sftp
```

## 📁 Estructura del Proyecto

```
modulocobros/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── actions/              # Clases de acciones (lógica de negocio)
│   │   │   │   ├── Authentication/
│   │   │   │   ├── Batch/
│   │   │   │   ├── Certificate/
│   │   │   │   ├── Clients/
│   │   │   │   ├── Cobros/
│   │   │   │   ├── Embargo/
│   │   │   │   ├── ImportedFiles/
│   │   │   │   ├── Loans/
│   │   │   │   └── Maintenace/
│   │   │   ├── config/               # Clases de configuración
│   │   │   │   └── Browser.java
│   │   │   ├── dataStorageModel/     # Objetos de transferencia de datos
│   │   │   ├── dbConnections/        # Clases de conexión a base de datos
│   │   │   ├── fixtures/             # Fixtures y enums de prueba
│   │   │   ├── pageObjects/          # Clases del Page Object Model
│   │   │   │   ├── Accounts/
│   │   │   │   ├── Authentication/
│   │   │   │   ├── Cobros/
│   │   │   │   ├── Embargo/
│   │   │   │   └── ...
│   │   │   └── utils/                # Clases utilitarias
│   │   │       ├── reporting/
│   │   │       └── ...
│   │   └── resources/                # Datos de prueba y recursos
│   │       ├── ACH/                  # Archivos de prueba ACH
│   │       ├── Nomina/               # Archivos de prueba de nómina
│   │       └── ExtentConfig/         # Configuración de ExtentReports
│   └── test/
│       ├── java/
│       │   ├── runners/              # Test runners
│       │   │   └── TestRunner.java
│       │   └── stepDefinitions/      # Definiciones de pasos de Cucumber
│       │       ├── Authentication/
│       │       ├── Batch/
│       │       ├── Cobro/
│       │       ├── Embargo/
│       │       └── ...
│       └── resources/
│           ├── features/             # Archivos feature de Cucumber
│           │   ├── Autentificacion/
│           │   ├── Modulo de Cobros/
│           │   ├── Modulo de Embargos/
│           │   ├── Procesos Batch-ACH/
│           │   └── ...
│           ├── extent.properties    # Configuración de ExtentReports
│           └── userConfig.properties # Credenciales de usuario
├── queries/                          # Archivos de consultas SQL
├── pom.xml                           # Configuración de Maven
└── README.md                         # Este archivo
```

## ⚙️ Configuración

### 🌍 Configuración de Ambiente

Establece el ambiente usando propiedades del sistema:

```bash
-DEnvironment=QA1    # o UAT
-browsers=CHROME    `src/main/java/config/Browser`  # o FIREFOX, EDGE, SAFARI 
```

### 🌐 Configuración de Navegador

El framework soporta múltiples navegadores. El navegador por defecto es Chrome. Configura en `Browser.java` o mediante propiedad del sistema:

```java
-browsers=CHROME    `src/main/java/config/Browser`
```

### 🤖 Modo Headless

Para pipelines CI/CD, habilita el modo headless:

```bash
-DrunPipeline=true
```

### 🏷️ Tags de Prueba

Controla qué pruebas ejecutar usando tags de Cucumber:

```java
@IncludeTags({"test"})
@ExcludeTags("AuxiliaryScenarios")
```

## 🧪 Guía de Ejecución

Esta sección proporciona una guía detallada paso a paso sobre cómo ejecutar las pruebas en diferentes escenarios.

### 📝 Ejecución Básica

#### Ejecutar Todas las Pruebas

Para ejecutar todas las pruebas del proyecto:

```bash
mvn test
```

**Resultado esperado:**
- ✅ Se ejecutarán todos los escenarios marcados con el tag `@test`
- 📊 Se generará un reporte en `reports/ExtentHtml.html`
- 📸 Los screenshots se guardarán en `target/screenshots/`

#### Ejecutar desde el IDE

**IntelliJ IDEA:**
1. 🖱️ Haz clic derecho en `TestRunner.java`
2. ▶️ Selecciona "Run 'TestRunner'"
3. 📋 O ejecuta archivos feature individuales desde el directorio `features`

**Eclipse:**
1. 🖱️ Haz clic derecho en `TestRunner.java`
2. ▶️ Selecciona "Run As" → "JUnit Test"

**VS Code:**
1. 🔍 Abre el archivo `TestRunner.java`
2. ▶️ Haz clic en el botón "Run Test" sobre la clase

### 🎯 Ejecución por Ambiente

#### Ejecutar en Ambiente QA1

```bash
mvn test -DEnvironment=QA1 -DBrowser=CHROME
```

#### Ejecutar en Ambiente UAT

```bash
mvn test -DEnvironment=UAT -DBrowser=CHROME
```

**Nota:** Asegúrate de tener las credenciales correctas configuradas para cada ambiente.

### 🌐 Ejecución por Navegador

#### Chrome (Por defecto)

```bash
mvn test -DBrowser=CHROME
```

#### Firefox

```bash
mvn test -DBrowser=FIREFOX
```

#### Edge

```bash
mvn test -DBrowser=EDGE
```

#### Safari

```bash
mvn test -DBrowser=SAFARI
```

### 🏷️ Ejecución por Tags

#### Ejecutar Pruebas con Tag Específico

```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

#### Ejecutar Múltiples Tags (OR)

```bash
mvn test -Dcucumber.filter.tags="@smoke or @regression"
```

#### Ejecutar Múltiples Tags (AND)

```bash
mvn test -Dcucumber.filter.tags="@smoke and @embargo"
```

#### Excluir Tags Específicos

```bash
mvn test -Dcucumber.filter.tags="not @AuxiliaryScenarios"
```
### 📄 Ejecución por Feature Específico

#### Ejecutar un Feature Completo

```bash
mvn test -Dcucumber.features="src/test/resources/features/Modulo de Cobros/Transacciones.feature"
```

#### Ejecutar Múltiples Features

```bash
mvn test -Dcucumber.features="src/test/resources/features/Modulo de Cobros/Transacciones.feature,src/test/resources/features/Modulo de Cobros/Comisiones.feature"
```

#### Ejecutar Feature con Tag Específico

```bash
mvn test -Dcucumber.features="src/test/resources/features/Modulo de Cobros/Transacciones.feature" -Dcucumber.filter.tags="@test"
```

### 🤖 Ejecución en Modo Headless (CI/CD)

Para ejecutar en pipelines CI/CD sin interfaz gráfica:

```bash
mvn test -DrunPipeline=true -DEnvironment=QA1 -DBrowser=CHROME
```

**Características del modo headless:**
- 🚫 No se abre ventana del navegador
- ⚡ Ejecución más rápida
- 💻 Ideal para servidores sin interfaz gráfica

### 🔄 Ejecución Paralela

Para ejecutar pruebas en paralelo (requiere configuración adicional):

```bash
mvn test -Dparallel=true -Dthreads=4
```

### 📊 Ejecución con Reportes Detallados

#### Generar Reporte HTML

```bash
mvn test -Dextent.reporter.html.start=true
```

#### Generar Reporte XML

```bash
mvn test -Dextent.reporter.xml.start=true
```

### 🎬 Ejemplos de Ejecución Completos

#### Ejemplo 1: Smoke Tests en QA1 con Chrome

```bash
mvn test -DEnvironment=QA1 -DBrowser=CHROME -Dcucumber.filter.tags="@smoke"
```

#### Ejemplo 2: Pruebas de Embargos en UAT con Firefox

```bash
mvn test -DEnvironment=UAT -DBrowser=FIREFOX -Dcucumber.filter.tags="@embargo"
```

#### Ejemplo 3: Feature Específico en Headless Mode

```bash
mvn test -DrunPipeline=true -DEnvironment=QA1 -Dcucumber.features="src/test/resources/features/Modulo de Embargos/BasicEmbargoRNCClient.feature"
```

#### Ejemplo 4: Múltiples Features con Tags

```bash
mvn test -DEnvironment=QA1 -Dcucumber.features="src/test/resources/features/Modulo de Cobros/Transacciones.feature,src/test/resources/features/Modulo de Cobros/Comisiones.feature" -Dcucumber.filter.tags="@test"
```

### ⚠️ Solución de Problemas Comunes

#### Problema: Las pruebas no se ejecutan

**Solución:**
- ✅ Verifica que Maven esté instalado: `mvn --version`
- ✅ Verifica que Java 21 esté instalado: `java -version`
- ✅ Ejecuta `mvn clean install` primero

#### Problema: Error de conexión a base de datos

**Solución:**
- ✅ Verifica las credenciales en `DBConfig.java`
- ✅ Verifica la conectividad de red al servidor

#### Problema: Navegador no se abre

**Solución:**
- ✅ Verifica que WebDriverManager pueda descargar los drivers
- ✅ Verifica la configuración del navegador en `Browser.java`

## 🏷️ Uso de Tags en Features

Los tags en Cucumber permiten organizar y filtrar escenarios de prueba. Esta sección explica cómo usar tags efectivamente.

### 📌 ¿Qué son los Tags?

Los tags son etiquetas que puedes agregar a tus features y scenarios en Cucumber para categorizarlos y ejecutarlos selectivamente.

### ✍️ Cómo Agregar Tags a Features

#### Agregar Tags a un Scenario

```gherkin
@test
@smoke
Scenario: Inicio de sesión exitoso
  Given el usuario navega a la página de login
  When El usuario se autentica con sus credenciales
  Then el usuario debería estar en la página principal
```

#### Agregar Tags a un Feature Completo

```gherkin
@modulo_cobros
@regression
Feature: Funcionalidad de Cobros
  Como usuario del sistema
  Quiero poder procesar cobros
  Para gestionar las transacciones

  @test
  Scenario: Procesar cobro exitoso
    Given el usuario está en el módulo de cobros
    When procesa un cobro
    Then el cobro se procesa correctamente
```

#### Agregar Múltiples Tags

```gherkin
@test
@smoke
@embargo
@alta_prioridad
Scenario: Crear embargo básico
  Given el usuario está en el módulo de embargos
  When crea un embargo básico
  Then el embargo se crea exitosamente
```

### 🎯 Tags Recomendados

Aquí tienes una lista de tags recomendados para organizar tus pruebas:

#### Por Prioridad
- `@alta_prioridad` - Pruebas críticas que deben ejecutarse primero
- `@media_prioridad` - Pruebas importantes
- `@baja_prioridad` - Pruebas opcionales

#### Por Tipo de Prueba
- `@smoke` - Pruebas de humo (smoke tests)
- `@regression` - Pruebas de regresión
- `@sanity` - Pruebas de sanidad
- `@test` - Pruebas funcionales estándar

#### Por Módulo
- `@autenticacion` - Pruebas de autenticación
- `@cobros` - Pruebas del módulo de cobros
- `@embargo` - Pruebas del módulo de embargos
- `@prestamos` - Pruebas del módulo de préstamos
- `@batch` - Pruebas de procesos batch
- `@clientes` - Pruebas de gestión de clientes

#### Por Ambiente
- `@qa1` - Pruebas específicas para QA1
- `@uat` - Pruebas específicas para UAT
- `@dev` - Pruebas específicas para DEV

#### Por Estado
- `@pendiente` - Pruebas pendientes de implementar
- `@en_desarrollo` - Pruebas en desarrollo
- `@AuxiliaryScenarios` - Escenarios auxiliares (excluidos por defecto)

### 📝 Ejemplos de Uso de Tags

#### Ejemplo 1: Feature con Múltiples Scenarios

```gherkin
@modulo_embargos
@regression
Feature: Gestión de Embargos
  Como usuario del sistema
  Quiero gestionar embargos
  Para procesar órdenes judiciales

  @test
  @smoke
  @alta_prioridad
  Scenario: Crear embargo básico
    Given el usuario está en el módulo de embargos
    When crea un embargo básico
    Then el embargo se crea exitosamente

  @test
  @regression
  Scenario: Crear embargo con fianza
    Given el usuario está en el módulo de embargos
    When crea un embargo con fianza
    Then el embargo con fianza se crea exitosamente

  @test
  @regression
  Scenario: Levantar embargo
    Given existe un embargo creado
    When el usuario levanta el embargo
    Then el embargo se levanta exitosamente
```

#### Ejemplo 2: Tags para Excluir Scenarios

```gherkin
@modulo_cobros
Feature: Procesamiento de Cobros

  @test
  Scenario: Procesar cobro estándar
    Given el usuario está en el módulo de cobros
    When procesa un cobro
    Then el cobro se procesa correctamente

  @AuxiliaryScenarios
  @test
  Scenario: Preparar datos de prueba
    Given el sistema tiene datos de prueba
    When se preparan los datos
    Then los datos están listos
```

**Nota:** Los scenarios con tag `@AuxiliaryScenarios` están excluidos por defecto en `TestRunner.java`.

### 🔍 Filtrar por Tags en la Ejecución

#### Ejecutar Solo Smoke Tests

```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

#### Ejecutar Pruebas de un Módulo Específico

```bash
mvn test -Dcucumber.filter.tags="@embargo"
```

#### Ejecutar Pruebas de Alta Prioridad

```bash
mvn test -Dcucumber.filter.tags="@alta_prioridad"
```

#### Combinar Múltiples Tags (OR)

```bash
mvn test -Dcucumber.filter.tags="@smoke or @regression"
```

#### Combinar Múltiples Tags (AND)

```bash
mvn test -Dcucumber.filter.tags="@smoke and @embargo"
```

#### Excluir Tags Específicos

```bash
mvn test -Dcucumber.filter.tags="not @AuxiliaryScenarios"
```

### ⚙️ Configurar Tags en TestRunner

El `TestRunner.java` ya tiene configurados tags por defecto:

```java
@IncludeTags({"test"})
@ExcludeTags("AuxiliaryScenarios")
```

Esto significa que:
- ✅ Solo se ejecutan scenarios con tag `@test`
- ❌ Se excluyen scenarios con tag `@AuxiliaryScenarios`

### 💡 Mejores Prácticas

1. ✅ **Usa tags consistentes**: Establece una convención de nombres y síguela
2. ✅ **Tags descriptivos**: Usa nombres que indiquen claramente el propósito
3. ✅ **No abuses de los tags**: Demasiados tags pueden hacer difícil la gestión
4. ✅ **Documenta tus tags**: Mantén una lista de tags usados en el proyecto
5. ✅ **Combina tags estratégicamente**: Usa combinaciones lógicas para filtrar pruebas

### 📋 Checklist para Agregar Tags

- [ ] ¿El scenario tiene al menos el tag `@test`?
- [ ] ¿El scenario tiene tags que indican su módulo?
- [ ] ¿El scenario tiene tags que indican su prioridad?
- [ ] ¿Los tags siguen la convención del proyecto?
- [ ] ¿Los scenarios auxiliares tienen el tag `@AuxiliaryScenarios`?

## 📊 Módulos de Prueba

### 1️⃣ Autenticación (`Autentificacion`)
- 🔐 Escenarios de login/logout de usuario
- 🔄 Gestión de sesiones
- ✅ Validación de credenciales

### 2️⃣ Módulo de Cobros (`Modulo de Cobros`)
- 💰 Procesamiento de transacciones
- 📊 Cálculo de comisiones
- 💳 Cobros de préstamos
- 🔍 Consultas de cuentas

### 3️⃣ Módulo de Embargos (`Modulo de Embargos`)
- 📝 Creación de embargo básico
- 🏛️ Embargos con fianza (bond)
- 📄 Embargos con carta (letter)
- 🚫 Levantamiento de embargos
- 👤 Reasignación de usuario
- 📋 Consulta de información completa

### 4️⃣ Procesos Batch (`Procesos Batch-ACH`)
- 🔄 Procesamiento de transacciones ACH
- ↩️ Reversos ACH
- 💼 Procesamiento de archivos de nómina
- 🏦 Procesamiento de archivos de tesorería
- 📮 Archivos de domiciliación
- 👥 Archivos de pago a afiliados

### 5️⃣ Préstamos (`Prestamos`)
- ➕ Creación de préstamos
- 📋 Gestión de préstamos
- 💰 Cobros de préstamos

### 6️⃣ Gestión de Clientes (`Accounts`)
- 👤 Creación de clientes
- 📝 Gestión de información de clientes

### 7️⃣ Mantenimiento (`Maintenace`)
- 💰 Mantenimiento de cuentas de ahorro
- 💳 Mantenimiento de cuentas corrientes
- ⚠️ Manejo de cuentas desbalanceadas

### 8️⃣ Preparación (`Preparacion Insumos Procesos Batch`)
- 📦 Preparación de insumos para batch
- 🔧 Escenarios de mantenimiento de cuentas
- 🔄 Escenarios auxiliares

## 📈 Reportes

### 📊 ExtentReports

El framework genera reportes completos en HTML y XML:

- **Reporte HTML**: `reports/ExtentHtml.html`
- **Reporte XML**: `XmlReport/Extent.xml`

Los reportes incluyen:
- 📋 Resumen de ejecución de pruebas
- ✅ Estadísticas de pass/fail
- 📸 Screenshots en fallos
- 📝 Logs de ejecución paso a paso
- 💻 Información del sistema
- ⏱️ Duración de las pruebas

### 👀 Ver Reportes

Después de la ejecución de pruebas, abre el reporte HTML:

```bash
# Windows
start reports/ExtentHtml.html

# Linux/Mac
open reports/ExtentHtml.html
```

### 📸 Screenshots

Los screenshots se capturan automáticamente:
- ❌ En fallos de pruebas
- 📸 En pasos específicos de prueba (usando `ScreenshotBus`)
- 💾 Almacenados en `target/screenshots/`

### 📝 Logs

Los logs de Log4j se generan en el directorio `logs/` con información detallada de ejecución.

## 🔧 Utilidades

### Clases Utilitarias Principales

- **`Browser.java`**: Gestión de WebDriver y configuración de navegador
- **`RobustWebDriverWait.java`**: WebDriverWait mejorado con lógica de reintento
- **`Navigator.java`**: Utilidades de navegación
- **`WindowManager.java`**: Manejo de múltiples ventanas
- **`ScreenshotBus.java`**: Gestión de screenshots
- **`ExtentReportManager.java`**: Integración con ExtentReports
- **`SftpUploader.java`**: Operaciones de carga de archivos SFTP
- **`ACHExtractDATA.java`**: Extracción de datos ACH
- **`ExtractDATANomina.java`**: Extracción de datos de nómina

## 🤝 Contribución

### 📐 Guías de Estilo de Código

1. ✅ Sigue las convenciones de nombres de Java
2. ✅ Usa nombres de variables y métodos significativos
3. ✅ Agrega comentarios JavaDoc para métodos públicos
4. ✅ Sigue el patrón Page Object Model
5. ✅ Mantén las definiciones de pasos limpias y legibles
6. ✅ Usa anotaciones de Lombok donde sea apropiado

### ➕ Agregar Nuevas Pruebas

1. 📝 Crea archivo feature en `src/test/resources/features/`
2. 🔧 Implementa step definitions en `src/test/java/stepDefinitions/`
3. 📄 Crea page objects en `src/main/java/pageObjects/`
4. ⚙️ Crea clases de acción en `src/main/java/actions/`
5. 📦 Agrega datos de prueba a `src/main/resources/` si es necesario

### 🌿 Estrategia de Ramas

- `main`: Código listo para producción
- `develop`: Rama de desarrollo
- Ramas de feature: `feature/nombre-feature`

### 💬 Mensajes de Commit

Usa mensajes de commit claros y descriptivos:
```
feat: Agregar nueva prueba de creación de embargo
fix: Resolver problema de timeout en login
docs: Actualizar README con nueva configuración
```

## 👥 Autores

- Trabajo inicial y desarrollo del framework

## 📝 Licencia

Este proyecto es software propietario desarrollado para Banreservas. Todos los derechos reservados.

## 🔗 Recursos Relacionados

- 📚 [Documentación de Selenium](https://www.selenium.dev/documentation/)
- 🥒 [Documentación de Cucumber](https://cucumber.io/docs/cucumber/)
- 🧪 [Documentación de JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- 📊 [Documentación de ExtentReports](https://www.extentreports.com/docs/versions/5/java/index.html)

## 📞 Soporte

Para problemas, preguntas o contribuciones, por favor contacta al equipo de QA o crea un issue en el repositorio de GitLab.

---

**Última Actualización**: 2024  
**Versión**: 1.0-SNAPSHOT
