/*
 * BetterReports - PagedMenu.java
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

package dev.austech.betterreports.util.menu.defaults.paged;

import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.config.impl.GuiConfig;
import dev.austech.betterreports.util.menu.Menu;
import dev.austech.betterreports.util.menu.defaults.paged.buttons.PageButton;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import dev.austech.betterreports.util.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class PagedMenu extends Menu {
    private int page = 1;
    private int maxItemsPerPage = 27;

    protected abstract String getMenuTitle(Player player);

    public abstract Map<Integer, MenuButton> getPagedButtons(final Player player);

    public Map<Integer, MenuButton> getFixedButtons(final Player player) {
        return new HashMap<>();
    }

    @Override
    public String getPlayerTitle(final Player player) {
        return getMenuTitle(player) + "&7 - " + getPage() + "/" + getPages(player);
    }

    public void changePage(final Player player, final int amount) {
        this.page += amount;
        this.getButtons().clear();
        this.open(player);
    }

    public final int getPages(final Player player) {
        final int buttonAmount = this.getPagedButtons(player).size();
        if (buttonAmount == 0) {
            return 1;
        }
        return (int) Math.ceil((double) buttonAmount / (double) maxItemsPerPage);
    }

    @Override
    public Map<Integer, MenuButton> getButtons(final Player player) {
        int minItems = Math.max(0, this.page - 1) * maxItemsPerPage;
        int maxItems = this.page * maxItemsPerPage;

        Map<Integer, MenuButton> fixedButtons = getFixedButtons(player);

        // Remove fixed buttons just in-case they conflict with the default page buttons
        fixedButtons.remove(0); // back
        fixedButtons.remove(8); // forward

        final HashMap<Integer, MenuButton> buttons = new HashMap<>(fixedButtons);
        buttons.put(0, new PageButton(-1, page, this));
        buttons.put(8, new PageButton(1, page, this));

        if (fixedButtons.get(4) == null) {
            buttons.put(4, MenuButton.builder()
                    .stack(StackBuilder.create(XMaterial.NETHER_STAR)
                            .name(GuiConfig.Values.PAGINATED_MENU_PAGE_NUMBER_BUTTON_NAME.getString().replace("%page%", String.valueOf(this.page)))
                            .glow())
                    .action((e, p) -> open(p))
                    .build());
        }

        this.getPagedButtons(player).forEach((key, value) -> {
            if (key >= minItems && key < maxItems) {
                buttons.put(key -= (maxItemsPerPage * (page - 1)) - 9, value);
            }
        });

        return buttons;
    }
}
