package dataStorageModel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.DecimalFormat;

/**
 * Representa un asiento de monto ACH individual.
 */
@Getter
@RequiredArgsConstructor
public class AmountEntry {
    private final double transactionAmounts;
}
