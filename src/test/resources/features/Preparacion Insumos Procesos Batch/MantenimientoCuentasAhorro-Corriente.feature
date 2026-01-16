# File: savings.feature
# Author: Rancer Ventura
# Version: 1.0
# Description: This file defines the test scenarios for the savings maintenance functionality.
@Regression @Mantenimiento
Feature: Funcionalidad de mantenimiento de cuentas de ahorro
  Como usuario del sistema
  Quiero poder ir a la pagina de mantenimiento
  Para poder acceder a las funcionalidades


  Scenario Outline: TESTC-1267: Cambiar el estado de cuenta de ahorro
    Given El usuario se autentica con sus credenciales
    And ir a la  pagina de mantenimiento de cuentas con la opcion "1" y funcion "202030"
    When el usuario ingresa el numero de cuenta de ahorro en el menu de mantenimiento
    And el usuario va a la pagina de cambio de estatus a traves de la opcion "1" en el menu de mantenimiento
    And el usuario hace clic en el botón de ok
    And el usuario cambia el estatus de la cuenta con la opcion "<Status>" en el menu
    And regresar a la  pagina del menu principal y dirigirse a la pagina de consulta con la funcion "201030"
    And el usuario ingresa el numero de cuenta de ahorro en el menu de consulta
    Then el usuario vizualiza el estatus de la cuenta
    Examples:
      | Status |
      | 1 |
      | 3 |
      | 4 |
      | 5 |
      | 6 |
      | 7 |

# Current accounts tests.
  Scenario Outline: TESTC-1219: Cambiar el estado de cuenta corriente
    Given El usuario se autentica con sus credenciales
    And ir a la  pagina de mantenimiento de cuentas con la opcion "1" y funcion "202035"
    When el usuario ingresa el numero de cuenta corriente en el menu de mantenimiento
    And el usuario va a la pagina de cambio de estatus a traves de la opcion "1" en el menu de mantenimiento
    And el usuario hace clic en el botón de ok
    And el usuario cambia el estatus de la cuenta con la opcion "<Status>" en el menu
    And regresar a la  pagina del menu principal y dirigirse a la pagina de consulta con la funcion "201020"
    And el usuario ingresa el numero de cuenta corriente en el menu de consulta de cuenta corriente
    Then el usuario vizualiza el estatus de la cuenta
    Examples:
      | Status |
      | 1 |
      | 3 |
      | 4 |
      | 5 |
      | 6 |
      | 7 |


