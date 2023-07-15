/*
 * BetterReports - StackBuilder.java
 *
 * Copyright (c) 2023 AusTech Development
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

import dev.austech.betterreports.util.xseries.XMaterial;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class StackBuilder {
    private final ItemStack item;
    private String type;

    public static StackBuilder create(final XMaterial material) {
        if (material == null || !material.isSupported()) {
            throw new IllegalArgumentException("Material \"" + material + "\" is not supported");
        }

        return new StackBuilder(material.parseItem());
    }

    public static StackBuilder from(final ItemStack item) {
        return new StackBuilder(item);
    }

    public void applyMeta(final Consumer<ItemMeta> function) {
        final ItemMeta meta = item.getItemMeta();
        function.accept(meta);
        item.setItemMeta(meta);
    }

    public StackBuilder type(final String type) {
        this.type = type;
        return this;
    }

    public StackBuilder amount(final int amount) {
        item.setAmount(amount);
        return this;
    }


    public StackBuilder name(final String name) {
        applyMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
        return this;
    }

    public StackBuilder lore(final String lore) {
        if (lore == null || lore.isEmpty()) return this;
        return lore(lore.split("\n"));
    }


    public StackBuilder lore(final String... lore) {
        return lore(Arrays.asList(lore));
    }

    public StackBuilder lore(final List<String> lore) {
        final List<String> newList = lore.stream().map(str -> ChatColor.translateAlternateColorCodes('&', str)).collect(Collectors.toList());
        applyMeta(meta -> meta.setLore(newList));
        return this;
    }

    public StackBuilder enchant(final Enchantment enchantment, final int level) {
        applyMeta(meta -> meta.addEnchant(enchantment, level, true));

        return this;
    }

    public StackBuilder unEnchant(final Enchantment enchantment) {
        applyMeta(meta -> meta.removeEnchant(enchantment));

        return this;
    }

    public StackBuilder glow(final boolean bool) {
        if (bool) glow();
        else removeGlow();
        return this;
    }

    public StackBuilder glow() {
        enchant(Enchantment.LUCK, 1);
        applyMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_ENCHANTS));
        return this;
    }

    public StackBuilder removeGlow() {
        unEnchant(Enchantment.LUCK);
        applyMeta(meta -> meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS));
        return this;
    }

    public static StackBuilder skull(final String owner) {
        final StackBuilder builder = create(XMaterial.PLAYER_HEAD);
        builder.applyMeta(meta -> {
            final SkullMeta skullMeta = (SkullMeta) meta;
            skullMeta.setOwner(owner);
        });

        return builder;
    }

    public ItemStack build() {
        return item;
    }
}
