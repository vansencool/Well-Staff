package dev.vansen.wellstaff.api.events.eventbus.utils;

public enum EventPriority {
    LOW(1), NORMAL(2), HIGH(3);

    private final int value;

    EventPriority(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}