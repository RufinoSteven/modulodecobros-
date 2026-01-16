package dataStorageModel;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Representa los datos ACH extraídos que contienen asientos de cuenta, subtotales y montos totales.
 */
@Getter
@RequiredArgsConstructor
public class ACHData {
    private final List<AccountEntry> accounts;
    private final List<AmountEntry> transactionAmount;
    private final double subtotal;
    private final double total;
    private final List<String> accountsDebited;
    private final List<RegionalAccount> regionalAccounts;
}