package dataStorageModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa la información de una cuenta, incluyendo su tipo, estado y saldo actual.
 */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class AccounInfo {
    private final String accountType;
    private final String accountStatus;
    private final String accountCurrent;

}
