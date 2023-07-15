/*
 * BetterReports - ReportBugCommand.java
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

package dev.austech.betterreports.commands;

import dev.austech.betterreports.model.report.Report;
import dev.austech.betterreports.model.report.menu.creation.ConfirmReportMenu;
import dev.austech.betterreports.util.config.impl.MainConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static dev.austech.betterreports.model.report.ReportManager.checkCooldown;

public class ReportBugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("betterreports.use.bug")) {
            MainConfig.Values.LANG_NO_PERMISSION.send(sender);
            return true;
        }

        if (!MainConfig.Values.BUG_REPORT_ENABLED.getBoolean()) {
            MainConfig.Values.LANG_UNKNOWN_COMMAND.send(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            MainConfig.Values.LANG_PLAYER_ONLY.send(sender);
            return true;
        }

        if (checkCooldown((Player) sender, Report.Type.BUG))
            return true;

        if (args.length == 0) {
            MainConfig.Values.LANG_NO_BUG.send(sender);
            return true;
        }

        final String bug = String.join(" ", Arrays.asList(args).subList(0, args.length));

        final Report report = Report.builder()
                .type(Report.Type.BUG)
                .creator((Player) sender)
                .reason(bug)
                .build();

        if (MainConfig.Values.BUG_REPORT_MENUS_CONFIRM_REPORT.getBoolean())
            new ConfirmReportMenu(((Player) sender), report).open(((Player) sender));
        else
            report.save();

        return true;
    }
}
