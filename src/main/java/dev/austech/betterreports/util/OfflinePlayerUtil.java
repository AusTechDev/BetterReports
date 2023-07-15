package dev.austech.betterreports.util;

import dev.austech.betterreports.util.config.impl.MainConfig;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

@UtilityClass
public class OfflinePlayerUtil {
    @SuppressWarnings("deprecation")
    public OfflinePlayer get(String name) {
        OfflinePlayer found = Bukkit.getOfflinePlayer(name);

        if (found == null
                || !found.isOnline() && !MainConfig.Values.PLAYER_REPORT_OFFLINE_ENABLED.getBoolean()
                || !found.hasPlayedBefore() && !MainConfig.Values.PLAYER_REPORT_OFFLINE_NEVER_JOINED.getBoolean())
            return null;
        else return found;
    }
}
