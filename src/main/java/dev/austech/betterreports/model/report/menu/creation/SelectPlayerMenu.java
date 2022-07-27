/*
 * BetterReports - SelectPlayerMenu.java
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

package dev.austech.betterreports.model.report.menu.creation;

import dev.austech.betterreports.model.report.menu.creation.reason.PlayerReportPagedReasonMenu;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.ConversationUtil;
import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.TriConsumer;
import dev.austech.betterreports.util.config.impl.MainConfig;
import dev.austech.betterreports.util.menu.defaults.ListPlayersMenu;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import dev.austech.betterreports.util.xseries.XMaterial;
import dev.austech.betterreports.util.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public class SelectPlayerMenu extends ListPlayersMenu {
    @Override
    protected TriConsumer<InventoryClickEvent, Player, Player> getAction() {
        return (event, player, target) -> handle(player, target);
    }

    @Override
    public Map<Integer, MenuButton> getFixedButtons(final Player player) {
        final Map<Integer, MenuButton> buttons = new HashMap<>();

        buttons.put(7, MenuButton.builder()
                .stack(
                        StackBuilder.create(XMaterial.NAME_TAG)
                                .name("&e&lEnter Name")
                )
                .action((e, p) -> searchPlayer(p))
                .closeMenu(true)
                .build());

        return buttons;
    }

    private void handle(final Player player, final Player target) {
        if (player == target) {
            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1, 1);
            MainConfig.Values.LANG_PLAYER_SELF.sendRaw(player);
            return;
        }

        new PlayerReportPagedReasonMenu(player, target).open(player);
    }

    private void searchPlayer(final Player player) {
        ConversationUtil.run(player, () -> {
            Common.sendTitle(player, MainConfig.Values.LANG_QUESTION_PLAYER_SEARCH_TITLE.getPlaceholderString(player), MainConfig.Values.LANG_QUESTION_PLAYER_SEARCH_SUBTITLE.getPlaceholderString(player), 10, 20 * 15, 10);
            return Common.color(MainConfig.Values.LANG_QUESTION_PLAYER_SEARCH_MESSAGE.getPlaceholderString(player));
        }, (s) -> {
            final Player found = Bukkit.getPlayer(s);
            if (found == null) {
                MainConfig.Values.LANG_PLAYER_NOT_FOUND.sendRaw(player);
                return new ConversationUtil.RerunPrompt();
            }

            Common.resetTitle(player);

            handle(player, found);

            return Prompt.END_OF_CONVERSATION;
        });
    }
}
