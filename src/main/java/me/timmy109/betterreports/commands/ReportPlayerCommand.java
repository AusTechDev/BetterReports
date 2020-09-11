/*
 * BetterReports - ReportPlayerCommand.java
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
import me.timmy109.betterreports.discord.DiscordWebhook;
import me.timmy109.betterreports.utils.ArrayUtils;
import me.timmy109.betterreports.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static me.timmy109.betterreports.BetterReports.*;

public class ReportPlayerCommand implements CommandExecutor {

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

		if (args.length == 1) {
			sender.sendMessage(Common.color("&cPlease specify a reason for reporting!"));
			return true;
		}

		if (args[0].equals(sender.getName())) {
			sender.sendMessage(Common.color("&cYou can't report yourself!"));
			return true;
		}

		// Grab bug report details
		String report = String.join(" ", Arrays.asList(args).subList(1, args.length-1));
		String playerName = sender.getName();
		String targetPlayer = args[0];

		if (Bukkit.getPlayer(targetPlayer) == null) {
			sender.sendMessage(Common.color("&cPlayer has to be online to be reported!"));
			return true;
		}

		// Sending the player report to Discord via a webhook
		DiscordWebhook webhook = new DiscordWebhook(getInstance().getConfig().getString("discord-player-webhook-url"));
		DiscordWebhook.EmbedObject eb = new DiscordWebhook.EmbedObject();
		for (int i = 1; i < 26; i++) {
			if (Common.getConfig().getString("player-report-fields." + i + ".title") == null) continue;
			eb.addField(Common.getConfig().getString("player-report-fields." + i + ".title").replace("{player}", playerName).replace("{report}", report).replace("{target}", targetPlayer),
					Common.getConfig().getString("player-report-fields." + i + ".content").replace("{player}", playerName).replace("{report}", report).replace("{target}", targetPlayer),
					Common.getConfig().getBoolean("player-report-fields." + i + ".inline"));
		}
		eb.setColor(Color.decode(getInstance().getConfig().getString("discord-embed-player-report-colour")));
		eb.setFooter("BetterReports - Timmy109", "");
		webhook.addEmbed(eb);

		try {
			webhook.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
			sender.sendMessage(Common.color("&cError sending the bug report to discord. Please contact the admin."));
			return true;
		}

		// Successful in sending report to discord
		Arrays.stream(getInstance().getConfig().getString("player-report-success")
				.replace("{player}", playerName).split("\\n"))
				.forEach(s -> sender.sendMessage(Common.color(s)));

		// Send notification to relevant players
		String[] reportAlertMessage = BetterReports.getInstance().getConfig().getString("staff-player-report-message")
				.replace("{player}", playerName)
				.split("\\n");

		Bukkit.getOnlinePlayers().stream()
				.filter(player -> player.hasPermission("betterreports.alerts"))
				.forEach(staff -> Arrays.stream(reportAlertMessage)
						.forEach(msg -> staff.sendMessage(Common.color(msg))));

		return true;
	}
}





