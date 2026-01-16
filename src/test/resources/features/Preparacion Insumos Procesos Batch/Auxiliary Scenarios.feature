@AuxiliaryScenarios
Feature: Auxiliary Scenarios - Preparación Insumos Procesos Batch
  Este feature contiene escenarios auxiliares que se utilizan para la construccion de los casos de pruebas del modulo de preparacion de insumos para procesos batch.

  Scenario: Create and post a new batch
    Given El usuario se autentica con sus credenciales
    Given The user navigates to the batch transaction by entering the "QA" environment
    When A transaction must be created for the new batch and then posted by entering a batch name "PRUEBA008" a batch type "02", an account "2330194819", a transaction code "CS04", an amount "350000", and a cost center "010"
    Then The user should log out of Signature

  Scenario: Ir a la pagina de mantenimiento de cuentas de corrientes
    Given El usuario se autentica con sus credenciales
    Then el usuario ingresa la opcion "option" en el menu
    And el usuario hace clic en el botón de ok
    When el usuario ingresa la funcion "function"
    And el usuario hace clic en el botón de ok
    Then el usuario visualiza la página de mantenimiento de cuentas de corrientes


  Scenario: Verificar fecha de creacion de la cuenta
    Given El usuario se autentica con sus credenciales
    Given ir a la  pagina de mantenimiento de cuentas de corriente con la opcion "option" y funcion "function"
    When el usuario ingresa el numero de cuenta corriente en el menu de mantenimiento
    And el usuario ingresa la opcion "option" en el menu de mantenimiento
    And el usuario hace clic en el botón de ok
    Then el usuario vizualiza la fecha de creacion de la cuenta de corriente

  Scenario: Cambiar el estatus de una cuenta
    Given El usuario se autentica con sus credenciales
    Given ir a la  pagina de mantenimiento de cuentas de corriente con la opcion "option" y funcion "function"
    When el usuario ingresa el numero de cuenta corriente en el menu de mantenimiento
    And el usuario va a la pagina de cambio de estatus a traves de la opcion "option" en el menu de mantenimiento de cuenta corriente
    And el usuario hace clic en el botón de ok
    And el usuario cambia el estatus de la cuenta con la opcion "option" en el menu de cuenta corriente
    Then el usuario vizualiza el menu de mantenimiento de cuenta corriente

  Scenario: Verificar el estatus de una cuenta
    Given El usuario se autentica con sus credenciales
    Given ir a la  pagina de mantenimiento de cuentas de ahorro con la opcion "option" y funcion "function"
    When el usuario ingresa el numero de cuenta corriente en el menu de consulta de cuenta corriente
    Then el usuario vizualiza el estatus de la cuenta de cuenta corriente

  Scenario: Ir a la pagina de mantenimiento de cuentas de ahorros
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa la opcion "option" en el menu
    And el usuario hace clic en el botón de ok
    When el usuario ingresa la funcion "function"
    And el usuario hace clic en el botón de ok
    Then el usuario visualiza la página de mantenimiento de cuentas de ahorros

  Scenario: Verificar fecha de creacion de la cuenta
    Given El usuario se autentica con sus credenciales
    Given ir a la  pagina de mantenimiento de cuentas de ahorro con la opcion "option" y funcion "function"
    When el usuario ingresa el numero de cuenta de ahorro en el menu de mantenimiento
    And el usuario ingresa la opcion "option" en el menu de mantenimiento
    And el usuario hace clic en el botón de ok
    Then el usuario vizualiza la fecha de creacion de la cuenta de ahorro

  Scenario: Cambiar el estatus de una cuenta
    Given El usuario se autentica con sus credenciales
    Given ir a la  pagina de mantenimiento de cuentas de ahorro con la opcion "option" y funcion "function"
    When el usuario ingresa el numero de cuenta de ahorro en el menu de mantenimiento
    And el usuario va a la pagina de cambio de estatus a traves de la opcion "option" en el menu de mantenimiento
    And el usuario hace clic en el botón de ok
    And el usuario cambia el estatus de la cuenta con la opcion "option" en el menu
    Then el usuario vizualiza el menu de mantenimiento

  Scenario: Verificar el estatus de una cuenta
    Given El usuario se autentica con sus credenciales
    Given ir a la  pagina de mantenimiento de cuentas de ahorro con la opcion "option" y funcion "function"
    When el usuario ingresa el numero de cuenta de ahorro en el menu de consulta
    Then el usuario vizualiza el estatus de la cuenta

  Scenario: Ir a la pagina de consulta de cuentas
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa la opcion "" en el menu
    And el usuario hace clic en el botón de ok
    When el usuario ingresa la funcion ""
    And el usuario hace clic en el botón de ok
    Then el usuario visualiza la página de consulta de cuentas

  Scenario: Verificar el monto balance en .00 de la cuenta
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa en el menu de consulta de cuentas
    When el usuario ingresa el numero de cuenta en el menu de consulta e ingresa el numero de pagina ""
    Then el usuario vizualiza el balance de la cuenta
