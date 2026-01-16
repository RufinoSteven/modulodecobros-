package dataStorageModel;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Representa un asiento de cuenta ACH individual.
 */
@Getter
@RequiredArgsConstructor
public class AccountEntry {
    private final String accountNumber;
    private final double amount;
    private final String beneficiary;
}
