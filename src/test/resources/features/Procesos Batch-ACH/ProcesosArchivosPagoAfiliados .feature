@Regression @ProcesosBatch @Afiliados
Feature: Casos de Pago Afiliados
  Como un usuario del sistema
  Yo quiero exportar un archivo ACH de Pago de Afiliados al servidor
  Para que se realicen las transacciones y se generen los reportes correspondientes

  Scenario: TESTC-3814 - TESTC-3818: Validar que las Transacciones del proceso ACH0005CL sean posteadas correctamente &  Validar que se generen los reportes correctamente en Spool file
    Given El usuario ingresa la ruta del archivo local "Valido" para el tipo de proceso "Afiliados" y luego la ruta remota "INPUT_AFPACH" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de "Afiliados" se valida la importación del archivo ACH "Valido", los reportes generados en el SpoolFile "ACH005" y las transacciones a cuentas del archivo


  Scenario: TESTC-3819: Validar que no se pueda procesar el mismo archivo más de una vez
    Given El usuario ingresa la ruta del archivo local que generada "Duplicado" y luego la ruta remota "INPUT_AFPACH" donde se enviará al servidor para el tipo de proceso "Afiliados"
    When El usuario se autentica con sus credenciales
    Then En los casos de "Afiliados" se valida la importación del archivo ACH "Duplicado", los reportes generados en el SpoolFile "ACH005" y las transacciones a cuentas del archivo


  Scenario: TESTC-2759: Validar el  formato de las cuenta regional  estándar en el sitio web provisto por  el banco central.
    When el usuario ingresa a la página de verificación de cuentas regionales
    Given El usuario ingresa el archivo para el tipo de proceso "Afiliados" donde se encuentran las cuentas regionales a verificar
    Then el usuario regresa a la ventana principal

  Scenario: TESTC-3816: Validar que las transacciones del proceso ACH0005CL sean rechazadas si la cuenta no exite o es erronea
    Given El usuario ingresa la ruta del archivo local "Rechazado" para el tipo de proceso "Afiliados" y luego la ruta remota "INPUT_AFPACH" donde se enviará al servidor
    When El usuario se autentica con sus credenciales
    Then En los casos de "Afiliados" se valida la importación del archivo ACH "Rechazado", los reportes generados en el SpoolFile "ACH005" y las transacciones a cuentas del archivo






