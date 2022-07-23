/*
 * BetterReports - BetterReportsCommand.java
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

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class BetterReportsCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("betterreports.use")) {
            Config.Values.LANG_NO_PERMISSION.send(sender);
            return true;
        }

        if (args.length == 0) {
            if (sender.hasPermission("betterreports.admin"))
                Config.Values.LANG_HELP_MESSAGE_ADMIN.sendList(sender);
            else
                Config.Values.LANG_HELP_MESSAGE.sendList(sender);

            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload": {
                if (!sender.hasPermission("betterreports.reload")) {
                    Config.Values.LANG_NO_PERMISSION.send(sender);
                    return true;
                }

                BetterReports.getInstance().reloadConfig();

                Common.log("The configuration has been reloaded.");
                Config.Values.LANG_CONFIG_RELOADED.send(sender);

                break;
            }
            case "help": {
                if (sender.hasPermission("betterreports.admin"))
                    Config.Values.LANG_HELP_MESSAGE_ADMIN.sendList(sender);
                else
                    Config.Values.LANG_HELP_MESSAGE.sendList(sender);

                break;
            }
            default:
                Config.Values.LANG_UNKNOWN_COMMAND.send(sender);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return null;
    }
}
