/*
 * BetterReports - Common.java
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
package dev.austech.betterreports.utils;

import dev.austech.betterreports.BetterReports;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Common {

    public static void log(String message) {

        Bukkit.getServer().getConsoleSender().sendMessage(color(message));

    }

    public static String color(String message) {

        return ChatColor.translateAlternateColorCodes('&', message);

    }

    public static String arrayColor(String[] message) {

        return ChatColor.translateAlternateColorCodes('&', Arrays.toString(message));

    }

    public static FileConfiguration getConfig() {
        return BetterReports.getInstance().getConfig();
    }

    private final Map<UUID, Long> playerReportHash = new HashMap<>();

    public static final long playerReportCooldown = Long.parseLong(getConfig().getString("report-player-cooldown"));

    public void setPlayerReportCooldown(UUID player, long time) {
        if(time < 1) {
            playerReportHash.remove(player);
        } else {
            playerReportHash.put(player, time);
        }
    }

    public long getPlayerReportCooldown(UUID player) {
        return playerReportHash.getOrDefault(player, playerReportCooldown);
    }

    private final Map<UUID, Long> bugReportHash = new HashMap<>();

    public static final long bugReportCooldown = Long.parseLong(getConfig().getString("report-bug-cooldown"));

    public void setBugReportCooldown(UUID player, long time) {
        if (time < 1) {
            bugReportHash.remove(player);
        } else {
            bugReportHash.put(player, time);
        }
    }

    public long getBugReportCooldown(UUID player) {
        return bugReportHash.getOrDefault(player, bugReportCooldown);
    }

}
