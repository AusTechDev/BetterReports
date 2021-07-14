package dev.austech.betterreports.events;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.utils.Common;
import dev.austech.betterreports.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(BetterReports.getInstance(), () -> {
            if ((e.getPlayer().hasPermission("betterreports.admin")
                    && UpdateChecker.needsUpdate(BetterReports.getInstance().getDescription().getVersion()))
                    && Common.getConfig().getBoolean("check-for-updates")) {

                e.getPlayer().sendMessage(Common.color("&c&l&oBetter&4&l&oReports &7- &aThere is a new update available."));
                e.getPlayer().sendMessage(Common.color("&cDownload here: &7&nhttps://austech.dev/to/betterreports"));
            }
        });
    }
}
