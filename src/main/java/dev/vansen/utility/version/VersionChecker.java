package dev.vansen.utility.version;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public final class VersionChecker {
    /**
     * <b>WARNING:</b> THE FOLLOWING EXPLANATION MAY CAUSE INFORMATION OVERLOAD, PROCEED WITH CAUTION :D
     *
     * <p>
     * This comparator works by comparing the binary representations of the version parts.
     *
     * <p>
     * When comparing two version parts, the comparator performs the following steps:
     *
     * <ol>
     *     <li>Converts each version part to its binary representation.</li>
     *     <li>Compares the binary representations bit by bit, starting from the most significant bit (MSB).</li>
     *     <li>If the bits are different, the comparator returns the result of the comparison.</li>
     *     <li>If the bits are the same, the comparator moves to the next bit and repeats the comparison.</li>
     * </ol>
     *
     * <p>
     * If no non-zero difference is found, the comparator returns 0, indicating that the versions are equal.
     * <p>
     * Yes, I know that's not a good example, but it's the best I could think of.
     */
    private static final Comparator<String> VERSION_COMPARATOR = (v1, v2) -> {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        return IntStream.range(0, Math.max(parts1.length, parts2.length))
                .map(i -> Integer.parseInt(parts1.length > i ? parts1[i] : "0")
                        - Integer.parseInt(parts2.length > i ? parts2[i] : "0"))
                .filter(diff -> diff != 0)
                .findFirst()
                .orElse(0);
    };

    private static String getServerVersion() {
        return Bukkit.getBukkitVersion().split("-")[0];
    }

    public static boolean isLower(@NotNull String targetVersion) {
        return VERSION_COMPARATOR.compare(getServerVersion(), targetVersion) < 0;
    }
}