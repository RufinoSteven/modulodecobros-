@Regression @Embargos
Feature: Embargos de cuenta sin saldo
# creacion de embargo
  Scenario: TESTC-3236 creacion de embargo basico funcion 209450 - Crear
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa la referencia "1"
    Then El usuario crea un embargo sobre una cuenta de ahorro sin saldo con tipo de operacion "2" y tipo de identificacion "6"
    When el usuario selecciona la operación consultar embargo con "5"

# Reasignar del embargo
  Scenario: TESTC-3237 Asignaci�n de embargo creado funcion 209455 - Reasignar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Given el usuario ingresa la referencia "1"
    Then El usuario crea un embargo sobre una cuenta de ahorro sin saldo con tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And  el usuario proporciona el número de función para reasignación "209455"
    And el usuario coloca el numero de embargo que se va a reasignar "Existente"
    And el usuario da click Enter, confirmar
    And el usuario introduce la nueva referencia para modificar "2"
    And el usuario ingresa el nombre de usuario que quiere asignar
    Then el usuario finaliza la confirmación

# Complear el embargo
  Scenario: TESTC-3238 Revisi�n Operaci�n de Embargos funcion 209460 - Completar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Given el usuario ingresa la referencia "1"
    Then El usuario crea un embargo sobre una cuenta de ahorro sin saldo con tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    When el usuario ingresa el numero de operación "Existente"
    Then el usuario selecciona la opción "2" para completar la información del embargo
    And el usuario completa el formulario
    Then el usuario da cliclk ok y confirma la operación
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso

# Completar el embargo
  Scenario: TESTC-3239 Revisi�n Operaci�n de Embargos funcion 209460 - Completar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Given el usuario ingresa la referencia "1"
    Then El usuario crea un embargo sobre una cuenta de ahorro sin saldo con tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    When el usuario ingresa el numero de operación "Existente"
    Then el usuario selecciona la opción "2" para completar la información del embargo
    And el usuario completa el formulario
    Then el usuario da cliclk ok y confirma la operación
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso

# Enviar a procesar estando completada
  Scenario: TESTC-3240 Revisi�n Operaci�n de Embargos funcion 209460 - Enviar a procesar estando completada
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa la referencia "1"
    Then El usuario crea un embargo sobre una cuenta de ahorro sin saldo con tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    When el usuario ingresa numero de funcion "209460"
    When el usuario selecciona la operación consultar embargo con "10"

# Consulta del embargo
  Scenario: TESTC-3241 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Consultar
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa la referencia "1"
    Then El usuario crea un embargo sobre una cuenta de ahorro sin saldo con tipo de operacion "2" y tipo de identificacion "6"
    And El usuario clickea el boton de Salir
    When el usuario ingresa numero de funcion "209450"
    When el usuario selecciona la operación consultar embargo con "5"

# Procesar operacion
  Scenario: TESTC-3242 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Procesar Operaci�n
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    When el usuario ingresa la referencia "1"
    Then El usuario crea un embargo sobre una cuenta de ahorro sin saldo con tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    Then el usuario completa y procesa el embargo sin saldo "Existente"

  Scenario: TESTC-3245 Mostrar cuenta corriente por n�mero de cuenta  Funcion  201020
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Then El usuario crea un embargo sobre una cuenta de ahorro sin saldo con tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo sin saldo "Existente"
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta sin saldo embargada

# Consulta por operaciones
  Scenario: TESTC-3246 Consultas y Reportes de Embargos Funcion 209470 - Consulta por operaciones
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Then El usuario crea un embargo sobre una cuenta de ahorro sin saldo con tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo sin saldo "Existente"
    Then El usuario navega a la pagina principal
    Then el usuario consulta reportes del embargo por operaciones
