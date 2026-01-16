package utils.DataGenerator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utilidad para generar datos aleatorios para embargos
 */
public class EmbargoDataGenerator {
    private static final String[] FIRST_NAMES = {
            "JUAN", "MARIA", "PEDRO", "ANA", "LUIS", "CARMEN", "JOSE", "ROSA", "MANUEL", "FRANCISCA",
            "MIGUEL", "ALTAGRACIA", "RAFAEL", "RAMONA", "FRANCISCO", "JUANA", "ANTONIO", "MERCEDES"
    };

    private static final String[] LAST_NAMES = {
            "PEREZ", "RODRIGUEZ", "GOMEZ", "FERNANDEZ", "MARTINEZ", "GARCIA", "LOPEZ", "DIAZ",
            "SANCHEZ", "HERNANDEZ", "RAMIREZ", "TORRES", "GONZALEZ", "REYES", "MORALES", "JIMENEZ"
    };

    private static final String[] SOCIAL_REASONS = {
            "DISTRIBUIDORA", "SUPERMERCADO", "INVERSIONES", "SOLUCIONES", "GRUPO", "SERVICIOS",
            "CONSULTORES", "CONSTRUCTORA", "INMOBILIARIA", "COMERCIAL", "INDUSTRIAS", "MANUFACTURA"
    };

    private static final Random random = new Random();

    /**
     * Generates a random valid Dominican cedula number (format: 00100000000)
     */
    public static String generateDominicanId() {
        String[] prefixes = {"001", "002", "031", "402"};
        return prefixes[random.nextInt(prefixes.length)] + String.format("%08d", random.nextInt(100000000));
    }

    /**
     * Generates a random first name
     */
    public static String generateRandomFirstName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
    }

    /**
     * Generates a random last name
     */
    public static String generateRandomLastName() {
        return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    /**
     * Generates a random last name
     */
    public static String generateSocialReasons() {
        return SOCIAL_REASONS[random.nextInt(SOCIAL_REASONS.length)];
    }

    /**
     * Generates a random amount between 1000 and 50000
     */
    public static String generateRandomAmount() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100, 501));
    }

    public static String generateInsuranceCompany() {
        String[] companies = {
                "MAPFRE SALUD ARS",
                "Humano Seguros",
                "ARS Universal",
                "Primera ARS",
                "Yunen Salud"
        };
        return companies[new Random().nextInt(companies.length)];
    }

    public static String generatePolicyCode() {
        int code = 10000 + new Random().nextInt(90000);
        return String.valueOf(code);
    }
}