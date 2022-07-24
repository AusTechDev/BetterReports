/*
 * BetterReports - BetterReports.java
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

package dev.austech.betterreports;

import dev.austech.betterreports.commands.BetterReportsCommand;
import dev.austech.betterreports.commands.ReportBugCommand;
import dev.austech.betterreports.commands.ReportCommand;
import dev.austech.betterreports.commands.ReportPlayerCommand;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.Counter;
import dev.austech.betterreports.util.UpdateCheck;
import dev.austech.betterreports.util.data.ConfigManager;
import dev.austech.betterreports.util.data.MainConfig;
import dev.austech.betterreports.util.menu.listener.MenuListener;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterReports extends JavaPlugin {
    @Getter
    private static BetterReports instance;

    @Getter
    @Setter
    private Counter counter;

    @Getter
    private boolean usePlaceholderApi = false;

    @Getter
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager();

        configManager.reload();

        if (!MainConfig.Values.PLAYER_REPORT_ENABLED.getBoolean() && !MainConfig.Values.BUG_REPORT_ENABLED.getBoolean()) {
            Common.error("BetterReports has been disabled as both player and bug reports are disabled in the config.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            usePlaceholderApi = true;
        }

        new UpdateCheck("br", this).check();
        new Metrics(this, 15884);

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

        getCommand("betterreports").setExecutor(new BetterReportsCommand());
        getCommand("report").setExecutor(new ReportCommand());
        reloadCommands();
    }

    public void reloadCommands() {
        if (MainConfig.Values.BUG_REPORT_ENABLED.getBoolean())
            getCommand("reportbug").setExecutor(new ReportBugCommand());
        else getCommand("reportbug").setExecutor(null);

        if (MainConfig.Values.PLAYER_REPORT_ENABLED.getBoolean())
            getCommand("reportplayer").setExecutor(new ReportPlayerCommand());
        else getCommand("reportplayer").setExecutor(null);
    }
}
