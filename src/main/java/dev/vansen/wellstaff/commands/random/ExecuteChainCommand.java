package dev.vansen.wellstaff.commands.random;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.wellstaff.message.Messager;

import java.util.Arrays;
import java.util.stream.IntStream;

@SuppressWarnings("all")
public class ExecuteChainCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("executechain")
                        .info(CommandInfo.info()
                                .permission(CommandPermission.OP)
                                .aliases("echain"))
                        .argument(CommandArgument.integer("amount", 1))
                        .argument(CommandArgument.greedy("commands")
                                .defaultExecute(context -> {
                                    Messager.sender()
                                            .who(context.sender())
                                            .player();
                                    boolean chained = context.inputAsStream().anyMatch(input -> input.equalsIgnoreCase("--chain") || input.equalsIgnoreCase("-c"));
                                    if (chained) {
                                        Arrays.stream(context.argString("commands")
                                                        .split(" ;"))
                                                .forEach(command -> IntStream.range(0, context.argInt("amount"))
                                                        .forEach(i -> context.player().performCommand(command
                                                                .replaceAll("<player>", context.player().getName())
                                                                .replaceAll("<amount>", String.valueOf(i)))));
                                        Messager.sender()
                                                .who(context.sender())
                                                .send("chained_executed", "<command>", context.argString("commands"), "<amount>", String.valueOf(context.argInt("amount")), "<chained>", String.valueOf(chained));
                                    } else {
                                        Arrays.stream(context.argString("commands")
                                                        .split(" ;"))
                                                .unordered()
                                                .forEach(command -> IntStream.range(0, context.argInt("amount"))
                                                        .forEach(i -> context.player().performCommand(command
                                                                .replaceAll("<player>", context.player().getName())
                                                                .replaceAll("<amount>", String.valueOf(i)))));
                                        Messager.sender()
                                                .who(context.sender())
                                                .send("chain_executed", "<command>", context.argString("commands"), "<amount>", String.valueOf(context.argInt("amount")), "<chained>", String.valueOf(chained));
                                    }
                                }))
                , "executechain");
    }
}
