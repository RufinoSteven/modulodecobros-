package actions.dbActions;

import dataStorageModel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ExtentReportManager;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static fixtures.TableFields.*;

/**
 * Gestiona la recuperación de detalles relacionados con las comisiones.
 */
public class CommissionActions {
    private static final Logger log = LogManager.getLogger(CommissionActions.class);

    /**
     * Recupera la información de la comisión basada en el ID de la comisión proporcionado.
     *
     * @param commissionId El identificador de referencia de la comisión.
     * @return Un objeto {@link CommissionInfo} que contiene la deuda, el importe restante y el importe pendiente,
     * o {@code null} si se produce un error o no se encuentran datos.
     */
    public CommissionInfo getCommissionInfo(String commissionId) {
        try {
            Object result = new EntriesActions().getCommissionDebtAndPendingAmountById(commissionId);
            if (result instanceof List && !((List<?>) result).isEmpty()) {
                Map<?, ?> data = (Map<?, ?>) ((List<?>) result).getFirst();
                return new CommissionInfo(
                        getStringFromMap(data, C10VACOOR.toString()),
                        getStringFromMap(data, C10VALCOB.toString()),
                        getStringFromMap(data, C10VACOPE.toString())
                );
            }
        } catch (Exception e) {
            log.error("Error al obtener la información de la comisión", e);
        }
        return null;
    }

    /**
     * Recupera la información de la comisión basada en el ID de la comisión proporcionado.
     *
     * @param Id_Document El identificador de referencia de la comisión.
     * @return Un objeto {@link CommissionById} que contiene el estado, la descripción, el tipo de cargo y el importe del cargo.
     * O {@code null} si se produce un error o no se encuentran datos.
     */
    public CommissionById getCommissionById(String Id_Document) {
        ExtentReportManager.logMessage = "Referencia" + Id_Document;
        List<Map<String, Object>> result = (List<Map<String, Object>>) new EntriesActions().getCommissionById(Id_Document);
        if (result.isEmpty()) {
            return null;
        }
        Map<String, Object> data = result.getFirst();
        ExtentReportManager.logMessage = "Estado: " + C10ESTCOB + "Descripción: " + C10DESCRI
                + "Tipo: " + C10TIPCOB + "Importe: " + C10VALCOB;
        return new CommissionById(
                (String) data.get(C10ESTCOB.toString()),
                (String) data.get(C10DESCRI.toString()),
                (String) data.get(C10TIPCOB.toString()),
                (String) data.get(C10VALCOB.toString())
        );
    }


    /**
     * Recupera los detalles del saldo de la cuenta para una identificación dada.
     * <p>
     * Este método invoca el método `getIdentificationAccountBalance` para obtener los datos de la cuenta,
     * esperando una lista de resultados. Extrae el primer registro de la lista, mapea los campos relevantes,
     * y los devuelve encapsulados en un objeto {@link AccountBalanceId}.
     *
     * @param limit el número máximo de registros a recuperar
     * @param identificationTypeInput el tipo de identificación a buscar
     * @return un objeto {@link AccountBalanceId} que contiene la identificación, el número de cuenta, el saldo actual,
     * y el saldo disponible de hoy; o {@code null} si no se encuentran datos o se produce un error
     */


    public AccountBalanceId getIdIdentificationBalanceAccount(Number limit, String identificationTypeInput) {
        try {
            Object result = null;
            if (identificationTypeInput.equals("6")) {
                result = new EntriesActions().getIdentificationAccountBalance(limit);
            } else if (identificationTypeInput.equals("3")) {
                result = new EntriesActions().getIdentificationAccountBalanceRNC(limit);
            }

            if (result instanceof List<?> dataList && !dataList.isEmpty()) {
                int randomIndex = new Random().nextInt(dataList.size());
                Map<?, ?> data = (Map<?, ?>) dataList.get(randomIndex);

                return new AccountBalanceId(
                        getStringFromMap(data, NATIONAL_ID_NUMBER.toString()),
                        getStringFromMap(data, ACCOUNT_NBR.toString()),
                        getStringFromMap(data, ACCOUNT_TYPE.toString()),
                        getStringFromMap(data, CURRENT_BALANCE.toString()),
                        getStringFromMap(data, DISPONIBLE_HOY.toString()),
                        getStringFromMap(data, CODIGO_MONEDA.toString()),
                        getStringFromMap(data, TAX_ID_NUMBER.toString())
                );
            }
        } catch (Exception e) {
            log.error("Error al obtener el ID del saldo de la cuenta: {}", e.getMessage());
        }
        return null;
    }


