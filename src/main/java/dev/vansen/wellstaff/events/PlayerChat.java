package dev.vansen.wellstaff.events;

import dev.vansen.utility.annotations.Register;
import dev.vansen.wellstaff.message.Messager;
import dev.vansen.wellstaff.values.impl.ChatValue;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

@Register
@SuppressWarnings("deprecation")
public final class PlayerChat implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(@NotNull AsyncChatEvent event) {
        if (!ChatValue.chat) {
            event.setCancelled(true);
            Messager.sender()
                    .who(event.getPlayer())
                    .send("chat_is_locked");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncChat(@NotNull AsyncPlayerChatEvent event) {
        if (!ChatValue.chat) {
            event.setCancelled(true);
            Messager.sender()
                    .who(event.getPlayer())
                    .send("chat_is_locked");
        }
    }
}