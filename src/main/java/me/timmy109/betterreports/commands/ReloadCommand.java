package me.timmy109.betterreports.commands;

import me.timmy109.betterreports.BetterReports;
import me.timmy109.betterreports.utils.ArrayUtils;
import me.timmy109.betterreports.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.List;

public class ReloadCommand implements CommandExecutor {

	List<String> debug = ArrayUtils.getDebugList();
	List<String> reload = ArrayUtils.getReloadList();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		List<String> adminHelp = ArrayUtils.getAdminHelpList();
		List<String> playerHelp = ArrayUtils.getPlayerHelpList();


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

		switch (args[0]) {
			case "debug":
				debug(sender);
				break;
			case "reload":
				reloading(sender);
				break;
		}

		if (args.length > 1) {
			sender.sendMessage(ChatUtils.color("&cUnknown Command"));
			return true;
		}
		return true;
	}

		private void reloading(CommandSender sender) {
		if (sender.hasPermission("betterreports.reload")) {
			try {
				BetterReports.getInstance().reloadConfig();
				for (String s : reload) {
					sender.sendMessage(ChatUtils.color(s));
				}
			} catch (Exception ex) {
				sender.sendMessage(ChatUtils.color("&cThere was an error reloading the config. Check console for more details."));
			}
			return;
		}
		sender.sendMessage(ChatUtils.color("&cYou do not have permission to execute this command!"));
		}

		private void debug(CommandSender sender) {
		if (sender.hasPermission("betterreports.admin")) {
			for (String s : debug) {
				sender.sendMessage(ChatUtils.color(s));
			}
			return;
		}
		sender.sendMessage(ChatUtils.color("&cYou do not have permission to execute this command!"));
	}
}
