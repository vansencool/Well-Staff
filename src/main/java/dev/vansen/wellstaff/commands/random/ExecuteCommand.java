package dev.vansen.wellstaff.commands.random;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.Argument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.commandutils.subcommand.SubCommand;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.command.arguments.CommandArgumentType;
import dev.vansen.utility.command.arguments.PlayerArgumentType;
import dev.vansen.wellstaff.message.Messager;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public class ExecuteCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("execute")
                .info(CommandInfo.info()
                        .permission(CommandPermission.OP)
                        .aliases("exec"))
                .subCommand(SubCommand.of("console")
                        .argument(CommandArgument.integer("amount")
                                .argument(CommandArgument.of(new Argument("command", CommandArgumentType.command(3)))
                                        .defaultExecute(context -> {
                                            context.throwAndRunIfNot(CommandWrapper::isPlayer, () -> {
                                                Messager.sender()
                                                        .who(context.sender())
                                                        .send("consoles_only");
                                            });
                                            for (int i = 0; i < context.argInt("amount"); i++) {
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), context.argString("command"));
                                            }
                                            Messager.sender()
                                                    .who(context.player())
                                                    .send("console_command_executed", "<command>", context.argString("command"), "<amount>", String.valueOf(context.argInt("amount")));
                                        }))))
                .subCommand(SubCommand.of("player")
                        .argument(CommandArgument.of(new Argument("player", new PlayerArgumentType("Execute as <player>", TextColor.fromHexString("#d4ffe4"))))
                                .argument(CommandArgument.integer("amount")
                                        .argument(CommandArgument.of(new Argument("command", CommandArgumentType.command(4)))
                                                .defaultExecute(context -> {
                                                    context.throwAndRunIfNot(CommandWrapper::isPlayer, () -> {
                                                        Messager.sender()
                                                                .who(context.sender())
                                                                .send("players_only");
                                                    });
                                                    for (int i = 0; i < context.argInt("amount"); i++) {
                                                        context.arg("player", Player.class).performCommand(context.argString("command"));
                                                    }
                                                    Messager.sender()
                                                            .who(context.player())
                                                            .send("player_command_executed", "<player>", context.arg("player", Player.class).getName(), "<command>", context.argString("command"), "<amount>", String.valueOf(context.argInt("amount")));
                                                }))))), "execute");
    }
}
