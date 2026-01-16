package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountConsultOption {
    BASIC_INFORMATION("01"),
    CURRENT_ACCOUNT_STATUS("04");
    private final String code;
}

