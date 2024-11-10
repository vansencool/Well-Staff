package dev.vansen.wellstaff.commands.staff;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.inventoryutils.inventory.FairInventory;
import dev.vansen.inventoryutils.inventory.InventorySize;
import dev.vansen.scheduleutils.SchedulerUtils;
import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.command.Command;
import dev.vansen.utility.command.CommandRegistrar;
import dev.vansen.utility.command.arguments.PlayerArgumentType;
import dev.vansen.wellstaff.message.Messager;
import dev.vansen.wellstaff.values.impl.PausedValue;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("all")
public final class InvseeCommand implements Command {
    public static Set<Player> invsee = new HashSet<>();

    @Override
    @Init
    public void register() {
        CommandRegistrar.register(CommandUtils.newCommand("invsee")
                .info(CommandInfo.info()
                        .permission(CommandPermission.permission("wellstaff.invsee"))
                        .aliases("ic", "is"))
                .argument(CommandArgument.of("player", new PlayerArgumentType("See the inventory of <player>", TextColor.fromHexString("#d4ffe4")))
                        .defaultExecute(context -> {
                            Messager.sender()
                                    .who(context.sender())
                                    .player();
                            Player target = context.arg("player", Player.class);
                            if (target.getName().equals(context.player().getName())) {
                                Messager.sender()
                                        .who(context.player())
                                        .send("cant_invsee_self");
                                return;
                            }
                            FairInventory menu = FairInventory.create("Inventory", InventorySize.rows(6))
                                    .close(event -> {
                                        SchedulerUtils.cancel()
                                                .cancel(context.player().getName() + "_invsee");
                                        invsee.remove(context.player());
                                    });
                            for (int i = 0; i < 36; i++) {
                                menu.set(i, target.getInventory().getItem(i), false);
                            }

                            menu.set(36, target.getInventory().getHelmet(), false);
                            menu.set(37, target.getInventory().getChestplate(), false);
                            menu.set(38, target.getInventory().getLeggings(), false);
                            menu.set(39, target.getInventory().getBoots(), false);
                            menu.set(40, target.getInventory().getItemInOffHand(), false);
                            invsee.add(context.player());
                            menu.show(context.player());
                            SchedulerUtils.get(false).repeater()
                                    .task(() -> {
                                        PlayerInventory targetInventory = target.getInventory();
                                        if (PausedValue.isPaused) {
                                            PausedValue.isPaused = false;
                                            for (int i = 0; i < 36; i++) {
                                                menu.set(i, targetInventory.getItem(i), false);
                                            }

                                            menu.set(36, targetInventory.getHelmet(), false);
                                            menu.set(37, targetInventory.getChestplate(), false);
                                            menu.set(38, targetInventory.getLeggings(), false);
                                            menu.set(39, targetInventory.getBoots(), false);
                                            menu.set(40, targetInventory.getItemInOffHand(), false);
                                            return;
                                        }

                                        for (int i = 0; i < 36; i++) {
                                            targetInventory.setItem(i, menu.getInventory().getItem(i));
                                        }

                                        targetInventory.setHelmet(menu.getInventory().getItem(36));
                                        targetInventory.setChestplate(menu.getInventory().getItem(37));
                                        targetInventory.setLeggings(menu.getInventory().getItem(38));
                                        targetInventory.setBoots(menu.getInventory().getItem(39));
                                        targetInventory.setItemInOffHand(menu.getInventory().getItem(40));
                                        for (int i = 0; i < 36; i++) {
                                            menu.set(i, targetInventory.getItem(i), false);
                                        }

                                        menu.set(36, targetInventory.getHelmet(), false);
                                        menu.set(37, targetInventory.getChestplate(), false);
                                        menu.set(38, targetInventory.getLeggings(), false);
                                        menu.set(39, targetInventory.getBoots(), false);
                                        menu.set(40, targetInventory.getItemInOffHand(), false);
                                    })
                                    .uniqueId(target.getName() + "_invsee")
                                    .repeats(Duration.ofMillis(800))
                                    .repeatsForever()
                                    .run();
                        })), "invsee");
    }
}
