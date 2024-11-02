package dev.vansen.utility.debugging;

import dev.vansen.welldevelopment.Holder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class Debug {
    private static boolean debug = false;

    private Debug() {
    }

    public static void debug(boolean debug) {
        Debug.debug = debug;
    }

    public static void debug(@NotNull String... debugs) {
        if (!debug) return;
        Arrays.stream(debugs)
                .forEach(Holder.get().getComponentLogger()::info);
    }

    public static void debug(@NotNull Component... debugs) {
        if (!debug) return;
        Arrays.stream(debugs)
                .forEach(Holder.get().getComponentLogger()::info);
    }
}
