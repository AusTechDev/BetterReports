/*
 * BetterReports - BetterReports.java
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
package me.timmy109.betterreports;
import me.timmy109.betterreports.commands.AdminCommand;
import me.timmy109.betterreports.commands.ReportBugCommand;
import me.timmy109.betterreports.commands.ReportPlayerCommand;
import me.timmy109.betterreports.utils.ArrayUtils;
import me.timmy109.betterreports.utils.ChatUtils;
import me.timmy109.betterreports.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;

public final class BetterReports extends JavaPlugin {

    private static BetterReports instance;

    @Override
    public void onEnable() {
        Common.log("&aStarting...");

        instance = this;

        // Registering commands
        getCommand("report").setExecutor(new ReportPlayerCommand());
        getCommand("reportbug").setExecutor(new ReportBugCommand());
        getCommand("betterreports").setExecutor(new AdminCommand());

        // Saving the default config
        saveDefaultConfig();

        // Setting the ArrayLists for the help messages
        adminHelpArrayList();
        playerHelpArrayList();
        debugArrayList();
        reloadArrayList();

        // Displaying the successfully loaded screen in console
        loadingScreen();
    }

    public static BetterReports getInstance() {
        return instance;
    }

    public void adminHelpArrayList() {
        ArrayList<String> adminHelp = ArrayUtils.getAdminHelpList();
        adminHelp.add("&8&l&m-------------------------------");
        adminHelp.add("&c&l&o        Better&4&l&oReports");
        adminHelp.add("&8    Reporting bugs & players");
        adminHelp.add("&7/report <player> <reason>" + "&c Report a player");
        adminHelp.add("&7/reportbug <bug>" + "&c Report a bug");
        adminHelp.add("&7/br reload" + "&c Reload the configuration");
        adminHelp.add("&8&l&m-------------------------------");
        ArrayUtils.setAdminHelpList(adminHelp);
    }

    public void playerHelpArrayList() {
        ArrayList<String> playerHelp = ArrayUtils.getPlayerHelpList();
        playerHelp.add("&8&l&m-------------------------------");
        playerHelp.add("&c&l&o        Better&4&l&oReports");
        playerHelp.add("&8    Reporting bugs & players");
        playerHelp.add("&7/report <player> <reason>" + "&c Report a player");
        playerHelp.add("&7/reportbug <bug>" + "&c Report a bug");
        playerHelp.add("&8&l&m-------------------------------");
        ArrayUtils.setPlayerHelpList(playerHelp);
    }

    public void debugArrayList() {
        ArrayList<String> debugList = ArrayUtils.getDebugList();
        debugList.add("&8&l&m-------------------------");
        debugList.add("&c&l&o        Better&4&l&oReports");
        debugList.add("&7Version: &c" + getDescription().getVersion());
        debugList.add("&7Author: &cTimmy109");
        debugList.add("&7Server: &c" + Bukkit.getVersion());
        debugList.add("&7PR WH Color: &c" + BetterReports.getInstance().getConfig().getString("discord-embed-player-report-colour"));
        debugList.add("&7BR WH Color: &c" + BetterReports.getInstance().getConfig().getString("discord-embed-bug-report-colour"));
        debugList.add("&8&l&m-------------------------");
        ArrayUtils.setDebugList(debugList);
    }

    public void reloadArrayList() {
        ArrayList<String> reloadList = ArrayUtils.getReloadList();
        reloadList.add("&8&l&m-------------------------");
        reloadList.add("&c&l&o        Better&4&l&oReports");
        reloadList.add("&a      Successfully reloaded!");
        reloadList.add("&8&l&m-------------------------");
        ArrayUtils.setReloadList(reloadList);
    }

    public void loadingScreen() {
        Common.logNoPrefix("&r &r");
        Common.logNoPrefix("&d    ____  &b____ ");
        Common.logNoPrefix("&d   / __ )&b/ __ \\");
        Common.logNoPrefix("&d  / __  &b/ /_/ /");
        Common.logNoPrefix("&d / /_/ &b/ _, _/ ");
        Common.logNoPrefix("&d/_____&b/_/ |_|  ");
        Common.logNoPrefix("&r &r");
        Common.logNoPrefix("&bVersion: " + getDescription().getVersion() + " - Timmy109");
        Common.logNoPrefix("&bSuccessfully enabled");
        Common.logNoPrefix("&r &r");
    }
}
