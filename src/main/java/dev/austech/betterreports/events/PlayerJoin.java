/*
 * BetterReports - PlayerJoin.java
 *
 * Github: https://github.com/AusTechDev/
 * Spigot Profile: https://www.spigotmc.org/members/austech.919057/
 * Discord Server: https://austech.dev/to/support
 *
 * MIT License
 *
 * Copyright (c) 2022 Timmy109.
 * Copyright (c) 2022 Contributors.
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
