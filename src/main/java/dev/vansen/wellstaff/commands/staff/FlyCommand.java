package dev.vansen.wellstaff.commands.staff;

import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.Argument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.player.PlayerUtils;
import dev.vansen.wellstaff.message.Messager;

@SuppressWarnings("all")
public final class FlyCommand implements Command {

    @Override
    public void register() {
        CommandUtils.newCommand("fly")
                .info(CommandInfo.info()
                        .aliases("flymode")
                        .permission(CommandPermission.permission("wellstaff.fly")))
                .defaultExecute(context -> {
                    context.throwAndRunIfNot(c -> c.isPlayer(), () -> {
                        Messager.sender()
                                .who(context.sender())
                                .send("players_only");
                    });
                    if (context.player().getAllowFlight()) {
                        PlayerUtils.player(context.player())
                                .unfly();
                    } else {
                        PlayerUtils.player(context.player())
                                .fly();
                    }
                })
                .argument(CommandArgument.of(new Argument("enable", BoolArgumentType.bool()))
                        .defaultExecute(context -> {
                            context.throwAndRunIfNot(c -> c.isPlayer(), () -> {
                                Messager.sender()
                                        .who(context.sender())
                                        .send("players_only");
                            });
                            if (context.argBoolean("enable")) {
                                PlayerUtils.player(context.player())
                                        .fly();
                            } else {
                                PlayerUtils.player(context.player())
                                        .unfly();
                            }
                        }))
                .register();
    }
}
