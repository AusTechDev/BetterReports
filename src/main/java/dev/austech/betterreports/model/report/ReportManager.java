/*
 * BetterReports - ReportManager.java
 *
 * Copyright (c) 2023 AusTech Development
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

package dev.austech.betterreports.model.report;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.PlaceholderAPIWrapper;
import dev.austech.betterreports.util.config.impl.MainConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ReportManager {
    @Getter
    private static final ReportManager instance = new ReportManager();

    @Getter
    private static final HashMap<UUID, HashMap<Report.Type, Long>> cooldowns = new HashMap<>();

    @Getter
    private final List<Report> reports = new ArrayList<>();

    public static boolean checkCooldown(final Player player, final Report.Type type) {
        final long cooldown = ReportManager.getInstance().getCooldown(player, type);
        if (cooldown == -1) {
            return false;
        } else {
            String message = MainConfig.Values.LANG_COOLDOWN.getString()
                    .replace("{time}", (cooldown / 1000) + "")
                    .replace("{type}", type.name().toLowerCase());
            if (BetterReports.getInstance().isUsePlaceholderApi()) {
                message = PlaceholderAPIWrapper.setPlaceholders(player, message);
            }

            player.sendMessage(Common.color(message));
            return true;
        }
    }

    public boolean isBugReportsEnabled() {
        return MainConfig.Values.BUG_REPORT_ENABLED.getBoolean();
    }

    public boolean isPlayerReportsEnabled() {
        return MainConfig.Values.PLAYER_REPORT_ENABLED.getBoolean();
    }

    public long getCooldown(final UUID uuid, final Report.Type type) {
        if (Bukkit.getPlayer(uuid).hasPermission("betterreports.cooldown.bypass")) return -1L;
        if (!MainConfig.Values.valueOf(type.name().toUpperCase() + "_REPORT_COOLDOWN_ENABLED").getBoolean()) return -1;
        if (!cooldowns.containsKey(uuid)) return -1L;
        if (!cooldowns.get(uuid).containsKey(type)) return -1L;

        final long time = System.currentTimeMillis();
        final long cooldown = cooldowns.get(uuid).get(type);
        final long cooldownTime = MainConfig.Values.valueOf(type.name().toUpperCase() + "_REPORT_COOLDOWN_TIME").getInteger() * 1000L;

        if (time - cooldown >= cooldownTime) {
            return -1L;
        } else {
            return cooldownTime - (time - cooldown);
        }
    }

    public long getCooldown(final Player player, final Report.Type type) {
        if (player.hasPermission("betterreports.cooldown.bypass")) return -1L;
        return getCooldown(player.getUniqueId(), type);
    }

    public void addCooldown(final UUID uuid, final Report.Type type) {
        if (!cooldowns.containsKey(uuid)) {
            cooldowns.put(uuid, new HashMap<>());
        }
        cooldowns.get(uuid).put(type, System.currentTimeMillis());
    }

    public void addCooldown(final Player player, final Report.Type type) {
        addCooldown(player.getUniqueId(), type);
    }
}
