/*
 * BetterReports - ConfigurationFile.java
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

package dev.austech.betterreports.util.data;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.util.Common;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;

@Getter
@RequiredArgsConstructor
public class ConfigurationFile {
    private final String fileName;
    private final File file;
    private final boolean configVersion;
    private final YamlConfiguration config = new YamlConfiguration();

    public ConfigurationFile(final String fileName, final boolean configVersion) {
        this.fileName = fileName;
        this.configVersion = configVersion;

        this.file = new File(BetterReports.getInstance().getDataFolder(), fileName);

//        load();
    }

    public void onReload() {
    }

    private void load() {
        file.getParentFile().mkdirs();

        if (!file.exists()) {
            try (InputStream stream = BetterReports.getInstance().getResource(fileName)) {
                Files.copy(stream, file.toPath());
            } catch (final IOException exception) {
                Common.error("Failed to create config file \"" + fileName + "\".");
                exception.printStackTrace();
                return;
            }
        }


        try {
            this.config.load(file);
            checkNewVersion();
        } catch (final InvalidConfigurationException exception) {
            exception.printStackTrace();

            final long time = System.currentTimeMillis();
            final File brokenFile = new File(file.getAbsolutePath() + ".broken." + time);
            file.renameTo(brokenFile);

            Common.error("Failed to load \"" + fileName + "\" due to an invalid configuration.");
            Common.error("The config file has been renamed to " + brokenFile.getName() + ".");

            reload(); // Re initialize the config file.
        } catch (final IOException exception) {
            exception.printStackTrace();
            Common.error("Failed to load \"" + fileName + "\".");
        }
    }

    public final void reload() {
        load();
        onReload();
    }

    private void checkNewVersion() {
        if (!configVersion) return;

        final int latestVersion = YamlConfiguration.loadConfiguration(new InputStreamReader(BetterReports.getInstance().getResource("config.yml"))).getInt("config-version");
        final int currentVersion = config.getInt("config-version");

        if (currentVersion != latestVersion) {
            final File brokenFile = new File(file.getAbsolutePath() + ".old." + config.getInt("config-version"));
            file.renameTo(brokenFile);

            Common.error("Failed to load \"" + fileName + "\" as it is outdated.");
            Common.error("The config file has been renamed to " + brokenFile.getName() + ", and the config has been reset.");

            reload();
        }
    }
}
