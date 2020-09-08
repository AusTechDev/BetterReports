package me.timmy109.betterreports.utils;

import org.bukkit.ChatColor;

public class ChatUtils {

    // This is used to shorten "translateAlterNateColorCodes" to just "ChatUtils.color"
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
