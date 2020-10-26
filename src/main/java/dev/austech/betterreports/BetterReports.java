/*
 * BetterReports - BetterReports.java
 *
 * Github: https://github.com/AusTechDev/
 * Spigot Profile: https://www.spigotmc.org/members/_timmyy_.919057/
 * Discord Server: https://discord.austech.dev/
 *
 * MIT License
 *
 * Copyright (c) 2020 Timmy109.
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
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dev.austech.betterreports;
import dev.austech.betterreports.commands.AdminCommand;
import dev.austech.betterreports.commands.ReportBugCommand;
import dev.austech.betterreports.commands.ReportPlayerCommand;
import dev.austech.betterreports.utils.Common;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterReports extends JavaPlugin {

    private static BetterReports instance;

    @Override
    public void onEnable() {

        // Recording the system time in milliseconds
        long startTimer = System.currentTimeMillis();

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

        // Recording the system time in milliseconds after everything has loaded
        long endTimer = System.currentTimeMillis();

        // Subtracting the end time from the start time to display how long the plugin took to enable
        long time = endTimer - startTimer;

        // Logging to console that the plugin has enabled, accompanied by the time taken to do so
        Common.log("&5|     &bSuccessfully Enabled - Took &7" + time + "&bms");
        Common.log("&5|");
    }

    public static BetterReports getInstance() {
        return instance;
    }

    public void loadingScreenFrames() {
        Common.log("&5|");
        Common.log("&5|     &d    ____  &b____ ");
        Common.log("&5|     &d   / __ )&b/ __ \\");
        Common.log("&5|     &d  / __  &b/ /_/ /");
        Common.log("&5|     &d / /_/ &b/ _, _/ ");
        Common.log("&5|     &d/_____&b/_/ |_|  ");
        Common.log("&5|");
        Common.log("&5|     &bVersion: " + getDescription().getVersion() + " - Timmy109");
    }
}
