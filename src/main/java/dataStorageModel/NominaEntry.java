package dataStorageModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
public class NominaEntry {
    private final String company;
    private final String companyID;
    private final String typeAccount;
    private final String accounts;
    private final double Amount;
    private final String date;
}