    /**
     * Recupera de forma segura un valor de un mapa y lo convierte en una cadena.
     *
     * @param data El mapa de datos que contiene los detalles de la comisión.
     * @param key  La clave para recuperar el valor.
     * @return La representación en cadena del valor, o "0" si el valor es nulo.
     */
    private String getStringFromMap(Map<?, ?> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : "0";
    }


    public CommissionInfoAccount getCollectionThadOnlyAccount(String accoundId) {
        try {
            EntriesActions entriesActions = new EntriesActions();
            Object result = entriesActions.getComssionPendingThadOnlyAccount(accoundId);
            log.info("Tipo de resultado: {}", result != null ? result.getClass().getName() : "nulo");
            log.info("Contenido del resultado: {}", result);
            if (result instanceof List && !((List<?>) result).isEmpty()) {
                Map<?, ?> data = (Map<?, ?>) ((List<?>) result).getFirst();
                return new CommissionInfoAccount(
                        getStringFromMap(data, C10VACOOR.toString()),
                        getStringFromMap(data, C10VALCOB.toString()),
                        getStringFromMap(data, C10VACOPE.toString()),
                        getStringFromMap(data, ACCOUNT_NBR.toString()),
                        getStringFromMap(data, CURRENCY_CODE.toString()),
                        getStringFromMap(data, C10DESCRI.toString())
                );
            }
        } catch (Exception e) {
            log.error("Error al obtener la información de la comisión: {}", e.getMessage());
        }
        return null;
    }

    public AccounInfo getAccountBalanceAndTypeAccount(String accoundId) {
        try {
            EntriesActions entriesActions = new EntriesActions();
            Object result = entriesActions.getAccountBalanceAndTypeAccount(accoundId);
            log.info("Tipo de resultado: {}", result != null ? result.getClass().getName() : "nulo");
            log.info("Contenido del resultado: {}", result);
            if (result instanceof List && !((List<?>) result).isEmpty()) {
                Map<?, ?> data = (Map<?, ?>) ((List<?>) result).getFirst();
                return new AccounInfo(
                        getStringFromMap(data, ACCOUNT_TYPE.toString()),
                        getStringFromMap(data, ACCOUNT_STATUS.toString()),
                        getStringFromMap(data, CURRENT_BALANCE.toString())
                );
            }
        } catch (Exception e) {
            log.error("Error al obtener la información de la comisión: {}", e.getMessage());
        }
        return null;
    }


    public AccountBalanceId getIdIdentificationBalanceAccountByCurrencyCodeUSD(String codeCurrency) {
        try {
            Object result = new EntriesActions().getIdentificacionAccountBalanceByCurrencyCodeUSD(codeCurrency);
            log.info(result);
            if (result instanceof List<?> dataList && !dataList.isEmpty()) {
                int randomIndex = new Random().nextInt(dataList.size());
                Map<?, ?> data = (Map<?, ?>) dataList.get(randomIndex);

                return new AccountBalanceId(
                        getStringFromMap(data, NATIONAL_ID_NUMBER.toString()),
                        getStringFromMap(data, ACCOUNT_NBR.toString()),
                        getStringFromMap(data, ACCOUNT_TYPE.toString()),
                        getStringFromMap(data, CURRENT_BALANCE.toString()),
                        getStringFromMap(data, DISPONIBLE_HOY.toString()),
                        getStringFromMap(data, CODIGO_MONEDA.toString()),
                        getStringFromMap(data, TAX_ID_NUMBER.toString())
                );
            }
        } catch (Exception e) {
            log.error("Error al obtener el ID del saldo de la cuenta: {}", e.getMessage());
        }
        return null;
    }

