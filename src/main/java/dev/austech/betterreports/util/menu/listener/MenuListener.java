/*
 * BetterReports - MenuListener.java
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

package dev.austech.betterreports.util.menu.listener;

import dev.austech.betterreports.util.menu.Menu;
import dev.austech.betterreports.util.menu.MenuManager;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Menu menu = MenuManager.OPENED_MENUS.get(player.getUniqueId());

        if (menu == null) {
            return;
        }

        if (menu.getButtons().containsKey(event.getSlot())) {
            final MenuButton clickedButton = menu.getButtons().get(event.getSlot());

            if (!clickedButton.isAllowEditing())
                event.setCancelled(true);

            if (clickedButton.getAction() != null)
                clickedButton.getAction().accept(event, player);

            if (clickedButton.isCloseMenu()) {
                player.closeInventory();
                return;
            }

            if (event.isCancelled()) {
                if (MenuManager.OPENED_MENUS.get(player.getUniqueId()) == menu) {
                    menu.update(player);
                }
                player.updateInventory();
            }
        } else {
            if (!menu.isAllowEditing())
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer(); // Bukkit being stupid, so we have to cast it.
        final Menu menu = MenuManager.OPENED_MENUS.get(player.getUniqueId());
        if (menu != null) {
            menu.onClose(player);
            MenuManager.cancelTask(player);
            MenuManager.OPENED_MENUS.remove(player.getUniqueId());
        }
    }
}
