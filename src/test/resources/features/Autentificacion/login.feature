# File: login.feature
# Author: Cristofer Nuñez
# Version: 1.0
# Description: This file defines the test scenarios for the login functionality.
Feature: Funcionalidad de Inicio de sesión
  Como usuario del sistema
  Quiero poder iniciar sesión con mis credenciales
  Para poder acceder a las funcionalidades

  Scenario: Inicio de sesión exitoso
    Given el usuario navega a la página de login
    When El usuario se autentica con sus credenciales
    Then el usuario debería estar en la página principal

#  Scenario: Inicio de sesión con credenciales inválidas
#    Given el usuario navega a la página de login
#    When el usuario iniciar sesión con el nombre de usuario "usuario_incorrecto" y la contraseña "password_incorrecta"
#    Then el usuario visualiza un mensaje de error
