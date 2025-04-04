package dev.vansen.wellstaff.message.title;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class TitleSender {

    public static void send(@NotNull CommandSender sender, @NotNull Component titleMessage, @NotNull Component subtitleMessage, double fadeIn, double stay, double fadeOut) {
        Title title = Title.title(
                titleMessage,
                subtitleMessage,
                Title.Times.times(
                        Duration.ofMillis((long) fadeIn * 1000L),
                        Duration.ofMillis((long) stay * 1000L),
                        Duration.ofMillis((long) fadeOut * 1000L)
                )
        );

        sender.showTitle(title);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CommandSender sender;
        private Component titleMessage;
        private Component subtitleMessage;
        private double fadeIn;
        private double stay;
        private double fadeOut;

        public Builder who(@NotNull CommandSender sender) {
            this.sender = sender;
            return this;
        }

        public Builder title(@NotNull Component titleMessage) {
            this.titleMessage = titleMessage;
            return this;
        }

        public Builder subtitle(@NotNull Component subtitleMessage) {
            this.subtitleMessage = subtitleMessage;
            return this;
        }

        public Builder fadeIn(double fadeIn) {
            this.fadeIn = fadeIn;
            return this;
        }

        public Builder stay(double stay) {
            this.stay = stay;
            return this;
        }

        public Builder fadeOut(double fadeOut) {
            this.fadeOut = fadeOut;
            return this;
        }

        public void send() {
            TitleSender.send(sender, titleMessage, subtitleMessage, fadeIn, stay, fadeOut);
        }
    }
}