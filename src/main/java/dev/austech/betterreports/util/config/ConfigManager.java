/*
 * BetterReports - ConfigManager.java
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

package dev.austech.betterreports.util.config;

import dev.austech.betterreports.util.config.impl.GuiConfig;
import dev.austech.betterreports.util.config.impl.MainConfig;
import dev.austech.betterreports.util.config.impl.ReasonsConfig;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ConfigManager {
    private final MainConfig mainConfig;
    private final ReasonsConfig reasonsConfig;
    private final GuiConfig guiConfig;

    @Getter(AccessLevel.PRIVATE)
    private final List<ConfigurationFile> configs = new ArrayList<>();

    public ConfigManager() {
        mainConfig = new MainConfig();
        reasonsConfig = new ReasonsConfig();
        guiConfig = new GuiConfig();

        configs.add(mainConfig);
        configs.add(reasonsConfig);
        configs.add(guiConfig);
    }

    public void reload() {
        configs.forEach(ConfigurationFile::reload);
    }
}
