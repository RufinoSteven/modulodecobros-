@Regression @Desembolso @Prestamos  @test2
Feature: Desembolso Prestamo.

    Scenario Outline: TESTC-568 - TESTC-5998: Desembolso de prestamos
      Given El usuario se autentica con sus credenciales
      And Se obtiene el monto maximo y minimo del prestamo "20633"
      And Se ingresa a la funcion "502010" y se inserta la cedula del cliente "40239552629"
      And Agregar cuenta de prestamo con su tipo "<TipoPrestamo>" Y seleccionar cuenta
      Then Consultar prestamo en funcion "501020" y salir de signature
      Examples:
        | TipoPrestamo   |
        | Consumo        |
        | EmplFeliz      |

      Scenario: TESTC-600 Desem_Préstamo Empleado Feliz con CR a cuenta de depósitos
        Given El usuario se autentica con sus credenciales
        And Se obtiene el monto maximo y minimo del prestamo "50648"
        And Se ingresa a la funcion "681110" y seleccionamos tipo de credito conjunto insertar la cedula del beneficiario "40239552629"
         And Agregar cuenta de prestamo con su tipo "PROMIPYME" Y seleccionar cuenta
        Then Consultar prestamo en funcion "501020" y salir de signature

    Scenario Outline: TESTC-601 Desem_Préstamo con CR a cuenta contable (cheque)
      Given El usuario se autentica con sus credenciales
      And Se obtiene el monto maximo y minimo del prestamo "20633"
      And Se ingresa a la funcion "502010" y se inserta la cedula del cliente "40239552629"
      And Agregar cuenta de prestamo con su tipo "<TipoPrestamo>" Y seleccionar cuenta
      Then Consultar prestamo en funcion "501020" y salir de signature
      Examples:
        | TipoPrestamo |
        | Consumo      |
        | EmplFeliz    |
        | PROMIPYME    |