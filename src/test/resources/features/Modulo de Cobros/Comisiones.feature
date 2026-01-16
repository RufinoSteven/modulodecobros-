@Regression @Cobros @Comisiones
Feature: Cobro de comisiones pendientes
  Como usuario del sistema,
  Quiero que las comisiones pendientes, castigadas o en no acumulación
  se cobren automáticamente cuando la cuenta reciba un crédito,
  Para asegurar que las deudas sean saldadas correctamente y
  el estado de las partidas se actualice de forma adecuada.

  @Total
  Scenario: TESTC-2662: Cobro total de comisiones pendientes
    Given El usuario se autentica con sus credenciales
    And el cliente posee "comisiones" "pendientes"
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
  Scenario: TESTC-2663: Cobro parcial de comisiones pendientes
    Given El usuario se autentica con sus credenciales
    And el cliente posee "comisiones" "pendientes"
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
  Scenario: TESTC-2672: Cobro total de comisiones castigadas
    Given El usuario se autentica con sus credenciales
    And el cliente posee "comisiones" "castigadas"
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
  Scenario: TESTC-2672: Cobro parcial de comisiones castigadas
    Given El usuario se autentica con sus credenciales
    And el cliente posee "comisiones" "castigadas"
    And la cuenta no posee balance actual
    When la cuenta recibe un 'crédito' cuyo Monto Transacción es 'menor' al Monto Pendiente
    And el código de transacción es adecuado para el tipo de cuenta
    Then se activa el cobro en línea
    And la cuenta recibe un débito por el monto aplicado
    And la descripción de la transacción posee el numero de referencia de la partida pendiente
    And el monto pendiente disminuye en el módulo de cobros
    And el campo monto cobrado coincide con el monto del débito a la cuenta
    And el estado de la partida queda en "7"

  @Total
  Scenario: TESTC-2671: Cobro total de comisiones pendientes en no acumulacion
    Given El usuario se autentica con sus credenciales
    And el cliente posee "comisiones" "no acumulacion"
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
  Scenario: TESTC-2674: Cobro parcial de comisiones pendientes en no acumulacion
    Given El usuario se autentica con sus credenciales
    And el cliente posee "comisiones" "no acumulacion"
    And la cuenta no posee balance actual
    When la cuenta recibe un 'crédito' cuyo Monto Transacción es 'menor' al Monto Pendiente
    And el código de transacción es adecuado para el tipo de cuenta
    Then se activa el cobro en línea
    And la cuenta recibe un débito por el monto aplicado
    And la descripción de la transacción posee el numero de referencia de la partida pendiente
    And el monto pendiente disminuye en el módulo de cobros
    And el campo monto cobrado coincide con el monto del débito a la cuenta
    And el estado de la partida queda en "6"
