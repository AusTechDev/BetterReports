/*
 * BetterReports - Common.java
 *
 * Plugin created by Timmy109
 * Github profile: https://github.com/Timmy109
 * Spigot Profile: https://www.spigotmc.org/members/_timmyy_.919057/
 * Discord Server: https://discord.gg/wafV4VP
 *
 * MIT License
 *
 * Copyright (c) 2020 Joshua Sing.
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
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public final class Common {
    private static String logPrefix = "&8[&c&l&oBetter&4&l&oReports&r&8] &f";

    public static void log(String message){
        Bukkit.getServer().getConsoleSender().sendMessage(color(logPrefix + message));
    }
    
    public static void logNoPrefix(String message){
        Bukkit.getServer().getConsoleSender().sendMessage(color(message));
    }

    public static String color(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static FileConfiguration getConfig() {
        return BetterReports.getInstance().getConfig();
    }
}
