package dev.vansen.utility.time;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class TimeFormatter {
    private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d+)([A-Z]+)");

    public static Duration parse(String duration) {
        Matcher matcher = DURATION_PATTERN.matcher(duration.toUpperCase());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid duration format: " + duration);
        }
        int value = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2);

        switch (unit) {
            case "S":
                return Duration.ofSeconds(value);
            case "MS":
                return Duration.ofMillis(value);
            case "M":
                return Duration.ofMinutes(value);
            case "H":
                return Duration.ofHours(value);
            case "D":
                return Duration.ofDays(value);
            case "W":
                return Duration.ofDays(value * 7);
            case "MO":
                return Duration.ofDays(value * 30);
            case "Y":
                return Duration.ofDays(value * 365);
            default:
                throw new IllegalArgumentException("Invalid unit: " + unit);
        }
    }

    public static boolean valid(String duration) {
        try {
            parse(duration);
            return true;
        } catch (@NotNull IllegalArgumentException e) {
            return false;
        }
    }
}