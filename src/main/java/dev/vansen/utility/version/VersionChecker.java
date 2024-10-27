package dev.vansen.utility.version;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public final class VersionChecker {
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