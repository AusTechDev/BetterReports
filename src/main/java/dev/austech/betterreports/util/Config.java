/*
 * BetterReports - Config.java
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Config {
    @Getter
    private YamlConfiguration configuration;
    private final BetterReports instance;
    final File configFile;

    public Config(final BetterReports plugin) {
        instance = plugin;
        configFile = new File(instance.getDataFolder(), "config.yml");
        reload();
    }

    public void reload() {

        if (!configFile.getParentFile().isDirectory()) {
            configFile.getParentFile().mkdirs();
        }

        if (!configFile.exists()) {
            try (InputStream stream = instance.getResource("config.yml")) {
                Files.copy(stream, configFile.toPath());
            } catch (final IOException exception) {
                Common.error("Failed to create config file (INT_IOEXP).");
                exception.printStackTrace();
                return;
            }
        }

        final YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(configFile);
            configuration = config;

            checkForNewVersion();
        } catch (final InvalidConfigurationException exception) {
            exception.printStackTrace();

            final long time = System.currentTimeMillis();

            final File broken = new File(configFile.getAbsolutePath() + ".broken." + time);
            configFile.renameTo(broken);

            Common.error("Failed to load configuration due to an invalid configuration.");
            Common.error("The config file has been renamed to config.yml.broken." + time);

            reload(); // recursive lmao
        } catch (final Exception exception) {
            Common.error("Failed to load configuration.");
            exception.printStackTrace();
        }
    }

    private void checkForNewVersion() {
        final int latestVersion = YamlConfiguration.loadConfiguration(new InputStreamReader(instance.getResource("config.yml"))).getInt("config-version");
        final int configVersion = configuration.getInt("config-version");

        if (latestVersion == configVersion) return;

        final File old = new File(configFile.getAbsolutePath() + ".old." + configVersion);
        configFile.renameTo(old);

        try (InputStream inputStream = instance.getResource("config.yml")) {
            Files.copy(inputStream, configFile.toPath());
        } catch (final IOException e) {
            e.printStackTrace();
        }

        Common.error("Your config is outdated. It has been renamed to config.yml.old." + configVersion);
        configuration = YamlConfiguration.loadConfiguration(configFile);
    }

    @RequiredArgsConstructor
    public static enum Values {
        VERSION("config-version"),
        SECURITY_CHECK("check-for-security"),
        UPDATE_CHECK("check-for-updates"),
        UPDATE_NOTIFY("notify-for-updates"),

        COUNTER("counter"),

        REPORT_OFFLINE_PLAYERS("report-offline-players"),

        PLAYER_REPORT_ENABLED("player-reports"),
        PLAYER_REPORT_SUCCESS("player-report-success"),
        PLAYER_REPORT_MESSAGE("staff-player-report-message"),
        PLAYER_REPORT_WEBHOOK_URI("discord-player-webhook-url"),
        PLAYER_REPORT_FIELDS("player-report-fields"), // unused
        PLAYER_REPORT_EMBED_FOOTER("player-report-embed-footer"),
        PLAYER_REPORT_EMBED_ICON("player-report-embed-icon"),
        PLAYER_REPORT_EMBED_COLOR("discord-embed-player-report-colour"),
        PLAYER_REPORT_COOLDOWN("report-player-cooldown"),

        BUG_REPORT_ENABLED("bug-reports"),
        BUG_REPORT_SUCCESS("bug-report-success"),
        BUG_REPORT_MESSAGE("staff-bug-report-message"),
        BUG_REPORT_WEBHOOK_URI("discord-bug-webhook-url"),
        BUG_REPORT_FIELDS("bug-report-fields"), // unused
        BUG_REPORT_EMBED_FOOTER("bug-report-embed-footer"),
        BUG_REPORT_EMBED_ICON("bug-report-embed-icon"),
        BUG_REPORT_EMBED_COLOR("discord-embed-bug-report-colour"),
        BUG_REPORT_COOLDOWN("report-player-cooldown"),

        DISCORD_PING_PLAYER_ENABLED("discord-ping-bug-enable"),
        DISCORD_PING_PLAYER_MENTION("discord-ping-bug"),

        LANG_HELP_MESSAGE_ADMIN("lang.admin-help-message"),
        LANG_HELP_MESSAGE("lang.player-help-message"),
        LANG_NO_PERMISSION("lang.no-permission-message"),
        LANG_ERROR("erlang.ror-sending-message"),
        LANG_PLAYER_ONLY("lang.player-only-message"),
        LANG_NO_REASON("lang.no-reason-message"),
        LANG_NO_BUG("lang.no-bug-provided-message"),
        LANG_PLAYER_SELF("lang.report-self-message"),
        LANG_PLAYER_OFFLINE("lang.not-online-message"),
        LANG_PLAYER_BYPASS("lang.cannot-report-message"),
        LANG_UNKNOWN_COMMAND("lang.unknown-command"),
        LANG_CONFIG_RELOADED("lang.config-reloaded");

        private final String key;

        public String getString() {
            return BetterReports.getInstance().getConfig().getString(key);
        }

        public boolean getBoolean() {
            return BetterReports.getInstance().getConfig().getBoolean(key);
        }

        public int getInteger() {
            return BetterReports.getInstance().getConfig().getInt(key);
        }

        public Object get() {
            return BetterReports.getInstance().getConfig().get(key);
        }

        @SuppressWarnings("unchecked")
        public <T> T getType() {
            return (T) BetterReports.getInstance().getConfig().get(key);
        }

        public void send(final CommandSender sender) {
            if (sender instanceof Player && BetterReports.getInstance().isUsePlaceholderApi())
                sender.sendMessage(Common.color(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders((Player) sender, getString())));
            else
                sender.sendMessage(Common.color(getString()));
        }

        public void sendList(final CommandSender sender) {
            final List<String> arr = Arrays.asList(BetterReports.getInstance().getConfig().getString(key).split("\n"));

            if (sender instanceof Player && BetterReports.getInstance().isUsePlaceholderApi())
                arr.stream().map((it) -> Common.color(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders((Player) sender, it))).forEach(sender::sendMessage);
            else
                arr.stream().map(Common::color).forEach(sender::sendMessage);
        }

        public static final String DEFAULT_URI = "https://discord.com/api/webhooks/853603639553818684/JjxAoNimfG1XWa5wtTIp0zohIrr3vRSo0mv4qYKeMWYrfErgOJBdieU_HXTY9Suzd-MJ";
    }
}
