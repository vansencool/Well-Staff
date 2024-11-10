package dev.vansen.wellstaff.commands.staff;

import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.Argument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.wellstaff.message.Messager;
import dev.vansen.wellstaff.values.impl.ChatValue;

@SuppressWarnings("all")
public final class ChatCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.newCommand("chat")
                .info(CommandInfo.info()
                        .permission(CommandPermission.permission("wellstaff.chat")))
                .defaultExecute(context -> {
                    if (!ChatValue.chat) {
                        ChatValue.chat = true;
                        Messager.sender()
                                .who(context.sender())
                                .send("chat_enabled");
                    } else {
                        ChatValue.chat = false;
                        Messager.sender()
                                .who(context.sender())
                                .send("chat_disabled");
                    }
                })
                .argument(CommandArgument.of(new Argument("enable", BoolArgumentType.bool()))
                        .defaultExecute(context -> {
                            ChatValue.chat = context.argBoolean("enable");
                            Messager.sender()
                                    .who(context.sender())
                                    .send("chat_" + (ChatValue.chat ? "enabled" : "disabled"));
                        })), "chat");
    }
}