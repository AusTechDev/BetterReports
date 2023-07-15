/*
 * BetterReports - TitleUtil.java
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

package dev.austech.betterreports.util;

import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class TitleUtil {
    @Builder
    @Data
    public static class Title {
        private String title;
        private String subtitle;

        private int fadeIn;
        private int fadeOut;
        private int stay;

        public static class TitleBuilder {
            public TitleBuilder title(final String string) {
                if (string == null || string.isEmpty()) {
                    return this;
                }

                final JsonObject object = new JsonObject();
                object.addProperty("text", string);

                this.title = object.toString();

                return this;
            }

            public TitleBuilder subtitle(final String string) {
                if (string == null || string.isEmpty()) {
                    return this;
                }

                final JsonObject object = new JsonObject();
                object.addProperty("text", string);

                this.subtitle = object.toString();

                return this;
            }
        }
    }

    public void sendLegacyTitle(final Player player, final Title title) {
        try {
            final Object bukkitPlayer = player.getClass().getMethod("getHandle").invoke(player);
            final Object connection = bukkitPlayer.getClass().getField("playerConnection").get(bukkitPlayer);

            final Class<?> packetClass = ReflectionUtil.getNMSClass("Packet");
            final Class<?> packetPlayOutTitle = ReflectionUtil.getNMSClass("PacketPlayOutTitle");

            final Class<?> enumTitleAction = ReflectionUtil.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
            final Class<?> chatBaseComponent = ReflectionUtil.getNMSClass("IChatBaseComponent");
            final Class<?> chatSerializer = ReflectionUtil.getNMSClass("IChatBaseComponent$ChatSerializer");

            final Object timingsPacket = packetPlayOutTitle.getConstructor(int.class, int.class, int.class).newInstance(title.getFadeIn(), title.getStay(), title.getFadeOut());
            connection.getClass().getMethod("sendPacket", packetClass).invoke(connection, timingsPacket);

            if (title.getTitle() != null) {
                final Object component = chatSerializer.getMethod("a", String.class).invoke(null, title.getTitle());
                final Object packet = packetPlayOutTitle.getConstructor(enumTitleAction, chatBaseComponent).newInstance(enumTitleAction.getField("TITLE").get(null), component);
                connection.getClass().getMethod("sendPacket", packetClass).invoke(connection, packet);
            }

            if (title.getSubtitle() != null) {
                final Object component = chatSerializer.getMethod("a", String.class).invoke(null, title.getSubtitle());
                final Object packet = packetPlayOutTitle.getConstructor(enumTitleAction, chatBaseComponent).newInstance(enumTitleAction.getField("SUBTITLE").get(null), component);
                connection.getClass().getMethod("sendPacket", packetClass).invoke(connection, packet);
            }
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
}
