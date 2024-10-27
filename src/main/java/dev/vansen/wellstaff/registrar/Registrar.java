package dev.vansen.wellstaff.registrar;

import dev.vansen.utility.debugging.Debug;
import dev.vansen.utility.resource.ResourceSaver;
import dev.vansen.welldevelopment.Holder;
import dev.vansen.wellstaff.commands.staff.*;
import dev.vansen.wellstaff.events.PlayerChat;
import dev.vansen.wellstaff.events.PlayerCommand;
import dev.vansen.wellstaff.events.PlayerDrop;
import dev.vansen.wellstaff.events.PlayerJoin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public final class Registrar {

    public static void register() {
        Debug.debug(Component.text(
                "Registering commands"
        ));
        new ChatCommand().register();
        new CommandSpyCommand().register();
        new InvseeCommand().register();
        new EnderChestCommand().register();
        new FlyCommand().register();
        new LogsCommand().register();
        Debug.debug(Component.text(
                "Registering listeners"
        ));
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChat(), Holder.get());
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerCommand(), Holder.get());
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDrop(), Holder.get());
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), Holder.get());
        Debug.debug(Component.text(
                "Saving resources"
        ));
        ResourceSaver.save();
    }
}
