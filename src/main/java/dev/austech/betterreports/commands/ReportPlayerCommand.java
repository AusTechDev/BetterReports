/*
 * BetterReports - ReportPlayerCommand.java
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

import dev.austech.betterreports.model.Report;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReportPlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("betterreports.use.player")) {
            Config.Values.LANG_NO_PERMISSION.send(sender);
            return true;
        }

        if (!Config.Values.PLAYER_REPORT_ENABLED.getBoolean()) {
            Config.Values.LANG_UNKNOWN_COMMAND.send(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            Config.Values.LANG_PLAYER_ONLY.send(sender);
            return true;
        }

        final long time = System.currentTimeMillis();
        final long future = Report.cooldownMap.getOrDefault(((Player) sender).getUniqueId(), 0L);
        final long cooldown = Config.Values.PLAYER_REPORT_COOLDOWN.getInteger() * 1000L;

        if (!sender.hasPermission("betterreports.cooldown.bypass") &&
                !(time - future >= cooldown)
        ) {
            sender.sendMessage(Common.color("&cYou need to wait " + ((cooldown + (future - time)) / 1000) + " seconds before reporting again!"));
            return true;
        }

        if (args.length <= 1) {
            Config.Values.LANG_NO_REASON.send(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase(sender.getName())) {
            Config.Values.LANG_PLAYER_SELF.send(sender);
            return true;
        }

        final String reason = String.join(" ", Arrays.asList(args).subList(1, args.length));
        final OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!Config.Values.REPORT_OFFLINE_PLAYERS.getBoolean() && !target.isOnline()) {
            Config.Values.LANG_PLAYER_OFFLINE.send(sender);
            return false;
        }

        if (target.isOnline() && target.getPlayer().hasPermission("betterreports.exempt")) {
            Config.Values.LANG_PLAYER_BYPASS.send(sender);
            return true;
        }

        final Report report = new Report(Report.Type.PLAYER, ((Player) sender), reason);

        report.setTarget(target);
        report.execute();

        return true;
    }
}
