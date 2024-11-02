package dev.vansen.wellstaff.commands.random;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.command.arguments.CommandArgumentType;
import dev.vansen.wellstaff.message.Messager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

@SuppressWarnings("all")
public class ExecuteForRandomCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("executeforrandom")
                .info(CommandInfo.info()
                        .permission(CommandPermission.OP)
                        .aliases("efr"))
                .argument(CommandArgument.of("command", CommandArgumentType.command(1))
                        .defaultExecute(context -> {
                            context.throwAndRunIfNot(CommandWrapper::isPlayer, () -> {
                                Messager.sender()
                                        .who(context.sender())
                                        .send("players_only");
                            });
                            int random = new Random().nextInt(Bukkit.getOnlinePlayers().size());
                            ((Player) Bukkit.getOnlinePlayers().toArray()[random]).performCommand(context.argString("command"));
                            Messager.sender()
                                    .who(context.player())
                                    .send("player_executed", "<player>", ((Player) Bukkit.getOnlinePlayers().toArray()[random]).getName(), "<command>", context.argString("command"));
                        })), "executeforrandom");
    }
}
