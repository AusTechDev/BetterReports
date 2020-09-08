package me.timmy109.betterreports.commands;
import me.timmy109.betterreports.BetterReports;
import me.timmy109.betterreports.discord.DiscordWebhook;
import me.timmy109.betterreports.utils.ArrayUtils;
import me.timmy109.betterreports.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.awt.*;
import java.util.List;
import static me.timmy109.betterreports.BetterReports.*;

public class ReportPlayerCommand implements CommandExecutor {

	public static String report;
	public static String playerName;
	public static int i = 0;
	public static String targetPlayer;

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

		if (args.length == 1) {
			sender.sendMessage(ChatUtils.color("&cPlease specify a reason for reporting!"));
			return true;
		}

		if (args.length >= 2) {
			if (args[0].equals(sender.getName())) {
				sender.sendMessage(ChatUtils.color("&cYou can't report yourself!"));
				return true;
			}

			Player target = Bukkit.getPlayer(args[0]);

			if (target == null) {
				sender.sendMessage(ChatUtils.color("&cPlayer has to be online to be reported!"));
				return true;
			}

			StringBuilder builder = new StringBuilder();

			for (int i = 1; i < args.length; i++)
				builder.append(args[i]).append(" ");

			report = builder.toString();
			playerName = sender.getName();
			targetPlayer = String.valueOf(args[0]);

			for (String s : (BetterReports.getInstance().getConfig().getString("player-report-success")
					.replace("{player}", playerName).split("\\n"))) {
				sender.sendMessage(ChatUtils.color(s));
			}

			i++;

			for (Player staff : Bukkit.getOnlinePlayers()) {
				if (staff.hasPermission("betterreports.alerts")) {
					for (String staffAlert : (BetterReports.getInstance().getConfig().getString("staff-player-report-message")
							.replace("{player}", playerName).split("\\n"))) {
						staff.sendMessage(ChatUtils.color(staffAlert));
					}
				}
			}

			// Sending the player report to Discord via a webhook
			DiscordWebhook webhook = new DiscordWebhook(getInstance().getConfig().getString("discord-player-webhook-url"));
			DiscordWebhook.EmbedObject eb = new DiscordWebhook.EmbedObject();
			eb.addField("**" + "Reports" + "**", "Reported by: " + "`" + playerName + "`", false);
			eb.addField("Report type", "Player report", false);
			eb.addField("Reported player", "`" + String.valueOf(args[0]) + "`", false);
			eb.addField("Reason", report, false);
			eb.setColor(Color.decode(getInstance().getConfig().getString("discord-embed-player-report-colour")));
			eb.setFooter("BetterReports - Timmy109", "");
			webhook.addEmbed(eb);

			try {
				webhook.execute();
			} catch (Exception ex) {
				ex.printStackTrace();
				return true;
			}
			return true;
		}
		return true;
	}
}





