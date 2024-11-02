package dev.vansen.utility.command;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.configutils.ConfigUtils;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentLinkedDeque;

public class CommandRegistrar {
    private static final ConcurrentLinkedDeque<CommandUtils> COMMANDS = new ConcurrentLinkedDeque<>();

    public static void register(@NotNull CommandUtils command, @NotNull String commandName) {
        ConfigUtils.getAsync("command.yml")
                .thenAcceptAsync(config -> {
                    if (!config.getBoolean(commandName + ".enabled", true)) return;
                    COMMANDS.add(command);
                });
    }

    public static void registerAll() {
        COMMANDS.forEach(CommandUtils::register);
    }
}
