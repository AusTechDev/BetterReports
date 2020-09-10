/*
 * BetterReports - ArrayUtils.java
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
package me.timmy109.betterreports.utils;
import me.timmy109.betterreports.BetterReports;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class ArrayUtils {

    public static ArrayList<String> getAdminHelpList() {
        ArrayList<String> adminHelp = new ArrayList<>();
        adminHelp.add("&8&l&m-------------------------------");
        adminHelp.add("&c&l&o        Better&4&l&oReports");
        adminHelp.add("&8    Reporting bugs & players");
        adminHelp.add("&7/report <player> <reason>" + "&c Report a player");
        adminHelp.add("&7/reportbug <bug>" + "&c Report a bug");
        adminHelp.add("&7/br reload" + "&c Reload the configuration");
        adminHelp.add("&8&l&m-------------------------------");

        return adminHelp;
    }

    public static ArrayList<String> getPlayerHelpList() {
        ArrayList<String> playerHelp = new ArrayList<>();
        playerHelp.add("&8&l&m-------------------------------");
        playerHelp.add("&c&l&o        Better&4&l&oReports");
        playerHelp.add("&8    Reporting bugs & players");
        playerHelp.add("&7/report <player> <reason>" + "&c Report a player");
        playerHelp.add("&7/reportbug <bug>" + "&c Report a bug");
        playerHelp.add("&8&l&m-------------------------------");
        return playerHelp;
    }

    public static ArrayList<String> getDebugList(JavaPlugin plugin) {
        ArrayList<String> debugList = new ArrayList<>();
        debugList.add("&8&l&m-------------------------");
        debugList.add("&c&l&o        Better&4&l&oReports");
        debugList.add("&7Version: &c" + BetterReports.getInstance().getDescription().getVersion());
        debugList.add("&7Author: &cTimmy109");
        debugList.add("&7Server: &c" + Bukkit.getVersion());
        debugList.add("&7PR WH Color: &c" + plugin.getConfig().getString("discord-embed-player-report-colour"));
        debugList.add("&7BR WH Color: &c" + plugin.getConfig().getString("discord-embed-bug-report-colour"));
        debugList.add("&8&l&m-------------------------");
        return debugList;
    }

    public static ArrayList<String> getReloadList() {
        ArrayList<String> reloadList = new ArrayList<>();
        reloadList.add("&8&l&m-------------------------");
        reloadList.add("&c&l&o        Better&4&l&oReports");
        reloadList.add("&a      Successfully reloaded!");
        reloadList.add("&8&l&m-------------------------");
        return reloadList;
    }
}

