
Feature: Embargos de Cuenta disponible
  # Consultar un embargo
  Scenario: TESTC-3254 creacion de embargo basico funcion 209450 - Consulta
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    And El usuario clickea el boton de Salir
    When el usuario ingresa numero de funcion "209450"
    When el usuario selecciona la operación consultar embargo con "5"

  # Elimacion de un embargo

  Scenario: TESTC-3255 creacion de embargo basico funcion 209450 - Eliminar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    And El usuario clickea el boton de Salir
    When el usuario ingresa numero de funcion "209450"
    When el usuario selecciona la operación consultar embargo con "4"

  # Modificacion de un embargo

  Scenario: TESTC-3256 creacion de embargo basico funcion 209450 - Modificar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    And El usuario clickea el boton de Salir
    When el usuario ingresa numero de funcion "209450"
    When el usuario selecciona la operación consultar embargo con "2"
    And el usuario ingresa la oficina "19"
    Then el usuario presiona Enter y confirma la modificacion

  # Busqueda de embargos

  Scenario:  TESTC-3257 creacion de embargo basico funcion 209450 - Buscar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    And El usuario clickea el boton de Salir
    When el usuario ingresa numero de funcion "209450"
    When el usuario escribe el numero de embargo "35101"
    When el usuario hace click en el boton OK y Confirmar

  # Creacion de embargos
  Scenario: TESTC-3258 creacion de embargo basico funcion 209450 - Crear
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"


  #  Asingancion de embargos (Reasignacion de embargo)
  Scenario: TESTC-3259 Asignaci�n de embargo creado funcion 209455 - Reasignar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And  el usuario proporciona el número de función para reasignación "209455"
    And el usuario coloca el numero de embargo que se va a reasignar "Existente"
    And el usuario da click Enter, confirmar
    And el usuario introduce la nueva referencia para modificar "2"
    And el usuario ingresa el nombre de usuario que quiere asignar
    Then el usuario finaliza la confirmación

  # Revision de operacion de embargos
  Scenario: TESTC-3260 Revisi�n Operaci�n de Embargos funcion 209460 - Eliminar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    When el usuario ingresa numero de funcion "209460"
    When el usuario selecciona la operación consultar embargo con "4"
    When el usuario hace click en el boton OK y Confirmar

  # Revision de operacion embargos (Enviar a procesar sin completar)
  Scenario: TESTC-3261 Revisi�n Operaci�n de Embargos funcion 209460 - Enviar a procesar sin completar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    When el usuario ingresa numero de funcion "209460"
    When el usuario selecciona la operación consultar embargo con "10"

  # Revision operacion de embargos (Completar)
  Scenario: TESTC-3262 Revisi�n Operaci�n de Embargos funcion 209460 - Completar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
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

 # Revision de operacion embargos (Enviar a procesar estando completa)
  Scenario: TESTC-3265 Revisi�n Operaci�n de Embargos funcion 209460 - Enviar a procesar estando completada
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    When el usuario ingresa numero de funcion "209460"
    When el usuario selecciona la operación consultar embargo con "10"

 # Seleccion y procesamiento de operaciones (Retornar a revision)

  Scenario: TESTC-3266 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Retornar a revision
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    When el usuario ingresa numero de funcion "209465"
    When el usuario selecciona la operación consultar embargo con "7"

  # Seleccion y pocesamiento de operacion (Consultar)

  Scenario: TESTC-3267 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Consultar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    When el usuario ingresa numero de funcion "209450"
    When el usuario selecciona la operación consultar embargo con "5"
    Then El usuario navega a la pagina principal

   # Seleccion y procesamiento de operacion (Procesar operacion)

  Scenario: TESTC-3268 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Procesar Operaci�n
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    Then el usuario completa y procesa el embargo "Existente"

   # Mostrar cuenta corriente por numero de cuenta
  Scenario: TESTC-3271 Mostrar cuenta corriente por n�mero de cuenta  Funcion  201020
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta disponible embargada

  # Consultar y reportes de embargos

  Scenario: TESTC-3272 Consultas y Reportes de Embargos Funcion 209470 - Consulta por operaciones
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    Then el usuario consulta reportes del embargo por operaciones