/*
 * BetterReports - Menu.java
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

package dev.austech.betterreports.menu;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.menu.defaults.paged.PagedMenu;
import dev.austech.betterreports.menu.layout.MenuButton;
import dev.austech.betterreports.util.Common;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Getter
public abstract class Menu {
    private String title;
    private boolean autoUpdate = true;
    private int size = -1;

    public String getTitle(final Player player) {
        return getTitle();
    }

    public void setTitle(final Player player, final String title) {
        setTitle(title);
    }

    @Getter
    private final ConcurrentHashMap<Integer, MenuButton> buttons = new ConcurrentHashMap<>();

    public abstract Map<Integer, MenuButton> getButtons(Player player);

    private void initializeButtons(final Player player) {
        buttons.putAll(getButtons(player));
    }

    private Inventory create(final Player player) {
        if (!(this instanceof PagedMenu))
            initializeButtons(player);

        if (size == -1) {
            int highest = 0;
            for (final int buttonValue : buttons.keySet()) {
                if (buttonValue <= highest) continue;
                highest = buttonValue;
            }
            size = (int) (Math.ceil((double) (highest + 1) / 9.0) * 9.0);
        }

        final Inventory inventory = Bukkit.createInventory(player, size, getTitle(player) != null ? Common.color(getTitle(player)) : "Menu");
        buttons.forEach((slot, button) -> {
            inventory.setItem(slot, button.getItem(player));
        });

        return inventory;
    }

    public void open(final Player... players) {
        for (final Player player : players) {
            initializeButtons(player);
            player.openInventory(create(player));
            update(player);
        }
    }

    private void update(final Player player) {
        MenuManager.cancelTask(player);
        MenuManager.getCurrentlyOpenedMenus().put(player.getUniqueId(), this);

        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    MenuManager.getCurrentlyOpenedMenus().remove(player.getUniqueId());
                    return;
                }

                if (autoUpdate) {
                    player.getOpenInventory().getTopInventory().setContents(create(player).getContents());
                }
            }
        };

        runnable.runTaskTimer(BetterReports.getInstance(), 10L, 10L); // Update every .5 seconds, with the first task starting after .5 seconds.
        MenuManager.getCheckTasks().put(player.getUniqueId(), runnable);
    }

    public void onClose() {
    }
}
