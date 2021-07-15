package dev.austech.betterreports.utils;

import dev.austech.betterreports.BetterReports;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;

@UtilityClass
public class Config {
    public void load() throws IOException {
        BetterReports.getInstance().getDataFolder().mkdir();
        File file = new File(BetterReports.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream inputStream = BetterReports.getInstance().getClass().getResourceAsStream("/config.yml")) {
                Files.copy(inputStream, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);

            int currentVersion = YamlConfiguration.loadConfiguration(new InputStreamReader(BetterReports.getInstance().getClass().getResourceAsStream("/config.yml"))).getInt("config-version");

            if (config.getInt("config-version") != currentVersion) {
                int ver = config.getInt("config-version");

                File broken = new File(file.getAbsolutePath() + ".old." + ver);
                file.renameTo(broken);
                BetterReports.getInstance().getLogger().log(Level.WARNING, "The config file is old, and has been renamed to config.yml.old." + ver);

                try (InputStream inputStream = BetterReports.getInstance().getClass().getResourceAsStream("/config.yml")) {
                    Files.copy(inputStream, file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BetterReports.getInstance().setConfig(YamlConfiguration.loadConfiguration(new File(BetterReports.getInstance().getDataFolder(), "config.yml")));
            } else
                BetterReports.getInstance().setConfig(config);
        } catch (InvalidConfigurationException exception) {
            exception.printStackTrace();
            File broken = new File(file.getAbsolutePath() + ".broken." + System.currentTimeMillis());
            file.renameTo(broken);
            BetterReports.getInstance().getLogger().log(Level.WARNING, "The config file is broken, and has been renamed to config.yml.broken." + System.currentTimeMillis());

            try (InputStream in = BetterReports.getInstance().getClass().getResourceAsStream("/config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            BetterReports.getInstance().setConfig(YamlConfiguration.loadConfiguration(file));
        } catch (IOException e) {
            throw e;
        }
    }
}
