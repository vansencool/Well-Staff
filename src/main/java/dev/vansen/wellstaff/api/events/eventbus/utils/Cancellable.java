package dev.vansen.wellstaff.api.events.eventbus.utils;

/**
 * Used to mark a {@link dev.vansen.wellstaff.api.events.eventbus.utils.Event} as cancellable.
 */
@SuppressWarnings("unused")
public interface Cancellable {
    boolean cancel();

    void cancel(boolean cancelled);
}
