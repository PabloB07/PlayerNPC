/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  net.md_5.bungee.api.chat.hover.content.Content
 *  net.md_5.bungee.api.chat.hover.content.Text
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package dev.sergiferry.playernpc.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClickableText {
    private TextComponent component;

    public ClickableText(String text, HoverEvent hoverEvent, ClickEvent clickEvent) {
        this.component = ClickableText.get(text, hoverEvent, clickEvent);
    }

    public ClickableText() {
        this("", (HoverEvent)null, null);
    }

    public ClickableText(String text) {
        this(text, (HoverEvent)null, null);
    }

    public ClickableText(String text, String hover) {
        this(text, ClickableText.hoverText(hover), null);
    }

    public ClickableText(String text, String hover, ClickEvent clickEvent) {
        this(text, ClickableText.hoverText(hover), clickEvent);
    }

    public ClickableText(String text, String hover, ClickEvent.Action action, String actionString) {
        this(text, ClickableText.hoverText(hover), new ClickEvent(action, actionString));
    }

    public ClickableText(String text, ClickEvent.Action action, String actionString) {
        this(text, (HoverEvent)null, new ClickEvent(action, actionString));
    }

    public ClickableText add(String text, HoverEvent hoverEvent, ClickEvent clickEvent) {
        this.component.addExtra((BaseComponent)ClickableText.get(text, hoverEvent, clickEvent));
        return this;
    }

    public ClickableText add(TextComponent textComponent) {
        this.component.addExtra((BaseComponent)textComponent);
        return this;
    }

    public ClickableText add(String text) {
        return this.add(text, (HoverEvent)null, (ClickEvent)null);
    }

    public ClickableText add(String text, ChatColor color) {
        return this.add(ClickableText.get(text, color));
    }

    public ClickableText add(String text, ChatColor color, String hover) {
        TextComponent textComponent = ClickableText.get(text, color);
        HoverEvent hoverEvent = ClickableText.hoverText(hover);
        if (hoverEvent != null) {
            textComponent.setHoverEvent(hoverEvent);
        }
        return this.add(textComponent);
    }

    public ClickableText add(String text, String hover) {
        return this.add(text, ClickableText.hoverText(hover), null);
    }

    public ClickableText add(String text, String hover, ClickEvent clickEvent) {
        return this.add(text, ClickableText.hoverText(hover), clickEvent);
    }

    public ClickableText add(String text, ClickEvent.Action action, String actionString) {
        return this.add(text, (HoverEvent)null, new ClickEvent(action, actionString));
    }

    public ClickableText add(String text, String hover, ClickEvent.Action action, String actionString) {
        return this.add(text, ClickableText.hoverText(hover), new ClickEvent(action, actionString));
    }

    public ClickableText add(String text, String hover, boolean clickAction, ClickEvent.Action action, String actionString) {
        if (!clickAction) {
            return this.add(text, hover);
        }
        return this.add(text, hover, action, actionString);
    }

    public TextComponent getTextComponent() {
        return this.component;
    }

    public ClickableText send(Player player) {
        player.spigot().sendMessage((BaseComponent)this.component);
        return this;
    }

    public ClickableText send(CommandSender sender) {
        sender.spigot().sendMessage((BaseComponent)this.component);
        return this;
    }

    public static TextComponent get(String text, ChatColor color, HoverEvent hoverEvent, ClickEvent clickEvent) {
        TextComponent textComponent = new TextComponent(text);
        if (color != null) {
            textComponent.setColor(color);
        }
        if (clickEvent != null) {
            textComponent.setClickEvent(clickEvent);
        }
        if (hoverEvent != null) {
            textComponent.setHoverEvent(hoverEvent);
        }
        return textComponent;
    }

    public static TextComponent get(String text, ChatColor color) {
        return ClickableText.get(text, color, null, null);
    }

    public static TextComponent get(String text, HoverEvent hoverEvent, ClickEvent clickEvent) {
        return ClickableText.get(text, null, hoverEvent, clickEvent);
    }

    private static HoverEvent hoverText(String hover) {
        if (hover == null || hover.isBlank()) {
            return null;
        }
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Content[]{new Text(hover)});
    }
}
