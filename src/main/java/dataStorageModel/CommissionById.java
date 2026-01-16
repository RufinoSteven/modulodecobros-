package dataStorageModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CommissionById {
    private final String status;
    private final String description;
    private final String type_ofCharge;
    private final String commission_pending;
}
