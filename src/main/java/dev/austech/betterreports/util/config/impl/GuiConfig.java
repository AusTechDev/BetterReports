/*
 * BetterReports - GuiConfig.java
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

package dev.austech.betterreports.util.config.impl;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.config.ConfigurationFile;
import dev.austech.betterreports.util.xseries.XMaterial;
import dev.austech.betterreports.util.xseries.XSound;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public class GuiConfig extends ConfigurationFile {
    public GuiConfig() {
        super("gui.yml", true);
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    @Getter
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
        MENU_REASON_BACK_BUTTON("menus.reason-menu.back-button-slot"),
        MENU_REASON_CUSTOM_BUTTON("menus.reason-menu.custom-reason-button"),

        MENU_SELECT_PLAYER_NAME("menus.select-player-menu.name"),
        MENU_SELECT_PLAYER_BACK_BUTTON("menus.select-player-menu.back-button-slot"),
        MENU_SELECT_PLAYER_CUSTOM_BUTTON("menus.select-player-menu.custom-player-button"),
        MENU_SELECT_PLAYER_LIST_BUTTON_NAME("menus.select-player-menu.player-button.name"),
        MENU_SELECT_PLAYER_LIST_BUTTON_HIDE_VANISHED("menus.select-player-menu.player-button.hide-vanished"),

        SOUNDS_REPORT_SUCCESS("sounds.report-success"),
        SOUNDS_SELF_REPORT("sounds.self-report-error"),
        SOUNDS_INVALID_PLAYER("sounds.invalid-player-error"),
        SOUNDS_PLAYER_REPORTS_DISABLED("sounds.player-reports-not-enabled"),
        SOUNDS_BUG_REPORTS_DISABLED("sounds.bug-reports-not-enabled"),
        SOUNDS_NO_PERMISSION("sounds.no-permission"),
        SOUNDS_GENERIC_ERROR("sounds.generic-error", "ENTITY_VILLAGER_NO"),

        PAGINATED_MENU_BACK_BUTTON_NAME("paginated-menus.back-button.name"),
        PAGINATED_MENU_BACK_BUTTON_LORE("paginated-menus.back-button.lore"),
        PAGINATED_MENU_ERROR_BUTTON_NAME("paginated-menus.error-button.name"),
        PAGINATED_MENU_ERROR_BUTTON_LORE("paginated-menus.error-button.lore"),
        PAGINATED_MENU_PAGE_BUTTON_NEXT("paginated-menus.page-button.next"),
        PAGINATED_MENU_PAGE_BUTTON_PREVIOUS("paginated-menus.page-button.previous"),
        PAGINATED_MENU_PAGE_BUTTON_FIRST("paginated-menus.page-button.first"),
        PAGINATED_MENU_PAGE_BUTTON_LAST("paginated-menus.page-button.last"),
        PAGINATED_MENU_PAGE_LIST_TITLE("paginated-menus.page-list-menu.title"),
        PAGINATED_MENU_PAGE_LIST_CHANGE_BUTTON_NAME("paginated-menus.page-list-menu.change-page-button.name"),
        PAGINATED_MENU_PAGE_LIST_CHANGE_BUTTON_LORE("paginated-menus.page-list-menu.change-page-button.lore"),
        PAGINATED_MENU_PAGE_LIST_CHANGE_CURRENT_BUTTON_NAME("paginated-menus.page-list-menu.current-page-button.name"),
        PAGINATED_MENU_PAGE_LIST_CHANGE_CURRENT_BUTTON_LORE("paginated-menus.page-list-menu.current-page-button.lore"),
        PAGINATED_MENU_PAGE_LIST_CHANGE_CURRENT_BUTTON_GLOWING("paginated-menus.page-list-menu.current-page-button.glowing"),
        PAGINATED_MENU_PAGE_NUMBER_BUTTON_NAME("paginated-menus.page-number-button.name");

        private final String key;
        private Object defaultStringValue;

        private YamlConfiguration getConfig() {
            return BetterReports.getInstance().getConfigManager().getGuiConfig().getConfig();
        }

        public String getString() {
            String foundValue = getConfig().getString(key);
            if (foundValue == null && defaultStringValue instanceof String) return (String) defaultStringValue;
            else return foundValue;
        }

        public boolean getBoolean() {
            return getConfig().getBoolean(key);
        }

        public int getInteger() {
            return getConfig().getInt(key);
        }

        public void playSound(final Player player) {
            final XSound.Record record = XSound.parse(getString());
            if (record == null) return;
            record.forPlayer(player).play();
        }

        public void playErrorSound(final Player player) {
            XSound.Record record = XSound.parse(getString());
            if (record == null && !Objects.equals(key, "sounds.generic-error")) {
                record = XSound.parse(Values.SOUNDS_GENERIC_ERROR.getString());
            }
            record.forPlayer(player).play();
        }

        private StackBuilder getStack(final String key) {
            final ConfigurationSection section = getConfig().getConfigurationSection(key);
            if (section == null) return null;

            final StackBuilder builder = StackBuilder.create(XMaterial.valueOf(section.getString("material")))
                    .name(section.getString("name"))
                    .lore(section.getString("lore"));

            if (section.getBoolean("glowing"))
                builder.glow();

            if (section.contains("type"))
                builder.type(section.getString("type"));

            return builder;
        }

        public StackBuilder getStack() {
            return getStack(key);
        }

        public HashMap<Integer, StackBuilder> getStackMap() {
            final ConfigurationSection section = getConfig().getConfigurationSection(key);
            if (section == null) return null;

            final HashMap<Integer, StackBuilder> map = new HashMap<>();
            for (final String item : section.getKeys(false)) {
                final StackBuilder builder = getStack(key + "." + item);
                if (builder == null) continue;
                map.put(Integer.parseInt(item), builder);
            }
            return map;
        }

    }
}
