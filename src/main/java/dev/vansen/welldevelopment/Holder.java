package dev.vansen.welldevelopment;

import dev.vansen.wellstaff.api.events.eventbus.Eventer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Holder {
    private static final Eventer eventer = new Eventer();
    private static WellDevelopment plugin;

    public static void set(@NotNull WellDevelopment plugin) {
        Holder.plugin = plugin;
    }

    /**
     * Get the instance of {@link WellDevelopment} aka {@link JavaPlugin}
     *
     * @return The instance of {@link WellDevelopment} aka {@link JavaPlugin}
     * @throws IllegalStateException If the plugin is not set
     */
    public static WellDevelopment get() {
        if (plugin == null) throw new IllegalStateException("Plugin not set");
        return plugin;
    }

    /**
     * Get the instance of {@link Eventer} for registering/posting events globally and in other plugins (if non-shaded)
     *
     * @return The instance of {@link Eventer}
     */
    public static Eventer getEventer() {
        return eventer;
    }
}
