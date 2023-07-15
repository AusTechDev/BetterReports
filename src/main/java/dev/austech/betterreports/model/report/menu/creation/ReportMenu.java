/*
 * BetterReports - ReportMenu.java
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

import dev.austech.betterreports.model.report.Report;
import dev.austech.betterreports.model.report.ReportManager;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.ConversationUtil;
import dev.austech.betterreports.util.PlaceholderUtil;
import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.config.impl.GuiConfig;
import dev.austech.betterreports.util.config.impl.MainConfig;
import dev.austech.betterreports.util.menu.Menu;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReportMenu extends Menu {
    @Override
    public String getTitle(final Player player) {
        return GuiConfig.Values.MENU_MAIN_NAME.getString();
    }

    @Override
    public int getSize() {
        return GuiConfig.Values.MENU_MAIN_SIZE.getInteger();
    }

    @Override
    public boolean canOpen(final Player player) {
        return MainConfig.Values.REPORT_MENU.getBoolean();
    }

    @Override
    public Map<Integer, MenuButton> getButtons(final Player player) {
        final Map<Integer, MenuButton> buttons = new HashMap<>();

        final boolean bugReports = ReportManager.getInstance().isBugReportsEnabled();
        final boolean playerReports = ReportManager.getInstance().isPlayerReportsEnabled();

        final HashMap<Integer, StackBuilder> stacks = new HashMap<>();

        if (bugReports && !playerReports) { // Bug reports only
            stacks.putAll(GuiConfig.Values.MENU_MAIN_BUTTONS_BUG_ONLY.getStackMap());
        } else if (!bugReports && playerReports) { // Player reports only
            stacks.putAll(GuiConfig.Values.MENU_MAIN_BUTTONS_PLAYER_ONLY.getStackMap());
        } else {
            stacks.putAll(GuiConfig.Values.MENU_MAIN_BUTTONS_ALL.getStackMap());
        }

        stacks.forEach((i, stack) -> {
            final ItemMeta meta = stack.build().getItemMeta();

            stack.name(PlaceholderUtil.applyPlaceholders(player, meta.getDisplayName()));
            if (meta.hasLore())
                stack.lore(PlaceholderUtil.applyPlaceholders(player, String.join("\n", meta.getLore())));

            if (Objects.equals(stack.getType(), "BUG")) {
                buttons.put(i, MenuButton.builder()
                        .stack(stack)
                        .action((e, p) -> reportBug(p))
                        .closeMenu(true)
                        .build());
            } else if (Objects.equals(stack.getType(), "PLAYER")) {
                buttons.put(i, MenuButton.builder()
                        .stack(stack)
                        .action((e, p) -> reportPlayer(p))
                        .build());
            } else {
                buttons.put(i, MenuButton.builder()
                        .stack(stack)
                        .closeMenu(true)
                        .build());
            }
        });

        return buttons;
    }

    private void reportPlayer(final Player creator) {
        if (!creator.hasPermission("betterreports.use.player")) {
            MainConfig.Values.LANG_NO_PERMISSION.send(creator);
            return;
        }

        if (MainConfig.Values.PLAYER_REPORT_MENUS_SELECT_PLAYER.getBoolean())
            new SelectPlayerMenu().setReturn(this).open(creator);
        else {
            creator.closeInventory();
            new SelectPlayerMenu().searchPlayer(creator);
        }
    }

    public void reportBug(final Player creator) {
        if (!creator.hasPermission("betterreports.use.bug")) {
            MainConfig.Values.LANG_NO_PERMISSION.send(creator);
            return;
        }

        ConversationUtil.run(creator, () -> {
            if (MainConfig.Values.LANG_QUESTION_BUG_ENABLED.getBoolean())
                Common.sendTitle(creator, MainConfig.Values.LANG_QUESTION_BUG_TITLE.getPlaceholderString(creator), MainConfig.Values.LANG_QUESTION_BUG_SUBTITLE.getPlaceholderString(creator), 10, 20 * 15, 10);
            return MainConfig.Values.LANG_QUESTION_BUG_MESSAGE.getPlaceholderString(creator);
        }, (s) -> {
            Common.resetTitle(creator);
            final Report report = Report.builder()
                    .type(Report.Type.BUG)
                    .creator(creator)
                    .reason(s)
                    .build();

            if (MainConfig.Values.BUG_REPORT_MENUS_CONFIRM_REPORT.getBoolean())
                new ConfirmReportMenu(creator, report).open(creator);
            else
                report.save();

            return Prompt.END_OF_CONVERSATION;
        });
    }
}
