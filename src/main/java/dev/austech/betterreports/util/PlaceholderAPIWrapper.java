package dev.austech.betterreports.util;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class PlaceholderAPIWrapper {
    @NotNull
    public static String setPlaceholders(final Player player, @NotNull String text) {
        return PlaceholderAPI.setPlaceholders(((OfflinePlayer) player), text);
    }

    @NotNull
    public static String setPlaceholders(final OfflinePlayer player, @NotNull String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
