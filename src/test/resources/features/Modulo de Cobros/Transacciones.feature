@Regression @Cobros @Transacciones
Feature: Cobro de transacciones pendientes

  @Total
  Scenario: TESTC-2675: Cobro total de transacciones pendiente
    Given El usuario se autentica con sus credenciales
    And el cliente posee "transacciones" "pendientes"
    And la cuenta no posee balance actual
    When la cuenta recibe un 'crédito' cuyo Monto Transacción es 'mayor' al Monto Pendiente
    And el código de transacción es adecuado para el tipo de cuenta
    Then se activa el cobro en línea
    And la cuenta recibe un débito por el monto aplicado
    And la descripción de la transacción posee el numero de referencia de la partida pendiente
    And el monto pendiente queda en 0 en el módulo de cobros
    And el campo monto cobrado coincide con el monto del débito a la cuenta
    And el estado de la partida queda en "4"

  @Parcial
  Scenario: TESTC-2665: Cobro parcial de transacciones pendientes
    Given El usuario se autentica con sus credenciales
    And el cliente posee "transacciones" "pendientes"
    And la cuenta no posee balance actual
    When la cuenta recibe un 'crédito' cuyo Monto Transacción es 'menor' al Monto Pendiente
    And el código de transacción es adecuado para el tipo de cuenta
    Then se activa el cobro en línea
    And la cuenta recibe un débito por el monto aplicado
    And la descripción de la transacción posee el numero de referencia de la partida pendiente
    And el monto pendiente disminuye en el módulo de cobros
    And el campo monto cobrado coincide con el monto del débito a la cuenta
    And el estado de la partida queda en "3"

  @Total
  Scenario: TESTC-2676: Cobro total de transacciones pendientes en castigo
    Given El usuario se autentica con sus credenciales
    And el cliente posee "transacciones" "castigadas"
    And la cuenta no posee balance actual
    When la cuenta recibe un 'crédito' cuyo Monto Transacción es 'mayor' al Monto Pendiente
    And el código de transacción es adecuado para el tipo de cuenta
    Then se activa el cobro en línea
    And la cuenta recibe un débito por el monto aplicado
    And la descripción de la transacción posee el numero de referencia de la partida pendiente
    And el monto pendiente queda en 0 en el módulo de cobros
    And el campo monto cobrado coincide con el monto del débito a la cuenta
    And el estado de la partida queda en "4"

  @Parcial
  Scenario: TESTC-2677: Cobro parcial de transacciones pendientes en castigo
    Given El usuario se autentica con sus credenciales
    And el cliente posee "transacciones" "castigadas"
    And la cuenta no posee balance actual
    When la cuenta recibe un 'crédito' cuyo Monto Transacción es 'menor' al Monto Pendiente
    And el código de transacción es adecuado para el tipo de cuenta
    Then se activa el cobro en línea
    And la cuenta recibe un débito por el monto aplicado
    And la descripción de la transacción posee el numero de referencia de la partida pendiente
    And el monto pendiente disminuye en el módulo de cobros
    And el campo monto cobrado coincide con el monto del débito a la cuenta
    And el estado de la partida queda en "7"

#
#  Respecto a los casos pendientes relacionados con los escenarios de transacciones en no acumulación,
#  Aquino nos comentó que el esfuerzo de automatización para estos casos no resulta viable, ya que los datos necesarios
#  deben extraerse directamente de producción y no contamos con una forma sencilla de generarlos en un entorno controlado.
#  una alternativa que cubriría únicamente el escenario de pago total;
#  sin embargo, consideramos que dicha solución podría impactar negativamente otros escenarios.
#  En este sentido, Aquino nos indicó que lo más adecuado sería documentar claramente las limitaciones técnicas
#  y justificar por qué no es posible automatizar estos casos de manera efectiva.

  @AuxiliaryScenarios
  Scenario: TESTC-2664 Cobro total de transacciones en no acumulacion
    Given El usuario se autentica con sus credenciales
    And el cliente posee "transacciones" "no acumulacion"
    And la cuenta no posee balance actual
    When la cuenta recibe un 'crédito' cuyo Monto Transacción es 'mayor' al Monto Pendiente
    And el código de transacción es adecuado para el tipo de cuenta
    Then se activa el cobro en línea
    And la cuenta recibe un débito por el monto aplicado
    And la descripción de la transacción posee el numero de referencia de la partida pendiente
    And el monto pendiente queda en 0 en el módulo de cobros
    And el campo monto cobrado coincide con el monto del débito a la cuenta
    And el estado de la partida queda en "4"

  @AuxiliaryScenarios
  Scenario:TESTC-2677 Cobro parcial de transacciones pendientes en no acumulacion
    Given El usuario se autentica con sus credenciales
    And el cliente posee "transacciones" "no acumulacion"
    And la cuenta no posee balance actual
    When la cuenta recibe un 'crédito' cuyo Monto Transacción es 'menor' al Monto Pendiente
    And el código de transacción es adecuado para el tipo de cuenta
    Then se activa el cobro en línea
    And la cuenta recibe un débito por el monto aplicado
    And la descripción de la transacción posee el numero de referencia de la partida pendiente
    And el monto pendiente disminuye en el módulo de cobros
    And el campo monto cobrado coincide con el monto del débito a la cuenta
    And el estado de la partida queda en "3"