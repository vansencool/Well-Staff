package dev.vansen.utility.item;

import dev.vansen.configutils.Configer;
import dev.vansen.inventoryutils.item.ItemBuilder;
import dev.vansen.inventoryutils.item.ItemUtils;
import dev.vansen.libs.fastapi.java.JavaUtils;
import dev.vansen.libs.fastapi.text.Replacer;
import dev.vansen.wellstaff.records.impl.Enchanted;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public final class ConfigItem {

    @SuppressWarnings("all")
    public static ItemUtils item(@NotNull Configer config, @NotNull String path, String... placeholders) {
        if (JavaUtils.isNullOrEmpty(config.getKeys(path, false))) return null;
        AtomicReference<String> name = new AtomicReference<>(config.getString(path + ".name"));
        IntStream.range(0, placeholders.length / 2)
                .forEach(i -> {
                    name.set(Replacer.builder()
                            .target(placeholders[i * 2])
                            .replacement(placeholders[i * 2 + 1])
                            .all()
                            .build()
                            .replace(name.get()));
                });

        ItemBuilder item = ItemBuilder.create(Material.valueOf(config.getString(path + ".material")))
                .name(MiniMessage.miniMessage().deserialize("<!i>" + name.get()));

        Optional.ofNullable(config.getStringList(path + ".lore"))
                .map(lore -> lore.stream()
                        .map(loreLine -> {
                            AtomicReference<String> result = new AtomicReference<>(loreLine);
                            IntStream.range(0, placeholders.length / 2)
                                    .forEach(i -> {
                                        result.set(Replacer.builder()
                                                .target(placeholders[i * 2])
                                                .replacement(placeholders[i * 2 + 1])
                                                .all()
                                                .build()
                                                .replace(result.get()));
                                    });
                            return MiniMessage.miniMessage().deserialize("<!i>" + result.get());
                        })
                        .toList())
                .orElse(Collections.emptyList())
                .forEach(item::lore);

        JavaUtils.taskIfFalse(JavaUtils.isNullOrEmpty(config.getStringList(path + ".enchantments")), () -> config.getStringList(path + ".enchantments").stream()
                .map(e -> new Enchanted(NamespacedKey.fromString(e.split(":")[0]), Integer.parseInt(e.split(":")[1])))
                .forEach(e -> JavaUtils.taskIfNotNull(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(e.enchantment()), () -> item.enchant(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(e.enchantment()), e.level()))));

        JavaUtils.taskIfFalse(JavaUtils.isNullOrEmpty(config.getStringList(path + ".flags")), () -> config.getStringList(path + ".flags").stream()
                .map(e -> ItemFlag.valueOf(e.toUpperCase()))
                .forEach(item::itemFlags));
        return item.build();
    }
}
