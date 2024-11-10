package dev.vansen.utility.source;

import dev.vansen.welldevelopment.Holder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public final class SoundSource {

    /**
     * Converts a string to a {@link Sound.Source}
     *
     * @param source The source of the sound
     * @return The sound source
     */
    public static Sound.Source fromString(@NotNull String source) {
        try {
            return Sound.Source.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            Holder.get()
                    .getComponentLogger()
                    .error(Component.text("Invalid sound source: " + source + " (defaulting to player)"));
            return Sound.Source.PLAYER;
        }
    }
}
