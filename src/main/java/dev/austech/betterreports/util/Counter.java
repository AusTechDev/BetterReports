/*
 * BetterReports - Counter.java
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

import dev.austech.betterreports.BetterReports;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Counter {
    final File file = new File(BetterReports.getInstance().getDataFolder(), "counter.db");

    @Getter
    private int globalCounter = 1;

    @Getter
    private int bugCounter = 1;

    @Getter
    private int playerCounter = 1;

    public void incrementBug() {
        globalCounter++;
        bugCounter++;
        save();
    }

    public void incrementPlayer() {
        globalCounter++;
        bugCounter++;
        save();
    }

    private void save() {
        Bukkit.getScheduler().runTaskAsynchronously(BetterReports.getInstance(), () -> {
            try {
                final String str = "G: " + globalCounter + "\nB: " + bugCounter + "\nP: " + playerCounter;
                Files.write(file.toPath(), str.getBytes(StandardCharsets.UTF_8));
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void load() {
        try {
            if (!file.exists()) {
                save();
                return;
            }

            final Stream<String> lines = Files.lines(file.toPath());
            final List<String> data = lines.map(str -> str.substring(3)).collect(Collectors.toList());
            lines.close();

            globalCounter = Integer.parseInt(data.get(0));
            bugCounter = Integer.parseInt(data.get(1));
            playerCounter = Integer.parseInt(data.get(2));

        } catch (final Exception exception) {
            globalCounter = -1;
            bugCounter = -1;
            playerCounter = -1;

            exception.printStackTrace();
        }
    }
}
