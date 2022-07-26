/*
 * BetterReports - ConfirmReportMenu.java
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
import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.data.MainConfig;
import dev.austech.betterreports.util.menu.Menu;
import dev.austech.betterreports.util.menu.defaults.buttons.BackButton;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import dev.austech.betterreports.util.xseries.XMaterial;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ConfirmReportMenu extends Menu {
    private final Player creator;
    private final Report report;

    private boolean success = false;

    @Override
    public String getTitle(final Player player) {
        return "Confirm " + (report.isPlayer() ? "Player" : "Bug") + " Report";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public Map<Integer, MenuButton> getButtons(final Player player) {
        final Map<Integer, MenuButton> buttons = new HashMap<>();

        if (getToReturn() != null)
            buttons.put(0, new BackButton(getToReturn()).setOnConfirm((e, p) -> success = true));

        buttons.put(11, MenuButton.builder()
                .stack(
                        StackBuilder.create(XMaterial.RED_WOOL)
                                .name("&c&lClick to cancel.")
                )
                .closeMenu(true)
                .build());

        buttons.put(13, MenuButton.builder()
                .stack(
                        report.getType() == Report.Type.PLAYER ?
                                (
                                        StackBuilder.create(XMaterial.BOOK)
                                                .name("&7Report against &c" + report.getTarget().getName())
                                                .lore(
                                                        "&7Reason: &c" + report.getReason()
                                                )
                                                .glow()
                                ) :
                                (
                                        StackBuilder.create(XMaterial.BOOK)
                                                .name("&7Bug Report")
                                                .lore(
                                                        "&7Reason: &c" + report.getReason()
                                                )
                                                .glow()
                                )
                )
                .build());

        buttons.put(15, MenuButton.builder()
                .stack(
                        StackBuilder.create(XMaterial.LIME_WOOL)
                                .name("&a&lClick to confirm report.")
                )
                .closeMenu(true)
                .action((e, p) -> confirm())
                .build());

        return buttons;
    }

    private void confirm() {
        success = true;

        if (creator == report.getTarget()) {
            MainConfig.Values.LANG_PLAYER_SELF.send(creator);
            creator.playSound(creator.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        if (report.getType() == Report.Type.BUG && !ReportManager.getInstance().isBugReportsEnabled()) {
            creator.playSound(creator.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            MainConfig.Values.LANG_BUG_REPORTS_DISABLED.send(creator);
            return;
        } else if (report.getType() == Report.Type.PLAYER && !ReportManager.getInstance().isPlayerReportsEnabled()) {
            creator.playSound(creator.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            MainConfig.Values.LANG_PLAYER_REPORTS_DISABLED.send(creator);
            return;
        }

        report.save();
    }

    @Override
    public void onClose() {
        if (!success)
            MainConfig.Values.LANG_REPORT_CANCELLED.send(creator);
    }
}
