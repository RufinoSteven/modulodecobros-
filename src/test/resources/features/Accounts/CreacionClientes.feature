@Regression @CORE

Feature:Casos Creación de Clientes
  Como un usuario del sistema
  Yo quiero crear un cliente, luego crearle cuenta corriente o  de ahorro
  Para luego generar depósitos a plazos


  Scenario: TEST-3937: Crear Cliente
    When El usuario se autentica con sus credenciales
    Then Se ingresa a la transacción de clientes
    And Se busca la cédula del cliente "00102003781"
    When Se completan los formularios del cliente con los siguientes datos:
      | Cédula                   | 00102003781        |
      | Nombre corto             | PRUEBA39           |
      | Título                   | ING                |
      | Nombre                   | Edgar              |
      | Segundo nombre           | Alexander          |
      | Apellido                 | Perez              |
      | Segundo apellido         | Corripio           |
      | Apartamento              | 5B                 |
      | Nombre casa              | Casa Azul          |
      | Número casa              | 123                |
      | Calle                    | Calle Falsa        |
      | Sector                   | Centro             |
      | Tipo dirección           | 0                  |
      | Vive aquí desde          | 01012004           |
      | Confirmar fecha          | 1                  |
      | Post                     | JEFE               |
      | Patrón                   | EL ALFA            |
      | Dirección 1              | Direccion A        |
      | Dirección 2              | Direccion B        |
      | Código postal trabajo    | 45678              |
      | SIC Code                 | 00001              |
      | Email                    | prueba@mail.com    |
      | Teléfono                 | 8091234567         |
      | Extensión trabajo        | 101                |
      | Ingresos                 | 50000              |
      | Fecha inicio             | 010120             |
      | Años empleo              | 8                  |
      | Número nómina            | 960948434          |
      | Código profesión         | 001                |
      | Número seguro social     | 00123456361        |
      | Número identificación    | 00102003781        |
      | Clasificación cliente    | 1                  |
      | Impuesto extranjero      | N                  |
      | Número sucursal          | 00001              |
      | Oficial principal        | ATM                |
      | Segmento mercado         | 03                 |
      | Fecha nacimiento         | 01012000           |
      | Género                   | M                  |
      | Estado civil             | 2                  |


  Scenario: TEST-4392: Crear Cuenta de Ahorros Producto 00074 -Resto de Hogares sin Tarjeta
    When El usuario se autentica con sus credenciales
    Then Se ingresa a la transacción 202020 para buscar el cliente y crearle una nueva cuenta
    And Se ingresan las informaciones de la cuenta a crear para el cliente
      | Número de transacción      | 202020           |
      | Número identificación      | 00102003781      |
      | Nombre corto               | PRUEBA39         |
      | Tipo de cuenta             | AHORRO           |
      | Código producto            | 00074            |
      | Código sucursal cuenta     | 00001            |
      | Código oficina cuenta      | ATM              |
      | Título cuenta              | NUEVA CUENTA AHORRO|
      | Código moneda              | 000              |
      | ¿Tendrá tarjeta?           | Si               |
      | Número tarjetas a generar  | 1                |
      | Fecha vencimiento tarjeta  | 10528            |
      | Fecha próxima revisión     | 10528            |
      | Fecha emisión próximo PIN  | 10528            |


  Scenario: TEST-4411: Crear Cuenta Corriente Persona Fisica
    When El usuario se autentica con sus credenciales
    Then Se ingresa a la transacción 202010 para buscar el cliente y crearle una nueva cuenta
    And Se ingresan las informaciones de la cuenta a crear para el cliente
      | Número de transacción      | 202010           |
      | Número identificación      | 00102003781      |
      | Nombre corto               | PRUEBA39         |
      | Tipo de cuenta             | CORRIENTE        |
      | Código producto            | 00048            |
      | Código sucursal cuenta     | 00001            |
      | Código oficina cuenta      | ATM              |
      | Título cuenta              | NUEVA CUENTA CORRIENTE|
      | Código moneda              | 000              |
      | ¿Tendrá tarjeta?           | Si               |
      | Número tarjetas a generar  | 1                |
      | Fecha vencimiento tarjeta  | 10528            |
      | Fecha próxima revisión     | 10528            |
      | Fecha emisión próximo PIN  | 10528            |


  Scenario: TEST-4382: Crear Depósito a Plazo
    When El usuario se autentica con sus credenciales
    Then Se ingresa a la transacción de depósito a plazo
    And Se ingresan las informaciones para crear un depósito a plazos
      | Número identificación        | 00102003781      |
      | Tipo de la cuenta nueva      | 00114            |
      | Número sucursal              | 00010            |
      | Issue Amount                 | 10000000         |
      | Opción renovación            | A                |
      | Período de renovación        | M                |
      | Día específico de renovación | 00               |
      | Código disposición           | N                |
      | Tipo cuenta destino          | AHORRO           |
      | Referencia de la orden       | PRUEBA EDGAR 2   |
      | Monto depósito a plazo       | 0                |
      | Código de moneda             | 000              |







