package dev.vansen.wellstaff.commands.random;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.Argument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.command.arguments.CommandArgumentType;
import dev.vansen.wellstaff.message.Messager;
import org.bukkit.Bukkit;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SuppressWarnings("all")
public class ExecuteWithinCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("executewithin")
                        .info(CommandInfo.info()
                                .permission(CommandPermission.OP)
                                .aliases("ew"))
                        .argument(CommandArgument.integer("radius", 1)
                                .argument(CommandArgument.integer("amount", 1)
                                        .argument(CommandArgument.of(new Argument("command", CommandArgumentType.command(3)))
                                                .defaultExecute(context -> {
                                                    Messager.sender()
                                                            .who(context.sender())
                                                            .player();
                                                    AtomicInteger playerCount = new AtomicInteger();
                                                    Bukkit.getOnlinePlayers().forEach(p -> {
                                                        if (p.getLocation().distance(context.player().getLocation()) <= context.argInt("radius")) {
                                                            IntStream.range(0, context.argInt("amount")).forEach(i -> p.performCommand(context.argString("command")));
                                                            playerCount.getAndIncrement();
                                                        }
                                                    });

                                                    Messager.sender()
                                                            .who(context.sender())
                                                            .send("players_within_executed", "<amount>", String.valueOf(context.argInt("amount")), "<radius>", String.valueOf(context.argInt("radius")), "<player_count>", String.valueOf(playerCount.get()), "<command>", context.argString("command"));
                                                }))))
                , "executewithin");
    }
}
