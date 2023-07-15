/*
 * BetterReports - Report.java
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

package dev.austech.betterreports.model.report;

import dev.austech.betterreports.util.config.impl.GuiConfig;
import dev.austech.betterreports.util.config.impl.MainConfig;
import dev.austech.betterreports.util.discord.DiscordManager;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Getter
@Builder
public class Report {
    private final Type type;

    private final Player creator;
    private final String reason;

    private OfflinePlayer target; // null if not applicable

    private final long timestamp = System.currentTimeMillis();

    public boolean isPlayer() {
        return type == Type.PLAYER;
    }

    public void save() {
        if (creator == getTarget()) {
            GuiConfig.Values.SOUNDS_SELF_REPORT.playErrorSound(creator);
            MainConfig.Values.LANG_PLAYER_SELF.send(creator);
            return;
        }

        if (!creator.hasPermission("betterreports.use." + getType().toString().toLowerCase())) {
            GuiConfig.Values.SOUNDS_NO_PERMISSION.playErrorSound(creator);
            MainConfig.Values.LANG_NO_PERMISSION.send(creator);
            return;
        }

        if (getType() == Report.Type.BUG && !ReportManager.getInstance().isBugReportsEnabled()) {
            GuiConfig.Values.SOUNDS_BUG_REPORTS_DISABLED.playErrorSound(creator);
            MainConfig.Values.LANG_BUG_REPORTS_DISABLED.send(creator);
            return;
        } else if (getType() == Report.Type.PLAYER && !ReportManager.getInstance().isPlayerReportsEnabled()) {
            GuiConfig.Values.SOUNDS_PLAYER_REPORTS_DISABLED.playErrorSound(creator);
            MainConfig.Values.LANG_PLAYER_REPORTS_DISABLED.send(creator);
            return;
        }

        ReportManager.getInstance().getReports().add(this);
        ReportManager.getInstance().addCooldown(creator, type);
        DiscordManager.getInstance().sendReport(creator, this);
    }

    public static enum Type {
        BUG, PLAYER
    }
}
