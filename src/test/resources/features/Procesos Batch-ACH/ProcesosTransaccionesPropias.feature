@Regression @ProcesosBatch @TransaccionesACH


Feature: Procesos Batch/ACH - Transacciones Propias
  Como un usuario del sistema
  Yo quiero exportar un archivo ACH de Tesorería al servidor
  Para que se realicen las transacciones y se generen los reportes correspondientes


  Scenario: TEST-2713 - TESTC-2761 - TESTTC-2691: Validar que las Transacciones del proceso CTA140 sean posteadas correctamente & Validar que se generen los reportes  correctamente en Spool file
  & Validar que las transacciones posteadas se registren a nivel contable
    Given El usuario ingresa la ruta del archivo local "Valido" para el tipo de proceso "TransACH" y luego la ruta remota "INPUT_PROACH" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de "TransACH" se valida la importación del archivo ACH "Valido", los reportes generados en el SpoolFile "CTA140" y las transacciones a cuentas del archivo
    And Se validan los registros en la cuenta contable de las transacciones del archivo ACH para el tipo de proceso "TransACH"

  Scenario: TESTC-2759: Validar el  formato de las cuenta regional  estándar en el sitio web provisto por  el banco central.
    When el usuario ingresa a la página de verificación de cuentas regionales
    Given El usuario ingresa el archivo para el tipo de proceso "TransACH" donde se encuentran las cuentas regionales a verificar
    Then el usuario regresa a la ventana principal

  Scenario: TESTC-3835: Validar que no se pueda procesar el mismo archivo insumo más de una vez
    Given El usuario ingresa la ruta del archivo local que generada "Duplicado" y luego la ruta remota "INPUT_PROACH" donde se enviará al servidor para el tipo de proceso "TransACH"
    When El usuario se autentica con sus credenciales
    Then En los casos de "TransACH" se valida la importación del archivo ACH "Duplicado", los reportes generados en el SpoolFile "CTA140" y las transacciones a cuentas del archivo

  Scenario: TESTC-2714: Validar que las transacciones del proceso CTA140 sean rechazadas si la cuenta no exite o es erronea
    Given El usuario ingresa la ruta del archivo local "Rechazado" para el tipo de proceso "TransACH" y luego la ruta remota "INPUT_PROACH" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de "TransACH" se valida la importación del archivo ACH "Rechazado", los reportes generados en el SpoolFile "CTA140" y las transacciones a cuentas del archivo

@AuxiliaryScenarios
  Scenario: TESTC-3570: Validar que no se posteen transacciones si una de la cuentas tiene un estado que lo impida
    When El usuario se autentica con sus credenciales
    When Se "inactivan las cuentas" del archivo ACH para el tipo de proceso "TransACH"
    Given El usuario ingresa la ruta del archivo local con cuentas inactivas y luego la ruta remota "INPUT_PROACH" donde se enviará al servidor para el tipo de archivo "TransaccionesACH"
    Then En los casos de "TransACH" se valida la importación del archivo ACH "cuentas inactivas", los reportes generados en el SpoolFile "CTA140" y las transacciones a cuentas del archivo
    And Se activan las cuentas del archivo ACH para el tipo de archivo "TransACH"







