# File: debitosCreditos.feature
# Author: Rancer Ventura
# Version: 1.0
# Description: This file defines the test scenarios for debits and credits upload files functionality.

Feature: Procesos de creditos y debitos
  Como un usuario del sistema
  Yo quiero exportar un archivo con debitos y creditos a cuentas al servidor
  Para que se realicen las transacciones y se generen los reportes correspondientes

  Scenario: TESTC-6718: Validar que no se pueda procesar el archivo en un programa distinto al original
    Given El usuario ingresa la ruta del archivo local valido "src\test\resources\DC\AHDOEV280725" y luego la ruta remota "INPUT_TESACH" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de debito y credito se valida la importación del archivo en la funcion "240070" y se confirma que el reporte generado tiene el estatus de cancelado en el SpoolFile

  Scenario: TESTC-6710- TESTC-6711: Validar que al proceso de subir el archivo CTA062 sean posteado correctamente con creditos y debitos a cuentas activas.
    Given El usuario ingresa la ruta del archivo local "Valido" para el tipo de proceso "DC" y luego la ruta remota "INPUT_DBCRGEN" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de "DC" se valida la importación del archivo ACH "Valido", los reportes generados en el SpoolFile "CTA062" y las transacciones a cuentas del archivo


  Scenario: TESTC-6715: Procesar el mismo archivo insumo más de una vez en CTA062CL00
    Given El usuario ingresa la ruta del archivo local que generada "Duplicado" y luego la ruta remota "INPUT_DBCRGEN" donde se enviará al servidor para el tipo de proceso "DC"
    When El usuario se autentica con sus credenciales
    Then En los casos de "DC" se valida la importación del archivo ACH "Duplicado", los reportes generados en el SpoolFile "CTA062" y las transacciones a cuentas del archivo


  Scenario: TESTC-6713: Validar que al proceso de subir el archivo CTA062 sean posteado correctamente con creditos y debitos a cuentas cuentas rechazadas por estado.
    Given El usuario ingresa la ruta del archivo local "Rechazado" para el tipo de proceso "DC" y luego la ruta remota "INPUT_DBCRGEN" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de "DC" se valida la importación del archivo ACH "Rechazado", los reportes generados en el SpoolFile "CTA062" y las transacciones a cuentas del archivo