/*
 * BetterReports - UpdateCheck.java
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

package dev.austech.betterreports.util;

import dev.austech.betterreports.util.config.impl.MainConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateCheck implements Listener {
    private final String plugin;
    private final JavaPlugin javaPlugin;
    private final String version;

    @Getter
    public String updateAvailable;

    public UpdateCheck(final String plugin, final JavaPlugin javaPlugin) {
        this.plugin = plugin;
        this.javaPlugin = javaPlugin;
        this.version = javaPlugin.getDescription().getVersion();

        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    public UpdateCheck(final String plugin, final JavaPlugin javaPlugin, final String version) {
        this.plugin = plugin;
        this.javaPlugin = javaPlugin;
        this.version = version;

        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    private final BukkitRunnable runnable = new BukkitRunnable() {
        @Override
        public void run() {
            final Response response = fetchResponse();

            if (response != null) {
                if (response.getRestriction() != null && MainConfig.Values.SECURITY_CHECK.getBoolean()) {
                    final UpdateCheck.Response.Restriction restriction = response.getRestriction();
                    if (restriction.getType() == Response.Restriction.Type.ADVISORY) {
                        String reason = restriction.getReason();
                        if (reason.startsWith("RAW:")) {
                            Common.error(reason.substring(4));
                        } else {
                            Common.error("An advisory has been issued for this version: " + reason);
                            Common.error("Please update " + javaPlugin.getDescription().getName() + " to the latest version.");
                        }
                    } else {
                        Common.error("Could not start " + javaPlugin.getDescription().getName() + ": " + restriction.getReason());
                        Common.error("Please update " + javaPlugin.getDescription().getName() + " to the latest version.");
                        Bukkit.getPluginManager().disablePlugin(javaPlugin);
                    }
                    return;
                }

                if (response.getVersion() != null && MainConfig.Values.UPDATE_CHECK.getBoolean()) {
                    Common.log("A new version for " + javaPlugin.getDescription().getName() + " is available.");
                    Common.log("&rNew Version: &a" + response.getVersion() + "&r, Old Version: &c" + version + "&r.");
                    updateAvailable = response.getVersion();
                    return;
                }


                if (response.getError() != null) {
                    if (response.getError().message == null) {
                        Common.debug("Failed Response: " + Common.GSON.toJson(response));
                    }
                    Common.error("An error occurred whilst fetching updates: " + response.getError().message);
                }
            } else {
                Common.error("An error occurred whilst fetching updates.");
            }
        }
    };

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (updateAvailable != null && MainConfig.Values.UPDATE_CHECK.getBoolean() && MainConfig.Values.UPDATE_NOTIFY.getBoolean() && event.getPlayer().hasPermission("betterreports.admin")) {
            Bukkit.getScheduler().runTaskLater(javaPlugin, () -> {
                event.getPlayer().sendMessage(Common.color("&c&l&oBetter&4&l&oReports &7- &aThere is a new update available."));
                event.getPlayer().sendMessage(Common.color("&cCurrent Version: &c" + version + "&r, &aNew Version: &a" + updateAvailable));
                event.getPlayer().sendMessage(Common.color("&cDownload here: &7&nhttps://austech.dev/to/betterreports"));
            }, 10);
        }
    }

    public void check() {
        if (!MainConfig.Values.SECURITY_CHECK.getBoolean() && !MainConfig.Values.UPDATE_CHECK.getBoolean()) return;

        if (this.version.contains("-dev") || this.version.contains("-SNAPSHOT")) {
            Common.debug("Skipping update check for development build.");
            return;
        }

        runnable.runTaskTimerAsynchronously(javaPlugin, 0, 20 * 60 * 30);
    }

    public Response fetchResponse() {
        try {
            final URL url = new URL("https://api.austech.dev/v1/plugin/" + plugin + "/check?version=" + version);

            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");

            connection.connect();

            final int statusCode = connection.getResponseCode();

            if (statusCode == 200) return new Response();
            final InputStream is = connection.getErrorStream();

            final InputStream stream = new BufferedInputStream(is);

            final Response response = Common.GSON.fromJson(new InputStreamReader(stream), Response.class);

            is.close();
            connection.disconnect();

            return response;
        } catch (final Exception exception) {
            if (MainConfig.Values.DEBUG.getBoolean()) exception.printStackTrace();
        }

        return null;
    }

    @NoArgsConstructor
    @Data
    public static class Response {
        private ResponseCode error;

        private Restriction restriction;
        private String version;

        @Data
        public static class Restriction {
            private Type type;
            private String range;
            private String reason;

            enum Type {
                ADVISORY, FORCE
            }
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    enum ResponseCode {
        NO_PLUGIN("The provided plugin could not be found."),
        NO_VERSION_PROVIDED("No version was provided."),
        INVALID_VERSION("The provided version is invalid."),
        NO_VERSION_FOUND("The provided version could not be found for this plugin."),

        RESTRICTION, // Auto Handled
        NEW_VERSION; // Auto Handled

        String message;
    }
}
