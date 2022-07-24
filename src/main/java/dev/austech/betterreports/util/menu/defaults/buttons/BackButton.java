/*
 * BetterReports - BackButton.java
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

package dev.austech.betterreports.util.menu.defaults.buttons;

import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.menu.Menu;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import dev.austech.betterreports.util.xseries.XMaterial;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class BackButton extends MenuButton {
    private final Menu toReturn;
    private BiConsumer<InventoryClickEvent, Player> onConfirm;

    public BackButton setOnConfirm(final BiConsumer<InventoryClickEvent, Player> onConfirm) {
        this.onConfirm = onConfirm;
        return this;
    }

    @Override
    public BiConsumer<InventoryClickEvent, Player> getAction() {
        return (event, player) -> {
            if (onConfirm != null) {
                onConfirm.accept(event, player);
            }
            toReturn.open(player);
        };
    }

    @Override
    public ItemStack getStack(final Player player) {
        return StackBuilder.create(XMaterial.ARROW).name("&c&lBack").lore("", "&cClick to return to", "&cthe previous menu.").build();
    }
}
