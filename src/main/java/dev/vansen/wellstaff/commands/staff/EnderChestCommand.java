package dev.vansen.wellstaff.commands.staff;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.utility.command.Command;
import dev.vansen.wellstaff.message.Messager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public final class EnderChestCommand implements Command {

    @Override
    public void register() {
        CommandUtils.newCommand("enderchest")
                .info(CommandInfo.info()
                        .aliases("ec", "echest")
                        .permission(CommandPermission.permission("wellstaff.enderchest")))
                .defaultExecute(context -> {
                    context.throwAndRunIfNot(c -> c.isPlayer(), () -> {
                        Messager.sender()
                                .who(context.sender())
                                .send("players_only");
                    });
                    context.player().openInventory(context.player().getEnderChest());
                })
                .argument(CommandArgument.string("player")
                        .defaultExecute(context -> {
                            Player target = Bukkit.getPlayer(context.argString("player"));
                            if (target == null) {
                                Messager.sender()
                                        .who(context.entity())
                                        .send("player_not_found", "<player>", context.argString("player"));
                                return;
                            }
                            context.player().openInventory(target.getEnderChest());
                        })
                        .completion((context, wrapper) -> {
                            Bukkit.getOnlinePlayers()
                                    .parallelStream()
                                    .forEach(player -> wrapper.suggest(player.getName(), "See the enderchest of " + player.getName()));
                            return wrapper.build();
                        }))
                .register();
    }
}
