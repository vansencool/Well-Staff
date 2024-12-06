package dev.vansen.wellstaff.commands.random;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.command.arguments.CommandArgumentType;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.stream.IntStream;

@SuppressWarnings("all")
public class ExecuteForWorldCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("executeforworld")
                        .info(CommandInfo.info()
                                .permission(CommandPermission.OP)
                                .aliases("efw"))
                        .argument(CommandArgument.world("world"))
                        .argument(CommandArgument.integer("amount", 1))
                        .argument(CommandArgument.of("command", CommandArgumentType.command(3))
                                .defaultExecute(context -> {
                                    World world = context.argWorld("world"); // Caching, just in case of too many players
                                    Bukkit.getOnlinePlayers()
                                            .forEach(player -> {
                                                if (player.getWorld().equals(world)) {
                                                    IntStream.range(0, context.argInt("amount")).forEach(i -> player.performCommand(context.argString("command")));
                                                }
                                            });
                                }))
                , "executeforworld");
    }
}
