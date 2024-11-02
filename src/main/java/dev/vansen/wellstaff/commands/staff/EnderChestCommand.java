package dev.vansen.wellstaff.commands.staff;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.command.arguments.PlayerArgumentType;
import dev.vansen.wellstaff.message.Messager;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public final class EnderChestCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.newCommand("enderchest")
                .info(CommandInfo.info()
                        .aliases("ec", "echest")
                        .permission(CommandPermission.permission("wellstaff.enderchest")))
                .defaultExecute(context -> {
                    context.throwAndRunIfNot(CommandWrapper::isPlayer, () -> {
                        Messager.sender()
                                .who(context.sender())
                                .send("players_only");
                    });
                    context.player().openInventory(context.player().getEnderChest());
                })
                .argument(CommandArgument.of("player", new PlayerArgumentType("See the enderchest of <player>", TextColor.fromHexString("#d4ffe4")))
                        .defaultExecute(context -> {
                            context.throwAndRunIfNot(CommandWrapper::isPlayer, () -> {
                                Messager.sender()
                                        .who(context.sender())
                                        .send("players_only");
                            });
                            context.player().openInventory(context.arg("player", Player.class).getEnderChest());
                        })), "enderchest");
    }
}
