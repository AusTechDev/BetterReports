/*
 * BetterReports - PlayerReportPagedReasonMenu.java
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

package dev.austech.betterreports.model.report.menu.creation.reason;

import dev.austech.betterreports.BetterReports;
import dev.austech.betterreports.model.report.Report;
import dev.austech.betterreports.model.report.menu.creation.ConfirmReportMenu;
import dev.austech.betterreports.util.Common;
import dev.austech.betterreports.util.ConversationUtil;
import dev.austech.betterreports.util.PlaceholderUtil;
import dev.austech.betterreports.util.StackBuilder;
import dev.austech.betterreports.util.config.impl.GuiConfig;
import dev.austech.betterreports.util.config.impl.MainConfig;
import dev.austech.betterreports.util.menu.defaults.buttons.BackButton;
import dev.austech.betterreports.util.menu.defaults.paged.PagedMenu;
import dev.austech.betterreports.util.menu.layout.MenuButton;
import dev.austech.betterreports.util.xseries.XMaterial;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PlayerReportPagedReasonMenu extends PagedMenu {
    private final Player creator;
    private final Player target;

    @Override
    protected String getMenuTitle(final Player player) {
        return "Select Reason";
    }


    @Override
    public Map<Integer, MenuButton> getPagedButtons(final Player player) {
        final Map<Integer, MenuButton> buttons = new HashMap<>();

        final List<Map<String, Object>> reasons = BetterReports.getInstance().getConfigManager().getReasonsConfig().getConfig().getMapList("reasons").stream().map((m) -> {
            final Map<String, Object> map = new HashMap<>();
            m.forEach((k, v) -> map.put(k.toString().toLowerCase(), v));
            return map;
        }).collect(Collectors.toList());

        final AtomicInteger index = new AtomicInteger();

        reasons.forEach(reason -> {
            final StackBuilder stack = StackBuilder.create(XMaterial.valueOf(reason.get("material").toString()))
                    .name(reason.get("name").toString())
                    .lore(replacePlaceholders(reason.get("name").toString(), reason.get("lore").toString()));

            if (((boolean) reason.get("glowing")))
                stack.glow();

            buttons.put(index.getAndIncrement(), MenuButton.builder()
                    .stack(stack)
                    .action((e, p) -> new ConfirmReportMenu(creator, Report.builder()
                            .type(Report.Type.PLAYER)
                            .creator(creator)
                            .reason(reason.get("reason").toString())
                            .target(target).build())
                            .setReturn(this)
                            .open(creator))
                    .build());
        });

        return buttons;
    }

    private String replacePlaceholders(final String name, final String string) {
        return string
                .replace("{player}", creator.getName())
                .replace("{creator}", creator.getName())
                .replace("{target}", target.getName())
                .replace("{offence}", name);
    }

    @Override
    public Map<Integer, MenuButton> getFixedButtons(final Player player) {
        final Map<Integer, MenuButton> buttons = new HashMap<>();

        final int backButtonSlot = GuiConfig.Values.MENU_REASON_BACK_BUTTON.getInteger();
        if (getToReturn() != null && backButtonSlot >= 0) {
            buttons.put(backButtonSlot, new BackButton(getToReturn()));
        }

        if (BetterReports.getInstance().getConfigManager().getReasonsConfig().getConfig().getBoolean("allow-custom-reason"))
            buttons.put(7, MenuButton.builder()
                    .stack(
                            GuiConfig.Values.MENU_REASON_CUSTOM_BUTTON.getStack()
                    )
                    .action((e, p) -> customReason())
                    .closeMenu(true)
                    .build());

        return buttons;
    }

    private void customReason() {
        ConversationUtil.run(creator, () -> {
            String message = MainConfig.Values.LANG_QUESTION_CUSTOM_REASON_MESSAGE.getString();
            message = message.replace("{player}", creator.getName()).replace("{creator}", creator.getName()).replace("{target}", target.getName());

            if (MainConfig.Values.LANG_QUESTION_CUSTOM_REASON_ENABLED.getBoolean()) {
                String titleMessage = MainConfig.Values.LANG_QUESTION_CUSTOM_REASON_TITLE.getString();
                String subtitleMessage = MainConfig.Values.LANG_QUESTION_CUSTOM_REASON_SUBTITLE.getString();

                titleMessage = titleMessage.replace("{player}", creator.getName()).replace("{creator}", creator.getName()).replace("{target}", target.getName());
                subtitleMessage = subtitleMessage.replace("{player}", creator.getName()).replace("{creator}", creator.getName()).replace("{target}", target.getName());

                if (BetterReports.getInstance().isUsePlaceholderApi()) {
                    titleMessage = PlaceholderUtil.handleDualPlaceholders(titleMessage, "creator", creator, "target", target);
                    subtitleMessage = PlaceholderUtil.handleDualPlaceholders(subtitleMessage, "creator", creator, "target", target);
                }

                Common.sendTitle(creator, titleMessage, subtitleMessage, 10, 20 * 15, 10);
            }

            if (BetterReports.getInstance().isUsePlaceholderApi()) {
                message = PlaceholderUtil.handleDualPlaceholders(message, "creator", creator, "target", target);
            }

            return Common.color(message);
        }, (s) -> {
            Common.resetTitle(creator);
            new ConfirmReportMenu(creator, Report.builder().type(Report.Type.PLAYER).creator(creator).reason(s).target(target).build()).setReturn(this).open(creator);
            return Prompt.END_OF_CONVERSATION;
        });
    }
}
