/*
 * BetterReports - ConversationUtil.java
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
import dev.austech.betterreports.util.config.impl.MainConfig;
import lombok.experimental.UtilityClass;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class ConversationUtil {
    private final ConversationFactory conversationFactory = new ConversationFactory(BetterReports.getInstance())
            .withLocalEcho(false);

    private final String[] escapeSequences = {"quit", "exit", "escape", "cancel"};

    private StringPrompt create(final Supplier<String> message, final Function<String, Prompt> function) {
        return new StringPrompt() {
            @Override
            public String getPromptText(final ConversationContext context) {
                return message.get();
            }

            @Override
            public Prompt acceptInput(final ConversationContext context, final String input) {
                if (Arrays.stream(escapeSequences).anyMatch(input::equalsIgnoreCase)) {
                    Common.resetTitle(((Player) context.getForWhom()));
                    context.getForWhom().sendRawMessage(Common.color(MainConfig.Values.LANG_CONVERSATION_CANCELLED.getString()));
                    return Prompt.END_OF_CONVERSATION;
                }

                final Prompt prompt = function.apply(input);

                if (prompt instanceof RerunPrompt) return this;
                else return prompt;
            }
        };
    }

    private StringPrompt create(final String message, final Function<String, Prompt> function) {
        return create(() -> message, function);
    }

    public void run(final Player player, final Supplier<String> message, final Function<String, Prompt> function) {
        conversationFactory.withFirstPrompt(create(message, function)).buildConversation(player).begin();
    }

    public static class RerunPrompt extends StringPrompt {
        public RerunPrompt() {
        }

        @Override
        public String getPromptText(final ConversationContext context) {
            return null;
        }

        @Override
        public Prompt acceptInput(final ConversationContext context, final String input) {
            return null;
        }
    }
}
