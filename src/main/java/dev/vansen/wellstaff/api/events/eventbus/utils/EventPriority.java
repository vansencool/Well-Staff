package dev.vansen.wellstaff.api.events.eventbus.utils;

/**
 * Defines the priority of an event.
 */
public enum EventPriority {

    /**
     * Low priority, used for a listener that should be processed at the last.
     */
    LOW(1),

    /**
     * Normal priority, used for a listener that should be processed after high and before low.
     */
    NORMAL(2),

    /**
     * High priority, used for a listener that should be processed before normal and low (or can be said as processed first).
     */
    HIGH(3);

    private final int value;

    EventPriority(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}