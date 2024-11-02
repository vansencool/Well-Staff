package dev.vansen.welldevelopment;

import dev.vansen.commandutils.api.CommandAPI;
import dev.vansen.configutils.ConfigUtils;
import dev.vansen.inventoryutils.InventoryUtils;
import dev.vansen.scheduleutils.SchedulerHolder;
import dev.vansen.utility.annotations.impl.AnnotationImpl;
import dev.vansen.utility.version.VersionChecker;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WellDevelopment extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig"); // Legacy config
        } catch (ClassNotFoundException ignored) {
            getLogger()
                    .severe("=".repeat(50));
            getLogger()
                    .severe("Spigot detected!");
            getLogger()
                    .severe(" ");
            getLogger()
                    .severe("Spigot is not supported by this plugin.");
            getLogger()
                    .severe("However, Paper offers several benefits, including:");
            getLogger()
                    .severe(" ");
            getLogger()
                    .severe("Performance improvements: faster server performance and reduced lag");
            getLogger()
                    .severe("Bug fixes: resolve issues and improves overall server stability");
            getLogger()
                    .severe("Security enhancements: protect your server from potential threats");
            getLogger()
                    .severe("Timings v2: a powerful tool for diagnosing and resolving lag issues");
            getLogger()
                    .severe(" ");
            getLogger()
                    .severe("The plugin will be disabled. Consider switching to Paper for a better experience.");
            getLogger()
                    .severe("Learn how to migrate to Paper at https://docs.papermc.io/paper/migration");
            getLogger()
                    .severe("=".repeat(50));
            // getLogger because of spigot
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (VersionChecker.isLower("1.20.6")) {
            getComponentLogger()
                    .error(Component.text("=".repeat(50)));
            getComponentLogger()
                    .error(Component.text("Your server is running on a Minecraft version lower than 1.20.6."));
            getComponentLogger()
                    .error(Component.text(" "));
            getComponentLogger()
                    .error(Component.text("This plugin is not compatible with versions lower than 1.20.6."));
            getComponentLogger()
                    .error(Component.text("Please update your server to a newer version to use this plugin."));
            getComponentLogger()
                    .error(Component.text("Learn more about updating your server at https://docs.papermc.io/paper/updating"));
            getComponentLogger()
                    .error(Component.text("=".repeat(50)));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        SchedulerHolder.set(this);
        ConfigUtils.init(this);
        InventoryUtils.init(this);
        CommandAPI.set(this);
        Holder.set(this);
        AnnotationImpl.process();
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