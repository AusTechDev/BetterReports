/*
 * BetterReports - AdminCommand.java
 *
 * Github: https://github.com/AusTechDev/
 * Spigot Profile: https://www.spigotmc.org/members/_timmyy_.919057/
 * Discord Server: https://discord.austech.dev/
 *
 * MIT License
 *
 * Copyright (c) 2020 Tim Uding.
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
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dev.austech.betterreports.commands;
import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.utils.ArrayUtils;
import dev.austech.betterreports.utils.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.List;

public class AdminCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		List<String> reload = ArrayUtils.getReloadList();
		List<String> debug = ArrayUtils.getDebugList(BetterReports.getInstance());
		List<String> adminHelp = ArrayUtils.getAdminHelpList();
		List<String> playerHelp = ArrayUtils.getPlayerHelpList();

		if (args.length == 0) {
			if (sender.hasPermission("betterreports.admin")) {
				adminHelp.forEach(s -> sender.sendMessage(Common.color(s)));
				return true;
			}

			playerHelp.forEach(s -> sender.sendMessage(Common.color(s)));
			return true;
		}

		if (args.length > 1) {
			sender.sendMessage(Common.color("&cUnknown Command"));
			return true;
		}

		switch (args[0]) {
			case "debug":
				if (!sender.hasPermission("betterreports.admin")) {
					sender.sendMessage(Common.color("&cYou do not have permission to execute this command!"));
					break;
				}
				debug.forEach(s -> sender.sendMessage(Common.color(s)));
				break;

			case "reload":
				if (!sender.hasPermission("betterreports.reload")) {
					sender.sendMessage(Common.color("&cYou do not have permission to execute this command!"));
					break;
				}
				try {
					BetterReports.getInstance().reloadConfig();
					reload.forEach(s -> sender.sendMessage(Common.color(s)));
				} catch (Exception ex) {
					ex.printStackTrace();
					sender.sendMessage(Common.color("&cThere was an error reloading the config. Check console for more details."));
				}
				break;

			default:
				sender.sendMessage(Common.color("&cUnknown Command"));
		}

		return true;
	}
}
