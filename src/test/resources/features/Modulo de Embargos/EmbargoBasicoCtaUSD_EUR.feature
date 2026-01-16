@Regression @Embargos
Feature: Procesamiento de Embargos Basico Cta USD - EUR.
  Como usuario del sistema
  Quiero procesar operaciones de embargo Basico Cta
  Para gestionar las solicitudes de embargo

# Creacion de embargo basisco funcion  209450 - Crear - USD
  Scenario: TESTC-3036 creacion de embargo basico funcion 209450 - Crear - USD
    Given El usuario se autentica con sus credenciales
    Then el usuario ingresa la referencia "1"
    When el usuario ingresa numero de funcion "209450"
    And el usuario selecciona la opción de creación con F6
    And el usuario selecciona el tipo de operación "2" y tipo de identificación "6"
    And el usuario ingresa el número de identificación "00100841360" y la oficina "17"
    Then el usuario presiona Enter y confirma la creacion

# Asignaci�n de embargo creado funcion 209455 - Reasignar - USD
  Scenario: TESTC-3037 Asignacion de embargo creado funcion 209455 - Reasignar - USD
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    When el usuario ingresa numero de funcion "209450"
    And el usuario selecciona la opción de creación con F6
    And el usuario selecciona el tipo de operación "2" y tipo de identificación "6"
    And el usuario ingresa el número de identificación "00100841360" y la oficina "17"
    And el usuario presiona Enter y confirma la creacion
    Then El usuario navega a la pagina principal
    And  el usuario proporciona el número de función para reasignación "209455"
    And el usuario coloca el numero de embargo que se va a reasignar "Existente"
    And el usuario da click Enter, confirmar
    And el usuario introduce la nueva referencia para modificar "2"
    And el usuario ingresa el nombre de usuario que quiere asignar
    Then el usuario finaliza la confirmación

# Revisi�n Operaci�n de Embargos funcion 209460 - Completar - USD
  Scenario: TESTC-3038: Revision Operacion de Embargos funcion 209460 - Completar - USD
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6" y del tipo de moneda "1"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And El usuario se dirige a completar el embargo USD
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso

# Revisi�n Operaci�n de Embargos funcion 209460 - Enviar a procesar estando completada - USD
  Scenario: TESTC-3040 Revision Operacion de Embargos funcion 209460 - Enviar a procesar estando completada - USD
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6" y del tipo de moneda "1"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And El usuario se dirige a completar el embargo USD
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    And el usuario envia a procesar el embargo

# Seleccion y pocesamiento de operacion (Consultar) - USD
  Scenario: TESTC-3267 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Consultar - USD
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6" y del tipo de moneda "1"
    And el usuario selecciona la operación consultar embargo con "5"
    Then El usuario navega a la pagina principal

# Selecci�n y Procesamiento de Operaciones Funcion 209465 - Procesar Operaci�n - USD
  Scenario: TESTC-3042 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Procesar Operaci�n - USD
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6" y del tipo de moneda "1"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And El usuario se dirige a completar el embargo USD
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    And el usuario envia a procesar el embargo
    Then El usuario navega a la pagina principal
    And El usuario selecciona la operacion de embargo con 1
    Then El usuario navega a la pagina principal

# Selecci�n y Procesamiento de Operaciones Funcion 209465  - Procesar Operaci�n - USD
  Scenario: TESTC-3043 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Procesar Operaci�n - USD
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6" y del tipo de moneda "1"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And El usuario se dirige a completar el embargo USD
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    And el usuario envia a procesar el embargo
    Then El usuario navega a la pagina principal
    And El usuario selecciona la operacion de embargo con 1
    And El usuario selecciona Aplicar
    Then El usuario navega a la pagina principal

# Selecci�n y Procesamiento de Operaciones Funcion 209465  - Procesar Operaci�n -USD
  Scenario:TESTC-3044 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Procesar Operaci�n - USD
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6" y del tipo de moneda "1"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And El usuario se dirige a completar el embargo USD
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    And el usuario envia a procesar el embargo
    Then El usuario navega a la pagina principal
    And El usuario selecciona la operacion de embargo con 1
    And El usuario selecciona Aplicar
    And El usuario selecciona la opcion 1
    Then El usuario navega a la pagina principal

#Mostrar cuenta corriente por n�mero de cuenta  Funcion  201020  - Consulta de cuenta seg�n aplique - USD
  Scenario: TESTC-3045 - 3046 Mostrar cuenta corriente por n�mero de cuenta  Funcion  201020  - Consulta de cuenta seg�n aplique  - USD
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6" y del tipo de moneda "1"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And El usuario se dirige a completar el embargo USD
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    And el usuario envia a procesar el embargo
    Then El usuario navega a la pagina principal
    And el usuario procesa el embargo USD
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta disponible embargada usd o eur
