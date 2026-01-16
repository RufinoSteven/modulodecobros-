@Regression @Embargos
Feature: Procesamiento de Embargos carta compromiso
  Como usuario del sistema
  Quiero procesar operaciones de embargo carta compromiso
  Para gestionar las solicitudes de embargo

   #El tipo de operacion 8 representa carta
  Scenario: TESTC-3099 creacion de embargo basico funcion 209450 - Crear
    Given El usuario se autentica con sus credenciales
    Then el usuario ingresa la referencia "1"
    When el usuario ingresa numero de funcion "209450"
    And el usuario selecciona la opción de creación con F6
    And el usuario selecciona el tipo de operación "8" y tipo de identificación "6"
    And el usuario ingresa el número de identificación "01800502989" y la oficina "17"
    Then el usuario presiona Enter y confirma la creacion

  Scenario: TESTC-3100 Asignacion de embargo creado funcion 209455 - Reasignar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    When el usuario ingresa numero de funcion "209450"
    And el usuario selecciona la opción de creación con F6
    And el usuario selecciona el tipo de operación "8" y tipo de identificación "6"
    And el usuario ingresa el número de identificación "01800502989" y la oficina "17"
    And el usuario presiona Enter y confirma la creacion
    Then El usuario navega a la pagina principal
    And  el usuario proporciona el número de función para reasignación "209455"
    And el usuario coloca el numero de embargo que se va a reasignar "Existente"
    And el usuario da click Enter, confirmar
    And el usuario introduce la nueva referencia para modificar "2"
    And el usuario ingresa el nombre de usuario que quiere asignar
    Then el usuario finaliza la confirmación

   #Proceso para  completar carta compromiso
  Scenario: TESTC-3101 - TESTC-3102: Revision Operacion de Embargos funcion 209460 - Completar
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "8" y tipo de identificacion "6"
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

#Proceso para enviar a procesar carta compromiso
  Scenario: TESTC-3103 Revision Operacion de Embargos funcion 209460 - Enviar a procesar estando completada
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "8" y tipo de identificacion "6"
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
    Then el usuario envia a procesar el embargo

    # Seleccion y pocesamiento de operacion (Consultar)
  Scenario: TESTC-3267 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Consultar
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa la referencia "1"
    And El usuario crea un embargo que sea del tipo de operacion "8" y tipo de identificacion "6"
    And el usuario selecciona la operación consultar embargo con "5"
    Then El usuario navega a la pagina principal


  Scenario: TESTC-3107-3106-3105 Seleccion y Procesamiento de Operaciones Funcion 209465  - Procesar Operacion
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "8" y tipo de identificacion "6"
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
    Then el usuario envia a procesar el embargo
    And el usuario se dirige a procesar el embargo
    And el usuario procesa el embargo con tipo de operacion "8"


  Scenario: TESTC-3108 Mostrar cuenta corriente por numero de cuenta Funcion 201020
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "8" y tipo de identificacion "6"
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
    Then el usuario envia a procesar el embargo
    And el usuario se dirige a procesar el embargo
    And el usuario procesa el embargo con tipo de operacion "8"
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada


  Scenario: TESTC-3109 Consultas y Reportes de Embargos Funcion 209470 - Consulta por operaciones
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "6"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And el usuario completa y procesa el embargo "Existente"
    Then El usuario navega a la pagina principal
    And El usuario crea un embargo que sea del tipo de operacion "8" y tipo de identificacion "6"
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
    Then el usuario envia a procesar el embargo
    And el usuario se dirige a procesar el embargo
    And el usuario procesa el embargo con tipo de operacion "8"
    Then El usuario navega a la pagina principal
    Then el usuario consulta reportes del embargo por operaciones

