package dev.vansen.utility.player;

import dev.vansen.libs.fastapi.java.JavaUtils;
import dev.vansen.scheduleutils.SchedulerUtils;
import dev.vansen.wellstaff.message.Messager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

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

    public void gameMode(@NotNull GameMode gameMode) {
        player.setGameMode(gameMode);
        Messager.sender()
                .who(player)
                .send("game_mode_set", "<game_mode>", gameMode.name().substring(0, 1).toUpperCase() + gameMode.name().substring(1).toLowerCase());
    }

    public void startMonitoring() {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExhaustion(20);
        player.setSaturation(20);
        player.setGameMode(GameMode.CREATIVE);
        SchedulerUtils.get(true)
                .repeater()
                .task(() -> player.sendActionBar(Component.text(
                                String.format("TPS: %.2f | MSPT: %.2f", Bukkit.getTPS()[0] > 20.00 ? 20.00 : Bukkit.getTPS()[0], Bukkit.getAverageTickTime()))
                        .color(TextColor.fromHexString("#81ff85"))
                ))
                .repeatsForever()
                .repeats(Duration.ofMillis(700))
                .delay(Duration.ofSeconds(1))
                .uniqueId(player.getUniqueId() + "_monitoring")
                .run();
        Messager.sender()
                .who(player)
                .send("monitoring_started");
    }

    public void stopMonitoring() {
        SchedulerUtils.get(true)
                .canceller()
                .cancel(player.getUniqueId() + "_monitoring");
        Messager.sender()
                .who(player)
                .send("monitoring_stopped");
    }

    public void staffMode() {

    }
}
