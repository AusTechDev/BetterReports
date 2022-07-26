/*
 * BetterReports - MenuButton.java
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

package dev.austech.betterreports.util.menu.layout;

import dev.austech.betterreports.util.StackBuilder;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

@Getter
@Builder(builderClassName = "Builder")
public class MenuButton {
    public MenuButton(final BiConsumer<InventoryClickEvent, Player> action, final boolean closeMenu, final ItemStack stack) {
        this.action = action;
        this.closeMenu = closeMenu;
        this.stack = stack;
    }

    protected MenuButton() {
    }

    private BiConsumer<InventoryClickEvent, Player> action;
    private boolean closeMenu;
    private ItemStack stack;

    public static class Builder {
        public Builder stack(final StackBuilder builder) {
            return stack(builder.build());
        }

        public Builder stack(final ItemStack stack) {
            this.stack = stack;
            return this;
        }
    }

    public ItemStack getStack(final Player player) {
        return stack;
    }

    public ItemStack getItem(final Player player) {
        return getStack(player);
    }
}
