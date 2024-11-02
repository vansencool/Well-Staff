package dev.vansen.utility.command.arguments;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("all")
public class PlayerArgumentType implements CustomArgumentType.Converted<Player, String> {
    private final @NotNull String tooltip;
    private final @NotNull TextColor color;

    public PlayerArgumentType(@NotNull String tooltip, @NotNull TextColor color) {
        this.tooltip = tooltip;
        this.color = color;
    }

    @Override
    public @NotNull Player convert(String nativeType) throws CommandSyntaxException {
        if (nativeType.length() < 3) {
            throw new CommandSyntaxException(new SimpleCommandExceptionType(MessageComponentSerializer.message().serialize(Component
                    .text("Too short player name!"))), MessageComponentSerializer.message().serialize(Component
                    .text("Too short player name!")));
        }
        if (nativeType.length() > 16) {
            throw new CommandSyntaxException(new SimpleCommandExceptionType(MessageComponentSerializer.message().serialize(Component
                    .text("Too long player name!"))), MessageComponentSerializer.message().serialize(Component
                    .text("Too long player name!")));
        }
        if (Bukkit.getPlayer(nativeType) == null) {
            Message message = MessageComponentSerializer.message().serialize(Component.text("Invalid player ")
                    .append(Component.text(
                            nativeType + "!"
                    ))
                    .color(TextColor.fromHexString("#ff576d")));

            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }

        return Bukkit.getPlayer(nativeType);
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Bukkit.getOnlinePlayers()
                .parallelStream()
                .forEach(player -> builder.suggest(player.getName(), MessageComponentSerializer.message()
                        .serialize(Component.text(tooltip.replaceAll("<player>", player.getName()))
                                .color(color))));
        return builder.buildFuture();
    }
}