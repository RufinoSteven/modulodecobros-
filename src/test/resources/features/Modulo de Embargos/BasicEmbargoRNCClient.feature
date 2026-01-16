# Version: 1.0
# Author: Jodir Jiménez
# Description: This file defines the test scenarios for the basic embargo rnc client functionality.
@Regression @Embargos
Feature: Procesamiento de Embargo Básico Cliente RNC
  Como usuario del sistema
  Quiero procesar operaciones de embargo básico cliente RNC
  Para gestionar las solicitudes de embargo

Scenario: TESTC-3077 Creacion de embargo basico funcion 209450 - Crear
  Given El usuario se autentica con sus credenciales
  Then el usuario ingresa la referencia "1"
  When el usuario ingresa numero de funcion "209450"
  And el usuario selecciona la opción de creación con F6
  And el usuario selecciona el tipo de operación "2" y tipo de identificación "3"
  And el usuario ingresa el número de identificación "101002026" y la oficina "10"
  Then el usuario presiona Enter y confirma la creacion

Scenario: TESTC-3078 Asignacion de embargo creado funcion 209455 - Reasignar
  Given El usuario se autentica con sus credenciales
  Then se captura el nombre del usuario
  Then el usuario ingresa la referencia "1"
  When el usuario ingresa numero de funcion "209450"
  And el usuario selecciona la opción de creación con F6
  And el usuario selecciona el tipo de operación "2" y tipo de identificación "3"
  And el usuario ingresa el número de identificación "101002026" y la oficina "10"
  Then el usuario presiona Enter y confirma la creacion
  Then El usuario navega a la pagina principal
  And  el usuario proporciona el número de función para reasignación "209455"
  And el usuario coloca el numero de embargo que se va a reasignar "Existente"
  And el usuario da click Enter, confirmar
  And el usuario introduce la nueva referencia para modificar "2"
  And el usuario ingresa el nombre de usuario que quiere asignar
  Then el usuario finaliza la confirmación

Scenario: TESTC-3079 - TESTC-3080: Revision Operacion de Embargos funcion 209460 - Completar
  Given El usuario se autentica con sus credenciales
  Then se captura el nombre del usuario
  Then el usuario ingresa la referencia "1"
  Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "3"
  Then El usuario navega a la pagina principal
  And El usuario se asigna el embargo "Existente" al usuario logueado
  Then El usuario navega a la pagina principal
  And El usuario se dirige a completar el embargo


Scenario: TESTC-3081 Revision Operacion de Embargos funcion 209460 - Enviar a procesar estando completada
  Given El usuario se autentica con sus credenciales
  Then se captura el nombre del usuario
  Then el usuario ingresa la referencia "1"
  Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "3"
  Then El usuario navega a la pagina principal
  And El usuario se asigna el embargo "Existente" al usuario logueado
  Then El usuario navega a la pagina principal
  And El usuario se dirige a completar el embargo
  Then El usuario navega a la pagina principal
  Then el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso
  Then El usuario navega a la pagina principal
  Then el usuario accede a la pagina de revision de embargos
  Then El usuario envia a procesar el embargo


Scenario: TESTC-3082 Selecci�n y Procesamiento de Operaciones Funcion 209465  - Consultar
  Given El usuario se autentica con sus credenciales
  Then el usuario ingresa la referencia "1"
  When el usuario ingresa numero de funcion "209460"
  When el usuario selecciona la operación consultar embargo con "5"

Scenario: TESTC-3083 - TESTC-3085 Seleccion y Procesamiento de Operaciones Funcion 209465 - Procesar Operacion
  Given El usuario se autentica con sus credenciales
  Then se captura el nombre del usuario
  Then el usuario ingresa la referencia "1"
  Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "3"
  Then El usuario navega a la pagina principal
  And El usuario se asigna el embargo "Existente" al usuario logueado
  Then El usuario navega a la pagina principal
  And El usuario se dirige a completar el embargo
  Then El usuario navega a la pagina principal
  Then el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso
  Then El usuario navega a la pagina principal
  Then el usuario accede a la pagina de revision de embargos
  Then El usuario envia a procesar el embargo
  Then El usuario navega a la pagina principal
  Then el usuario procesa el embargo RNC

  Scenario: TESTC-3086 - Mostrar cuenta corriente por numero de cuenta Funcion 201020
    Given El usuario se autentica con sus credenciales
    Then se captura el nombre del usuario
    Then el usuario ingresa la referencia "1"
    Given El usuario crea un embargo que sea del tipo de operacion "2" y tipo de identificacion "3"
    Then El usuario navega a la pagina principal
    And El usuario se asigna el embargo "Existente" al usuario logueado
    Then El usuario navega a la pagina principal
    And El usuario se dirige a completar el embargo
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso
    Then El usuario navega a la pagina principal
    Then el usuario accede a la pagina de revision de embargos
    Then El usuario envia a procesar el embargo
    Then El usuario navega a la pagina principal
    Then el usuario procesa el embargo RNC
    Then El usuario navega a la pagina principal
    Then el usuario consulta estado de la cuenta disponible embargada