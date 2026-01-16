package utils;

import dataStorageModel.NominaEntry;
import dataStorageModel.NominaExtractDTO;
import dataStorageModel.PayrollInfoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Singleton class for extracting Nomina data from a file.
 * It reads and processes Nomina records, extracting account Company, amounts, and date.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtractDATANomina {
    private static volatile ExtractDATANomina instance;
    private static final Logger log = Logger.getLogger(ExtractDATANomina.class.getName());

    /**
     * Returns the singleton instance of ExtractDATANomina.
     * Uses double-checked locking to ensure thread safety.
     *
     * @return Singleton instance of ExtractDATANomina.
     */
    public static synchronized ExtractDATANomina getInstance() {
        return (instance == null) ? (instance = new ExtractDATANomina()) : instance;
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
            log.severe("Error reading the file: " + filePath + " - " + e.getMessage());
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

    /**
     * Extracts payroll information from a file and returns it as a DTO.
     *
     * @param filePath Path to the payroll file
     * @return PayrollInfoDTO containing the extracted data
     */
    @SneakyThrows
    public PayrollInfoDTO extractNominaInformation(String filePath) {
        log.info(() -> "Starting payroll extraction from file: " + filePath);
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            List<NominaEntry> entries = lines
                    .filter(line -> line.startsWith("0"))
                    .map(this::parseNominaLine)
                    .filter(Objects::nonNull)
                    .toList();
            if (entries.isEmpty()) {
                log.warning(() -> "No valid payroll entries found in file: " + filePath);
                return null;
            }
            log.info(() -> String.format("Successfully extracted %d payroll entries", entries.size()));
            return new PayrollInfoDTO(entries);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error processing payroll file: " + e.getMessage(), e);
            throw new RuntimeException("Failed to process payroll file", e);
        }
    }

    private NominaEntry parseNominaLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        try {
            return new NominaEntry(safeSubstring(line, 0, 1),    // company
                    safeSubstring(line, 2, 7),    // companyID
                    safeSubstring(line, 8, 9),    // typeAccount
                    safeSubstring(line, 9, 21),   // accounts
                    parseAmount(line),            // amount
                    safeSubstring(line, 40, 46)   // date
            );
        } catch (Exception e) {
            log.log(Level.WARNING, "Error parsing line: " + line, e);
            return null;
        }
    }

    private String safeSubstring(String str, int start, int end) {
        if (str.length() <= start) return "";
        return str.substring(start, Math.min(str.length(), end)).trim();
    }

    private double parseAmount(String line) {
        try {
            return Double.parseDouble(safeSubstring(line, 22, 39)) / 100.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void logExtractionResults(NominaExtractDTO dto) {
        log.info("\n=== NOMINA EXTRACTION RESULTS ===");
        log.info("COMPANY, COMPANYID, TYPEACCOUNT, ACCOUNT, AMOUNT, DATE:");
        log.info("--------------------------------------------------------");
        for (NominaEntry entry : dto.getNominainfo()) {
            log.info(String.format("Account: %s | Amount: $%.2f | COMPANY: %s | COMPANYID: %s | TYPEACCOUNT: %s | DATE: %s", entry.getAccounts(), entry.getAmount(), entry.getCompany(), entry.getCompanyID(), entry.getTypeAccount(), entry.getDate()));
        }
        log.info("--------------------------------------------------------");
        log.info("Total Records: " + dto.getNominainfo().size());
        log.info("========================================================");
    }
}
