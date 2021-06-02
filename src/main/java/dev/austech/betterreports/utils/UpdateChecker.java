package dev.austech.betterreports.utils;

/*
 * UpdateChecker class to check for newer versions of the plugin on the spigot website
 * Source: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates/
 */

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

@UtilityClass
public class UpdateChecker {

    public boolean needsUpdate(String currentVersion) {
        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=83689").openStream()) {
            Scanner scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                return !currentVersion.equals(scanner.next());
            }
        } catch (IOException exception) {
            Common.log("&cFailed to check for updates. Contact support or manually download here: https://austech.dev/to/betterreports");
        }
        return false;
    }
}