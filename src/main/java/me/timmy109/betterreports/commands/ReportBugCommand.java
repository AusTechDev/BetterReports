package me.timmy109.betterreports.commands;
import me.timmy109.betterreports.BetterReports;
import me.timmy109.betterreports.utils.ChatUtils;
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
                    sender.sendMessage(ChatUtils.color(s));
                }
                return true;
            }
            for (String s : playerHelp) {
                sender.sendMessage(ChatUtils.color(s));
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
                sender.sendMessage(ChatUtils.color(s));
            }

            i++;

            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("betterreports.alerts")) {
                    for (String staffAlert : (BetterReports.getInstance().getConfig().getString("staff-bug-report-message")
                            .replace("{player}", playersName).split("\\n"))) {
                        staff.sendMessage(ChatUtils.color(staffAlert));
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



