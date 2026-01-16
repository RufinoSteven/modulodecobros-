package dataStorageModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa la deuda por comisiones, montos pendientes y detalles de una cuenta.
 */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class CommissionInfoAccount {
    private final String debt;
    private final String remainingAmount;
    private final String pendingAmount;
    private final String account;
    private final String currencyCode;
    private final String description;
}
