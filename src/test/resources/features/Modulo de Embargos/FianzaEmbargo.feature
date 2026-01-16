@PRUEBA
Feature: Procesamiento de Embargos Fianza
  Como usuario del sistema
  Quiero procesar operaciones de embargo Fianza
  Para gestionar las solicitudes de embargo

    #El tipo de operacion 6 representa fianza
  Scenario: TESTC-3110 creacion de embargo basico funcion 209450 - Crear
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "6" y tipo de identificacion "6"

  Scenario: TESTC-3111 Asignacion de embargo creado funcion 209455 - Reasignar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "6" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And  el usuario proporciona el número de función para reasignación "209455"
    And el usuario coloca el numero de embargo que se va a reasignar "Existente"
    And el usuario da click Enter, confirmar
    And el usuario introduce la nueva referencia para modificar "2"
    And el usuario ingresa el nombre de usuario que quiere asignar
    Then el usuario finaliza la confirmación

   #Proceso para  completar fianza
  Scenario: TESTC-3112 - TESTC-3113 Revision Operacion de Embargos funcion 209460 - Completar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "6" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    When el usuario ingresa el numero de operación "Existente"
    Then el usuario selecciona la opción "2" para completar la información del embargo
    And el usuario completa el formulario
    When el usuario ingresa el numero de operación "Existente" del embargo previamente completado con el monto anterior ""
    Then el usuario confirma la operación
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo

  Scenario: TESTC-3114 Revision Operacion de Embargos funcion 209460 - Enviar a procesar estando completada
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "6" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    When el usuario ingresa el numero de operación "Existente"
    Then el usuario selecciona la opción "2" para completar la información del embargo
    And el usuario completa el formulario
    When el usuario ingresa el numero de operación "Existente" del embargo previamente completado con el monto anterior ""
    And el usuario confirma la operación
    Then el usuario envia a procesar el embargo

  Scenario: TESTC-3115 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Consultar
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa la referencia "1"
    And El usuario crea un embargo que sea del tipo de operacion "6" y tipo de identificacion "6"
    And el usuario selecciona la operación consultar embargo con "5"
    Then El usuario navega a la pagina principal

  Scenario: TESTC-3116 Seleccion y Procesamiento de Operaciones Funcion 209465  - Procesar Operacion
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "6" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    When el usuario ingresa el numero de operación "Existente"
    Then el usuario selecciona la opción "2" para completar la información del embargo
    And el usuario completa el formulario
    When el usuario ingresa el numero de operación "Existente" del embargo previamente completado con el monto anterior ""
    And el usuario confirma la operación
    And el usuario envia a procesar el embargo
    Then el usuario se dirige a procesar el embargo

  Scenario: TESTC-3117 Seleccion y Procesamiento de Operaciones Funcion 209465  - Procesar Operacion
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "6" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    When el usuario ingresa el numero de operación "Existente"
    Then el usuario selecciona la opción "2" para completar la información del embargo
    And el usuario completa el formulario
    When el usuario ingresa el numero de operación "Existente" del embargo previamente completado con el monto anterior ""
    And el usuario confirma la operación
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo
    Then El usuario navega a la pagina principal
    Then el usuario accede a la pagina de revision de embargos
    And el usuario envia a procesar el embargo
    And el usuario se dirige a procesar el embargo
    Then el usuario usuario presiona la opcion aplicar

  Scenario: TESTC-3118 Seleccion y Procesamiento de Operaciones Funcion 209465  - Procesar Operacion
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "6" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    When el usuario ingresa el numero de operación "Existente"
    Then el usuario selecciona la opción "2" para completar la información del embargo
    And el usuario completa el formulario
    When el usuario ingresa el numero de operación "Existente" del embargo previamente completado con el monto anterior ""
    And el usuario confirma la operación
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo
    Then El usuario navega a la pagina principal
    Then el usuario accede a la pagina de revision de embargos
    And el usuario envia a procesar el embargo
    And el usuario se dirige a procesar el embargo
    And el usuario procesa el embargo con tipo de operacion "6"


  Scenario: TESTC-3119 Mostrar cuenta corriente por numero de cuenta Funcion 201020
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "6" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    When el usuario ingresa el numero de operación "Existente"
    Then el usuario selecciona la opción "2" para completar la información del embargo
    And el usuario completa el formulario
    When el usuario ingresa el numero de operación "Existente" del embargo previamente completado con el monto anterior ""
    And el usuario confirma la operación
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo
    Then El usuario navega a la pagina principal
    Then el usuario accede a la pagina de revision de embargos
    And el usuario envia a procesar el embargo
    And el usuario se dirige a procesar el embargo
    And el usuario procesa el embargo con tipo de operacion "6"
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada

  Scenario: TESTC-3120 Consultas y Reportes de Embargos Funcion 209470 - Consulta por operaciones
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "6" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario accede a la pagina de revision de embargos
    When el usuario ingresa el numero de operación "Existente"
    Then el usuario selecciona la opción "2" para completar la información del embargo
    And el usuario completa el formulario
    When el usuario ingresa el numero de operación "Existente" del embargo previamente completado con el monto anterior ""
    And el usuario confirma la operación
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo
    Then El usuario navega a la pagina principal
    Then el usuario accede a la pagina de revision de embargos
    And el usuario envia a procesar el embargo
    And el usuario se dirige a procesar el embargo
    And el usuario procesa el embargo con tipo de operacion "6"
    Then El usuario navega a la pagina principal
    Then el usuario consulta reportes del embargo por operaciones