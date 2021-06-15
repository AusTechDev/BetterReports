/*
 * BetterReports - ReportPlayerCommand.java
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
import dev.austech.betterreports.utils.ArrayUtils;
import dev.austech.betterreports.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReportPlayerCommand implements CommandExecutor {

	private final Common cooldown = new Common();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// Checking to see if player reports are enabled in the config
		if (!Common.getConfig().getBoolean("player-reports")) {
			sender.sendMessage(Common.color(Common.getConfig().getString("unknown-command")));
			return true;
		}

		// Checking if the sender is not a player. If so, send error message
		if (!(sender instanceof Player)) {
			sender.sendMessage(Common.color(Common.getConfig().getString("player-only-message")));
			return true;
		}

		if (!(sender.hasPermission("betterreports.use.player"))) {
			sender.sendMessage(Common.color(Common.getConfig().getString("no-permission-message")));
			return true;
		}

		// Calculations for the 'timeLeft' variable
		long timeLeft = System.currentTimeMillis() - cooldown.getPlayerReportCooldown(((Player) sender).getUniqueId());

		if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= Common.playerReportCooldown || sender.hasPermission("betterreports.cooldown.bypass")) {

			// Checking to see if only the base command is entered

			if (args.length == 0) {
				BaseCommand.base(sender, args);
				return true;
			}

			// If player does not provide a reason for reporting, send error message
			if (args.length == 1) {
				sender.sendMessage(Common.color(Common.getConfig().getString("no-reason-message")));
				return true;
			}

			// If players try to report themself, send error message
			if (args[0].equals(sender.getName())) {
				sender.sendMessage(Common.color(Common.getConfig().getString("report-self-message")));
				return true;
			}

			// Grab bug report details
			String report = String.join(" ", Arrays.asList(args).subList(1, args.length));
			String playerName = sender.getName();
			String targetPlayer = args[0];

			// Check to see if the player is online, if not, send error message
			if (Bukkit.getPlayer(targetPlayer) == null) {
				sender.sendMessage(Common.color(Common.getConfig().getString("not-online-message")));
				return true;
			}

			// Checking to see if the player being reported has permissions that prevent them from being reported
			if (Bukkit.getPlayer(targetPlayer).hasPermission("betterreports.exempt")) {
				sender.sendMessage(Common.color(Common.getConfig().getString("cannot-report-message")));
				return true;
			}

			if (Common.getConfig().getString("discord-player-webhook-url").equals("https://discord.com/api/webhooks/786586736204840990/FmsKQdSqNZ9lqF3fmXyIjulDmuU0yVKlcXti8cbvGucsOsTs_XM-Zb4FLwueh1xq9WB0")) {
				sender.sendMessage(Common.color("&cYou must change the webhook url in the config.yml in order for the webhook to be successfully sent to Discord. Should you require assistance, please join our Discord server: &nhttps://Discord.AusTech.Dev/"));
			}


			// Sending the player report to Discord via a webhook
			DiscordWebhook webhook = new DiscordWebhook(Common.getConfig().getString("discord-player-webhook-url"));
			DiscordWebhook.EmbedObject eb = new DiscordWebhook.EmbedObject();
			for (int i = 1; i < 26; i++) {

				if (Common.getConfig().getString("player-report-fields." + i + ".title") == null) continue;

				eb.addField(Common.getConfig().getString("player-report-fields." + i + ".title")
								.replace("{player}", playerName)
								.replace("{report}", report)
								.replace("{target}", targetPlayer),
						Common.getConfig().getString("player-report-fields." + i + ".content")
								.replace("{player}", playerName)
								.replace("{report}", report)
								.replace("{target}", targetPlayer),
						Common.getConfig().getBoolean("player-report-fields." + i + ".inline"));
			}
			// Setting features of the embed
			eb.setColor(Color.decode(Common.getConfig().getString("discord-embed-player-report-colour")));
			eb.setFooter(Common.getConfig().getString("player-report-embed-footer"), Common.getConfig().getString("player-report-embed-icon"));
			webhook.addEmbed(eb);

			// Attempt to send webhook to Discord
			try {
				webhook.execute();

			// If the webhook is not successfully sent, print stacktrace and error message in console
			} catch (Exception ex) {
				ex.printStackTrace();
				sender.sendMessage(Common.color(Common.getConfig().getString("error-sending-message")));
				return true;
			}

			// Successful in sending report to discord
			Arrays.stream(Common.getConfig().getString("player-report-success")
					.replace("{player}", playerName).split("\\n"))
					.forEach(s -> sender.sendMessage(Common.color(s)));

			// Send notification to relevant players
			String[] reportAlertMessage = Common.getConfig().getString("staff-player-report-message")
					.replace("{player}", playerName)
					.replace("{report}", report)
					.replace("{target}", targetPlayer)
					.split("\\n");

			Bukkit.getOnlinePlayers().stream()
					.filter(player -> player.hasPermission("betterreports.alerts"))
					.forEach(staff -> Arrays.stream(reportAlertMessage)
							.forEach(msg -> staff.sendMessage(Common.color(msg))));

			// Setting the command cooldown after using
			cooldown.setPlayerReportCooldown(((Player) sender).getUniqueId(), System.currentTimeMillis());

		} else {
			// If the player still has a cooldown, tell them how long they have to wait before sending another report
			sender.sendMessage(Common.color("&cYou need to wait " + String.valueOf((TimeUnit.MILLISECONDS.toSeconds(timeLeft) - Common.playerReportCooldown)).replace("-", "") + " seconds before reporting again!"));
			return true;
		}
		return true;
	}
}





