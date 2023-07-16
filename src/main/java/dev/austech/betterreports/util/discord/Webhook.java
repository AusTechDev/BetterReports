/*
 * BetterReports - NewWebhook.java
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

package dev.austech.betterreports.util.discord;

import com.google.gson.annotations.SerializedName;
import dev.austech.betterreports.util.Common;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class Webhook {
    private String content;

    private String username;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("tts")
    private boolean textToSpeech;

    private List<EmbedObject> embeds;

    public void send(String uri) throws IOException {
        if (content != null && embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }

        if (content != null && content.length() > 2000) {
            throw new IllegalArgumentException("Content cannot be longer than 2000 characters");
        }


        String json = Common.GSON.toJson(this);

        final URL url = new URL(uri);
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "AusTechDev_BetterReports");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        final OutputStream stream = connection.getOutputStream();
        stream.write(json.getBytes(StandardCharsets.UTF_8));
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }

    // Extension of Lombok's @Builder
    public static class WebhookBuilder {
        public WebhookBuilder addEmbed(final EmbedObject embed) {
            if (this.embeds == null) this.embeds = new ArrayList<>();
            if (this.embeds.size() >= 10)
                throw new IllegalStateException("Cannot add more than 10 embeds to a webhook");
            this.embeds.add(embed);
            return this;
        }
    }

    @Data
    @Builder(toBuilder = true, builderClassName = "Builder")
    public static class EmbedObject {
        @Nullable
        private String title;

        @Nullable
        private String description;

        @Nullable
        private String url;

        @Nullable
        private String timestamp;

        @Nullable
        private Integer color;

        @Nullable
        private Footer footer;

        @Nullable
        private Media image;

        @Nullable
        private Media thumbnail;

        @Nullable
        private Media video;

        @Nullable
        private Provider provider;

        @Nullable
        private Author author;

        @Nullable
        private List<Field> fields;

        public EmbedObject setFooter(final String text, final String icon) {
            this.footer = new Footer(text, icon);
            return this;
        }

        public EmbedObject setAuthor(final String name, final String url, final String icon) {
            this.author = new Author(name, url, icon);
            return this;
        }

        public static class Builder {
            public Builder addField(final String name, final String value, final boolean inline) {
                if (fields == null) fields = new ArrayList<>();
                this.fields.add(new Field(name, value, inline));
                return this;
            }
        }

        @Data
        @AllArgsConstructor
        @lombok.Builder(builderClassName = "Builder")
        public static class Media {
            private String url;

            @SerializedName("proxy_url")
            private String proxyUrl;

            private int height;
            private int width;
        }

        @Data
        @AllArgsConstructor
        @lombok.Builder(builderClassName = "Builder")
        public static class Provider {
            private String name;
            private String url;
        }

        @Data
        @AllArgsConstructor
        @lombok.Builder(builderClassName = "Builder")
        public static class Footer {
            private String text;

            @SerializedName("icon_url")
            private String iconUrl;
        }

        @Data
        @lombok.Builder(builderClassName = "Builder")
        public static class Author {
            private String name;
            private String url;

            @SerializedName("icon_url")
            private String iconUrl;
        }

        @Data
        @AllArgsConstructor
        public static class Field {
            private String name;
            private String value;
            private boolean inline;
        }
    }
}
