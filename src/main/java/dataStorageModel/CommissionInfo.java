package dataStorageModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa la deuda relacionada con comisiones y los montos pendientes.
 */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class CommissionInfo {
    private final String debt;
    private final String remainingAmount;
    private final String pendingAmount;
}
