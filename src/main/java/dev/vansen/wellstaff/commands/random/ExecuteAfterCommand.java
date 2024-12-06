package dev.vansen.wellstaff.commands.random;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.scheduleutils.SchedulerUtils;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.command.arguments.CommandArgumentType;
import dev.vansen.utility.time.TimeFormatter;
import dev.vansen.wellstaff.message.Messager;

@SuppressWarnings("all")
public class ExecuteAfterCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("executeafter")
                .info(CommandInfo.info()
                        .permission(CommandPermission.OP)
                        .aliases("ea"))
                .argument(CommandArgument.string("after"))
                .argument(CommandArgument.of("command", CommandArgumentType.command(2))
                        .defaultExecute(context -> {
                            Messager.sender()
                                    .who(context.sender())
                                    .player();
                            if (!TimeFormatter.valid(context.argString("after"))) {
                                Messager.sender()
                                        .who(context.sender()).send("not_valid_time", "<time>", context.argString("after"));
                                return;
                            }
                            SchedulerUtils.get(false)
                                    .later()
                                    .task(() -> context.player().performCommand(context.argString("command")))
                                    .delay(TimeFormatter.parse(context.argString("after")))
                                    .run();
                        })), "executeafter");
    }
}
