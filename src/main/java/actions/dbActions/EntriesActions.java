package actions.dbActions;

import com.banreservas.core.Broker;
import com.banreservas.core.Core;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class EntriesActions {
    private static final Logger log = LogManager.getLogger(EntriesActions.class);


    /**
     * Obtiene los asientos pendientes según los parámetros proporcionados.
     * <p>
     * Este método realiza una consulta a la base de datos utilizando la función
     * {@code Broker.fetchRows} para recuperar los asientos pendientes asociados a un tipo de cuenta,
     * estado, tipo de asiento pendiente y una cantidad específica. En caso de error,
     * la excepción es capturada y se imprime un mensaje de error en la consola, devolviendo {@code null}.
     *
     * @param accountType        El tipo de cuenta (por ejemplo, "1=ahorros", "6=corriente").
     * @param accountStatus      El estado de la cuenta (por ejemplo, "1=Activa", "3=DB no permitido", "5=Inactiva", "6=Fallecido", "4=Cerrada", "7=Cerrada para posteo").
     * @param pendingEntriesType El tipo de asientos pendientes (por ejemplo, "0=préstamos especiales", "2=préstamos", "3=comisiones", "4 y 5=cuentas por cobrar o transacciones").
     * @param numberOfItems      El número de registros a mostrar. Por ejemplo, 1, 2 o 10.
     * @return Un objeto que contiene los asientos pendientes recuperados de la base de datos, o
     * {@code null} si ocurre un error durante la ejecución del método.
     */
    public Object getPendingEntries
    (String accountType, String accountStatus, String pendingEntriesType, String numberOfItems) {
        try {
            return Broker.fetchRows("QryPendingEntries", Core.SIGNATURE, accountType, accountStatus, pendingEntriesType, numberOfItems);
        } catch (Exception e) {
            log.error("Error al obtener los asientos pendientes", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene el saldo actual de una cuenta asociada a un número de identificación nacional.
     *
     * <p>
     * Este método realiza una consulta para obtener el saldo de la cuenta utilizando un número de identificación nacional
     * (como una cédula de identidad, pasaporte o número de identificación fiscal)
     * y devuelve el resultado correspondiente.
     *
     * @param nationalIdNumber El número de identificación nacional utilizado para buscar la cuenta.
     *                         Puede ser una cédula, pasaporte o cualquier otro identificador único
     *                         para la persona.
     * @return Un objeto que representa el saldo de la cuenta asociado con el número de identificación proporcionado.
     * Si ocurre un error, el método devuelve null.
     */
    public Object getRealAccountBalanceById(String nationalIdNumber) {
        try {
            return Broker.fetchRows("QryCustomerFinancialSummary", Core.SIGNATURE, nationalIdNumber);
        } catch (Exception e) {
            log.error("Error al obtener el saldo de la cuenta", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Recupera una lista de cuentas CANDIDATAS usando QryCandidateGenerator.
     * La consulta maneja el filtrado por pendingItemType y statusItem.
     *
     * @param pendingItemType Tipo de cargo (p.ej., 3 para comisiones).
     * @param statusItem      Estado del ítem (p.ej., 1 para pendiente).
     * @param limit           Número máximo de cuentas candidatas a obtener.
     * @return Lista de mapas, cada uno representando un ítem de cargo candidato.
     */
    public List<Map<String, Object>> getCandidateAccountsList(Number pendingItemType, Number statusItem, Number limit) {
        try {
            log.debug("Ejecutando QryCandidateGenerator con tipo: {}, estado: {}, límite: {}", pendingItemType, statusItem, limit);
            List<Map<String, Object>> results = Broker.fetchRows("QryCandidateGenerator", Core.SIGNATURE,
                    pendingItemType, statusItem, limit);
            if (results == null) return Collections.emptyList();
            log.debug("QryCandidateGenerator devolvió {} candidatos.", results.size());
            return results;
        } catch (Exception e) {
            log.error("Error en getCandidateAccountsList para el tipo: {}, estado: {}: {}", pendingItemType, statusItem, e.getMessage(), e);
            // Considerar si relanzar una excepción específica de la aplicación en lugar de devolver lista vacía
            // o si el manejo actual de devolver lista vacía es el comportamiento deseado.
            return Collections.emptyList();
        }
    }

    /**
     * Recupera los detalles del primer cargo real para una cuenta dada.
     * Asume que QryGetAccountActualFirstChargeDetails tiene "FETCH FIRST 1 ROW ONLY" codificado.
     *
     * @param accountNumber El número de cuenta.
     * @return Un mapa que contiene los detalles del primer cargo, o nulo si no se encuentra o se produce un error.
     */
    public Map<String, Object> getActualFirstChargeDetailsForAccount(String accountNumber) {
        try {
            log.debug("Ejecutando QryGetAccountActualFirstChargeDetails para la cuenta: {}", accountNumber);
            List<Map<String, Object>> results = Broker.fetchRows("QryGetAccountActualFirstChargeDetails", Core.SIGNATURE,
                    accountNumber); // Solo un parámetro: accountNumber

            if (results != null && !results.isEmpty()) {
                log.debug("QryGetAccountActualFirstChargeDetails encontró detalles para la cuenta {}.", accountNumber);
                return results.getFirst();
            }
            log.warn("QryGetAccountActualFirstChargeDetails no encontró detalles para la cuenta {}.", accountNumber);
            return null;
        } catch (Exception e) {
            log.error("Error en getActualFirstChargeDetailsForAccount para la cuenta {}: {}", accountNumber, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Cuenta el número total de cargos (independientemente del tipo/estado) para una cuenta determinada.
     *
     * @param accountNumber El número de cuenta.
     * @return El recuento total de cargos, o -1 si se produce un error o no se encuentra ningún recuento.
     */
    public int getTotalChargesInAccount(String accountNumber) {
        try {
            log.debug("Ejecutando QryCountTotalChargesInAccount para la cuenta: {}", accountNumber);
            List<Map<String, Object>> results = Broker.fetchRows("QryCountTotalChargesInAccount", Core.SIGNATURE,
                    accountNumber);

            if (results != null && !results.isEmpty()) {
                Map<String, Object> resultMap = results.getFirst();
                // El nombre de la columna del COUNT(*) debe coincidir con el alias en la query
                Object countObj = resultMap.get("TOTAL_CHARGE_COUNT");
                if (countObj instanceof Number) {
                    int count = ((Number) countObj).intValue();
                    log.debug("QryCountTotalChargesInAccount para la cuenta {} devolvió un recuento de: {}.", accountNumber, count);
                    return count;
                } else {
                    log.error("TOTAL_CHARGE_COUNT no es un número para la cuenta {}. Valor: {}", accountNumber, countObj);
                }
            } else {
                log.warn("QryCountTotalChargesInAccount no devolvió resultados para la cuenta {}. Asumiendo 0 cargos o error.", accountNumber);
                // Si no hay resultados, podría ser 0 o un error. Devolver 0 si es un conteo válido.
                // Si es un error, -1 es apropiado. Depende de cómo Broker.fetchRows maneje un COUNT vacío.
                // Asumamos que si no hay filas es 0, si hay error de query es -1.
                // Si la query de COUNT(*) siempre devuelve una fila (incluso con 0), entonces este else es menos probable.
            }
        } catch (Exception e) {
            log.error("Error en getTotalChargesInAccount para la cuenta {}: {}", accountNumber, e.getMessage(), e);
        }
        return -1; // Indicar error o incapacidad de determinar el conteo
    }

    /**
     * Recupera una lista de clientes con comisiones pendientes.
     *
     * @param limit           El número máximo de registros a obtener de la base de datos
     * @param pendingItemType se refiere al tipo de cargo (comisión, transacción o préstamo).
     * @param statusItem      El estado del ítem.
     * @return Objeto que contiene una lista de comisiones de clientes o una lista vacía si no se encuentran resultados
     */
    public Object getClientsWithPendingCommissions(Number pendingItemType, Number statusItem, Number limit) {
        try {
            List<Map<String, Object>> results = Broker.fetchRows("QryPendingComissions", Core.SIGNATURE, pendingItemType, statusItem, limit);
            return results.stream().map(row -> {
                Map<String, String> commission = new HashMap<>();
                commission.put("ClientId", String.valueOf(row.get("IDENTIFICACION")));
                commission.put("Account", String.valueOf(row.get("ACCOUNT_NBR")));
                commission.put("AccountCurrency", String.valueOf(row.get("CURRENCY_CODE")));
                commission.put("OriginalAmount", String.valueOf(row.get("C10VACOOR")).trim());
                commission.put("PendingAmount", String.valueOf(row.get("C10VACOPE")));
                commission.put("CollectedAmount", String.valueOf(row.get("C10VALCOB")));
                commission.put("CollectionValue", String.valueOf(row.get("C10TIPCOB")));
                commission.put("CollectionId", String.valueOf(row.get("C10NUMPROD")));
                commission.put("CollectionDescription", String.valueOf(row.get("C10DESCRI")));

                return commission;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener las comisiones pendientes: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Object getCollectionById(String collectionId, Number limit) {
        try {
            List<Map<String, Object>> results = Broker.fetchRows("QryGetCollectionById", Core.SIGNATURE, collectionId, limit);
            return results.stream().map(row -> {
                Map<String, String> commission = new HashMap<>();
                commission.put("ClientId", String.valueOf(row.get("IDENTIFICACION")).trim());
                commission.put("Account", String.valueOf(row.get("ACCOUNT_NBR")).trim());
                commission.put("AccountCurrency", String.valueOf(row.get("CURRENCY_CODE")).trim());
                commission.put("OriginalAmount", String.valueOf(row.get("C10VACOOR")).trim());
                commission.put("PendingAmount", String.valueOf(row.get("C10VACOPE")).trim());
                commission.put("CollectedAmount", String.valueOf(row.get("C10VALCOB")).trim());
                commission.put("CollectionValue", String.valueOf(row.get("C10TIPCOB")).trim());
                commission.put("CollectionId", String.valueOf(row.get("C10NUMPROD")).trim());
                commission.put("CollectionStatus", String.valueOf(row.get("C10ESTCOB")).trim());

                return commission;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener las comisiones pendientes: {}", e.getMessage());
            return Collections.emptyList();
        }
    }


    /**
     * Recupera la deuda de la comisión y el importe pendiente asociado a un ID de referencia de la comisión.
     *
     * @param idReferenceCommission Identificador de referencia de la comisión.
     * @return Un objeto que contiene la información de la deuda de la comisión y el importe pendiente,
     * o {@code null} si se produce un error durante la consulta.
     */
    public Object getCommissionDebtAndPendingAmountById(String idReferenceCommission) {
        try {
            return Broker.fetchRows("QryCommissionDebtAndPendingAmountById", Core.SIGNATURE, idReferenceCommission);
        } catch (Exception e) {
            log.error("Error al obtener las comisiones pendientes:", e);
            throw new RuntimeException(e);
        }
    }

    public Object getCommissionById(String id_Document) {
        try {
            return Broker.fetchRows("#QryGetComissionById", Core.SIGNATURE, id_Document);
        } catch (Exception e) {
            log.error("Error al obtener las comisiones pendientes:", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Recupera los asientos de transacciones de la cuenta según los parámetros especificados.
     * <p>
     * Este método obtiene los asientos de transacciones detallados para una cuenta específica,
     * filtrados por tipo de cuenta, fecha de transacción y limitados por un número de filas.
     *
     * @param accountNumber   El número de cuenta para el que se recuperarán las transacciones.
     * @param transactionDate La fecha específica de las transacciones en formato YYYY/MM/DD.
     * @return Un objeto que contiene los asientos de las transacciones o nulo si se produce un error.
     */
    public Object getAccountTransactionEntries(String accountNumber, String transactionDate) {
        try {
            return Broker.fetchRows("QryAccountTransactionEntries", Core.SIGNATURE,
                    accountNumber, transactionDate);
        } catch (Exception e) {
            log.error("Error al obtener las transacciones del cliente: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Recupera las transacciones diarias de una cuenta específica.
     *
     * @param accountNumber El número de cuenta a consultar.
     * @return Un objeto conteniendo las transacciones diarias o null si ocurre un error.
     */
    public Object getAccountDailyTransactions(String accountNumber) {
        try {
            return Broker.fetchRows("QryAccountTransactionDailyEntries", Core.SIGNATURE, accountNumber);
        } catch (Exception e) {
            log.error("Error obteniendo transacciones diarias: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Recupera el saldo actual y el tipo de cuenta para una cuenta específica.
     *
     * @param accountNumber El número de cuenta a consultar.
     * @return Un objeto que contiene el saldo y el tipo de cuenta, o nulo si se produce un error.
     */
    public Object getAccountBalanceAndTypeAccount(String accountNumber) {
        try {
            return Broker.fetchRows("QryGetAccountBalanceAndTypeAccount", Core.SIGNATURE, accountNumber);
        } catch (Exception e) {
            log.error("Error al obtener las transacciones del cliente: {}", e.getMessage());
            return null;
        }
    }


    /**
     * Recupera la comisión pendiente para las cuentas que cumplen con criterios específicos.
     *
     * @param accoundId El ID de la cuenta.
     * @return Un objeto que contiene los datos de la comisión pendiente, o nulo si se produce un error.
     */
    public Object getComssionPendingThadOnlyAccount(String accoundId) {
        try {
            return Broker.fetchRows("QryGetComssionPendingThadOnlyAccount", Core.SIGNATURE, accoundId);
        } catch (Exception e) {
            log.error("Error al obtener las transacciones del cliente: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Recupera una cuenta de ahorros.
     *
     * @return Objeto que contiene una lista de cuentas de ahorro encontrada aleatoriamente
     */
    public Object getSavingAccountNumber() {
        try {
            List<Map<String, Object>> results = Broker.fetchRows("QryGetSavingAccountNumber", Core.SIGNATURE);
            return results.stream()
                    .map(row -> {
                        Map<String, String> savingsAccount = new HashMap<>();
                        savingsAccount.put("AccountNumber", String.valueOf(row.get("ACCOUNT_NBR")));
                        return savingsAccount;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener la cuenta de ahorros pendiente: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Recupera una cuenta corriente.
     *
     * @return Objeto que contiene una lista de cuentas corrientes encontrada aleatoriamente
     */
    public Object getCurrentAccountNumber() {
        try {
            List<Map<String, Object>> results = Broker.fetchRows("QryGetCurrentAccountNumber", Core.SIGNATURE);
            return results.stream()
                    .map(row -> {
                        Map<String, String> currentAccount = new HashMap<>();
                        currentAccount.put("AccountNumber", String.valueOf(row.get("ACCOUNT_NBR")));
                        return currentAccount;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener la cuenta corriente pendiente: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Recupera cuentas de ahorro desbalanceadas.
     *
     * @return Objeto que contiene una lista de cuentas de ahorro desbalanceadas encontrada aleatoriamente
     */
    public Object getSavingUnbalancedAccount() {
        try {
            List<Map<String, Object>> results = Broker.fetchRows("QryGetSavingUnbalancedAccount", Core.SIGNATURE);
            return results.stream()
                    .map(row -> {
                        Map<String, String> savingUnbalancedAccount = new HashMap<>();
                        savingUnbalancedAccount.put("AccountNumber", String.valueOf(row.get("ACCOUNT_NBR")));
                        return savingUnbalancedAccount;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener la cuenta de ahorros pendiente: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Recupera cuentas corrientes desbalanceadas.
     *
     * @return Objeto que contiene una lista de cuentas corrientes desbalanceadas encontrada aleatoriamente
     */
    public Object geCurrentsUnbalancedAccount() {
        try {
            List<Map<String, Object>> results = Broker.fetchRows("QryGetCurrentsUnbalancedAccount", Core.SIGNATURE);
            return results.stream()
                    .map(row -> {
                        Map<String, String> currentsUnbalancedAccount = new HashMap<>();
                        currentsUnbalancedAccount.put("AccountNumber", String.valueOf(row.get("ACCOUNT_NBR")));
                        return currentsUnbalancedAccount;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener la cuenta corriente pendiente: {}", e.getMessage());
            return Collections.emptyList();
        }
    }


    /**
     * Recupera la información del saldo de la cuenta basada en la identificación, con un límite opcional en el número de registros.
     * <p>
     * Este método llama a la consulta "QRY_Identificacion_Account_Balance" a través del Broker,
     * utilizando una firma predefinida y un límite especificado para controlar el número de filas devueltas.
     *
     * @param limit el número máximo de registros a recuperar
     * @return el resultado de la consulta, típicamente una lista de filas o una estructura de datos que contiene la información de la cuenta
     * @throws RuntimeException si se produce un error durante la operación de obtención
     */
    public Object getIdentificationAccountBalance(Number limit) {
        try {
            return Broker.fetchRows("QRY_Identificacion_Account_Balance", Core.SIGNATURE, limit);
        } catch (Exception e) {
            log.error("Error:", e);
            throw new RuntimeException(e);
        }
    }

    public Object getIdentificationAccountBalanceByCurrencyCode(String codeCurrency) {
        try {
            return Broker.fetchRows("QRY_Identificacion_Account_Balance_By_currecyCode", Core.SIGNATURE, codeCurrency);
        } catch (Exception e) {
            log.error("Error:", e);
            throw new RuntimeException(e);
        }
    }

    public Object getIdentificacionAccountBalanceByCurrencyCodeUSD(String codeCurrency) {
        try {
            return Broker.fetchRows("QRY_Identificacion_Account_Balance_By_currecyCodeUSD", Core.SIGNATURE, codeCurrency);
        } catch (Exception e) {
            log.error("Error:", e);
            throw new RuntimeException(e);
        }
    }

    public Object getIdentificacionAccountBalanceByCurrencyCodeEUR(String codeCurrency) {
        try {
            return Broker.fetchRows("QRY_Identificacion_Account_Balance_By_currecyCodeEUR", Core.SIGNATURE, codeCurrency);
        } catch (Exception e) {
            log.error("Error:", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Recupera la información del saldo de la cuenta basada en la identificación, con un límite opcional en el número de registros.
     * <p>
     * Este método llama a la consulta "QRY_Identificacion_Account_Balance_RNC" a través del Broker,
     * utilizando una firma predefinida y un límite especificado para controlar el número de filas devueltas.
     *
     * @param limit el número máximo de registros a recuperar
     * @return el resultado de la consulta, típicamente una lista de filas o una estructura de datos que contiene la información de la cuenta
     * @throws RuntimeException si se produce un error durante la operación de obtención
     */
    public Object getIdentificationAccountBalanceRNC(Number limit) {
        try {
            return Broker.fetchRows("QRY_Identificacion_Account_Balance_RNC", Core.SIGNATURE, limit);
        } catch (Exception e) {
            log.error("Error:", e);
            throw new RuntimeException(e);
        }
    }

    public Object getSavingUnbalancedAccounts() {
        try {
            return Broker.fetchRows("QryAllUnbalancedAccountsss", Core.SIGNATURE);
        } catch (Exception e) {
            log.error("Error al obtener la cuenta de ahorros desbalanceada: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Ejecuta una consulta de base de datos predefinida para recuperar datos de cuentas
     * con saldos superiores a 100K.
     *
     * @return Una lista de filas de resultados si tiene éxito, o una lista vacía si se produce un error.
     */
    public Object getRandomAccountsWithAmountGreaterThan100K() {
        try {
            return Broker.fetchRows("QryRandomAccountsWithAmountGreaterThan100K", Core.SIGNATURE);
        } catch (Exception e) {
            log.error("Error al obtener cuentas aleatorias con un monto mayor a 100K: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Ejecuta una consulta de base de datos predefinida para recuperar datos de cuentas inactivas
     * con saldos superiores a 100K.
     *
     * @return Una lista de filas de resultados si tiene éxito, o una lista vacía si se produce un error.
     */
    public Object getRandomInactiveAccountsWithAmountGreaterThan100K() {
        try {
            return Broker.fetchRows("QryRandomInactiveAccountsWithAmountGreaterThan100K", Core.SIGNATURE);
        } catch (Exception e) {
            log.error("Error al obtener cuentas inactivas aleatorias con un monto mayor a 100K: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public String getAffectiveDate() {
        try {
            List<Map<String, Object>> rows = Broker.fetchRows("Qry_affectiveDate", Core.SIGNATURE);

            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.getFirst(); // primera fila
                Object value = row.values().iterator().next(); // primer valor
                return value != null ? value.toString() : null;
            }
            return null;
        } catch (Exception e) {
            log.error("Error al obtener la fecha: {}", e.getMessage());
            return null;
        }
    }

    public List<String> getCertificate() {
        try {
            List<Map<String, Object>> rows = Broker.fetchRows("Qry_consultCertificate", Core.SIGNATURE);
            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.getFirst(); // primera fila
                Object value = row.values().iterator().next(); // primer valor
                return value != null ? Collections.singletonList(value.toString()) : null;
            }
            return null;
        } catch (Exception e) {
            log.error("Error al obtener la fecha: {}", e.getMessage());
            return null;
        }
    }
    public List<String> getCertificateCanceled() {
        try {
            List<Map<String, Object>> rows = Broker.fetchRows("Qry_ConsulcertificateActive", Core.SIGNATURE);
            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.get(0); // primera fila
                Object value = row.values().iterator().next(); // primer valor
                return value != null ? Collections.singletonList(value.toString()) : null;
            }
            return null;
        } catch (Exception e) {
            log.error("Error en la fecha afectiva: {}", e.getMessage());
            return null;
        }
    }
    public List<String> getLOANS() {
        try {
            List<Map<String, Object>> rows = Broker.fetchRows("Qry_ConsultarPrestamos", Core.SIGNATURE);
            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.get(0); // primera fila
                Object value = row.values().iterator().next(); // primer valor
                return value != null ? Collections.singletonList(value.toString()) : null;
            }
            return null;
        } catch (Exception e) {
            log.error("Error en la fecha afectiva: {}", e.getMessage());
            return null;
        }
    }
}

