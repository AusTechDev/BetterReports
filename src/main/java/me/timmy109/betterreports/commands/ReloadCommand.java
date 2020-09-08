package me.timmy109.betterreports.commands;

import me.timmy109.betterreports.BetterReports;
import me.timmy109.betterreports.utils.ArrayUtils;
import me.timmy109.betterreports.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.List;

public class ReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		List<String> adminHelp = ArrayUtils.getAdminHelpList();
		List<String> playerHelp = ArrayUtils.getPlayerHelpList();
		List<String> debug = ArrayUtils.getDebugList();
		List<String> reload = ArrayUtils.getDebugList();

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

			if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("betterreports.admin")) {
				try {
					BetterReports.getInstance().reloadConfig();
				} catch (Exception ex) {
					ex.printStackTrace();
					sender.sendMessage(ChatUtils.color("&cThere was an error reloading the config. Please check console for more details"));
					return true;
				}
				for (String s : reload) {
					sender.sendMessage(ChatUtils.color(s));
				}
				return true;
			}

			else if (args[0].equalsIgnoreCase("debug") && sender.hasPermission("betterreports.admin")) {
				for (String s : debug) {
					sender.sendMessage(ChatUtils.color(s));
				}
				return true;
			}

			else if (!(args[0].compareTo("reload") == 0 || args[0].compareTo("debug") == 0)) {
				sender.sendMessage(ChatUtils.color("&cUnknown command"));
				return true;
			}

			else {
				sender.sendMessage(ChatUtils.color("&cYou do not have permission to execute this command!"));
				return true;
			}
		}

		if (args.length > 1) {
			sender.sendMessage(ChatUtils.color("&cUnknown command"));
			return true;
		}
		return true;
	}
}
