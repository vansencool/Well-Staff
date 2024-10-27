package dev.vansen.wellstaff.api.events.eventbus.utils;

@SuppressWarnings("unused")
public interface Cancellable {
    boolean cancel();

    void cancel(boolean cancelled);
}
