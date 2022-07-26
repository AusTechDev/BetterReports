/*
 * BetterReports - DiscordManager.java
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

package dev.austech.betterreports.util.discord;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.model.report.Report;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.PlaceholderUtil;
import dev.austech.betterreports.util.data.MainConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class DiscordManager {
    @Getter
    private static final DiscordManager instance = new DiscordManager();

    public void sendReport(final Player player, final Report report) {
        final Webhook.WebhookBuilder builder = Webhook.builder();
        final Webhook.EmbedObject embed = createEmbed(report);

        final String prefix = report.getType().name().toUpperCase() + "_REPORT";

        final String WEBHOOK_URI = MainConfig.Values.valueOf(prefix + "_DISCORD_WEBHOOK_URI").getString();

        final boolean PING_ENABLED = MainConfig.Values.valueOf(prefix + "_DISCORD_PING_ENABLED").getBoolean();
        final String PING_VALUE = MainConfig.Values.valueOf(prefix + "_DISCORD_PING_VALUE").getString();

        final String MESSAGES_NEW_REPORT = MainConfig.Values.valueOf(prefix + "_MESSAGES_NEW_REPORT").getString();
        final String MESSAGES_SUCCESS = MainConfig.Values.valueOf(prefix + "_MESSAGES_SUCCESS").getString();

        if (WEBHOOK_URI.equalsIgnoreCase(MainConfig.Values.DEFAULT_URI)) {
            final String message = Common.color("&cYou must change the webhook url in the config.yml in order for the webhook to be successfully sent to Discord. Should you require assistance, please join our Discord server: &nhttps://austech.dev/to/support/");
            Common.log(message);

            if (player.hasPermission("betterreports.admin")) {
                player.sendMessage(message);
            } else {
                MainConfig.Values.LANG_ERROR.send(player);
            }
        }

        builder.url(WEBHOOK_URI);
        builder.addEmbed(embed);

        if (PING_ENABLED) {
            builder.content(PING_VALUE);
        }

        Bukkit.getScheduler().runTaskAsynchronously(BetterReports.getInstance(), () -> {
            try {
                final Webhook webhook = builder.build();
                webhook.execute();

                Arrays.stream(Common.color(PlaceholderUtil.applyPlaceholders(report, MESSAGES_SUCCESS)).split("\\n")).forEach(player::sendMessage);

                Arrays.stream(Common.color(PlaceholderUtil.applyPlaceholders(report, MESSAGES_NEW_REPORT)).split("\\n")).forEach((s) ->
                        Bukkit.getOnlinePlayers()
                                .stream()
                                .filter(p -> p.hasPermission("betterreports.alerts"))
                                .forEach(p -> p.sendMessage(s))
                );

                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

                if (BetterReports.getInstance().getCounter() != null) {
                    if (!report.isPlayer()) {
                        BetterReports.getInstance().getCounter().incrementBug();
                    } else {
                        BetterReports.getInstance().getCounter().incrementPlayer();
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
                BetterReports.getInstance().getLogger().severe("This error generally indicates an incorrect setup. Please check your config.yml. If the issue persists, please join our Discord server: https://austech.dev/to/support/");
                MainConfig.Values.LANG_ERROR.send(player);
            }
        });
    }

    private String ap(final Report report, final String s) {
        return PlaceholderUtil.applyPlaceholders(report, s);
    }

    private Webhook.EmbedObject createEmbed(final Report report) {
        final Webhook.EmbedObject.Builder embed = Webhook.EmbedObject.builder();

        final String prefix = report.getType().name().toUpperCase() + "_REPORT";

        final String EMBED_AUTHOR_NAME = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_AUTHOR_NAME").getString();
        final String EMBED_AUTHOR_URL = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_AUTHOR_URL").getString();
        final String EMBED_AUTHOR_ICON_URL = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_AUTHOR_ICON_URL").getString();
        final String EMBED_BODY_TITLE = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_BODY_TITLE").getString();
        final String EMBED_BODY_TITLE_URL = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_BODY_TITLE_URL").getString();
        final String EMBED_BODY_DESCRIPTION = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_BODY_DESCRIPTION").getString();
        final String EMBED_BODY_COLOR = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_BODY_COLOR").getString();
        final List<Map<?, ?>> DISCORD_EMBED_FIELDS = BetterReports.getInstance().getConfigManager().getMainConfig().getConfig().getMapList("reports." + report.getType().name().toLowerCase() + ".discord.embed.fields");
        final String EMBED_IMAGES_IMAGE = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_IMAGES_IMAGE").getString();
        final String EMBED_IMAGES_THUMBNAIL = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_IMAGES_THUMBNAIL").getString();
        final boolean EMBED_FOOTER_TIMESTAMP = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_FOOTER_TIMESTAMP").getBoolean();
        final String EMBED_FOOTER_TEXT = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_FOOTER_TEXT").getString();
        final String EMBED_FOOTER_ICON_URL = MainConfig.Values.valueOf(prefix + "_DISCORD_EMBED_FOOTER_ICON_URL").getString();

        if (EMBED_AUTHOR_NAME != null) {
            embed.author(Webhook.EmbedObject.Author.builder().name(ap(report, EMBED_AUTHOR_NAME)).url(ap(report, EMBED_AUTHOR_URL)).iconUrl(ap(report, EMBED_AUTHOR_ICON_URL)).build());
        }

        if (EMBED_BODY_TITLE != null) {
            embed.title(ap(report, EMBED_BODY_TITLE));

            if (EMBED_BODY_TITLE_URL != null) {
                embed.url(ap(report, EMBED_BODY_TITLE_URL));
            }
        }

        if (EMBED_BODY_DESCRIPTION != null) {
            embed.description(ap(report, EMBED_BODY_DESCRIPTION));
        }

        if (EMBED_BODY_COLOR != null) {
            embed.color(Color.decode(EMBED_BODY_COLOR));
        }

        if (DISCORD_EMBED_FIELDS != null) {
            DISCORD_EMBED_FIELDS.forEach(it -> {
                embed.addField(ap(report, ((String) it.get("name"))), ap(report, ((String) it.get("value"))), ((Boolean) it.get("inline")));
            });
        }

        if (EMBED_IMAGES_THUMBNAIL != null) {
            embed.thumbnail(EMBED_IMAGES_THUMBNAIL);
        }

        if (EMBED_IMAGES_IMAGE != null) {
            embed.image(EMBED_IMAGES_IMAGE);
        }

        if (EMBED_FOOTER_TIMESTAMP) {
            final TimeZone timeZone = TimeZone.getTimeZone("UTC");
            final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'");
            format.setTimeZone(timeZone);
            embed.timestamp(format.format(System.currentTimeMillis()));
        }

        if (EMBED_FOOTER_TEXT != null) {
            if (EMBED_FOOTER_ICON_URL != null)
                embed.footer(Webhook.EmbedObject.Footer.builder().text(ap(report, EMBED_FOOTER_TEXT)).iconUrl(ap(report, EMBED_FOOTER_ICON_URL)).build());
            else
                embed.footer(Webhook.EmbedObject.Footer.builder().text(ap(report, EMBED_FOOTER_TEXT)).build());

        }

        return embed.build();
    }
}
