package dev.vansen.wellstaff.events;

import dev.vansen.utility.debugging.Debug;
import dev.vansen.wellstaff.message.Messager;
import dev.vansen.wellstaff.values.impl.CommandSpyValue;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerCommand implements Listener {

    @EventHandler
    public void onCommand(@NotNull PlayerCommandPreprocessEvent event) {
        Debug.debug("Command: " + event.getMessage() + " by " + event.getPlayer().getName());
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> CommandSpyValue.commandSpy.containsKey(player) && CommandSpyValue.commandSpy.get(player))
                .forEach(player -> Messager.sender()
                        .who(player)
                        .send("command_spy", "<message>", event.getMessage(), "<player>", event.getPlayer().getName()));
    }
}
