@Regression @ProcesosBatch @RevsersosACH
Feature: Casos de Reversos ACH
  Como un usuario del sistema
  Yo quiero exportar un archivo de Reversos ACH al servidor
  Para que se realicen las transacciones y se generen los reportes correspondientes


  Scenario: TESTC-3861 - TESTC-3863: Validar que las transaccioens del proceso sean posteadas & Validar archivos de Spool File
    Given El usuario ingresa la ruta del archivo local "Valido" para el tipo de proceso "ReversosACH" y luego la ruta remota "INPUT_REVACH" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de "ReversosACH" se valida la importación del archivo ACH "Valido", los reportes generados en el SpoolFile "PD" y las transacciones a cuentas del archivo


  Scenario: TESTC-3864: Validar que no se pueda procesar el mismo archivo más de una vez
    Given El usuario ingresa la ruta del archivo local "Duplicado" para el tipo de proceso "ReversosACH" y luego la ruta remota "INPUT_REVACH" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de "ReversosACH" se valida la importación del archivo ACH "Duplicado", los reportes generados en el SpoolFile "CTA1011" y las transacciones a cuentas del archivo

  Scenario: TESTC-3862: Validar que las transaccioens del proceso sean rechazadas por diferentes motivos
    Given El usuario ingresa la ruta del archivo local "Rechazado" para el tipo de proceso "ReversosACH" y luego la ruta remota "INPUT_REVACH" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de "ReversosACH" se valida la importación del archivo ACH "Rechazado", los reportes generados en el SpoolFile "CTA1011" y las transacciones a cuentas del archivo








