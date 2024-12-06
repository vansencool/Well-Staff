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
public class ExecuteTillForCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("executetillfor")
                        .info(CommandInfo.info()
                                .permission(CommandPermission.OP)
                                .aliases("etf"))
                        .argument(CommandArgument.string("delay"))
                        .argument(CommandArgument.string("for"))
                        .argument(CommandArgument.of("command", CommandArgumentType.command(3))
                                .defaultExecute(context -> {
                                    Messager.sender()
                                            .who(context.sender())
                                            .player();
                                    if (!TimeFormatter.valid(context.argString("delay"))) {
                                        Messager.sender()
                                                .who(context.sender())
                                                .send("not_valid_time", "<time>", context.argString("delay"));
                                        return;
                                    }
                                    if (!TimeFormatter.valid(context.argString("for"))) {
                                        Messager.sender()
                                                .who(context.sender())
                                                .send("not_valid_time", "<time>", context.argString("for"));
                                        return;
                                    }

                                    SchedulerUtils.get(false)
                                            .repeater()
                                            .task(() -> {
                                                context.player().performCommand(context.argString("command"));
                                            })
                                            .repeats(TimeFormatter.parse(context.argString("delay")))
                                            .repeatsFor(TimeFormatter.parse(context.argString("for")))
                                            .run();
                                    SchedulerUtils.get(false)
                                            .later()
                                            .delay(TimeFormatter.parse(context.argString("for")))
                                            .task(() -> {
                                                Messager.sender()
                                                        .who(context.sender())
                                                        .send("command_finished", "<command>", context.argString("command"));
                                            })
                                            .run();
                                }))
                , "executetillfor");
    }
}
