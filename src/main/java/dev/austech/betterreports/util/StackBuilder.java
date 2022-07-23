/*
 * BetterReports - StackBuilder.java
 *
 * Copyright (c) 2022 AusTech Development
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.austech.betterreports.util;

import dev.austech.betterreports.util.xseries.XEnchantment;
import dev.austech.betterreports.util.xseries.XMaterial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class StackBuilder {
    private ItemStack item;

    public static StackBuilder create(final XMaterial material) {
        return new StackBuilder(material.parseItem());
    }

    public void applyMeta(final Consumer<ItemMeta> function) {
        final ItemMeta meta = item.getItemMeta();
        function.accept(meta);
        item.setItemMeta(meta);
    }

    public StackBuilder amount(final int amount) {
        item.setAmount(amount);
        return this;
    }


    public StackBuilder name(final String name) {
        applyMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
        return this;
    }

    public StackBuilder lore(final String... lore) {
        return lore(Arrays.asList(lore));
    }

    public StackBuilder lore(final List<String> lore) {
        final List<String> newList = lore.stream().map(str -> ChatColor.translateAlternateColorCodes('&', str)).collect(Collectors.toList());
        applyMeta(meta -> meta.setLore(newList));
        return this;
    }

    public StackBuilder enchant(final XEnchantment enchantment, final int level) {
        if (!enchantment.isSupported() || enchantment.getEnchant() == null)
            throw new IllegalArgumentException("Enchantment is not supported on this server.");

        applyMeta(meta -> meta.addEnchant(enchantment.getEnchant(), level, true));

        return this;
    }

    public StackBuilder unEnchant(final XEnchantment enchantment) {
        if (!enchantment.isSupported() || enchantment.getEnchant() == null)
            throw new IllegalArgumentException("Enchantment is not supported on this server.");

        applyMeta(meta -> meta.removeEnchant(enchantment.getEnchant()));

        return this;
    }

    public StackBuilder glow() {
        enchant(XEnchantment.LUCK, 1);
        applyMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_ENCHANTS));
        return this;
    }

    public StackBuilder removeGlow() {
        unEnchant(XEnchantment.LUCK);
        applyMeta(meta -> meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS));
        return this;
    }

    public ItemStack build() {
        return item;
    }
}
