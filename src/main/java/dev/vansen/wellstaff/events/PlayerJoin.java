package dev.vansen.wellstaff.events;

import dev.vansen.configutils.Configer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        saveLog(event.getPlayer(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Join");
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        saveLog(event.getPlayer(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Quit");
    }

    private void saveLog(@NotNull Player player, @NotNull String log) {
        Configer.loadAsync("logs/" + player.getUniqueId() + ".yml").thenAcceptAsync(config -> {
            config.set("logs." + System.currentTimeMillis(), log);
            config.saveAsync();
        });
    }
}
