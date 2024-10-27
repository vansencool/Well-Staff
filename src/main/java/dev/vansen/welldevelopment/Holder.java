package dev.vansen.welldevelopment;

import dev.vansen.wellstaff.api.events.eventbus.Eventer;

public final class Holder {
    private static final Eventer eventer = new Eventer();
    private static WellDevelopment plugin;

    public static void set(WellDevelopment plugin) {
        Holder.plugin = plugin;
    }

    public static WellDevelopment get() {
        return plugin;
    }

    public static Eventer getEventer() {
        return eventer;
    }
}
