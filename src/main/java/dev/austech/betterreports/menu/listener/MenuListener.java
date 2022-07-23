/*
 * BetterReports - MenuListener.java
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

package dev.austech.betterreports.menu.listener;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.menu.Menu;
import dev.austech.betterreports.menu.MenuManager;
import dev.austech.betterreports.menu.layout.MenuButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Menu menu = MenuManager.getCurrentlyOpenedMenus().get(player.getUniqueId());

        if (menu == null) {
            return;
        }

        if (event.getRawSlot() != event.getSlot()) {
            if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                event.setCancelled(true);
            }
            return;
        }

        if (menu.getButtons().containsKey(event.getSlot())) {
            final MenuButton clickedButton = menu.getButtons().get(event.getSlot());
            event.setCancelled(true);

            if (clickedButton.getAction() != null)
                clickedButton.getAction().accept(event, player);

            if (clickedButton.isCloseMenu()) {
                player.closeInventory();
                return;
            }

            if (event.isCancelled()) {
                Bukkit.getScheduler().runTaskLater(BetterReports.getInstance(), player::updateInventory, 1L);
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer(); // Bukkit being stupid, so we have to cast it.
        final Menu menu = MenuManager.getCurrentlyOpenedMenus().get(player.getUniqueId());
        if (menu != null) {
            menu.onClose();
            MenuManager.cancelTask(player);
            MenuManager.getCurrentlyOpenedMenus().remove(player.getUniqueId());
        }
    }
}
