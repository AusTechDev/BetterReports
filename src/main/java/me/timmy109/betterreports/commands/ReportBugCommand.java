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
import java.awt.*;
import java.util.List;

public class ReportBugCommand implements CommandExecutor {
    public static String bug;
    public static String playersName;
    public static int i = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        List<String> adminHelp = ArrayUtils.getAdminHelpList();
        List<String> playerHelp = ArrayUtils.getPlayerHelpList();

        if (!(sender instanceof Player)) {
            System.out.println("Only players can execute that command!");
            return true;
        }

        if (args.length == 0) {

            if (sender.hasPermission("betterreports.admin")) {

                for (String s : adminHelp) {
                    sender.sendMessage(Common.color(s));
                }
                return true;
            }
            for (String s : playerHelp) {
                sender.sendMessage(Common.color(s));
            }
            return true;

        }

        if (args.length > 0) {

            StringBuilder builder = new StringBuilder();
            for (String arg : args) builder.append(arg).append(" ");

            bug = builder.toString();
            playersName = sender.getName();

            for (String s : (BetterReports.getInstance().getConfig().getString("bug-report-success")
                    .replace("{player}", playersName).split("\\n"))) {
                sender.sendMessage(Common.color(s));
            }

            i++;

            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("betterreports.alerts")) {
                    for (String staffAlert : (BetterReports.getInstance().getConfig().getString("staff-bug-report-message")
                            .replace("{player}", playersName).split("\\n"))) {
                        staff.sendMessage(Common.color(staffAlert));
                    }
                }
            }

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
            }
            return true;
        }
        return true;
    }
}



