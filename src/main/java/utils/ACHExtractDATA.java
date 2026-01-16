package utils;

import dataStorageModel.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class for extracting ACH data from a file.
 * It reads and processes ACH records, extracting account details, amounts, and totals.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ACHExtractDATA {

    private static final int DC_AMOUNT_ZONE_START = 46;
    public static final String File_PATH_ACH = "src/test/resources/DC/AHDOEV280725";
    private static volatile ACHExtractDATA instance;
    private static final Logger log = LogManager.getLogger(ACHExtractDATA.class);


    /**
     * Returns the singleton instance of ACHExtractDATA.
     * Uses double-checked locking to ensure thread safety.
     *
     * @return Singleton instance of ACHExtractDATA.
     */
    public static synchronized ACHExtractDATA getInstance() {
        return (instance == null) ? (instance = new ACHExtractDATA()) : instance;
    }

    /**
     * Reads all lines from a file and returns them as a list of strings.
     *
     * @param filePath The path to the file to be read.
     * @return A list containing each line of the file.
     * @throws IOException If an error occurs while reading the file.
     */
    public List<String> readFileLines(String filePath) throws IOException {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            log.error("Error reading the file: {} - {}", filePath, e.getMessage());
            throw e;
        }
    }

    /**
     * Gets the name of the file you want to import.
     *
     * @param filePath The path to the file to be read.
     * @return Returns the file name.
     */
    public String getFileName(String filePath) {
        return Paths.get(filePath).getFileName().toString();
    }

    // Method to find the position where zeros end in the ACH file
    private static int findNonZeroAfterZeros(String line) {
        int start = -1;

        // // Start searching from position 30
        for (int i = 30; i < line.length(); i++) {
            if (line.charAt(i) != '0') {
                start = i;  // When a number other than '0' is found, that is the starting position
                break;
            }
        }
        return start;
    }

    /**
     * Extracts ACH information from a given file.
     *
     * @param filePath The path of the ACH file to be processed.
     * @return An ACHData object containing extracted account details and totals.
     * @throws IOException If an error occurs while processing the file.
     */
    public ACHData extractACHInformation(String filePath, String typeProcessACH) throws IOException {
        List<String> lines = readFileLines(filePath);
        ACHExtractDTO dto = ACHExtractDTO.builder()
                .accounts(new ArrayList<>())
                .transactionAmount(new ArrayList<>())
                .subtotal(0.0)
                .total(0.0)
                .foundData(false)
                .accountsDebited(new ArrayList<>())
                .regionalAccounts(new ArrayList<>())
                .build();

        try {
            List<String> accountLines = new ArrayList<>();
            List<String> amountLines = new ArrayList<>();


            for (String line : lines) {
                // --- Caso especial DC ---
                if ("DC".equalsIgnoreCase(typeProcessACH)) {
                    // Solo procesamos líneas que luzcan como las de DC (tienen ambos marcadores)
                    if (line.contains("000C1") && line.contains("28072025")) {
                        String account = extractDcAccount(line);
                        Double amount = extractDcAmount(line);

                        if (account != null && amount != null) {
                            // Beneficiario no está en el layout DC provisto; usamos la cuenta como placeholder
                            dto.getAccounts().add(new AccountEntry(account, amount, account));
                            dto.getTransactionAmount().add(new AmountEntry(amount));

                            dto.setSubtotal(dto.getSubtotal() + amount);
                            dto.setTotal(dto.getTotal() + amount);
                            dto.setFoundData(true);
                        } else {
                            log.warn("Línea DC no parseable. account='{}', amount='{}', line='{}'", account, amount, line);
                        }
                    }
                    // DC no usa los prefijos 622/820/520/522/621/631 del layout ACH estándar;
                    // pasamos a la siguiente línea.
                    continue;
                }

                if (line.startsWith("622")) {
                    double amount = 0;
                    if (!typeProcessACH.equalsIgnoreCase("Tesoreria") && !typeProcessACH.equalsIgnoreCase("TransACH")) {
                        amount = Double.parseDouble(line.substring(29, 43).trim()) / 100.0;
                    }
                    dto.getAccounts().add(new AccountEntry(
                            line.substring(31, 40).trim(),
                            amount,
                            line.substring(59, 76).trim()
                    ));
                    dto.setSubtotal(dto.getSubtotal() + amount);
                    dto.setFoundData(true);
                    dto.getRegionalAccounts().add(new RegionalAccount(
                            line.substring(12, 40).trim()
                    ));
                } else if (line.startsWith("820")) {
                    int start = findNonZeroAfterZeros(line);
                    int result = -1;
                    if (typeProcessACH.equalsIgnoreCase("Tesoreria") || typeProcessACH.equalsIgnoreCase("TransACH")) {
                        result = line.indexOf("401036959", 35);
                    } else if (typeProcessACH.equalsIgnoreCase("Afiliados")) {
                        result = line.indexOf("430000868", 35);
                    }
                    if (result != -1 && result > start && result <= line.length()) {
                        try {
                            String amountStr = line.substring(start, result).trim();
                            double transactionAmount = amountStr.isEmpty() ? 0.0 : Double.parseDouble(amountStr) / 100.0;
                            dto.getTransactionAmount().add(new AmountEntry(transactionAmount));
                            dto.setTotal(dto.getTotal() + transactionAmount);
                            dto.setFoundData(true);
                        } catch (NumberFormatException e) {
                            log.warn("Invalid number format in amount: '{}'");
                        }
                    } else {
                        log.warn("Invalid substring range or missing pattern. start={}, result={}, line='{}'", start, result, line);
                    }
                } else if (line.startsWith("520")) {
                    dto.getAccountsDebited().add(line.substring(25, 35).trim());
                } else if (line.startsWith("522")) {
                    accountLines.add(line);
                } else if (line.startsWith("621") || line.startsWith("631")) {
                    amountLines.add(line);
                } else if (typeProcessACH.equalsIgnoreCase("Domiciliacion") && line.startsWith("001")) {
                    String[] parts = line.split("\\|");

                    if (parts.length >= 4) {
                        String account = parts[1].trim();
                        String type = parts[2].trim();
                        String amountStr = parts[3].trim();

                        try {
                            double amount = Double.parseDouble(amountStr);
                            dto.setSubtotal(dto.getSubtotal() + amount);
                            dto.setFoundData(true);
                            dto.getTransactionAmount().add(new AmountEntry(amount));
                            dto.getAccounts().add(new AccountEntry(
                                    account,
                                    amount,
                                    account
                            ));

                            if (type.equalsIgnoreCase("D")) {
                                dto.getAccountsDebited().add(account);
                            }

                        } catch (NumberFormatException e) {
                            log.error("Invalid number format in domiciliation line: '{}'", amountStr, e);
                        }
                    } else {
                        log.warn("Invalid domiciliation line format: '{}', line='{}'", line, line);
                    }
                }
            }

            int pairCount = Math.min(accountLines.size(), amountLines.size());

            for (int i = 0; i < pairCount; i++) {
                String accountLine = accountLines.get(i);
                String amountLine = amountLines.get(i);

                try {
                    String accountNumber = accountLine.substring(20, 30).trim();
                    String beneficiary = accountLine.substring(54, 76).trim();
                    String amountStr = amountLine.substring(42, 50).trim(); // misma posición para 621/631

                    if (!amountStr.isEmpty()) {
                        double amount = Double.parseDouble(amountStr) / 100.0;

                        dto.getAccounts().add(new AccountEntry(accountNumber, amount, beneficiary));
                        dto.getTransactionAmount().add(new AmountEntry(amount));
                        dto.setSubtotal(dto.getSubtotal() + amount);
                        dto.setFoundData(true);
                        dto.getRegionalAccounts().add(new RegionalAccount(accountLine.substring(12, 40).trim()));
                    }
                } catch (Exception e) {
                    log.warn("Error parsing account/monto pair: 522='{}' - monto='{}'", accountLine, amountLine, e);
                }
            }

            if (accountLines.size() != amountLines.size()) {
                log.warn("Mismatch between number of 522 lines ({}) and 621/631 lines ({}).", accountLines.size(), amountLines.size());
            }

            logExtractionResults(dto);
            return new ACHData(dto.getAccounts(), dto.getTransactionAmount(), dto.getSubtotal(), dto.getTotal(), dto.getAccountsDebited(),
                    dto.getRegionalAccounts());
        } catch (Exception e) {
            log.error("Error processing ACH file: {}", e.getMessage());
            throw new IOException("Error processing ACH file", e);
        }
    }

    private String extractDcAccount(String line) {
        int firstOne = line.indexOf('1');          // primer '1' de la línea
        int cut = line.indexOf("000C1");           // marcador de fin de cuenta
        if (firstOne == -1 || cut == -1 || cut <= firstOne + 1) return null;
        return line.substring(firstOne + 1, cut).trim();
    }

    private Double extractDcAmount(String line) {
        int endTag = line.indexOf("28072025");
        if (endTag == -1) return null;

        int i = Math.min(DC_AMOUNT_ZONE_START, line.length()); // inicio zona ceros
        // avanzar hasta el primer dígito distinto de '0' dentro de la zona
        while (i < endTag && line.charAt(i) == '0') i++;
        if (i >= endTag) return null;

        // tomar todos los dígitos hasta antes del tag final
        int j = i;
        while (j < endTag && Character.isDigit(line.charAt(j))) j++;

        String centsStr = line.substring(i, j).trim();
        if (centsStr.isEmpty()) return null;

        try {
            long cents = Long.parseLong(centsStr);
            return cents / 100.0; // convertir a unidades
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void logExtractionResults(ACHExtractDTO dto) {
        log.info("\n=== ACH EXTRACTION RESULTS ===");
        log.info("ACCOUNTS, AMOUNTS, AND BENEFICIARIES:");
        log.info("--------------------------------");
        dto.getAccounts().forEach(account -> log.info(String.format("Account: %s | Amount: $%.2f | Beneficiary: %s",
                account.getAccountNumber(),
                account.getAmount(),
                account.getBeneficiary())));
        log.info("Number of accounts: {}", dto.getAccounts().size());
        dto.getTransactionAmount().forEach(transactionAmount -> log.info(String.format(" Transaction Amount: $%.2f",
                transactionAmount.getTransactionAmounts())));
        dto.getRegionalAccounts().forEach(regionalAccount -> log.info(String.format("Regional Account: %s ",
                regionalAccount.getRegionalAccountNumbers())));
        log.info("--------------------------------");
        log.info(String.format("SUBTOTAL: $%.2f", dto.getSubtotal()));
        log.info(String.format("TOTAL: $%.2f", dto.getTotal()));
        log.info(String.format("Account Debited: %s", dto.getAccountsDebited()));
        log.info("===============================");
    }

    @Test
    public void testExtractAchFile() throws IOException {
        ACHExtractDATA.getInstance().extractACHInformation(File_PATH_ACH, "DC");
        String achFileName = ModifyFileAch.getGeneratedACHFileName();
        log.info(achFileName);
    }
}
