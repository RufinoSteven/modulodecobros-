package utils;

import actions.dbActions.EntriesActions;
import com.banreservas.core.Broker;
import com.banreservas.core.Core;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ModifyFileAch {
    private static final Logger log = LogManager.getLogger(ModifyFileAch.class);
    @Getter
    private static String generatedACHFileName;
    private static final Random RANDOM = new Random();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyMMdd");
    private static final int LETTER_OFFSET = 'A';
    private static final int ALPHABET_SIZE = 26;

    /**
     * Procesa y modifica un archivo ACH según el tipo de proceso y el estado esperado.
     * Si el proceso es "Domiciliacion", aplica modificaciones personalizadas.
     * De lo contrario, reemplaza la fecha del archivo y lo renombra.
     *
     * @param typeProcessACH       El tipo de proceso ACH (ej. "Domiciliacion", "Tesoreria").
     * @param expectedStatusImport El estado de importación esperado (ej. "Valido", "Rechazado").
     * @return La ruta final del archivo ACH modificado.
     * @throws IOException Si las operaciones de archivo fallan.
     */
    public static String processAndModifyACHFile(String typeProcessACH, String expectedStatusImport) throws IOException {
        // Construir ruta del directorio
        String dirPath = "src/main/resources/ACH/" + typeProcessACH +
                ("Rechazado".equalsIgnoreCase(expectedStatusImport) ? "/" + expectedStatusImport : "");

        // Encontrar primer archivo ACH
        File[] files = new File(dirPath).listFiles((dir, name) ->
                name.matches("(?i).*\\.(txt|ach)$")
        );

        if (files == null || files.length == 0) {
            throw new FileNotFoundException("No ACH files found in: " + dirPath);
        }

        File achFile = files[0];  // Obtener el primer archivo
        Path originalPath = achFile.toPath();
        log.info("Processing ACH file: {}", originalPath);

        // Manejar caso Domiciliacion
        if ("Domiciliacion".equalsIgnoreCase(typeProcessACH)) {
            return modifyDomiciliaryFile(originalPath, dirPath, typeProcessACH);
        }

        // Manejar caso DC
        if ("DC".equalsIgnoreCase(typeProcessACH)) {
            return modifyDcFile(originalPath, dirPath, typeProcessACH, expectedStatusImport);
        }

        // Procesar contenido del archivo
        List<String> lines = Files.readAllLines(originalPath);
        String originalDate = lines.stream()
                .filter(line -> line.startsWith("101") && line.length() >= 29)
                .map(line -> line.substring(23, 29))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Date not found in line 101."));

        String newDate = generateRandomDateWithinPreviousMonth();
        log.info("Replacing date {} with {}", originalDate, newDate);

        // Actualizar líneas con nueva fecha y encabezado
        List<String> updatedLines = lines.stream()
                .map(line -> line.startsWith("101")
                        ? replaceHeaderLetter(line.replace(originalDate, newDate))
                        : line.replace(originalDate, newDate))
                .collect(Collectors.toList());

        // Escribir cambios y renombrar archivo
        Files.write(originalPath, updatedLines, StandardOpenOption.TRUNCATE_EXISTING);
        generatedACHFileName = generateRandomName(typeProcessACH) + ".txt";
        Path renamedPath = Paths.get(dirPath, generatedACHFileName);
        Files.move(originalPath, renamedPath);

        log.info("ACH file renamed to: {}", generatedACHFileName);
        return renamedPath.toString();
    }

    /**
     * Reemplaza la letra del encabezado (en la posición 34) de una línea ACH con una letra aleatoria diferente.
     * Se utiliza para evitar encabezados de archivo duplicados en el procesamiento de ACH.
     *
     * @param line La línea de encabezado original del archivo ACH (debe comenzar con "101").
     * @return La línea modificada con una nueva letra de encabezado aleatoria.
     */
    private static String replaceHeaderLetter(String line) {
        if (line.length() <= 33) return line;
        char currentChar = line.charAt(33);
        char newChar;
        do {
            newChar = (char) (RANDOM.nextInt(ALPHABET_SIZE) + LETTER_OFFSET);
        } while (Character.toUpperCase(newChar) == Character.toUpperCase(currentChar));
        return line.substring(0, 33) + newChar + line.substring(34);
    }

    /**
     * Genera una fecha aleatoria en el formato "yyMMdd" dentro del rango de los últimos 5 meses,
     * específicamente desde el inicio de hace 5 meses hasta el final del mes anterior.
     *
     * @return Una cadena que representa la fecha generada aleatoriamente en formato "yyMMdd".
     */
    private static String generateRandomDateWithinPreviousMonth() {
        LocalDate today = LocalDate.now();
        LocalDate fiveMonthsAgo = today.minusMonths(5);
        LocalDate lastDayOfLastMonth = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth());

        long daysBetween = ChronoUnit.DAYS.between(fiveMonthsAgo, lastDayOfLastMonth);
        return fiveMonthsAgo.plusDays(RANDOM.nextInt((int) daysBetween + 1))
                .format(DATE_FORMAT);
    }

    /**
     * Genera un nombre de archivo aleatorio añadiendo un número aleatorio de 3 dígitos al tipo de proceso dado.
     *
     * @param typeProcessACH El tipo de proceso a incluir en el nombre del archivo (ej. "Tesoreria", "Domiciliacion").
     * @return Una cadena que combina el tipo de proceso y un número aleatorio de 3 dígitos (ej. "Tesoreria042").
     */
    private static String generateRandomName(String typeProcessACH) {
        int randomNumber = new Random().nextInt(999);
        return typeProcessACH + String.format("%03d", randomNumber);
    }

    /**
     * Construye la ruta completa al archivo ACH generado según el tipo de proceso y el estado de importación esperado.
     *
     * @param typeProcessACH       El tipo de proceso ACH (ej. "Tesoreria", "Domiciliacion").
     * @param expectedStatusImport El estado esperado (ej. "Rechazado", "Valido").
     * @return La ruta de archivo completa al archivo ACH generado.
     */
    public static String getGeneratedACHFilePath(String typeProcessACH, String expectedStatusImport) {
        String directoryPath = "src/main/resources/ACH/" + typeProcessACH;
        if (expectedStatusImport.equalsIgnoreCase("Rechazado") && !typeProcessACH.equalsIgnoreCase("Domiciliacion")) {
            directoryPath += "/" + expectedStatusImport;
        }
        return Paths.get(directoryPath, generatedACHFileName).toString();
    }

    /**
     * Modifica un archivo ACH de Domiciliación reemplazando números de cuenta y generando montos aleatorios,
     * asegurando reemplazos de cuenta únicos y actualizando el monto total del débito.
     *
     * @param originalPath   Ruta al archivo original.
     * @param directoryPath  Directorio donde se guardará el archivo modificado.
     * @param typeProcessACH El tipo de proceso ACH ("Domiciliacion").
     * @return La ruta completa del archivo ACH renombrado y modificado.
     * @throws IOException si las operaciones de archivo fallan.
     */
    private static String modifyDomiciliaryFile(Path originalPath, String directoryPath, String typeProcessACH) throws IOException {
        List<String> lines = Files.readAllLines(originalPath);
        List<String> modifiedLines = new ArrayList<>(lines.size());
        Map<String, String> accountMapping = new HashMap<>();
        Set<String> usedNewAccounts = new HashSet<>();
        double totalAmount = 0.0;

        List<String> availableAccounts = getRandomAccountNumbersWithAmountGreaterThan100K();
        if (availableAccounts.isEmpty()) {
            throw new IllegalStateException("No se encontraron cuentas para reemplazo.");
        }

        for (String line : lines) {
            if (line.contains("|D|")) {
                String[] parts = line.split("\\|", -1); // -1 para mantener las cadenas vacías finales
                String originalAccount = parts[1];
                String newAccount = accountMapping.computeIfAbsent(originalAccount, k ->
                        availableAccounts.stream()
                                .filter(acc -> !usedNewAccounts.contains(acc))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException("Se agotaron las cuentas únicas para reemplazo."))
                );
                usedNewAccounts.add(newAccount);

                parts[1] = newAccount;
                if (parts.length > 4) {
                    parts[4] = parts[4].replace(originalAccount, newAccount);
                }

                String amountStr = parts[3];
                String[] amountParts = amountStr.split("\\.");
                int integerDigits = amountParts[0].length();
                int decimalDigits = amountParts.length > 1 ? amountParts[1].length() : 0;

                double maxAmount = Math.pow(10, integerDigits) - 1;
                double randomAmount = Math.round((RANDOM.nextDouble() * maxAmount) * Math.pow(10, decimalDigits)) / Math.pow(10, decimalDigits);

                // Usar DecimalFormat para un formato de número más fiable
                DecimalFormat df = new DecimalFormat("#." + "#".repeat(decimalDigits));
                df.setRoundingMode(RoundingMode.HALF_UP);
                parts[3] = df.format(randomAmount);
                totalAmount += randomAmount;
                modifiedLines.add(String.join("|", parts));
            } else if (line.contains("|C|")) {
                String[] parts = line.split("\\|", -1);
                parts[3] = String.format("%.2f", totalAmount);
                modifiedLines.add(String.join("|", parts));
            } else {
                modifiedLines.add(line);
            }
        }

        Files.write(originalPath, modifiedLines, StandardOpenOption.TRUNCATE_EXISTING);
        generatedACHFileName = generateRandomName(typeProcessACH) + ".txt";
        Path renamedPath = Paths.get(directoryPath, generatedACHFileName);
        Files.move(originalPath, renamedPath);

        log.info("Archivo Domiciliacion renombrado como: {}", generatedACHFileName);
        return renamedPath.toString();
    }

    private static double generateRandomAmount(int integerDigits, int decimalPlaces) {
        double maxValue = Math.pow(10, integerDigits) - 1;
        double factor = Math.pow(10, decimalPlaces);
        return Math.round((RANDOM.nextDouble() * maxValue) * factor) / factor;
    }

    /**
     * Recupera una lista de números de cuenta con saldos mayores a 100K de la base de datos.
     * Filtra los valores nulos o vacíos para asegurar que solo se devuelvan cuentas válidas.
     *
     * @return Una lista de números de cuenta válidos, o una lista vacía si no se encuentra ninguno o si ocurre un error.
     */
    public static List<String> getRandomAccountNumbersWithAmountGreaterThan100K() {
        EntriesActions entriesActions = new EntriesActions();

        try {
            List<Map<String, Object>> results = Broker.fetchRows("QryRandomAccountsWithAmountGreaterThan100K", Core.SIGNATURE);
            return results.stream()
                    .map(row -> row.get("ACCOUNT_NBR"))
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .map(String::trim)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener cuentas desde la base de datos: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Recupera una lista de números de cuenta inactivos con saldos mayores a 100K de la base de datos.
     * Filtra los valores nulos o vacíos para asegurar que solo se devuelvan cuentas válidas.
     *
     * @return Una lista de números de cuenta válidos, o una lista vacía si no se encuentra ninguno o si ocurre un error.
     */
    public static List<String> getRandomInactiveAccountNumbersWithAmountGreaterThan100K() {
        EntriesActions entriesActions = new EntriesActions();

        try {
            List<Map<String, Object>> results = Broker.fetchRows("QryRandomInactiveAccountsWithAmountGreaterThan100K", Core.SIGNATURE);
            return results.stream()
                    .map(row -> row.get("ACCOUNT_NBR"))
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .map(String::trim)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener cuentas inactivas desde la base de datos: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Modifica un archivo ACH del tipo "DC" reemplazando únicamente los números de cuenta,
     * sin alterar montos, encabezados o fechas, y renombrando el archivo al final.
     *
     * Reglas:
     *  - La cuenta a reemplazar está entre el primer '1' de la línea (excluyéndolo)
     *    y justo antes del marcador "000C1".
     *  - Cada cuenta original se mapea a una cuenta nueva única, tomada de BD (>= 100K).
     *  - Se preserva el ancho (longitud) de la cuenta original:
     *      * Si la cuenta nueva es más corta: se left-pad con ceros.
     *      * Si es más larga: se toman los últimos N dígitos.
     *
     * @param originalPath   Ruta del archivo original.
     * @param directoryPath  Carpeta de destino.
     * @param typeProcessACH Debe ser "DC".
     * @return Ruta final (renombrada) del archivo modificado.
     * @throws IOException Si falla E/S.
     */
    private static String modifyDcFile(Path originalPath, String directoryPath, String typeProcessACH, String expectedStatusImport) throws IOException {
        List<String> lines = Files.readAllLines(originalPath);
        if (lines.isEmpty()) {
            throw new IllegalStateException("El archivo DC está vacío: " + originalPath);
        }

        // Pool según estatus (inactivas si Rechazado, activas en otro caso)
        List<String> availableAccounts = getAccountPoolForDC(expectedStatusImport);
        if (availableAccounts.isEmpty()) {
            throw new IllegalStateException("No se encontraron cuentas para reemplazo (pool vacío).");
        }

        List<String> cleanPool = availableAccounts.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(s -> s.replaceAll("\\D", "")) // sólo dígitos
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        if (cleanPool.isEmpty()) {
            throw new IllegalStateException("Las cuentas recuperadas no contienen dígitos válidos.");
        }

        Map<String, String> accountMapping = new HashMap<>();
        Iterator<String> poolIterator = cleanPool.iterator();

        List<String> modified = new ArrayList<>(lines.size());
        for (String line : lines) {
            if (line != null && line.contains("000C1")) {
                Optional<AccountSlice> sliceOpt = findDcAccountSlice(line);
                if (sliceOpt.isPresent()) {
                    AccountSlice slice = sliceOpt.get();
                    String originalAccount = line.substring(slice.startExclusive, slice.endExclusive);

                    String mapped = accountMapping.get(originalAccount);
                    if (mapped == null) {
                        if (!poolIterator.hasNext()) {
                            throw new IllegalStateException("Se agotaron las cuentas disponibles para reemplazo.");
                        }
                        String candidate = poolIterator.next();
                        mapped = normalizeToLength(candidate, originalAccount.length());
                        accountMapping.put(originalAccount, mapped);
                    } else {
                        mapped = normalizeToLength(mapped, originalAccount.length());
                    }

                    String replaced = line.substring(0, slice.startExclusive) + mapped + line.substring(slice.endExclusive);
                    modified.add(replaced);
                    continue;
                } else {
                    log.warn("Línea DC con marcador '000C1' pero no se pudo localizar la cuenta: '{}'", line);
                }
            }
            modified.add(line); // líneas no-DC intactas
        }

        Files.write(originalPath, modified, StandardOpenOption.TRUNCATE_EXISTING);

        generatedACHFileName = generateRandomName(typeProcessACH) + ".txt";
        Path renamedPath = Paths.get(directoryPath, generatedACHFileName);
        Files.move(originalPath, renamedPath);

        log.info("Archivo DC renombrado como: {}", generatedACHFileName);
        return renamedPath.toString();
    }

    /** Pequeño contenedor para los índices del segmento de cuenta en DC. */
    private static class AccountSlice {
        final int startExclusive; // índice después del primer '1'
        final int endExclusive;   // índice donde empieza "000C1"
        AccountSlice(int startExclusive, int endExclusive) {
            this.startExclusive = startExclusive;
            this.endExclusive = endExclusive;
        }
    }

    /**
     * Localiza los límites de la cuenta en una línea DC.
     * Cuenta = (primer '1' + 1) .. (antes de "000C1").
     * Devuelve Optional vacío si no se encuentra el patrón.
     */
    private static Optional<AccountSlice> findDcAccountSlice(String line) {
        if (line == null) return Optional.empty();
        int firstOne = line.indexOf('1');         // primer '1' de la línea
        int cut = line.indexOf("000C1");          // marcador de fin de cuenta
        if (firstOne < 0 || cut < 0) return Optional.empty();

        int start = firstOne + 1;
        if (cut <= start) return Optional.empty();

        return Optional.of(new AccountSlice(start, cut));
    }

    /**
     * Normaliza una cuenta a un largo fijo:
     *  - Si es más corta -> left-pad con '0'
     *  - Si es más larga -> tomar los últimos N dígitos
     *  - Si no tiene dígitos -> rellena con '0'
     */
    private static String normalizeToLength(String accountDigitsOnly, int requiredLen) {
        if (requiredLen <= 0) return "";
        if (accountDigitsOnly == null) accountDigitsOnly = "";
        // Asegurar sólo dígitos
        String digits = accountDigitsOnly.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            return StringUtils.leftPad("", requiredLen, '0');
        }
        if (digits.length() == requiredLen) return digits;
        if (digits.length() < requiredLen) {
            return StringUtils.leftPad(digits, requiredLen, '0');
        }
        // más larga: tomar sufijo
        return digits.substring(digits.length() - requiredLen);
    }

    /**
     * Devuelve el pool de cuentas a usar para DC:
     * - Rechazado  -> cuentas INACTIVAS (>= 100K)
     * - Otro       -> cuentas ACTIVAS (>= 100K)
     */
    private static List<String> getAccountPoolForDC(String expectedStatusImport) {
        if (expectedStatusImport != null && expectedStatusImport.equalsIgnoreCase("Rechazado")) {
            return getRandomInactiveAccountNumbersWithAmountGreaterThan100K();
        }
        return getRandomAccountNumbersWithAmountGreaterThan100K();
    }

}


