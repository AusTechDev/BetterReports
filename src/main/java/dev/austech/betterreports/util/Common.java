/*
 * BetterReports - Common.java
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
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@UtilityClass
public class Common {
    private final static ConsoleCommandSender CONSOLE_SENDER = Bukkit.getConsoleSender();

    public void log(final String... string) {
        Arrays.stream(string).map(str -> "[BetterReports] " + str).map(Common::color).forEach(CONSOLE_SENDER::sendMessage);
    }

    public void logDirect(final String... string) {
        Arrays.stream(string).map(Common::color).forEach(CONSOLE_SENDER::sendMessage);
    }

    public void error(final String string) {
//        log("&c" + string);
        BetterReports.getInstance().getLogger().severe(string);
    }

    public String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public String[] color(final String... strings) {
        return (String[]) Arrays.stream(strings).map(Common::color).toArray();
    }

    public void resetTitle(final Player player) {
        sendTitle(player, "", "", 0, 0, 0);
        player.resetTitle();
    }

    public void sendTitle(final Player player, final String title, final String subtitle, final int fadeIn, final int stay, final int fadeOut) {
        if (VersionUtil.getVersion().olderThan(VersionUtil.V.V1_11)) {
            TitleUtil.sendTitle(player, TitleUtil.Title.builder().title(title).subtitle(subtitle).fadeIn(fadeIn).stay(stay).fadeOut(fadeOut).build());
        } else {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }
}
