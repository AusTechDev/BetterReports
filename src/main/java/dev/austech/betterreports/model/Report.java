/*
 * BetterReports - Report.java
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

package dev.austech.betterreports.model;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.Config;
import dev.austech.betterreports.util.Counter;
import dev.austech.betterreports.util.PlaceholderUtil;
import dev.austech.betterreports.util.discord.Webhook;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@Data
public class Report {
    public final static HashMap<UUID, Long> cooldownMap = new HashMap<>();

    private final Type type;
    private final Player player;
    private final String report;

    private OfflinePlayer target = null;

    private String applyPlaceholders(final String string) {
        String str = string
                .replace("{player}", player.getName())
                .replace("{report}", report);

        if (Config.Values.COUNTER.getBoolean()) {
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

    private Webhook.EmbedObject createEmbed() {
        final Webhook.EmbedObject eb = new Webhook.EmbedObject();

        final String prefix = type.name().toLowerCase();

        for (int i = 1; i < 26; i++) {
            if (BetterReports.getInstance().getConfig().getString(prefix + "-report-fields." + i + ".title") == null)
                continue;

            eb.addField(
                    applyPlaceholders(BetterReports.getInstance().getConfig().getString(prefix + "-report-fields." + i + ".title")),
                    applyPlaceholders(BetterReports.getInstance().getConfig().getString(prefix + "-report-fields." + i + ".content")),
                    BetterReports.getInstance().getConfig().getBoolean(prefix + "-report-fields." + i + ".inline")
            );
        }

        if (type == Type.BUG) {
            eb.setColor(Color.decode(Config.Values.BUG_REPORT_EMBED_COLOR.getString()));
            eb.setFooter(Config.Values.BUG_REPORT_EMBED_FOOTER.getString(), Config.Values.BUG_REPORT_EMBED_ICON.getString());
        } else {
            eb.setColor(Color.decode(Config.Values.PLAYER_REPORT_EMBED_COLOR.getString()));
            eb.setFooter(Config.Values.PLAYER_REPORT_EMBED_FOOTER.getString(), Config.Values.PLAYER_REPORT_EMBED_ICON.getString());
        }

        return eb;
    }

    public void execute() {
        final String uri = type == Type.BUG ? Config.Values.BUG_REPORT_WEBHOOK_URI.getString() : Config.Values.PLAYER_REPORT_WEBHOOK_URI.getString();

        if (uri.equalsIgnoreCase(Config.Values.DEFAULT_URI)) {
            final String message = Common.color("&cYou must change the webhook url in the config.yml in order for the webhook to be successfully sent to Discord. Should you require assistance, please join our Discord server: &nhttps://austech.dev/to/support/");
            Common.log(message);

            if (player.hasPermission("betterreports.admin")) {
                player.sendMessage(message);
            } else {
                Config.Values.LANG_ERROR.send(player);
            }
        }

        final Webhook webhook = new Webhook(uri);
        webhook.addEmbed(createEmbed());

        if (Config.Values.DISCORD_PING_PLAYER_ENABLED.getBoolean()) {
            webhook.setContent(Config.Values.DISCORD_PING_PLAYER_MENTION.getString());
        }

        Bukkit.getScheduler().runTaskAsynchronously(BetterReports.getInstance(), () -> {
            // Attempt to send webhook to Discord
            try {
                webhook.execute();

                // Successful in sending report to discord
                final String successMessage = type == Type.BUG ? Config.Values.BUG_REPORT_SUCCESS.getString() : Config.Values.PLAYER_REPORT_SUCCESS.getString();

                Arrays.stream(applyPlaceholders(successMessage).split("\\n"))
                        .forEach(s -> player.sendMessage(Common.color(s)));

                final String staffMessage = type == Type.BUG ? Config.Values.BUG_REPORT_MESSAGE.getString() : Config.Values.PLAYER_REPORT_MESSAGE.getString();

                // Send notification to relevant players
                final String[] reportAlertMessage = applyPlaceholders(staffMessage).split("\\n");

                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> player.hasPermission("betterreports.alerts"))
                        .forEach(staff -> Arrays.stream(reportAlertMessage)
                                .forEach(msg -> staff.sendMessage(Common.color(msg))));

                // If the webhook is not successfully sent, print stacktrace and error message in console

                cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());

                if (BetterReports.getInstance().getCounter() != null) {
                    if (type == Type.BUG) {
                        BetterReports.getInstance().getCounter().incrementBug();
                    } else {
                        BetterReports.getInstance().getCounter().incrementPlayer();
                    }
                }
            } catch (final Exception ex) {
                ex.printStackTrace();
                BetterReports.getInstance().getLogger().severe("This error generally indicates an incorrect setup. Please check your config.yml. If the issue persists, please join our Discord server: https://austech.dev/to/support/");
                Config.Values.LANG_ERROR.send(player);
            }
        });
    }

    public enum Type {
        BUG, PLAYER
    }
}
