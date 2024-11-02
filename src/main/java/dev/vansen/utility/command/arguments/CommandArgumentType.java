package dev.vansen.utility.command.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("all")
public class CommandArgumentType implements CustomArgumentType.Converted<String, String> {
    private final int argIndex;

    public CommandArgumentType(int argIndex) {
        this.argIndex = argIndex;
    }

    public static CommandArgumentType command(int argIndex) {
        return new CommandArgumentType(argIndex);
    }

    @Override
    public @NotNull String convert(String nativeType) throws CommandSyntaxException {
        return nativeType;
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.greedyString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        Bukkit.getServer().getHelpMap().getHelpTopics().stream()
                .map(topic -> topic.getName().startsWith("/") ? topic.getName().substring(1) : topic.getName())
                .filter(trimmedTopic ->
                        Optional.of(builder.getInput().split(" "))
                                .filter(parts -> parts.length > argIndex && parts[argIndex].equals(builder.getInput().split(" ")[argIndex]))
                                .map(parts -> parts[argIndex])
                                .orElse("")
                                .isEmpty() || trimmedTopic.contains(
                                Optional.of(builder.getInput().split(" "))
                                        .filter(parts -> parts.length > argIndex && parts[argIndex].equals(builder.getInput().split(" ")[argIndex]))
                                        .map(parts -> parts[argIndex])
                                        .orElse("")
                        ))
                .filter(topic -> Arrays.stream(Bukkit.getPluginManager().getPlugins())
                        .noneMatch(plugin -> plugin.getName().equals(topic)))
                .filter(topic -> builder.getInput().split(" ").length <= argIndex + 1)
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}