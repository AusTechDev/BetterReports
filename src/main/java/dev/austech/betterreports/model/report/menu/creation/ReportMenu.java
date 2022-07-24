/*
 * BetterReports - ReportMenu.java
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

package dev.austech.betterreports.model.report.menu.creation;

import dev.austech.betterreports.model.report.Report;
import dev.austech.betterreports.model.report.ReportManager;
import dev.austech.betterreports.util.ConversationUtil;
import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.data.MainConfig;
import dev.austech.betterreports.util.menu.Menu;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import dev.austech.betterreports.util.xseries.XMaterial;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ReportMenu extends Menu {
    @Override
    public String getTitle(final Player player) {
        return "Creating Report";
    }

    @Override
    public Map<Integer, MenuButton> getButtons(final Player player) {
        final Map<Integer, MenuButton> buttons = new HashMap<>();

        final MenuButton bugButton = MenuButton.builder()
                .stack(
                        StackBuilder.create(XMaterial.ORANGE_WOOL)
                                .name("&6Click to report a &l&nbug")
                )
                .action((e, p) -> reportBug(p))
                .closeMenu(true)
                .build();

        final MenuButton playerButton = MenuButton.builder()
                .stack(
                        StackBuilder.create(XMaterial.PLAYER_HEAD)
                                .name("&6Click to report a &l&nplayer")
                )
                .action((e, p) -> new SelectPlayerMenu().open(p))
                .build();

        final boolean bugReports = ReportManager.getInstance().isBugReportsEnabled();
        final boolean playerReports = ReportManager.getInstance().isPlayerReportsEnabled();

        if (bugReports && !playerReports) {
            buttons.put(4, bugButton);
        } else if (!bugReports && playerReports) {
            buttons.put(4, playerButton);
        } else if (bugReports && playerReports) {
            buttons.put(2, bugButton);
            buttons.put(6, playerButton);
        }

        return buttons;
    }

    public void reportBug(final Player creator) {
        ConversationUtil.run(creator, () -> {
            creator.sendTitle(MainConfig.Values.LANG_QUESTION_BUG_TITLE.getPlaceholderString(creator), MainConfig.Values.LANG_QUESTION_BUG_SUBTITLE.getPlaceholderString(creator), 10, 20 * 15, 10);
            return MainConfig.Values.LANG_QUESTION_BUG_MESSAGE.getPlaceholderString(creator);
        }, (s) -> {
            creator.sendTitle("", "", 10, 1, 10);
            new ConfirmReportMenu(creator,
                    Report.builder()
                            .type(Report.Type.BUG)
                            .creator(creator)
                            .reason(s)
                            .build()
            ).open(creator);
            return Prompt.END_OF_CONVERSATION;
        });
    }
}
