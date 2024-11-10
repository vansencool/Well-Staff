package dev.vansen.wellstaff.records.impl;

import dev.vansen.wellstaff.records.Record;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

/**
 * A record that can be used to make an enchantment
 */
public record Enchanted(NamespacedKey enchantment, int level) implements Record<Enchantment> {
}