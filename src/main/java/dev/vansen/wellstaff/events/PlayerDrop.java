package dev.vansen.wellstaff.events;

import dev.vansen.scheduleutils.SchedulerUtils;
import dev.vansen.utility.annotations.Register;
import dev.vansen.wellstaff.commands.staff.InvseeCommand;
import dev.vansen.wellstaff.values.impl.PausedValue;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

@Register
public final class PlayerDrop implements Listener {

    @EventHandler
    public void onDrop(@NotNull PlayerDropItemEvent event) {
        if (InvseeCommand.invsee.contains(event.getPlayer())) return;
        if (SchedulerUtils.cancel().exists(event.getPlayer().getName() + "_invsee")) {
            PausedValue.isPaused = true;
        }
    }
}
