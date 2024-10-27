package dev.vansen.utility.player;

import dev.vansen.libs.fastapi.java.JavaUtils;
import dev.vansen.wellstaff.message.Messager;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public final class PlayerUtils {
    private final @NotNull Player player;

    public PlayerUtils(@NotNull Player player) {
        this.player = player;
    }

    public static PlayerUtils player(@NotNull Player player) {
        return new PlayerUtils(player);
    }

    @SuppressWarnings("ConstantConditions")
    public void heal() {
        JavaUtils.taskIfNotNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), () -> player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
    }

    public void fly() {
        player.setAllowFlight(true);
        Messager.sender()
                .who(player)
                .send("flight_enabled");
    }

    public void unfly() {
        player.setAllowFlight(false);
        Messager.sender()
                .who(player)
                .send("flight_disabled");
    }

    public void feed() {
        player.setFoodLevel(20);
        player.setExhaustion(20);
    }
}