    public AccountBalanceId getidIdentificationBalanceAccountByCurrencyCodeEUR(String codeCurrency) {
        try {
            Object result = new EntriesActions().getIdentificacionAccountBalanceByCurrencyCodeEUR(codeCurrency);
            log.info(result);
            if (result instanceof List<?> dataList && !dataList.isEmpty()) {
                int randomIndex = new Random().nextInt(dataList.size());
                Map<?, ?> data = (Map<?, ?>) dataList.get(randomIndex);

                return new AccountBalanceId(
                        getStringFromMap(data, NATIONAL_ID_NUMBER.toString()),
                        getStringFromMap(data, ACCOUNT_NBR.toString()),
                        getStringFromMap(data, ACCOUNT_TYPE.toString()),
                        getStringFromMap(data, CURRENT_BALANCE.toString()),
                        getStringFromMap(data, DISPONIBLE_HOY.toString()),
                        getStringFromMap(data, CODIGO_MONEDA.toString()),
                        getStringFromMap(data, TAX_ID_NUMBER.toString())
                );
            }
        } catch (Exception e) {
            log.error("Error al obtener el ID del saldo de la cuenta: {}", e.getMessage());
        }
        return null;
    }

    public AccountBalanceId getIdIdentificationBalanceAccountByCurrencyCode(String codeCurrency) {
        try {
            Object result = new EntriesActions().getIdentificationAccountBalanceByCurrencyCode(codeCurrency);
            if (result instanceof List<?> dataList && !dataList.isEmpty()) {
                int randomIndex = new Random().nextInt(dataList.size());
                Map<?, ?> data = (Map<?, ?>) dataList.get(randomIndex);

                return new AccountBalanceId(
                        getStringFromMap(data, NATIONAL_ID_NUMBER.toString()),
                        getStringFromMap(data, ACCOUNT_NBR.toString()),
                        getStringFromMap(data, ACCOUNT_TYPE.toString()),
                        getStringFromMap(data, CURRENT_BALANCE.toString()),
                        getStringFromMap(data, DISPONIBLE_HOY.toString()),
                        getStringFromMap(data, CODIGO_MONEDA.toString()),
                        getStringFromMap(data, TAX_ID_NUMBER.toString())
                );
            }
        } catch (Exception e) {
            log.error("Error al obtener el ID del saldo de la cuenta: {}", e.getMessage());
        }
        return null;
    }

    public AccountBalanceId getCuentaAhorroSinSaldo() {
        EntriesActions entriesActions = new EntriesActions();
        Object result = entriesActions.getSavingUnbalancedAccounts();
        if (result instanceof List<?> && !((List<?>) result).isEmpty()) {
            Map<String, Object> cuenta = (Map<String, Object>) ((List<?>) result).get(1);
            return mapToAccountBalanceId(cuenta);
        }
        return null;
    }

    public AccountBalanceId getCuentaCorrienteSinSaldo() {
        EntriesActions entriesActions = new EntriesActions();
        Object result = entriesActions.geCurrentsUnbalancedAccount();
        if (result instanceof List<?> && !((List<?>) result).isEmpty()) {
            Map<String, Object> cuenta = (Map<String, Object>) ((List<?>) result).get(1);
            return mapToAccountBalanceId(cuenta);
        }
        return null;
    }

    private AccountBalanceId mapToAccountBalanceId(Map<String, Object> cuenta) {
        if (cuenta == null) return null;
        return new AccountBalanceId(
                String.valueOf(cuenta.getOrDefault("NATIONAL_ID_NUMBER", "")).trim(),
                String.valueOf(cuenta.getOrDefault("ACCOUNT_NBR", "")),
                String.valueOf(cuenta.getOrDefault("ACCOUNT_TYPE", "")),
                String.valueOf(cuenta.getOrDefault("CURRENT_BALANCE", "")),
                String.valueOf(cuenta.getOrDefault("CURRENT_BALANCE", "")),
                String.valueOf(cuenta.getOrDefault("CURRENCY_CODE", "")),
                ""
        );
    }
}

