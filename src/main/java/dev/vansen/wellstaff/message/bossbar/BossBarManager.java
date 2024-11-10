package dev.vansen.wellstaff.message.bossbar;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("all") // do you think i care? i don't
public class BossBarManager {
    private static final Map<UUID, BossBar> playerBossBars = new HashMap<>();
    private final Player player;

    public BossBarManager(@NotNull Player player) {
        this.player = player;
    }

    public static BossBarManager.Builder builder() {
        return new BossBarManager.Builder();
    }

    public static void create(@NotNull Player player, @NotNull Component title, @NotNull BossBar.Color color, @NotNull BossBar.Overlay overlay, float progress, @NotNull Set<BossBar.Flag> flags) {
        BossBar bossBar = BossBar.bossBar(title, progress, color, overlay, flags);
        playerBossBars.put(player.getUniqueId(), bossBar);
        player.showBossBar(bossBar);
    }

    public static BossBarManager get(@NotNull Player player) {
        return new BossBarManager(player);
    }

    public BossBarManager remove() {
        BossBar bossBar = playerBossBars.remove(player.getUniqueId());
        player.hideBossBar(bossBar);
        return this;
    }

    public BossBarManager title(@NotNull Component newTitle) {
        BossBar bossBar = playerBossBars.get(player.getUniqueId());
        if (bossBar != null) {
            bossBar.name(newTitle);
        }
        return this;
    }

    public BossBarManager progress(float newProgress) {
        BossBar bossBar = playerBossBars.get(player.getUniqueId());
        if (bossBar != null) {
            bossBar.progress(newProgress);
        }
        return this;
    }

    public BossBarManager color(@NotNull BossBar.Color newColor) {
        BossBar bossBar = playerBossBars.get(player.getUniqueId());
        if (bossBar != null) {
            bossBar.color(newColor);
        }
        return this;
    }

    public BossBarManager overlay(@NotNull BossBar.Overlay newOverlay) {
        BossBar bossBar = playerBossBars.get(player.getUniqueId());
        if (bossBar != null) {
            bossBar.overlay(newOverlay);
        }
        return this;
    }

    public BossBarManager flag(@NotNull BossBar.Flag flag) {
        BossBar bossBar = playerBossBars.get(player.getUniqueId());
        if (bossBar != null) {
            bossBar.addFlag(flag);
        }
        return this;
    }

    public BossBarManager removeFlag(@NotNull BossBar.Flag flag) {
        BossBar bossBar = playerBossBars.get(player.getUniqueId());
        if (bossBar != null) {
            bossBar.removeFlag(flag);
        }
        return this;
    }

    public static class Builder {
        private final Set<BossBar.Flag> flags = new HashSet<>();
        private Player player;
        private Component title;
        private BossBar.Color color = BossBar.Color.WHITE;
        private BossBar.Overlay overlay = BossBar.Overlay.PROGRESS;
        private float progress = 1.0f;

        public Builder player(@NotNull Player player) {
            this.player = player;
            return this;
        }

        public Builder title(@NotNull Component title) {
            this.title = title;
            return this;
        }

        public Builder color(@NotNull BossBar.Color color) {
            this.color = color;
            return this;
        }

        public Builder overlay(@NotNull BossBar.Overlay overlay) {
            this.overlay = overlay;
            return this;
        }

        public Builder progress(float progress) {
            this.progress = progress;
            return this;
        }

        public Builder flag(@NotNull BossBar.Flag flag) {
            this.flags.add(flag);
            return this;
        }

        public void create() {
            BossBarManager.create(player, title, color, overlay, progress, flags);
        }
    }
}