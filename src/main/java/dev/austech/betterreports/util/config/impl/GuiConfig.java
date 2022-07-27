/*
 * BetterReports - GuiConfig.java
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
import dev.austech.betterreports.util.config.ConfigurationFile;
import lombok.RequiredArgsConstructor;

public class GuiConfig extends ConfigurationFile {
    public GuiConfig() {
        super("gui", true);
    }

    @RequiredArgsConstructor
    public enum Values {
        MENU_MAIN_NAME("menus.main-menu.name"),
        MENU_MAIN_SIZE("menus.main-menu.size"),
        MENU_MAIN_BUTTONS_ALL("menus.main-menu.buttons-both-enabled"),
        MENU_MAIN_BUTTONS_BUG_ONLY("menus.main-menu.buttons-bug-only"),
        MENU_MAIN_BUTTONS_PLAYER_ONLY("menus.main-menu.buttons-player-only"),

        MENU_CONFIRM_NAME("menus.confirm-menu.name"),
        MENU_CONFIRM_SIZE("menus.confirm-menu.size"),
        MENU_CONFIRM_BACK_BUTTON("menus.confirm-menu.back-button-slot"),
        MENU_CONFIRM_BUTTONS("menus.confirm-menu.buttons"),

        MENU_REASON_NAME("menus.reason-menu.name"),
        MENU_REASON_NAME_BACK_BUTTON("menus.reason-menu.back-button-slot"),
        MENU_REASON_CUSTOM_NAME("menus.reason-menu.custom-reason-button.name"),
        MENU_REASON_CUSTOM_LORE("menus.reason-menu.custom-reason-button.lore"),
        MENU_REASON_CUSTOM_MATERIAL("menus.reason-menu.custom-reason-button.material"),
        MENU_REASON_CUSTOM_GLOWING("menus.reason-menu.custom-reason-button.glowing"),

        MENU_SELECT_PLAYER_NAME("menus.select-player-menu.name"),
        MENU_SELECT_PLAYER_BACK_BUTTON("menus.select-player-menu.back-button-slot"),
        MENU_SELECT_PLAYER_CUSTOM_NAME("menus.select-player-menu.custom-player-button.name"),
        MENU_SELECT_PLAYER_CUSTOM_LORE("menus.select-player-menu.custom-player-button.lore"),
        MENU_SELECT_PLAYER_CUSTOM_MATERIAL("menus.select-player-menu.custom-player-button.material"),
        MENU_SELECT_PLAYER_CUSTOM_GLOWING("menus.select-player-menu.custom-player-button.glowing"),
        MENU_SELECT_PLAYER_LIST_BUTTON_NAME("menus.select-player-menu.player-button.name"),
        MENU_SELECT_PLAYER_LIST_BUTTON_LORE("menus.select-player-menu.player-button.lore"),
        MENU_SELECT_PLAYER_LIST_BUTTON_MATERIAL("menus.select-player-menu.player-button.material"),
        MENU_SELECT_PLAYER_LIST_BUTTON_GLOWING("menus.select-player-menu.player-button.glowing"),

        SOUNDS_REPORT_SUCCESS("sounds.report-success"),
        SOUNDS_SELF_REPORT_ERROR("sounds.self-report-error"),
        SOUNDS_PLAYER_REPORTS_NOT_ENABLED("sounds.player-reports-not-enabled"),
        SOUNDS_BUG_REPORTS_NOT_ENABLED("sounds.bug-reports-not-enabled"),
        SOUNDS_NO_PERMISSION("sounds.no-permission");

        private final String key;

        public String getString() {
            return BetterReports.getInstance().getConfigManager().getMainConfig().getConfig().getString(key);
        }

        public boolean getBoolean() {
            return BetterReports.getInstance().getConfigManager().getMainConfig().getConfig().getBoolean(key);
        }

        public int getInteger() {
            return BetterReports.getInstance().getConfigManager().getMainConfig().getConfig().getInt(key);
        }
    }
}
