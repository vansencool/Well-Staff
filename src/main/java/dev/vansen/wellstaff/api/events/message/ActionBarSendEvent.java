package dev.vansen.wellstaff.api.events.message;

import dev.vansen.wellstaff.api.events.eventbus.utils.Cancellable;
import dev.vansen.wellstaff.api.events.eventbus.utils.Event;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public final class ActionBarSendEvent implements Event, Cancellable {
    private boolean cancelled = false;
    private Component message;

    public ActionBarSendEvent(@NotNull Component message) {
        this.message = message;
    }

    @Override
    public boolean cancel() {
        return cancelled;
    }

    @Override
    public void cancel(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Component message() {
        return message;
    }

    public void message(@NotNull Component message) {
        this.message = message;
    }
}
