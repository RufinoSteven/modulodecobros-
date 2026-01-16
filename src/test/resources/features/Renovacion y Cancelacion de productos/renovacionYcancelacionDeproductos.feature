
# File: savings.feature
# Author: Leuris Cuello
# Version: 1.0
# Description: This file defines the test scenarios for the creation loans.

Feature: Cancelacion de productos

  Scenario: TESTC - 4075: Cancelar préstamos AR018 (Clientes EIF)
    Given Usuario ingresa usuario "E243701" y contrasena "Pruebasqa002." correcta
    When Seleccionar opcion "1" he ir al menu 6 pantalla de creacion de prestamos opcion "502010" menu prestamo
    And Insertar documento de identidad "40225000021"
    And Agregar cuenta prestamo con tipo de prestamo "Consumo" Y seleccionar cuenta
    Then Ingresar a la pantalla "503450" para cancelar prestamo
  @testReporte
  Scenario: TESTC - 4076: cancelar certificados AR018 (Clientes EIF)
    Given Usuario ingresa "E243701" con y contrasena "Pruebasqa002." correcta
    When Seleccionar opcion "1" he ir a la pantalla cancelacion de certificados "303005"
    And Realizar "Cancelacion" certificados

  Scenario: TESTC - 4072: Renovar certificados AR010 (Clientes NO EIF)
    Given Usuario ingresa "E243701" con y contrasena "Pruebasqa002." correcta
    When Seleccionar opcion "1" he ir a la pantalla cancelacion de certificados "303005"
    And Realizar "Renovacion" certificados

  Scenario: TESTC - 4071: Renovar préstamos AR010 (Clientes NO EIF)
    Given Usuario ingresa "E243701" con y contrasena "Pruebasqa002." correcta
    When Seleccionar opcion "1" he ir a la pantalla cancelacion de certificados "503360"
    Then Renovar prestamos NO EIF