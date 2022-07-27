/*
 * BetterReports - PlaceholderUtil.java
 *
 * Copyright (c) 2022 AusTech Development
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

package dev.austech.betterreports.util;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.model.report.Report;
import dev.austech.betterreports.util.config.impl.MainConfig;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@UtilityClass
public class PlaceholderUtil {
    public String applyPlaceholders(final Report report, final String string) {
        final Player player = report.getCreator();
        final String reason = report.getReason();
        final OfflinePlayer target = report.getTarget();

        String str = string
                .replace("{player}", player.getName())
                .replace("{creator}", player.getName())
                .replace("{sender}", player.getName())
                .replace("{reason}", reason)
                .replace("{report}", reason);

        if (MainConfig.Values.COUNTER.getBoolean()) {
            final Counter counter = BetterReports.getInstance().getCounter();

            str = str.replace("{global_counter}", counter.getGlobalCounter() + "")
                    .replace("{bug_counter}", counter.getBugCounter() + "")
                    .replace("{player_counter}", counter.getPlayerCounter() + "");
        }

        if (target != null && target.getName() != null) {
            str = str.replace("{target}", target.getName());

            if (BetterReports.getInstance().isUsePlaceholderApi()) {
                str = PlaceholderUtil.handleDualPlaceholders(str, "sender", player, "target", target);
            }
        } else {
            if (BetterReports.getInstance().isUsePlaceholderApi()) {
                str = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, str.replace("%sender_", "%"));
            }
        }

        return str;
    }

    public String handleDualPlaceholders(final String string, final String first, final OfflinePlayer firstPlayer, final String second, final OfflinePlayer secondPlayer) {
        if (firstPlayer == null) {
            return handleConsolePlaceholders(string, first, second, secondPlayer);
        }

        final String newString = PlaceholderAPI.setPlaceholders(firstPlayer, string.replace("%" + first + "_", "%"));
        return PlaceholderAPI.setPlaceholders(secondPlayer, newString.replace("%" + second + "_", "%"));
    }

    public String handleConsolePlaceholders(final String string, final String first, final String second, final OfflinePlayer secondPlayer) {
        final String newString = string
                .replace("%" + first + "_player_name%", "Console")
                .replace("%" + first + "_player_displayname%", "Console");

        return PlaceholderAPI.setPlaceholders(secondPlayer, newString.replace("%" + second + "_", "%"));
    }
}
