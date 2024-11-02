package dev.vansen.wellstaff.commands.staff;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.configutils.Configer;
import dev.vansen.inventoryutils.inventory.FairInventory;
import dev.vansen.inventoryutils.inventory.InventorySize;
import dev.vansen.inventoryutils.item.ItemUtils;
import dev.vansen.scheduleutils.SchedulerUtils;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.item.ConfigItem;
import dev.vansen.welldevelopment.Holder;
import dev.vansen.wellstaff.message.Messager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.Map;

@SuppressWarnings("all")
public final class LogsCommand implements Command {

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.newCommand("logs")
                .info(CommandInfo.info()
                        .permission(CommandPermission.permission("wellstaff.logs")))
                .argument(CommandArgument.string("player")
                        .defaultExecute(context -> {
                            context.throwAndRunIfNot(CommandWrapper::isPlayer, () -> {
                                Messager.sender()
                                        .who(context.player())
                                        .send("players_only");
                            });
                            OfflinePlayer target = Bukkit.getOfflinePlayer(StringArgumentType.getString(context.context(), "player"));
                            if (!new File(Holder.get().getDataFolder() + "/logs/" + target.getUniqueId() + ".yml").exists()) {
                                Messager.sender()
                                        .who(context.player())
                                        .send("logs_not_found", "<player>", target.getName());
                                return;
                            }
                            Configer.loadAsync("menus/logs_menu.yml").thenAcceptAsync(config -> Configer.loadAsync("logs/" + target.getUniqueId() + ".yml").thenAcceptAsync(config2 -> SchedulerUtils.get(false).runner()
                                    .task(() -> {
                                        FairInventory menu = FairInventory.create(MiniMessage.miniMessage().deserialize(config.getString("menu.name").replaceAll("<player>", StringArgumentType.getString(context.context(), "player"))), InventorySize.rows(config.getInt("menu.rows")));
                                        int slot = 0;
                                        for (Map.Entry<String, Object> entry : config2.getConfigurationSection("logs").getValues(false).entrySet()) {
                                            if (slot >= menu.getInventory().getSize()) {
                                                break;
                                            }

                                            ItemUtils log = ConfigItem.item(config, entry.toString().endsWith("Join") ? "items.join_item" : "items.leave_item", "<timestamp>",
                                                    config2.getString("logs." + entry.getKey()).replaceAll(" Join", "").replaceAll(" Quit", ""), "<player>", StringArgumentType.getString(context.context(), "player"));
                                            menu.set(slot, log.click(event -> event.setCancelled(true)), true);
                                            slot++;
                                        }
                                        menu.show(context.player());
                                    })
                                    .run()));
                        })), "logs");
    }
}
