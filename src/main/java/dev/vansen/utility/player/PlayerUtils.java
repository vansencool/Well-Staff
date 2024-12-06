package dev.vansen.utility.player;

import dev.vansen.libs.fastapi.java.JavaUtils;
import dev.vansen.scheduleutils.SchedulerUtils;
import dev.vansen.wellstaff.message.Messager;
import dev.vansen.wellstaff.message.bossbar.BossBarManager;
import net.kyori.adventure.bossbar.BossBar;
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
        JavaUtils.taskIfNotNull(player.getAttribute(Attribute.MAX_HEALTH).getValue(), () -> player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue()));
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
                .send("game_mode_set", "<game_mode>", gameMode.name().charAt(0) + gameMode.name().substring(1).toLowerCase());
    }

    public void startMonitoring() {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExhaustion(20);
        player.setSaturation(20);
        player.setGameMode(GameMode.CREATIVE);

        BossBarManager.builder()
                .player(player)
                .title(generate())
                .color(color())
                .overlay(BossBar.Overlay.NOTCHED_20)
                .progress(progress())
                .flag(BossBar.Flag.PLAY_BOSS_MUSIC)
                .create();

        SchedulerUtils.get(true)
                .repeater()
                .task(this::updateBossBar)
                .repeatsForever()
                .repeats(Duration.ofMillis(700))
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
        BossBarManager.get(player)
                .remove();
    }

    private void updateBossBar() {
        BossBarManager.get(player)
                .title(generate())
                .color(color())
                .progress(progress());
    }

    private Component generate() {
        return Component.text(String.format("TPS: %.2f  MSPT: %.2f  Ping: %dms", Math.min(20.0, Bukkit.getTPS()[0]), Bukkit.getAverageTickTime(), player.getPing()))
                .color(TextColor.color(statusColor()));
    }

    private BossBar.Color color() {
        if (Math.min(20.0, Bukkit.getTPS()[0]) >= 19 && Bukkit.getAverageTickTime() <= 30)
            return BossBar.Color.GREEN; // Good
        else if (Math.min(20.0, Bukkit.getTPS()[0]) >= 17 && Bukkit.getAverageTickTime() <= 50)
            return BossBar.Color.YELLOW; // Mid
        return BossBar.Color.RED; // Bad
    }

    private float progress() {
        return (float) Math.min(1.0, Math.max(0.1, (Bukkit.getAverageTickTime() / 60.0))); // 0.1 - 1.0
    }

    private int statusColor() {
        if (Math.min(20.0, Bukkit.getTPS()[0]) >= 19 && Bukkit.getAverageTickTime() <= 30) return 0x81FF85; // Green
        if (Math.min(20.0, Bukkit.getTPS()[0]) >= 17 && Bukkit.getAverageTickTime() <= 50) return 0xFFF57F; // Yellow
        return 0xFF7A7A; // Red
    }
}
