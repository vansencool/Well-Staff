package dev.vansen.wellstaff.api.events.message;

import dev.vansen.wellstaff.api.events.eventbus.utils.Cancellable;
import dev.vansen.wellstaff.api.events.eventbus.utils.Event;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class SoundSendEvent implements Event, Cancellable {
    private boolean cancelled = false;
    private Sound.Builder sound;

    public SoundSendEvent(@NotNull Sound.Builder sound) {
        this.sound = sound;
    }

    @Override
    public boolean cancel() {
        return cancelled;
    }

    @Override
    public void cancel(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Sound.Builder sound() {
        return sound;
    }

    public void sound(@NotNull Sound.Builder sound) {
        this.sound = sound;
    }
}
