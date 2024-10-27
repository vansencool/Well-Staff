package dev.vansen.welldevelopment;

import dev.vansen.commandutils.api.CommandAPI;
import dev.vansen.configutils.ConfigUtils;
import dev.vansen.inventoryutils.InventoryUtils;
import dev.vansen.scheduleutils.SchedulerHolder;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WellDevelopment extends JavaPlugin {

    @Override
    public void onEnable() {
        SchedulerHolder.set(this);
        ConfigUtils.init(this);
        InventoryUtils.init(this);
        CommandAPI.set(this);
        Holder.set(this);
        start();
    }

    @Override
    public void onDisable() {
        stop();
    }

    protected abstract void start();

    protected void stop() {

    }
}