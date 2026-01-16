package dataStorageModel.Embargo;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmbargoPageData {
    private String embargoNumber;
    private String actNumber;
    private String requesterIdType;
    private String requesterIdNumber;
    private String requesterFirstName;
    private String requesterLastName;
    private String requesterSocialReason;
    private String attorneyIdType;
    private String attorneyIdNumber;
    private String attorneyFirstName;
    private String attorneyLastName;
    private String operationAmount;
    private String previousOperationNumber;
    private String previousOperationNumberClose;
    private String previousOperationNumberLifting;
    private String lastOperationAmount;
    private String accountType = "";
    private String accountNumber;
    private String accountTypeBefore = "";
    private String accountNumberBefore;
    private String insuranceCompany;
    private String policyCode;

}
