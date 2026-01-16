package dataStorageModel;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ACHExtractDTO {
    private List<dataStorageModel.AccountEntry> accounts;
    private List<AmountEntry> transactionAmount;
    private double subtotal;
    private double total;
    private boolean foundData;
    private List<String> accountsDebited;
    private List<RegionalAccount> regionalAccounts;
} 