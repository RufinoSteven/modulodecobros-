@Regression @ProcesosBatch @Nomina
Feature: Casos de Nomina
  Como un usuario del sistema
  Yo quiero cargar un archivo de Nomina al servidor
  Para que se realicen las transacciones y se generen los reportes correspondientes


  Scenario: TEST-3573 - Ejecutar el proceso  NOM012CL de las Nominas y validar Posteo & Que el archivo insumo a ser cargado no se haya cargado anteriormente
  & Ejecutar el proceso  CTA038CL de la Tesoreria Nacional y Validar que se generen los reportes correspondientes  & Ejecutar el proceso  CTA038CL de la Tesoreria Nacional y Validar que el proceso CTA038CL solo permita Creditos a las cuenta de clientes
    Given El usuario ingresa la ruta del archivo nomina local valido "src/main/resources/Nomina/NM0569668IN.txt" y luego la ruta remota "INPUT_NOMINA/NominaIN" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then Se valida la importación del archivo Nomina "Valido", los reportes generados y las transacciones a cuentas del archivo "src/main/resources/Nomina/NM010446IN.txt"

    @AuxiliaryScenarios
  Scenario Outline: TESTC-3609 - TESTC-3610: Ejecutar el proceso NOM012CL Nomina y Validar que se genere el archivo de rechazo en la ruta parametrizada
    Given El usuario ingresa la ruta del archivo local que generada rechazos "src/main/resources/Nomina/NM010408IN.txt" y luego la ruta remota "INPUT_NOMINA" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then Se valida la importación del archivo "src/main/resources/Nomina/NM010408IN.txt" Nomina Rechazado "<reportType>"
    Examples:
      | reportType |
      | Rechazado  |
      | Duplicado  |