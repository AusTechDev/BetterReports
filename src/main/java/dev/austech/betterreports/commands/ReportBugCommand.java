/*
 * BetterReports - ReportBugCommand.java
 *
 * Github: https://github.com/AusTechDev/
 * Spigot Profile: https://www.spigotmc.org/members/_timmyy_.919057/
 * Discord Server: https://discord.austech.dev/
 *
 * MIT License
 *
 * Copyright (c) 2020 Timmy109.
 * Copyright (c) 2020 Contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dev.austech.betterreports.commands;

import dev.austech.betterreports.discord.DiscordWebhook;
import dev.austech.betterreports.utils.Common;
import dev.austech.betterreports.utils.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReportBugCommand implements CommandExecutor {

    private final Common cooldown = new Common();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        List<String> adminHelp = ArrayUtils.getAdminHelpList();
        List<String> playerHelp = ArrayUtils.getPlayerHelpList();

        if (!(sender instanceof Player)) {
            sender.sendMessage(Common.color("&cOnly players can execute that command!"));
            return true;
        }

        if (!(sender.hasPermission("betterreports.use"))) {
            sender.sendMessage(Common.color("&cYou do not have permission to execute this command!"));
            return true;
        }

        // Calculations for the 'timeLeft' variable
        long timeLeft = System.currentTimeMillis() - cooldown.getBugReportCooldown(((Player) sender).getUniqueId());

        if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= Common.bugReportCooldown || sender.hasPermission("betterreports.cooldown.bypass")) {

            if (BaseCommand.base(sender, args, adminHelp, playerHelp)) return true;

            String bug = String.join(" ", Arrays.asList(args).subList(0, args.length));
            String playersName = sender.getName();

            // Sending the bug report to Discord via a webhook
            DiscordWebhook webhook = new DiscordWebhook(Common.getConfig().getString("discord-bug-webhook-url"));
            DiscordWebhook.EmbedObject eb = new DiscordWebhook.EmbedObject();
            for (int i = 1; i < 26; i++) {

                if (Common.getConfig().getString("bug-report-fields." + i + ".title") == null) continue;

                eb.addField(Common.getConfig().getString("bug-report-fields." + i + ".title")
                                .replace("{player}", playersName)
                                .replace("{report}", bug),
                        Common.getConfig().getString("bug-report-fields." + i + ".content")
                                .replace("{player}", playersName)
                                .replace("{report}", bug),
                        Common.getConfig().getBoolean("bug-report-fields." + i + ".inline"));
            }
            // Setting features of the embed
            eb.setColor(Color.decode(Common.getConfig().getString("discord-embed-bug-report-colour")));
            eb.setFooter("AusTech - BetterReports", "");
            webhook.addEmbed(eb);

            // Attempt to send webhook to Discord
            try {
                webhook.execute();

                // If the webhook is not successfully sent, print stacktrace and error message in console
            } catch (Exception ex) {
                ex.printStackTrace();
                sender.sendMessage(Common.color("&cError sending the bug report to discord. Please contact the admin."));
                sender.sendMessage(Common.color("&cAvoid using special characters in reports as this may cause issues!"));
                return true;
            }

            // Successful in sending report to discord
            Arrays.stream(Common.getConfig().getString("bug-report-success")
                    .replace("{player}", playersName).split("\\n"))
                    .forEach(s -> sender.sendMessage(Common.color(s)));

            // Send notification to relevant players
            String[] reportAlertMessage = Common.getConfig().getString("staff-bug-report-message")
                    .replace("{player}", playersName)
                    .replace("{report}", bug)
                    .split("\\n");

            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.hasPermission("betterreports.alerts"))
                    .forEach(staff -> Arrays.stream(reportAlertMessage)
                            .forEach(msg -> staff.sendMessage(Common.color(msg))));

            // Setting the command cooldown after using
            cooldown.setBugReportCooldown(((Player) sender).getUniqueId(), System.currentTimeMillis());

        } else {
            // If the player still has a cooldown, tell them how long they have to wait before sending another report
            sender.sendMessage(Common.color("&cYou need to wait " + String.valueOf((TimeUnit.MILLISECONDS.toSeconds(timeLeft) - Common.bugReportCooldown)).replace("-", "") + " seconds before reporting again!"));
        }
        return true;
    }
}



