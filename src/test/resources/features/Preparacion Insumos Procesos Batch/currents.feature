# File: currents.feature
# Author: Jodir Jiménez
# Version: 1.0
# Description: This file defines the test scenarios for the currents maintanace functionality.
@Regression @Mantenimiento

Feature: Funcionalidad de mantenimiento de cuentas de corriente
  Como usuario del sistema
  Quiero poder ir a la pagina de mantenimiento
  Para poder acceder a las funcionalidades

  Scenario: Ir a la pagina de mantenimiento de cuentas de corrientes
    Given El usuario se autentica con sus credenciales
    Given el usuario ingresa la opcion "1" en el menu
    And el usuario hace clic en el botón de ok
    When el usuario ingresa la funcion "20 2035"
    And el usuario hace clic en el botón de ok
    Then el usuario visualiza la página de mantenimiento de cuentas

  Scenario: Verificar fecha de creacion de la cuenta
    Given El usuario se autentica con sus credenciales
    When ir a la  pagina de mantenimiento de cuentas con la opcion "1" y funcion "202035"
    When el usuario ingresa el numero de cuenta corriente en el menu de mantenimiento
    And el usuario ingresa la opcion "1" en el menu de mantenimiento
    And el usuario hace clic en el botón de ok
    Then el usuario visualiza la página de mantenimiento de cuentas