package dataStorageModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa el saldo detallado y la información de identificación de una cuenta.
 */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class AccountBalanceId {
    private final String idIdentification;
    private final String account_Number;
    private final String account_Type;
    private final String current_Balance;
    private final String available_Today;
    private final String currency_code;
    private final String RNC;
}
