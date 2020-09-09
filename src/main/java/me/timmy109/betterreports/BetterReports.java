package me.timmy109.betterreports;

/*
 Plugin created by Timmy109
 Github profile: https://github.com/Timmy109
 Spigot Profile: https://www.spigotmc.org/members/_timmyy_.919057/
 Discord Server: https://discord.gg/wafV4VP

 Feel free to modify code, however I take no responsibility for code modifications which may result in unintended and potentially fatal behaviour.
 Please note that support cannot be provided to a modified version of this plugin.
 */

import me.timmy109.betterreports.commands.ReloadCommand;
import me.timmy109.betterreports.commands.ReportBugCommand;
import me.timmy109.betterreports.commands.ReportPlayerCommand;
import me.timmy109.betterreports.utils.ArrayUtils;
import me.timmy109.betterreports.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;

public final class BetterReports extends JavaPlugin {

    private static BetterReports instance;

    @Override
    public void onEnable() {

        instance = this;

        // Registering commands
        getCommand("report").setExecutor(new ReportPlayerCommand());
        getCommand("reportbug").setExecutor(new ReportBugCommand());
        getCommand("betterreports").setExecutor(new ReloadCommand());

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
        debugList.add("&7Version: &c1.0.0");
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
        this.getServer().getConsoleSender().sendMessage(ChatUtils.color(""));
        this.getServer().getConsoleSender().sendMessage(ChatUtils.color("&d    ____  &b____ "));
        this.getServer().getConsoleSender().sendMessage(ChatUtils.color("&d   / __ )&b/ __ \\"));
        this.getServer().getConsoleSender().sendMessage(ChatUtils.color("&d  / __  &b/ /_/ /"));
        this.getServer().getConsoleSender().sendMessage(ChatUtils.color("&d / /_/ &b/ _, _/ "));
        this.getServer().getConsoleSender().sendMessage(ChatUtils.color("&d/_____&b/_/ |_|  "));
        this.getServer().getConsoleSender().sendMessage(ChatUtils.color(""));
        this.getServer().getConsoleSender().sendMessage(ChatUtils.color("&bVersion 1.0.0 - Timmy109"));
        this.getServer().getConsoleSender().sendMessage(ChatUtils.color("&bSuccessfully enabled"));
        this.getServer().getConsoleSender().sendMessage(ChatUtils.color(""));
    }
}
