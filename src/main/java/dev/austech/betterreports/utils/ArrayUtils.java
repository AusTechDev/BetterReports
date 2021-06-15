/*
 * BetterReports - ArrayUtils.java
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
package dev.austech.betterreports.utils;

import dev.austech.betterreports.BetterReports;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;

public class ArrayUtils {

    public static ArrayList<String> getDebugList() {
        ArrayList<String> debugList = new ArrayList<>();
        debugList.add("&8&l&m-------------------------");
        debugList.add("&c&l&o        Better&4&l&oReports");
        debugList.add("&7Version: &c" + BetterReports.getInstance().getDescription().getVersion());
        debugList.add("&7Author: &cAusTech Development");
        debugList.add("&7Server: &c" + Bukkit.getVersion());
        debugList.add("&7PR WH Color: &c" + Common.getConfig().getString("discord-embed-player-report-colour"));
        debugList.add("&7BR WH Color: &c" + Common.getConfig().getString("discord-embed-bug-report-colour"));
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

