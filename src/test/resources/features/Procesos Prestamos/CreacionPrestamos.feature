# File: savings.feature
# Author: Leuris Cuello
# Version: 1.0
# Description: This file defines the test scenarios for the creation loans.
@Loan
Feature: Creacion de prestamos

  Scenario Outline: TESTC-4328 - TESTC-4353 - TESTC-4427 - TESTC-428-TESTC-4425: Crear Prestamos
    Given El usuario se autentica con sus credenciales
    When Seleccionar la opcion "1" he ir al menu 6 pantalla de creacion de prestamos opcion "502010" menu prestamo
    And Insertar el documento de identidad "40225000021"
    And Agregar cuenta de prestamo con tipo de prestamo "<TipoPrestamo>" Y seleccionar cuenta
    Then Consultar prestamo funcion "501020" y salir de signature
    Examples:
      | TipoPrestamo   |
      | Consumo        |
      | Comercial      |
      | EmplFeliz      |
      | Hipotecario    |

    Scenario: TESTC-4423: Crear Linea de Crédito y Sublimites
      Given El usuario se autentica con sus credenciales
    When Seleccionar la opcion "1" he ir al menu 6 pantalla de creacion de prestamos opcion "901020" menu prestamo
    And  Insertar el documento de identidad "40239552777"
    And Trabajar linea de credito
    Then Buscar linea de credito creada y salir de signature
