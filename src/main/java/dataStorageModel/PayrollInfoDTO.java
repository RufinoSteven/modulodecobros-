package dataStorageModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Representa los datos extraídos de la Nómina que contienen la Compañía, ID de la compañía, tipo de cuenta, cuentas y monto.
 */
@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class PayrollInfoDTO {
    private final List<NominaEntry> payrollInfo;
}