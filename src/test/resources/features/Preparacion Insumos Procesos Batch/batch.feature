@Regression @Mantenimiento
Feature: Creating and posting batches
  As a system user
  I want to process, create, and add transactions to a batch.
  In order to complete and post the batch

  Scenario: Crear y postear un nuevo lote
    Given El usuario se autentica con sus credenciales
    Given el usuario navega a la transacción de lote ingresando al entorno "QA"
    When se debe crear una transacción para el nuevo lote y luego postearla, ingresando un nombre de lote "PRUEBA008", un tipo de lote "02", una cuenta "2330194819", un código de transacción "CS04", un monto "3500", un codigo de moneda "000" y un centro de costos "010"
    Then el usuario debe cerrar sesión en Signature

  Scenario: Ir a la pagina de mantenimiento de cuentas de ahorros
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa la opcion "1" en el menu
    And el usuario hace clic en el botón de ok
    When el usuario ingresa la funcion "202030"
    Then el usuario visualiza la página de mantenimiento de cuentas


  Scenario: Verificar fecha de creacion de la cuenta
    Given El usuario se autentica con sus credenciales
    Given ir a la  pagina de mantenimiento de cuentas con la opcion "1" y funcion "202030"
    When el usuario ingresa el numero de cuenta de ahorro en el menu de mantenimiento
    And el usuario ingresa la opcion "1" en el menu de mantenimiento
    And el usuario hace clic en el botón de ok
    Then el usuario vizualiza la fecha de creacion de la cuenta

    #Current Accounts
  Scenario: Ir a la pagina de mantenimiento de cuentas de corrientes
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa la opcion "1" en el menu
    And el usuario hace clic en el botón de ok
    When el usuario ingresa la funcion "202035"
    Then el usuario visualiza la página de mantenimiento de cuentas

  Scenario: Verificar fecha de creacion de la cuenta
    Given El usuario se autentica con sus credenciales
    Given ir a la  pagina de mantenimiento de cuentas con la opcion "1" y funcion "202035"
    When el usuario ingresa el numero de cuenta corriente en el menu de mantenimiento
    And el usuario ingresa la opcion "1" en el menu de mantenimiento
    And el usuario hace clic en el botón de ok
    Then el usuario vizualiza la fecha de creacion de la cuenta
