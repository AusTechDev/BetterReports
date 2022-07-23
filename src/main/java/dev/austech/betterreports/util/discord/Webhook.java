package dev.austech.betterreports.util.discord;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

/*
 * Java DiscordWebhook class to easily execute Discord Webhooks
 * Created by: k3kdude
 * Source: https://gist.github.com/k3kdude/fba6f6b37594eae3d6f9475330733bdb
 *
 * Modified by mnewt00 & Timmy109.
 *
 */

@Data
public class Webhook {
    private final String url;
    private String content;
    private String username;
    private String avatarUrl;
    private boolean tts;
    private List<EmbedObject> embeds = new ArrayList<>();

    public void addEmbed(final EmbedObject embed) {
        this.embeds.add(embed);
    }

    public void execute() throws IOException {
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }

        final JSONObject json = new JSONObject();

        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);

        if (!this.embeds.isEmpty()) {
            final List<JSONObject> embedObjects = new ArrayList<>();

            for (final EmbedObject embed : this.embeds) {
                final JSONObject jsonEmbed = new JSONObject();

                jsonEmbed.put("title", embed.getTitle());
                jsonEmbed.put("description", embed.getDescription());
                jsonEmbed.put("url", embed.getUrl());

                if (embed.getColor() != null) {
                    final Color color = embed.getColor();
                    int rgb = color.getRed();
                    rgb = (rgb << 8) + color.getGreen();
                    rgb = (rgb << 8) + color.getBlue();

                    jsonEmbed.put("color", rgb);
                }

                final EmbedObject.Footer footer = embed.getFooter();
                final String image = embed.getImage();
                final String thumbnail = embed.getThumbnail();
                final EmbedObject.Author author = embed.getAuthor();
                final List<EmbedObject.Field> fields = embed.getFields();

                if (footer != null) {
                    final JSONObject jsonFooter = new JSONObject();

                    jsonFooter.put("text", footer.getText());
                    jsonFooter.put("icon_url", footer.getIconUrl());
                    jsonEmbed.put("footer", jsonFooter);
                }

                if (image != null) {
                    final JSONObject jsonImage = new JSONObject();

                    jsonImage.put("url", image);
                    jsonEmbed.put("image", jsonImage);
                }

                if (thumbnail != null) {
                    final JSONObject jsonThumbnail = new JSONObject();

                    jsonThumbnail.put("url", thumbnail);
                    jsonEmbed.put("thumbnail", jsonThumbnail);
                }

                if (author != null) {
                    final JSONObject jsonAuthor = new JSONObject();

                    jsonAuthor.put("name", author.getName());
                    jsonAuthor.put("url", author.getUrl());
                    jsonAuthor.put("icon_url", author.getIconUrl());
                    jsonEmbed.put("author", jsonAuthor);
                }

                final List<JSONObject> jsonFields = new ArrayList<>();
                for (final EmbedObject.Field field : fields) {
                    final JSONObject jsonField = new JSONObject();

                    jsonField.put("name", field.getName());
                    jsonField.put("value", field.getValue());
                    jsonField.put("inline", field.isInline());

                    jsonFields.add(jsonField);
                }

                jsonEmbed.put("fields", jsonFields.toArray());
                embedObjects.add(jsonEmbed);
            }

            json.put("embeds", embedObjects.toArray());
        }

        final URL url = new URL(this.url);
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "AusTechDev_BetterReports");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        final OutputStream stream = connection.getOutputStream();
        stream.write(json.toString().getBytes(StandardCharsets.UTF_8));
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }

    @Data
    public static class EmbedObject {
        private String title;
        private String description;
        private String url;
        private Color color;

        private Footer footer;
        private String thumbnail;
        private String image;
        private Author author;
        private List<Field> fields = new ArrayList<>();

        public EmbedObject setFooter(final String text, final String icon) {
            this.footer = new Footer(text, icon);
            return this;
        }

        public EmbedObject setAuthor(final String name, final String url, final String icon) {
            this.author = new Author(name, url, icon);
            return this;
        }

        public EmbedObject addField(final String name, final String value, final boolean inline) {
            this.fields.add(new Field(name, value, inline));
            return this;
        }

        @Data
        @AllArgsConstructor
        private static class Footer {
            private String text;
            private String iconUrl;
        }

        @Data
        @AllArgsConstructor
        private static class Author {
            private String name;
            private String url;
            private String iconUrl;
        }

        @Data
        @AllArgsConstructor
        private static class Field {
            private String name;
            private String value;
            private boolean inline;
        }
    }

    private static class JSONObject {

        private final HashMap<String, Object> map = new HashMap<>();

        void put(final String key, final Object value) {
            if (value != null) {
                map.put(key, value);
            }
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            final Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            builder.append("{");

            int i = 0;
            for (final Map.Entry<String, Object> entry : entrySet) {
                final Object val = entry.getValue();
                builder.append(quote(entry.getKey())).append(":");

                if (val instanceof String) {
                    builder.append(quote(String.valueOf(val)));
                } else if (val instanceof Integer) {
                    builder.append(Integer.valueOf(String.valueOf(val)));
                } else if (val instanceof Boolean) {
                    builder.append(val);
                } else if (val instanceof JSONObject) {
                    builder.append(val);
                } else if (val.getClass().isArray()) {
                    builder.append("[");
                    final int len = Array.getLength(val);
                    for (int j = 0; j < len; j++) {
                        builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                    }
                    builder.append("]");
                }

                builder.append(++i == entrySet.size() ? "}" : ",");
            }

            return builder.toString();
        }

        private String quote(final String string) {
            return "\"" + string + "\"";
        }
    }
}
