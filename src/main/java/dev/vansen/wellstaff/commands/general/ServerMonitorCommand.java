package dev.vansen.wellstaff.commands.general;

import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.Argument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.ExecutableSender;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.commandutils.sender.SenderTypes;
import dev.vansen.scheduleutils.SchedulerUtils;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.player.PlayerUtils;
import dev.vansen.wellstaff.message.Messager;

@SuppressWarnings("all")
public class ServerMonitorCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("servermonitor")
                .info(CommandInfo.info()
                        .permission(CommandPermission.permission("wellstaff.servermonitor"))
                        .aliases("sm"))
                .defaultExecute(context -> {
                    if (SchedulerUtils.cancel().exists(context.player().getUniqueId() + "_monitoring")) {
                        PlayerUtils.player(context.player())
                                .stopMonitoring();
                    } else {
                        PlayerUtils.player(context.player())
                                .startMonitoring();
                    }
                }, ExecutableSender.types(SenderTypes.PLAYER))
                .argument(CommandArgument.of(new Argument("enable", BoolArgumentType.bool()))
                        .defaultExecute(context -> {
                            Messager.sender()
                                    .who(context.sender())
                                    .player();
                            if (context.argBoolean("enable")) {
                                PlayerUtils.player(context.player())
                                        .stopMonitoring();
                            } else {
                                PlayerUtils.player(context.player())
                                        .startMonitoring();
                            }
                        })), "servermonitor");
    }
}
