/*
 * BetterReports - ReportCommand.java
 *
 * Copyright (c) 2022 AusTech Development
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.austech.betterreports.commands;

import com.google.common.collect.Lists;
import dev.austech.betterreports.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ReportCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("betterreports.admin"))
                Config.Values.LANG_HELP_MESSAGE_ADMIN.sendList(sender);
            else
                Config.Values.LANG_HELP_MESSAGE.sendList(sender);

            return true;
        }

        if (args[0].equalsIgnoreCase("bug") || args[0].equalsIgnoreCase("player"))
            Bukkit.dispatchCommand(sender, "report" + args[0] + " " + String.join(" ", Arrays.asList(args).subList(1, args.length)));
        else
            Bukkit.dispatchCommand(sender, "reportplayer " + String.join(" ", args));

        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (args.length <= 1) {
            final List<String> tab = Lists.newArrayList("player", "bug");
            Bukkit.getOnlinePlayers().stream().map(Player::getName).forEach(tab::add);
            return tab;
        }
        return null;
    }
}
