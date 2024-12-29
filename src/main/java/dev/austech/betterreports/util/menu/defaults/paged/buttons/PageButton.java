/*
 * BetterReports - PageButton.java
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

package dev.austech.betterreports.util.menu.defaults.paged.buttons;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.config.impl.GuiConfig;
import dev.austech.betterreports.util.menu.defaults.paged.ListPageMenu;
import dev.austech.betterreports.util.menu.defaults.paged.PagedMenu;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import dev.austech.betterreports.util.xseries.XMaterial;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class PageButton extends MenuButton {
    private final int mod;
    private final int current;
    private final PagedMenu pagedMenu;

    @Getter
    @Setter
    public boolean shouldError;

    private boolean hasNext(final Player player) {
        final int paginate = pagedMenu.getPage() + this.mod;
        return paginate > 0 && pagedMenu.getPages(player) >= paginate;
    }

    @Override
    public BiConsumer<InventoryClickEvent, Player> getAction() {
        return (event, player) -> {
            if (event.getClick().isLeftClick()) {
                if (hasNext(player)) {
                    pagedMenu.changePage(player, this.mod);
                } else {
                    GuiConfig.Values.SOUNDS_GENERIC_ERROR.playSound(player);
                    setShouldError(true);
                    event.setCurrentItem(getItem(player));
                    Bukkit.getScheduler().runTaskLater(BetterReports.getInstance(), () -> setShouldError(false), 30);
                }
            } else {
                new ListPageMenu(pagedMenu, current).open(player);
            }
        };
    }

    @Override
    public ItemStack getItem(Player player) {
        if (isShouldError()) {
            return GuiConfig.Values.PAGINATED_MENU_ERROR_BUTTON.getStack().build();
        }

        if (hasNext(player)) {
            if (this.mod > 0) {
                return GuiConfig.Values.PAGINATED_MENU_PAGE_BUTTON_NEXT.getStack().build();
            } else {
                return GuiConfig.Values.PAGINATED_MENU_PAGE_BUTTON_PREVIOUS.getStack().build();
            }
        } else {
            if (this.mod > 0) {
                return GuiConfig.Values.PAGINATED_MENU_PAGE_BUTTON_LAST.getStack().build();
            } else {
                return GuiConfig.Values.PAGINATED_MENU_PAGE_BUTTON_FIRST.getStack().build();
            }
        }
    }
}
