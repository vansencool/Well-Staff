package dev.vansen.wellstaff.commands.random;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.Argument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.commandutils.subcommand.SubCommand;
import dev.vansen.libs.fastapi.text.Replacer;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.command.arguments.CommandArgumentType;
import dev.vansen.utility.command.arguments.PlayerArgumentType;
import dev.vansen.wellstaff.message.Messager;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

@SuppressWarnings("all")
public class ExecuteAsCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("executeas")
                .info(CommandInfo.info()
                        .permission(CommandPermission.OP)
                        .aliases("execas"))
                .subCommand(SubCommand.of("console")
                        .argument(CommandArgument.integer("amount", 1)
                                .argument(CommandArgument.of(new Argument("command", CommandArgumentType.command(3)))
                                        .defaultExecute(context -> {
                                            IntStream.range(0, context.argInt("amount")).forEach(i -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Replacer.builder()
                                                    .target("<player>")
                                                    .replacement(context.player().getName())
                                                    .all()
                                                    .build()
                                                    .replace(Replacer.builder()
                                                            .target("<amount>")
                                                            .replacement(String.valueOf(context.argInt("amount")))
                                                            .all()
                                                            .build()
                                                            .replace(Replacer.builder()
                                                                    .target("<command>")
                                                                    .replacement(context.argString("command"))
                                                                    .all()
                                                                    .build()
                                                                    .replace(context.argString("command"))))));
                                            Messager.sender()
                                                    .who(context.sender())
                                                    .send("console_command_executed", "<command>", context.argString("command"), "<amount>", String.valueOf(context.argInt("amount")));
                                        }))))
                .subCommand(SubCommand.of("player")
                        .argument(CommandArgument.of(new Argument("player", new PlayerArgumentType("Execute as <player>", TextColor.fromHexString("#d4ffe4"))))
                                .argument(CommandArgument.integer("amount", 1)
                                        .argument(CommandArgument.of(new Argument("command", CommandArgumentType.command(4)))
                                                .defaultExecute(context -> {
                                                    IntStream.range(0, context.argInt("amount")).forEach(i -> context.arg("player", Player.class).performCommand(Replacer.builder()
                                                            .target("<target>")
                                                            .replacement(context.arg("player", Player.class).getName())
                                                            .all()
                                                            .build()
                                                            .replace(Replacer.builder()
                                                                    .target("<player>")
                                                                    .replacement(context.player().getName())
                                                                    .all()
                                                                    .build()
                                                                    .replace(Replacer.builder()
                                                                            .target("<amount>")
                                                                            .replacement(String.valueOf(context.argInt("amount")))
                                                                            .all()
                                                                            .build()
                                                                            .replace(Replacer.builder()
                                                                                    .target("<command>")
                                                                                    .replacement(context.argString("command"))
                                                                                    .all()
                                                                                    .build()
                                                                                    .replace(context.argString("command")))))));
                                                    Messager.sender()
                                                            .who(context.sender())
                                                            .send("player_command_executed", "<player>", context.arg("player", Player.class).getName(), "<command>", context.argString("command"), "<amount>", String.valueOf(context.argInt("amount")));
                                                }))))), "executeas");
    }
}
