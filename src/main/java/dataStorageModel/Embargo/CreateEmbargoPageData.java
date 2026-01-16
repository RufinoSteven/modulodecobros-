package dataStorageModel.Embargo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateEmbargoPageData {
    private static final String OPERATION_TYPE = "operationType",
            IDENTIFICATION_TYPE = "identificationType",
            IDENTIFICATION_NUMBER = "identificationNumber",
            USER_OFFICE_NUMBER = "userOffice";
    private String operatorType;
    private String identificationType;
    private String identificationNumber;
    private String userOfficeNumber;
    private String lastCreatedOperationNumber;
}
