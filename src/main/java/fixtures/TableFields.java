package fixtures;

public enum TableFields {

    /**
     * Campo del valor original del cobro.
     */
    C10VACOOR,

    /**
     * Campo del valor cobrado.
     */
    C10VALCOB,

    /**
     * Campo del valor pendiente de cobro.
     */
    C10VACOPE,
    /**
     * Representa el número de cuenta asociado al cliente.
     */
    ACCOUNT_NBR,
    /**
     * Campo del valor del estado.
     */
    C10ESTCOB,
    /**
     * Campo del valor del tipo de cobro.
     */
    C10TIPCOB,
    /**
     * Representa el tipo de cuenta (ahorros, corriente, etc.).
     */
    ACCOUNT_TYPE,
    /**
     * Representa el estado actual de la cuenta (activa, inactiva, etc.).
     */
    ACCOUNT_STATUS,
    /**
     * Representa el saldo actual de la cuenta.
     */
    CURRENT_BALANCE,
    /**
     * Representa el código de la moneda de la cuenta (DOP, USD, EUR.).
     */
    CURRENCY_CODE,
    /**
     * Representa la descripción del cobro.
     */
    C10DESCRI,

    /**
     * Representa el monto disponible en la cuenta a día de hoy.
     */
    DISPONIBLE_HOY,

    /**
     * Representa el número o documento de identificación del cliente.
     */
    NATIONAL_ID_NUMBER,
    /**
     * Representa el código de la moneda de la cuenta.
     */
    CODIGO_MONEDA,

    /**
     * Representa el RNC asociado al cliente.
     */
    TAX_ID_NUMBER


}
