/*
 * BetterReports - MainConfig.java
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

package dev.austech.betterreports.util.config.impl;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.Counter;
import dev.austech.betterreports.util.PlaceholderAPIWrapper;
import dev.austech.betterreports.util.config.ConfigurationFile;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MainConfig extends ConfigurationFile {

    public MainConfig() {
        super("config.yml", true);
    }

    @Override
    public void onReload() {
        BetterReports.getInstance().reloadCommands();

        if (MainConfig.Values.COUNTER.getBoolean()) {
            BetterReports.getInstance().setCounter(new Counter());
            BetterReports.getInstance().getCounter().load();
        } else {
            BetterReports.getInstance().setCounter(null);
        }
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    public enum Values {
        VERSION("config-version"),

        SECURITY_CHECK("checking.check-for-security"),
        UPDATE_CHECK("checking.check-for-updates"),
        UPDATE_NOTIFY("checking.notify-for-updates"),

        COUNTER("counter"),

        REPORT_MENU("reports.menu"),

        PLAYER_REPORT_ENABLED("reports.player.enabled"),
        PLAYER_REPORT_OFFLINE_ENABLED("reports.player.offline-players.reporting-enabled"),
        PLAYER_REPORT_OFFLINE_NEVER_JOINED("reports.player.offline-players.never-joined-before"),
        PLAYER_REPORT_COOLDOWN_ENABLED("reports.player.cooldown.enabled"),
        PLAYER_REPORT_COOLDOWN_TIME("reports.player.cooldown.time"),
        PLAYER_REPORT_MENUS_SELECT_PLAYER("reports.player.menus.select-players"),
        PLAYER_REPORT_MENUS_SELECT_REASON("reports.player.menus.select-reason"),
        PLAYER_REPORT_MENUS_CONFIRM_REPORT("reports.player.menus.confirm-report"),
        PLAYER_REPORT_DISCORD_WEBHOOK_URI("reports.player.discord.webhook"),
        PLAYER_REPORT_DISCORD_EMBED_AUTHOR_NAME("reports.player.discord.embed.author.name"),
        PLAYER_REPORT_DISCORD_EMBED_AUTHOR_URL("reports.player.discord.embed.author.url"),
        PLAYER_REPORT_DISCORD_EMBED_AUTHOR_ICON_URL("reports.player.discord.embed.author.icon-url"),
        PLAYER_REPORT_DISCORD_EMBED_BODY_TITLE("reports.player.discord.embed.body.title"),
        PLAYER_REPORT_DISCORD_EMBED_BODY_TITLE_URL("reports.player.discord.embed.body.title-url"),
        PLAYER_REPORT_DISCORD_EMBED_BODY_DESCRIPTION("reports.player.discord.embed.body.description"),
        PLAYER_REPORT_DISCORD_EMBED_BODY_COLOR("reports.player.discord.embed.body.color"),
        PLAYER_REPORT_DISCORD_EMBED_FIELDS("reports.player.discord.embed.fields"),
        PLAYER_REPORT_DISCORD_EMBED_IMAGES_IMAGE("reports.player.discord.embed.images.image"),
        PLAYER_REPORT_DISCORD_EMBED_IMAGES_THUMBNAIL("reports.player.discord.embed.images.thumbnail"),
        PLAYER_REPORT_DISCORD_EMBED_FOOTER_TIMESTAMP("reports.player.discord.embed.footer.timestamp"),
        PLAYER_REPORT_DISCORD_EMBED_FOOTER_TEXT("reports.player.discord.embed.footer.text"),
        PLAYER_REPORT_DISCORD_EMBED_FOOTER_ICON_URL("reports.player.discord.embed.footer.icon-url"),
        PLAYER_REPORT_DISCORD_PING_ENABLED("reports.player.discord.ping.enabled"),
        PLAYER_REPORT_DISCORD_PING_VALUE("reports.player.discord.ping.value"),
        PLAYER_REPORT_MESSAGES_NEW_REPORT("reports.player.messages.new-report"),
        PLAYER_REPORT_MESSAGES_SUCCESS("reports.player.messages.success"),

        BUG_REPORT_ENABLED("reports.bug.enabled"),
        BUG_REPORT_COOLDOWN_ENABLED("reports.bug.cooldown.enabled"),
        BUG_REPORT_COOLDOWN_TIME("reports.bug.cooldown.time"),
        BUG_REPORT_MENUS_CONFIRM_REPORT("reports.bug.menus.confirm-report"),
        BUG_REPORT_DISCORD_WEBHOOK_URI("reports.bug.discord.webhook"),
        BUG_REPORT_DISCORD_EMBED_AUTHOR_NAME("reports.bug.discord.embed.author.name"),
        BUG_REPORT_DISCORD_EMBED_AUTHOR_URL("reports.bug.discord.embed.author.url"),
        BUG_REPORT_DISCORD_EMBED_AUTHOR_ICON_URL("reports.bug.discord.embed.author.icon-url"),
        BUG_REPORT_DISCORD_EMBED_BODY_TITLE("reports.bug.discord.embed.body.title"),
        BUG_REPORT_DISCORD_EMBED_BODY_TITLE_URL("reports.bug.discord.embed.body.title-url"),
        BUG_REPORT_DISCORD_EMBED_BODY_DESCRIPTION("reports.bug.discord.embed.body.description"),
        BUG_REPORT_DISCORD_EMBED_BODY_COLOR("reports.bug.discord.embed.body.color"),
        BUG_REPORT_DISCORD_EMBED_FIELDS("reports.bug.discord.embed.fields"),
        BUG_REPORT_DISCORD_EMBED_IMAGES_IMAGE("reports.bug.discord.embed.images.image"),
        BUG_REPORT_DISCORD_EMBED_IMAGES_THUMBNAIL("reports.bug.discord.embed.images.thumbnail"), BUG_REPORT_DISCORD_EMBED_FOOTER_TIMESTAMP("reports.bug.discord.embed.footer.timestamp"),
        BUG_REPORT_DISCORD_EMBED_FOOTER_TEXT("reports.bug.discord.embed.footer.text"),
        BUG_REPORT_DISCORD_EMBED_FOOTER_ICON_URL("reports.bug.discord.embed.footer.icon-url"),
        BUG_REPORT_DISCORD_PING_ENABLED("reports.bug.discord.ping.enabled"),
        BUG_REPORT_DISCORD_PING_VALUE("reports.bug.discord.ping.value"),
        BUG_REPORT_MESSAGES_NEW_REPORT("reports.bug.messages.new-report"),
        BUG_REPORT_MESSAGES_SUCCESS("reports.bug.messages.success"),

        LANG_HELP_MESSAGE_ADMIN("language.admin-help-message"),
        LANG_HELP_MESSAGE("language.player-help-message"),
        LANG_NO_PERMISSION("language.no-permission-message"),
        LANG_ERROR("language.error-sending-message"),
        LANG_PLAYER_ONLY("language.player-only-message"),
        LANG_NO_REASON("language.no-reason-message"),
        LANG_NO_BUG("language.no-bug-provided-message"),
        LANG_PLAYER_SELF("language.report-self-message"),
        LANG_PLAYER_NOT_FOUND("language.player-not-found"),
        LANG_PLAYER_BYPASS("language.cannot-report-message"),
        LANG_UNKNOWN_COMMAND("language.unknown-command"),
        LANG_CONFIG_RELOADED("language.config-reloaded"),
        LANG_COOLDOWN("language.cooldown-message"),
        LANG_USAGE_PREFIX("language.usage.prefix"),
        LANG_USAGE_REPORT("language.usage.report"),
        LANG_USAGE_REPORT_BUG("language.usage.report-bug"),
        LANG_USAGE_REPORT_PLAYER("language.usage.report-player"),
        LANG_QUESTION_BUG_ENABLED("language.bug-question.enabled", true),
        LANG_QUESTION_BUG_MESSAGE("language.bug-question.message"),
        LANG_QUESTION_BUG_TITLE("language.bug-question.title"),
        LANG_QUESTION_BUG_SUBTITLE("language.bug-question.subtitle"),
        LANG_QUESTION_PLAYER_SEARCH_ENABLED("language.player-search-question.enabled", true),
        LANG_QUESTION_PLAYER_SEARCH_MESSAGE("language.player-search-question.message"),
        LANG_QUESTION_PLAYER_SEARCH_TITLE("language.player-search-question.title"),
        LANG_QUESTION_PLAYER_SEARCH_SUBTITLE("language.player-search-question.subtitle"),
        LANG_QUESTION_CUSTOM_REASON_ENABLED("language.custom-reason-question.enabled", true),
        LANG_QUESTION_CUSTOM_REASON_MESSAGE("language.custom-reason-question.message"),
        LANG_QUESTION_CUSTOM_REASON_TITLE("language.custom-reason-question.title"),
        LANG_QUESTION_CUSTOM_REASON_SUBTITLE("language.custom-reason-question.subtitle"),
        LANG_BUG_REPORTS_DISABLED("language.bug-reports-disabled"),
        LANG_PLAYER_REPORTS_DISABLED("language.player-reports-disabled"),
        LANG_REPORT_CANCELLED("language.report-cancelled"),

        DEBUG("debug", false);

        private final String key;
        private Object defaultValue;

        public YamlConfiguration getConfig() {
            return BetterReports.getInstance().getConfigManager().getMainConfig().getConfig();
        }

        public boolean contains() {
            return getConfig().contains(key);
        }

        public String getString() {
            if (!contains()) return defaultValue.toString();
            return getConfig().getString(key);
        }

        public boolean getBoolean() {
            if (!contains()) return (boolean) defaultValue;
            return getConfig().getBoolean(key);
        }

        public int getInteger() {
            if (!contains()) return (int) defaultValue;
            return getConfig().getInt(key);
        }

        public ConfigurationSection getSection() {
            return getConfig().getConfigurationSection(key);
        }

        public List<String> getStringList() {
            return getConfig().getStringList(key);
        }

        public String getPlaceholderString(final Player player) {
            if (BetterReports.getInstance().isUsePlaceholderApi())
                return Common.color(PlaceholderAPIWrapper.setPlaceholders(player, getString()));
            else
                return Common.color(getString());
        }

        public void send(final CommandSender sender) {
            sendInternal(sender, getString());
        }

        private void sendInternal(final CommandSender sender, final String s) {
            if (sender instanceof Player && BetterReports.getInstance().isUsePlaceholderApi())
                sender.sendMessage(Common.color(PlaceholderAPIWrapper.setPlaceholders((Player) sender, s)));
            else
                sender.sendMessage(Common.color(s));
        }

        public void sendRaw(final Player sender) {
            if (BetterReports.getInstance().isUsePlaceholderApi())
                sender.sendRawMessage(Common.color(PlaceholderAPIWrapper.setPlaceholders(sender, getString())));
            else
                sender.sendRawMessage(Common.color(getString()));
        }

        public void sendList(final CommandSender sender) {
            final List<String> arr = Arrays.asList(BetterReports.getInstance().getConfigManager().getMainConfig().getConfig().getString(key).split("\n"));

            if (sender instanceof Player && BetterReports.getInstance().isUsePlaceholderApi())
                arr.stream().map((it) -> Common.color(PlaceholderAPIWrapper.setPlaceholders((Player) sender, it))).forEach(sender::sendMessage);
            else
                arr.stream().map(Common::color).forEach(sender::sendMessage);
        }

        public void sendUsage(final CommandSender sender) {
            sendInternal(sender, Values.LANG_USAGE_PREFIX.getString() + getString());
        }

        public static final String DEFAULT_URI = "https://discord.com/api/webhooks/853603639553818684/JjxAoNimfG1XWa5wtTIp0zohIrr3vRSo0mv4qYKeMWYrfErgOJBdieU_HXTY9Suzd-MJ";
    }
}
