/*
 * BetterReports - PageButton.java
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

package dev.austech.betterreports.util.menu.defaults.paged.buttons;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.menu.defaults.paged.ListPageMenu;
import dev.austech.betterreports.util.menu.defaults.paged.PagedMenu;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import dev.austech.betterreports.util.xseries.XMaterial;
import dev.austech.betterreports.util.xseries.XSound;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class PageButton extends MenuButton {
    private int mod;
    private int current;
    private PagedMenu pagedMenu;

    public PageButton(final int mod, final int current, final PagedMenu pagedMenu) {
        this.mod = mod;
        this.current = current;
        this.pagedMenu = pagedMenu;
    }

    @Getter
    @Setter
    public boolean shouldError;

    private boolean hasNext(final Player player) {
        final int paginate = pagedMenu.getPage() + this.mod;
        return paginate > 0 && pagedMenu.getPages(player) >= paginate;
    }

    @Override
    public boolean isCloseMenu() {
        return false;
    }

    @Override
    public BiConsumer<InventoryClickEvent, Player> getAction() {
        return (event, player) -> {
            if (event.getClick().isLeftClick()) {
                if (hasNext(player)) {
                    pagedMenu.changePage(player, this.mod);
                } else {
                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1, 1);
                    setShouldError(true);
                    event.setCurrentItem(getStack(player));
                    Bukkit.getScheduler().runTaskLater(BetterReports.getInstance(), () -> setShouldError(false), 30);
                }
            } else {
                new ListPageMenu(pagedMenu, current).open(player);
            }
        };
    }

    @Override
    public ItemStack getStack(final Player player) {
        if (isShouldError()) {
            return StackBuilder.create(XMaterial.RED_CARPET)
                    .name(Common.color("&c&lError"))
                    .lore(Common.color("&cThere are no more pages in this direction."))
                    .build();
        }

        if (!hasNext(player))
            return StackBuilder.create(XMaterial.GRAY_CARPET).name("&7" + (this.mod > 0 ? "You are on the last page." : "You are on the first page.")).build();
        else
            return StackBuilder.create(XMaterial.BLUE_CARPET).name(this.mod > 0 ? "&aNext Page &l⟶" : "&c&l⟵ &cPrevious Page").build();
    }
}
