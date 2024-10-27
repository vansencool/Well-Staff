package dev.vansen.wellstaff.commands.staff;

import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.Argument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.utility.command.Command;
import dev.vansen.wellstaff.message.Messager;
import dev.vansen.wellstaff.values.impl.CommandSpyValue;

@SuppressWarnings("all")
public final class CommandSpyCommand implements Command {

    @Override
    public void register() {
        CommandUtils.newCommand("commandspy")
                .info(CommandInfo.info()
                        .permission(CommandPermission.permission("wellstaff.commandspy"))
                        .aliases("cmdspy", "cspy"))
                .playerExecute(context -> {
                    if (CommandSpyValue.commandSpy.containsKey(context.player()) && CommandSpyValue.commandSpy.get(context.player())) {
                        CommandSpyValue.commandSpy.put(context.player(), false);
                        Messager.sender()
                                .who(context.sender())
                                .send("command_spy_disabled");
                    } else {
                        CommandSpyValue.commandSpy.put(context.player(), true);
                        Messager.sender()
                                .who(context.sender())
                                .send("command_spy_enabled");
                    }
                })
                .consoleExecute(context -> {
                    Messager.sender()
                            .who(context.sender())
                            .send("players_only");
                })
                .argument(CommandArgument.of(new Argument("enable", BoolArgumentType.bool()))
                        .defaultExecute(context -> {
                            context.throwAndRunIfNot(CommandWrapper::isPlayer, () -> {
                                Messager.sender()
                                        .who(context.sender())
                                        .send("players_only");
                            });
                            CommandSpyValue.commandSpy.put(context.player(), context.argBoolean("enable"));
                            Messager.sender()
                                    .who(context.sender())
                                    .send("command_spy_" + (CommandSpyValue.commandSpy.get(context.player()) ? "enabled" : "disabled"));
                        }))
                .register();
    }
}
