package dev.vansen.wellstaff.commands.random;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;

@SuppressWarnings("all")
public class SayFromCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.command("sayfrom")
                        .info(CommandInfo.info()
                                .permission(CommandPermission.OP)
                                .aliases("sf"))
                        .argument(CommandArgument.player("player"))
                        .argument(CommandArgument.greedy("message")
                                .defaultExecute(context -> {
                                    context.argPlayer("player").chat(context.argString("message"));
                                }))
                , "sayfrom");
    }
}
