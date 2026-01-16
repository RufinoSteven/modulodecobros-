
Feature: Casos de Domiciliación
  Como un usuario del sistema
  Yo quiero exportar un archivo ACH de Domiciliación al servidor
  Para que se realicen las transacciones y se generen los reportes correspondientes

  Scenario: TESTC-3857 - TESTC-3859: Validar que las transaccioens del proceso sean posteadas & Validar archivos de Spool File
    Given El usuario ingresa la ruta del archivo local "Valido" para el tipo de proceso "Domiciliacion" y luego la ruta remota "INPUT_CASE_DOMICILIA" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de "Domiciliacion" se valida la importación del archivo ACH "Valido", los reportes generados en el SpoolFile "CTA710" y las transacciones a cuentas del archivo


  Scenario: TESTC-3860 - TESTC-3858: Validar que no se pueda procesar el mismo archivo más de una vez & Validar que las transaccioens del proceso sean rechazadas por diferentes motivos
    When El usuario se autentica con sus credenciales
    Given El usuario ingresa la ruta del archivo local que generada "Rechazado" y luego la ruta remota "INPUT_CASE_DOMICILIA" donde se enviará al servidor para el tipo de proceso "Domiciliacion"
    Then En los casos de "Domiciliacion" se valida la importación del archivo ACH "Rechazado", los reportes generados en el SpoolFile "CTA710" y las transacciones a cuentas del archivo


