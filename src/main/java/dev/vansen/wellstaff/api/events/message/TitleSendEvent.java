package dev.vansen.wellstaff.api.events.message;

import dev.vansen.wellstaff.api.events.eventbus.utils.Cancellable;
import dev.vansen.wellstaff.api.events.eventbus.utils.Event;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class TitleSendEvent implements Event, Cancellable {
    private boolean cancelled = false;
    private Component title;
    private Component subtitle;
    private long fadeIn;
    private long stay;
    private long fadeOut;

    public TitleSendEvent(@NotNull Component title, @NotNull Component subtitle, long fadeIn, long stay, long fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public boolean cancel() {
        return cancelled;
    }

    @Override
    public void cancel(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Component title() {
        return title;
    }

    public Component subtitle() {
        return subtitle;
    }

    public long fadeIn() {
        return fadeIn;
    }

    public long stay() {
        return stay;
    }

    public long fadeOut() {
        return fadeOut;
    }

    public void title(@NotNull Component title) {
        this.title = title;
    }

    public void subtitle(@NotNull Component subtitle) {
        this.subtitle = subtitle;
    }

    public void fadeIn(long fadeIn) {
        this.fadeIn = fadeIn;
    }

    public void stay(long stay) {
        this.stay = stay;
    }

    public void fadeOut(long fadeOut) {
        this.fadeOut = fadeOut;
    }
}