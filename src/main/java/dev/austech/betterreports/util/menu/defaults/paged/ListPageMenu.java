/*
 * BetterReports - ListPageMenu.java
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

package dev.austech.betterreports.util.menu.defaults.paged;

import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.config.impl.GuiConfig;
import dev.austech.betterreports.util.menu.Menu;
import dev.austech.betterreports.util.menu.defaults.buttons.BackButton;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import dev.austech.betterreports.util.xseries.XMaterial;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ListPageMenu extends Menu {
    private final PagedMenu menu;
    private final int current;

    @Override
    public String getTitle(final Player player) {
        return GuiConfig.Values.PAGINATED_MENU_PAGE_LIST_TITLE.getString();
    }

    @Override
    public Map<Integer, MenuButton> getButtons(final Player player) {
        final Map<Integer, MenuButton> buttons = new HashMap<>();

        buttons.put(0, new BackButton(menu));

        int index = 10;
        for (int page = 1; page <= this.menu.getPages(player); ++page) {
            final int finalI = page;

            StackBuilder stack = StackBuilder.create(XMaterial.PAPER)
                    .name(replacePage(page, GuiConfig.Values.PAGINATED_MENU_PAGE_LIST_CHANGE_BUTTON_NAME.getString()))
                    .lore(replacePage(page, GuiConfig.Values.PAGINATED_MENU_PAGE_LIST_CHANGE_BUTTON_LORE.getString()));

            if (this.current == finalI) {
                stack = stack
                        .glow(GuiConfig.Values.PAGINATED_MENU_PAGE_LIST_CHANGE_CURRENT_BUTTON_GLOWING.getBoolean())
                        .name(replacePage(page, GuiConfig.Values.PAGINATED_MENU_PAGE_LIST_CHANGE_CURRENT_BUTTON_NAME.getString()))
                        .lore(replacePage(page, GuiConfig.Values.PAGINATED_MENU_PAGE_LIST_CHANGE_CURRENT_BUTTON_LORE.getString()));
            }

            buttons.put(index++, MenuButton.builder()
                    .stack(
                            stack
                    )
                    .action((e, p) -> this.menu.changePage(player, finalI - menu.getPage()))
                    .build());
        }

        return buttons;
    }

    private String replacePage(final int page, final String str) {
        return str.replace("%page%", String.valueOf(page));
    }
}
