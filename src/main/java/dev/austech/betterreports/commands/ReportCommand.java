/*
 * BetterReports - ReportCommand.java
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

package dev.austech.betterreports.commands;

import dev.austech.betterreports.model.report.Report;
import dev.austech.betterreports.model.report.ReportManager;
import dev.austech.betterreports.model.report.menu.creation.ConfirmReportMenu;
import dev.austech.betterreports.model.report.menu.creation.ReportMenu;
import dev.austech.betterreports.model.report.menu.creation.SelectPlayerMenu;
import dev.austech.betterreports.model.report.menu.creation.reason.PlayerReportPagedReasonMenu;
import dev.austech.betterreports.util.data.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.austech.betterreports.model.report.ReportManager.checkCooldown;

public class ReportCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("betterreports.use")) {
            MainConfig.Values.LANG_NO_PERMISSION.send(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            MainConfig.Values.LANG_PLAYER_ONLY.send(sender);
            return true;
        }

        final boolean bugReports = ReportManager.getInstance().isBugReportsEnabled();
        final boolean playerReports = ReportManager.getInstance().isPlayerReportsEnabled();

        if (args.length == 0) {
            new ReportMenu().open((Player) sender);
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("bug") && bugReports) {
                if (checkCooldown((Player) sender, Report.Type.BUG))
                    return true;

                new ReportMenu().reportBug((Player) sender);
                return true;
            } else if (args[0].equalsIgnoreCase("player") && playerReports) {
                if (checkCooldown((Player) sender, Report.Type.PLAYER))
                    return true;

                new SelectPlayerMenu().open((Player) sender);
                return true;
            } else if (playerReports) {
                if (checkCooldown((Player) sender, Report.Type.PLAYER))
                    return true;

                final Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    new PlayerReportPagedReasonMenu(((Player) sender), target).open(((Player) sender));
                } else {
                    MainConfig.Values.LANG_PLAYER_NOT_FOUND.send(sender);
                }
            }
        } else {
            if (args[0].equalsIgnoreCase("bug") && bugReports) {
                if (checkCooldown((Player) sender, Report.Type.BUG))
                    return true;

                final Report report = Report.builder()
                        .type(Report.Type.BUG)
                        .creator(((Player) sender))
                        .reason(String.join(" ", Arrays.asList(args).subList(1, args.length)))
                        .build();

                new ConfirmReportMenu(((Player) sender), report).open(((Player) sender));
            } else if (args[0].equalsIgnoreCase("player") && playerReports) {
                if (checkCooldown((Player) sender, Report.Type.PLAYER))
                    return true;

                if (args.length > 2) {
                    final Report report = Report.builder()
                            .type(Report.Type.PLAYER)
                            .creator(((Player) sender))
                            .reason(String.join(" ", Arrays.asList(args).subList(2, args.length)))
                            .target(Bukkit.getPlayer(args[1]))
                            .build();

                    new ConfirmReportMenu(((Player) sender), report).open(((Player) sender));
                } else {
                    final Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        new PlayerReportPagedReasonMenu(((Player) sender), target).open(((Player) sender));
                    } else {
                        MainConfig.Values.LANG_PLAYER_NOT_FOUND.send(sender);
                    }
                }
            } else if (playerReports) {
                if (checkCooldown((Player) sender, Report.Type.PLAYER))
                    return true;

                final Player target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    MainConfig.Values.LANG_PLAYER_NOT_FOUND.send(sender);
                    return true;
                }

                final Report report = Report.builder()
                        .type(Report.Type.PLAYER)
                        .creator(((Player) sender))
                        .reason(String.join(" ", Arrays.asList(args).subList(1, args.length)))
                        .target(target)
                        .build();

                new ConfirmReportMenu(((Player) sender), report).open(((Player) sender));
            } else {
                if (sender.hasPermission("betterreports.admin"))
                    MainConfig.Values.LANG_HELP_MESSAGE_ADMIN.sendList(sender);
                else
                    MainConfig.Values.LANG_HELP_MESSAGE.sendList(sender);
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (args.length <= 1) {
            final List<String> tab = new ArrayList<>();
            final boolean bugReports = ReportManager.getInstance().isBugReportsEnabled();
            final boolean playerReports = ReportManager.getInstance().isPlayerReportsEnabled();

            if (bugReports)
                tab.add("bug");
            if (playerReports) {
                Bukkit.getOnlinePlayers().stream().filter(p -> (sender instanceof Player && sender != p)).map(Player::getName).forEach(tab::add);
                tab.add("player");
            }

            return tab;
        }
        return null;
    }
}
