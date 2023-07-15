/*
 * BetterReports - ActiveReportsMenu.java
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

package dev.austech.betterreports.model.report.menu.render;

import dev.austech.betterreports.model.report.Report;
import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.menu.Menu;
import dev.austech.betterreports.util.menu.defaults.buttons.BackButton;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import dev.austech.betterreports.util.xseries.XMaterial;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ActiveReportsMenu extends Menu {
    @Override
    public String getPlayerTitle(final Player player) {
        return "Active Reports";
    }

    @Override
    public Map<Integer, MenuButton> getButtons(final Player player) {
        final Map<Integer, MenuButton> buttons = new HashMap<>();

        buttons.put(0, new BackButton(getToReturn()));

        buttons.put(3, MenuButton.builder()
                .stack(
                        StackBuilder.create(XMaterial.ORANGE_WOOL)
                                .name("&6&lBug Reports")
                )
                .action((e, p) -> new ListReportMenu(null, Report.Type.BUG).setReturn(this).open(p))
                .build());

        buttons.put(5, MenuButton.builder()
                .stack(
                        StackBuilder.create(XMaterial.YELLOW_WOOL)
                                .name("&e&lPlayer Reports")
                )
                .action((e, p) -> new ListReportMenu(null, Report.Type.PLAYER).setReturn(this).open(p))
                .build());

        return buttons;
    }
}
