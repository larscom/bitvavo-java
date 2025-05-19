package io.github.larscom.internal;

public final class CaseConverter {

    public static String toCamelCase(final String snakeCase) {
        final var parts = snakeCase.toLowerCase().split("_");
        final var sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sb.append(Character.toUpperCase(parts[i].charAt(0)))
                .append(parts[i].substring(1));
        }
        return sb.toString();
    }
}
