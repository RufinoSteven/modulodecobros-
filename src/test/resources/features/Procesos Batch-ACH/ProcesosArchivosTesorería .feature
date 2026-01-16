@Regression @ProcesosBatch @Tesoreria

Feature: Casos de Tesorería
  Como un usuario del sistema
  Yo quiero exportar un archivo ACH de Tesorería al servidor
  Para que se realicen las transacciones y se generen los reportes correspondientes

  Scenario: TEST-2684 - TESTC-2685 - TESTTC-2691 - TESTC-2695 - TESTC-3569: Ejecutar el proceso  CTA038CL de la Tesoreria Nacional y validar Posteo & Que el archivo insumo a ser cargado no se haya cargado anteriormente
  & Ejecutar el proceso  CTA038CL de la Tesoreria Nacional y Validar que se generen los reportes correspondientes  & Ejecutar el proceso  CTA038CL de la Tesoreria Nacional y Validar que el proceso CTA038CL solo permita Creditos a las cuenta de clientes
  & Validar que las transacciones posteadas se registren a nivel contable
    Given El usuario ingresa la ruta del archivo local "Valido" para el tipo de proceso "Tesoreria" y luego la ruta remota "INPUT_TESACH" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de "Tesoreria" se valida la importación del archivo ACH "Valido", los reportes generados en el SpoolFile "CTA038" y las transacciones a cuentas del archivo
    And Se validan los registros en la cuenta contable de las transacciones del archivo ACH para el tipo de proceso "Tesoreria"


  Scenario: TESTC-2688 - TESTC-2692: Ejecutar el proceso  CTA038CL de la Tesoreria Nacional y Validar que se genere el archivo de rechazo en la ruta parametrizada
    Given El usuario ingresa la ruta del archivo local que generada "Rechazado" y luego la ruta remota "INPUT_TESACH" donde se enviará al servidor para el tipo de proceso "Tesoreria"
    When El usuario se autentica con sus credenciales
    Then En los casos de "Tesoreria" se valida la importación del archivo ACH "Rechazado", los reportes generados en el SpoolFile "CTA038" y las transacciones a cuentas del archivo

  @AuxiliaryScenarios
  Scenario: TESTC-3570: Validar que no se posteen transacciones si una de la cuentas tiene un estado que lo impida
    When El usuario se autentica con sus credenciales
    When Se "inactivan las cuentas" del archivo ACH para el tipo de proceso "Tesoreria"
    Given El usuario ingresa la ruta del archivo local con cuentas inactivas y luego la ruta remota "INPUT_TESACH" donde se enviará al servidor para el tipo de archivo "Tesoreria"
    Then En los casos de "Tesoreria" se valida la importación del archivo ACH "cuentas inactivas", los reportes generados en el SpoolFile "CTA038" y las transacciones a cuentas del archivo
    And Se activan las cuentas del archivo ACH para el tipo de archivo "Tesoreria"








