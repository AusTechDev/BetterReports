/*
 * BetterReports - ListReportMenu.java
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
import dev.austech.betterreports.model.report.ReportManager;
import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.menu.defaults.paged.PagedMenu;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import dev.austech.betterreports.util.xseries.XMaterial;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListReportMenu extends PagedMenu {
    private final Player player;
    private final Report.Type type;

    @Override
    protected String getMenuTitle(final Player player) {
        return type.name().charAt(0) + type.name().substring(1).toLowerCase() + " Reports";
    }

    @Override
    public Map<Integer, MenuButton> getPagedButtons(final Player player) {
        final Map<Integer, MenuButton> buttons = new HashMap<>();
        List<Report> reports = ReportManager.getInstance().getReports().stream()
                .sorted(Comparator.comparingLong(Report::getTimestamp))
                .filter(report -> report.getType() == type)
                .collect(Collectors.toList());

        if (this.player != null) {
            reports = reports.stream()
                    .filter(report -> report.getCreator() != null)
                    .filter(report -> report.getCreator().getUniqueId().equals(this.player.getUniqueId()))
                    .collect(Collectors.toList());
        }

        Collections.reverse(reports);

        for (int i = 0; i < reports.size(); i++) {
            final Report report = reports.get(i);

            buttons.put(i, MenuButton.builder()
                    .stack(
                            StackBuilder.create(XMaterial.RED_WOOL)
                                    .name(report.getReason())
                    )
                    .build());
        }

        return buttons;
    }
}
