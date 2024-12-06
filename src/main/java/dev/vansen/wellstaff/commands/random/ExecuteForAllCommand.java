package dev.vansen.wellstaff.commands.random;

import dev.vansen.commandutils.CommandUtils;
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
public class ExecuteForAllCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("executeforall")
                .info(CommandInfo.info()
                        .permission(CommandPermission.OP)
                        .aliases("efa"))
                .argument(CommandArgument.integer("amount", 1))
                .argument(CommandArgument.of("command", CommandArgumentType.command(2))
                        .defaultExecute(context -> {
                            AtomicInteger playerCount = new AtomicInteger();
                            Bukkit.getOnlinePlayers().forEach(p -> {
                                IntStream.range(0, context.argInt("amount")).forEach(i -> p.performCommand(context.argString("command")));
                                playerCount.getAndIncrement();
                            });
                            Messager.sender()
                                    .who(context.sender())
                                    .send("all_players_executed", "<amount>", String.valueOf(context.argInt("amount")), "<player_count>", String.valueOf(playerCount.get()), "<command>", context.argString("command"));
                        })), "executeforall");
    }
}
