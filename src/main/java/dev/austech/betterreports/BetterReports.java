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
import dev.austech.betterreports.util.Config;
import dev.austech.betterreports.util.Counter;
import dev.austech.betterreports.util.UpdateCheck;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterReports extends JavaPlugin {
    @Getter
    private static BetterReports instance;

    @Getter
    private FileConfiguration config;

    @Getter
    private Counter counter;

    @Getter
    private boolean usePlaceholderApi = false;

    private Config configUtil;

    @Override
    public void reloadConfig() {
        configUtil.reload();
        this.config = configUtil.getConfiguration();

        if (Config.Values.COUNTER.getBoolean()) {
            this.counter = new Counter();
            this.counter.load();
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        configUtil = new Config(this);
        this.config = configUtil.getConfiguration();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            usePlaceholderApi = true;
        }

        new UpdateCheck("br", this).check();
        new Metrics(this, 15884);

        if (Config.Values.COUNTER.getBoolean()) {
            this.counter = new Counter();
            this.counter.load();
        }


        getCommand("betterreports").setExecutor(new BetterReportsCommand());
        setupCommands();
    }

    private void setupCommands() {
        getCommand("report").setExecutor(new ReportCommand());

        if (Config.Values.BUG_REPORT_ENABLED.getBoolean())
            getCommand("reportbug").setExecutor(new ReportBugCommand());

        if (Config.Values.PLAYER_REPORT_ENABLED.getBoolean())
            getCommand("reportplayer").setExecutor(new ReportPlayerCommand());
    }
}
