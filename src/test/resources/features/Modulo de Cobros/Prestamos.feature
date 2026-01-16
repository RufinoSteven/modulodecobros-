@Regression @Cobros @Prestamos
Feature: Cobro de cuota pendiente de préstamo.

  @Total
  Scenario: TESTC-2648 Cobro total de una factura de préstamo pendiente
    Given El usuario se autentica con sus credenciales
    And el cliente posee "prestamos" "pendientes"
    And la cuenta no posee balance actual
    And se muestran los detalles del préstamo y se prepara el pago
    When la cuenta recibe un 'crédito' cuyo Monto Transacción es 'igual' al Monto Pendiente
    And el código de transacción es adecuado para el tipo de cuenta
    Then se activa el cobro en línea
    And la cuenta recibe un débito por el monto aplicado
    And la descripción de la transacción posee el numero de referencia de la partida pendiente
    And el monto pendiente queda en 0 en el módulo de cobros
    And el campo monto cobrado coincide con el monto del débito a la cuenta
    And el estado de la partida queda en "4"
    And el pago vencido del prestamo se muestra en cero

  @Parcial
  Scenario: TESTC-2649 Cobro parcial de una factura de préstamo pendiente
    Given El usuario se autentica con sus credenciales
    And el cliente posee "prestamos" "pendientes"
    And la cuenta no posee balance actual
    And se muestran los detalles del préstamo antes de hacer el pago parcial
    When la cuenta recibe un 'crédito' cuyo Monto Transacción es 'menor' al Monto Pendiente
    And el código de transacción es adecuado para el tipo de cuenta
    Then se activa el cobro en línea
    And la cuenta recibe un débito por el monto aplicado
    And la descripción de la transacción posee el numero de referencia de la partida pendiente
    And el monto pendiente disminuye en el módulo de cobros
    And el campo monto cobrado coincide con el monto del débito a la cuenta
    And el estado de la partida queda en "3"
    And se muestra los detalles del préstamo tras el pago parcial
