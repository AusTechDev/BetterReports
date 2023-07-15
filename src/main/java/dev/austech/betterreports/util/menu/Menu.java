/*
 * BetterReports - Menu.java
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

package dev.austech.betterreports.util.menu;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.menu.defaults.paged.PagedMenu;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public abstract class Menu {
    private boolean autoUpdate = true;

    private int size = -1;
    @Setter(AccessLevel.PRIVATE)
    private Menu toReturn;

    @Getter(AccessLevel.PRIVATE)
    private String title = null;
    @Getter
    private boolean allowEditing = false;

    private ConcurrentHashMap<Integer, MenuButton> buttons = new ConcurrentHashMap<>();

    public abstract Map<Integer, MenuButton> getButtons(Player player);

    public String getPlayerTitle(Player player) {
        return getTitle();
    }

    private void initializeButtons(final Player player) {
        buttons.putAll(getButtons(player));
    }

    public Menu setReturn(final Menu menu) {
        this.toReturn = menu;
        return this;
    }

    public boolean canOpen(final Player player) {
        return true;
    }

    private Inventory create(Player player) {
        if (!(this instanceof PagedMenu))
            initializeButtons(player);

        if (getSize() == -1) {
            int highest = 0;
            for (final int buttonValue : buttons.keySet()) {
                if (buttonValue <= highest) continue;
                highest = buttonValue;
            }
            setSize((int) (Math.ceil((highest + 1) / 9.0) * 9.0));
        }

        final Inventory inventory = Bukkit.createInventory(player, getSize(), getPlayerTitle(player) != null ? Common.color(getPlayerTitle(player)) : "Menu");
        buttons.forEach((slot, button) -> inventory.setItem(slot, button.getItem(player)));

        return inventory;
    }

    public final void open(Player... players) {
        for (final Player player : players) {
            if (!canOpen(player)) {
                Common.send(player, "&cYou cannot open this menu.");
                return;
            }
            initializeButtons(player);
            player.openInventory(create(player));
            startUpdate(player);
        }
    }

    private void startUpdate(final Player player) {
        MenuManager.cancelTask(player);
        MenuManager.OPENED_MENUS.put(player.getUniqueId(), this);
        onOpen(player);

        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    MenuManager.OPENED_MENUS.remove(player.getUniqueId());
                    return;
                }

                if (isAutoUpdate()) {
                    update(player);
                }
            }
        };

        runnable.runTaskTimer(BetterReports.getInstance(), 10L, 10L); // Update every .5 seconds, with the first task starting after .5 seconds.
        MenuManager.CHECK_TASKS.put(player.getUniqueId(), runnable);
    }

    public final void update(Player player) {
        player.getOpenInventory().getTopInventory().setContents(create(player).getContents());
    }

    public void onOpen(Player player) {
    }

    public void onClose(Player player) {
    }
}
