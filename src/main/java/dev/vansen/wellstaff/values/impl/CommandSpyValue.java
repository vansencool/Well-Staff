package dev.vansen.wellstaff.values.impl;

import dev.vansen.wellstaff.values.Valued;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class CommandSpyValue implements Valued<Map<Player, Boolean>> {
    public static ConcurrentMap<Player, Boolean> commandSpy = new ConcurrentHashMap<>();
}
