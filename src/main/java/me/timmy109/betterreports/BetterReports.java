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
import me.timmy109.betterreports.utils.Common;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterReports extends JavaPlugin {

    private static BetterReports instance;

    @Override
    public void onEnable() {
        Common.log("&aStarting...");

        // Setting the instance to the current JavaPlugin instance
        instance = this;

        // Registering commands
        getCommand("report").setExecutor(new ReportPlayerCommand());
        getCommand("reportbug").setExecutor(new ReportBugCommand());
        getCommand("betterreports").setExecutor(new AdminCommand());

        // Saving the default config
        saveDefaultConfig();

        // Displaying the successfully loaded screen in console
        loadingScreenFrames();
    }

    public static BetterReports getInstance() {
        return instance;
    }

    public void loadingScreenFrames() {
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
