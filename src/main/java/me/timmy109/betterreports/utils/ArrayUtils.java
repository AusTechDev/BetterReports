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
import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

    private static List<String> adminHelp = new ArrayList<>();

    private static List<String> playerHelp = new ArrayList<>();

    private static List<String> debug = new ArrayList<>();

    private static List<String> reload = new ArrayList<>();

    public static ArrayList<String> getAdminHelpList() {
        return (ArrayList<String>) adminHelp;
    }

    public static void setAdminHelpList(ArrayList<String> list) {
        ArrayUtils.adminHelp = list;
    }

    public static ArrayList<String> getPlayerHelpList() {
        return (ArrayList<String>) playerHelp;
    }

    public static void setPlayerHelpList(ArrayList<String> list) {
        ArrayUtils.playerHelp = list;
    }

    public static ArrayList<String> getDebugList() {
        return (ArrayList<String>) debug;
    }

    public static void setDebugList(ArrayList<String> list) {
        ArrayUtils.debug = list;
    }

    public static ArrayList<String> getReloadList() {
        return (ArrayList<String>) reload;
    }

    public static void setReloadList(ArrayList<String> list) {
        ArrayUtils.reload = list;
    }
}

