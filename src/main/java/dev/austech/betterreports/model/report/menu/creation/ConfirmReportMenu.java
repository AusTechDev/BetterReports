/*
 * BetterReports - ConfirmReportMenu.java
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

package dev.austech.betterreports.model.report.menu.creation;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.model.report.Report;
import dev.austech.betterreports.util.PlaceholderUtil;
import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.config.impl.GuiConfig;
import dev.austech.betterreports.util.config.impl.MainConfig;
import dev.austech.betterreports.util.menu.Menu;
import dev.austech.betterreports.util.menu.defaults.buttons.BackButton;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class ConfirmReportMenu extends Menu {
    private final Player creator;
    private final Report report;

    private boolean success = false;

    @Override
    public String getPlayerTitle(final Player player) {
        return PlaceholderUtil.applyPlaceholders(report, GuiConfig.Values.MENU_CONFIRM_NAME.getString());
    }

    @Override
    public int getSize() {
        return GuiConfig.Values.MENU_CONFIRM_SIZE.getInteger();
    }

    @Override
    public boolean canOpen(final Player player) {
        if (report.getType() == Report.Type.PLAYER) {
            return MainConfig.Values.PLAYER_REPORT_MENUS_CONFIRM_REPORT.getBoolean();
        } else {
            return MainConfig.Values.BUG_REPORT_MENUS_CONFIRM_REPORT.getBoolean();
        }
    }

    @Override
    public Map<Integer, MenuButton> getButtons(final Player player) {
        final Map<Integer, MenuButton> buttons = new HashMap<>();

        final int backButtonSlot = GuiConfig.Values.MENU_CONFIRM_BACK_BUTTON.getInteger();
        if (getToReturn() != null && backButtonSlot >= -1)
            buttons.put(backButtonSlot, new BackButton(getToReturn()).setOnConfirm((e, p) -> success = true));

        final HashMap<Integer, StackBuilder> stacks = new HashMap<>(GuiConfig.Values.MENU_CONFIRM_BUTTONS.getStackMap());

        stacks.forEach((i, stack) -> {
            final ItemMeta meta = stack.build().getItemMeta();

            stack.name(PlaceholderUtil.applyPlaceholders(report, meta.getDisplayName()));

            if (Objects.equals(stack.getType(), "DISPLAY_REPORT")) {
                final String placeholder;

                if (report.getType() == Report.Type.BUG) {
                    placeholder = BetterReports.getInstance().getConfigManager().getGuiConfig().getConfig().getString(GuiConfig.Values.MENU_CONFIRM_BUTTONS.getKey() + "." + i + ".bug-lore");
                } else {
                    placeholder = BetterReports.getInstance().getConfigManager().getGuiConfig().getConfig().getString(GuiConfig.Values.MENU_CONFIRM_BUTTONS.getKey() + "." + i + ".player-lore");
                }

                if (placeholder != null && !placeholder.isEmpty())
                    stack.lore(PlaceholderUtil.applyPlaceholders(report, placeholder));

                buttons.put(i, MenuButton.builder().stack(stack).build());
            } else {
                if (meta.hasLore())
                    stack.lore(PlaceholderUtil.applyPlaceholders(report, String.join("\n", meta.getLore())));

                if (Objects.equals(stack.getType(), "CLOSE")) {
                    buttons.put(i, MenuButton.builder().stack(stack).closeMenu(true).build());
                } else if (Objects.equals(stack.getType(), "CONFIRM")) {
                    buttons.put(i, MenuButton.builder().stack(stack).closeMenu(true).action((e, p) -> confirm()).build());
                } else {
                    buttons.put(i, MenuButton.builder().stack(stack).build());
                }
            }
        });

        return buttons;
    }

    private void confirm() {
        success = true;
        report.save();
    }

    @Override
    public void onClose(Player player) {
        if (!success)
            MainConfig.Values.LANG_REPORT_CANCELLED.send(creator);
    }
}
