package dev.vansen.wellstaff.commands.random;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.Argument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.command.arguments.CommandArgumentType;
import dev.vansen.utility.command.arguments.PlayerArgumentType;
import dev.vansen.wellstaff.message.Messager;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public class SudoCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("sudo")
                .info(CommandInfo.info()
                        .permission(CommandPermission.OP)
                        .aliases("s"))
                .argument(CommandArgument.of(new Argument("player", new PlayerArgumentType("Sudo a command from <player>", TextColor.fromHexString("#d4ffe4"))))
                        .argument(CommandArgument.of(new Argument("command", CommandArgumentType.command(2)))
                                .defaultExecute(context -> {
                                    context.throwAndRunIfNot(CommandWrapper::isPlayer, () -> {
                                        Messager.sender()
                                                .who(context.sender())
                                                .send("players_only");
                                    });
                                    context.arg("player", Player.class).performCommand(context.argString("command"));
                                    Messager.sender()
                                            .who(context.player())
                                            .send("player_executed", "<player>", context.arg("player", Player.class).getName(), "<command>", context.argString("command"));
                                }))), "sudo");
    }
}
