/*
 * BetterReports - ReportBugCommand.java
 *
 * Plugin created by Timmy109
 * Github profile: https://github.com/Timmy109
 * Spigot Profile: https://www.spigotmc.org/members/_timmyy_.919057/
 * Discord Server: https://discord.gg/wafV4VP
 *
 * MIT License
 *
 * Copyright (c) 2020 Tim Uding.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.timmy109.betterreports.commands;
import me.timmy109.betterreports.BetterReports;
import me.timmy109.betterreports.utils.Common;
import me.timmy109.betterreports.discord.DiscordWebhook;
import me.timmy109.betterreports.utils.ArrayUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class ReportBugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        List<String> adminHelp = ArrayUtils.getAdminHelpList();
        List<String> playerHelp = ArrayUtils.getPlayerHelpList();

        if (!(sender instanceof Player)) {
            sender.sendMessage(Common.color("&cOnly players can execute that command!"));
            return true;
        }

        if (args.length == 0) {
            if (sender.hasPermission("betterreports.admin")) {
                adminHelp.forEach(s -> sender.sendMessage(Common.color(s)));
                return true;
            }

            playerHelp.forEach(s -> sender.sendMessage(Common.color(s)));
            return true;
        }

        // Grab bug report details
        String bug = String.join(" ", args);
        String playersName = sender.getName();

        // Sending the bug report to Discord via a webhook
        DiscordWebhook webhook = new DiscordWebhook(BetterReports.getInstance().getConfig().getString("discord-bug-webhook-url"));
        DiscordWebhook.EmbedObject eb = new DiscordWebhook.EmbedObject();
        eb.addField("**" + "Reports" + "**", "Reported by: " + "`" + playersName + "`", false);
        eb.addField("Report type", "Bug report", false);
        eb.addField("Bug", bug, false);
        eb.setColor(Color.decode(BetterReports.getInstance().getConfig().getString("discord-embed-bug-report-colour")));
        eb.setFooter("BetterReports - Timmy109", "");
        webhook.addEmbed(eb);

        try {
            webhook.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
            sender.sendMessage(Common.color("&cError sending the bug report to discord. Please contact the admin."));
            return true;
        }

        // Successful bug report
        Arrays.stream(BetterReports.getInstance().getConfig().getString("bug-report-success")
                .replace("{player}", playersName)
                .split("\\n"))
                .forEach(s -> sender.sendMessage(Common.color(s)));

        // Send notification to relevant players
        String[] reportAlertMessage = BetterReports.getInstance().getConfig().getString("staff-bug-report-message")
                .replace("{player}", playersName)
                .split("\\n");

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("betterreports.alerts"))
                .forEach(staff -> Arrays.stream(reportAlertMessage)
                        .forEach(msg -> staff.sendMessage(Common.color(msg))));

        return true;
    }
}
