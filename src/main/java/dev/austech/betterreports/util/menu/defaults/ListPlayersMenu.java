/*
 * BetterReports - ListPlayersMenu.java
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

package dev.austech.betterreports.util.menu.defaults;

import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.TriConsumer;
import dev.austech.betterreports.util.menu.defaults.paged.PagedMenu;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class ListPlayersMenu extends PagedMenu {
    @Override
    protected String getMenuTitle(final Player player) {
        return "Players";
    }

    abstract protected TriConsumer<InventoryClickEvent, Player, Player> getAction();

    @Override
    public Map<Integer, MenuButton> getPagedButtons(final Player player) {
        final Map<Integer, MenuButton> buttons = new HashMap<>();

        Bukkit.getOnlinePlayers().stream().filter(p -> !p.hasMetadata("vanished")).map(
                p -> MenuButton.builder()
                        .stack(
                                StackBuilder.skull(p.getName())
                                        .name(ChatColor.WHITE + (p.getDisplayName() == null ? p.getName() : p.getDisplayName()))
                        )
                        .action((e, clicked) -> getAction().accept(e, clicked, p))
                        .build()
        ).forEach(v -> buttons.put(buttons.size(), v));

        return buttons;
    }
}
