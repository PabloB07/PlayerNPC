/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  net.md_5.bungee.api.chat.hover.content.Content
 *  net.md_5.bungee.api.chat.hover.content.Text
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.plugin.Plugin
 */
package dev.sergiferry.playernpc.command;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.sergiferry.playernpc.PlayerNPCPlugin;
import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import dev.sergiferry.playernpc.utils.ClickableText;
import dev.sergiferry.playernpc.utils.MathUtils;
import dev.sergiferry.playernpc.utils.TimerUtils;
import dev.sergiferry.spigot.SpigotPlugin;
import dev.sergiferry.spigot.commands.CommandInstance;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

public class NPCLibCommand
extends CommandInstance
implements TabCompleter {
    protected static final String COMMAND_LABEL = "npclib";
    private static final List<Command> commands = new ArrayList<Command>();
    protected static final Command INFO = new Command((Plugin)PlayerNPCPlugin.getInstance(), "info", "", false, true, "Get info of NPCLib", "\u00a77This will give you all the information about the NPCLib.", (command, data) -> {
        Player player = data.getPlayerSender();
        if (player == null) {
            NPCLibCommand.error(data.commandSender);
        }
        NPCLib npcLib = data.getNpcLib();
        Integer page = 1;
        Integer maxPage = 1;
        if (data.getCommandArgs().length >= 1 && MathUtils.isInteger(data.getCommandArgs()[0])) {
            page = Integer.valueOf(data.getCommandArgs()[0]);
        }
        for (int i = 0; i < 100; ++i) {
            player.sendMessage("");
        }
        player.sendMessage(new String[]{" \u00a7c\u00a7lInformation about NPCLib \u00a77(Page " + page + "/" + maxPage + ")", ""});
        if (page == 1) {
            new ClickableText("\u00a78\u2022 \u00a76\u00a7lID: ").add("\u00a77").add(" ").add("\u00a7e\u00a7l\u24d8", "\u00a7e\u00a7l\u24d8 Information about \n\u00a77").send(player);
        }
        player.sendMessage("");
        boolean next = page < maxPage;
        boolean previous = page > 1;
        ClickableText clickableText = new ClickableText("  ");
        if (previous) {
            clickableText.add("\u00a7c\u00a7l[PREVIOUS]", "\u00a7eClick to go to the previous page.", ClickEvent.Action.RUN_COMMAND, "/npclib info " + (page - 1));
        } else {
            clickableText.add("\u00a78\u00a7l[PREVIOUS]");
        }
        clickableText.add("    ");
        if (next) {
            clickableText.add("\u00a7a\u00a7l[NEXT]", "\u00a7eClick to go to the next page.", ClickEvent.Action.RUN_COMMAND, "/npclib info " + (page + 1));
        } else {
            clickableText.add("\u00a78\u00a7l[NEXT]");
        }
        clickableText.add("    ");
        clickableText.add("\u00a7c\u00a7l[CLOSE INFO]", "\u00a7eClick to close information.", ClickEvent.Action.RUN_COMMAND, "/npclib help");
        clickableText.send(player);
        player.sendMessage("");
    }, (command, data) -> null);
    protected static final Command RELOADCONFIG = new Command((Plugin)PlayerNPCPlugin.getInstance(), "reloadconfig", "", true, true, "Reload config", "\u00a77This will reload the configuration file.", (command, data) -> {
        data.getNpcLib().loadConfig();
        data.getCommandSender().sendMessage(NPCLibCommand.getPrefix() + "\u00a77Configuration has been reloaded.");
    }, (command, data) -> null);
    protected static final Command GETSKININFO = new Command((Plugin)PlayerNPCPlugin.getInstance(), "getskininfo", "(name)", true, false, "Get skin information", "\u00a77With this command you can request the Mojang API to return the information of a player's skin.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(name) \u00a77Name of the player.", (command, data) -> {
        boolean delete;
        if (data.getPlayerSender() == null) {
            NPCLibCommand.error(data.getCommandSender(), "This command cannot be executed from console.");
            return;
        }
        if (data.getCommandArgs().length < 1) {
            NPCLibCommand.notEnoughArguments(command, data);
            return;
        }
        boolean sendMessage = true;
        boolean clearChat = false;
        boolean forceDownload = data.getCommandArgs().length > 1 && data.getCommandArgs()[1].equals("-forceDownload");
        boolean giveHead = data.getCommandArgs().length > 1 && data.getCommandArgs()[1].equals("-giveHead");
        boolean bl = delete = data.getCommandArgs().length > 1 && data.getCommandArgs()[1].equals("-delete");
        if (giveHead || delete) {
            sendMessage = false;
        }
        if (forceDownload) {
            clearChat = true;
        }
        if (clearChat) {
            for (int i = 0; i < 100; ++i) {
                data.getPlayerSender().sendMessage("");
            }
        }
        if (sendMessage) {
            data.getCommandSender().sendMessage(NPCLibCommand.getPrefix() + "\u00a77Trying to fetch skin of " + data.getCommandArgs()[0]);
        }
        NPC.Skin.fetchSkinAsync(command.getPlugin(), data.getCommandArgs()[0], forceDownload, skin -> {
            int i;
            int lastUpdateSeconds;
            ChatColor mostCommonColor;
            if (skin == null) {
                NPCLibCommand.error(data.getCommandSender(), "There was an error trying to fetch that skin.");
                return;
            }
            if (giveHead) {
                ItemStack is = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta im = (SkullMeta)is.getItemMeta();
                im.setDisplayName((skin.getMostCommonColor() != null ? skin.getMostCommonColor() : ChatColor.YELLOW) + "\u00a7l" + skin.getPlayerName());
                GameProfile profile = new GameProfile(UUID.randomUUID(), skin.getPlayerName());
                profile.properties().put("textures", new Property("textures", skin.getTexture()));
                Field profileField = null;
                try {
                    profileField = im.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(im, profile);
                }
                catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e1) {
                    e1.printStackTrace();
                }
                is.setItemMeta((ItemMeta)im);
                data.getPlayerSender().getInventory().addItem(new ItemStack[]{is});
            }
            if (delete) {
                if (!skin.canBeDeleted()) {
                    NPCLibCommand.error(data.getCommandSender(), "This skin cannot be deleted");
                    return;
                }
                skin.delete();
                for (int i2 = 0; i2 < 100; ++i2) {
                    data.getPlayerSender().sendMessage("");
                }
                NPCLibCommand.sendMessage(data.getCommandSender(), "\u00a77Skin \u00a7c" + skin.getPlayerName() + " \u00a77has been removed successfully.");
                return;
            }
            ArrayList<ClickableText> lines = new ArrayList<ClickableText>();
            try {
                ChatColor[][] avatarData = skin.getAvatar();
                for (int y = 0; y < 8; ++y) {
                    lines.add(new ClickableText(" "));
                    for (int x = 0; x < 8; ++x) {
                        ChatColor color = avatarData[x][y];
                        ((ClickableText)lines.get(y)).add("\u2588", color, data.getNpcLib().isDebug() ? "\u00a7e\u00a7lPixel (" + x + "," + y + ")\n\u00a7c\u00a7lR: \u00a77" + color.getColor().getRed() + "\n\u00a7a\u00a7lG: \u00a77" + color.getColor().getGreen() + "\n\u00a79\u00a7lB: \u00a77" + color.getColor().getBlue() : null);
                    }
                }
            }
            catch (Exception avatarData) {
                // empty catch block
            }
            if ((mostCommonColor = skin.getMostCommonColor()) == null) {
                mostCommonColor = ChatColor.YELLOW;
            }
            String lastUpdateString = (lastUpdateSeconds = TimerUtils.getBetweenDatesString(skin.getLastUpdate(), TimerUtils.getCurrentDate(), TimerUtils.DATE_FORMAT_LARGE, TimeUnit.SECONDS).intValue()) > 5 ? TimerUtils.getCRCounterSimple(lastUpdateSeconds, true) : "moments";
            Player player = Bukkit.getServer().getPlayerExact(skin.getPlayerName());
            NPC.Skin.ObtainedFrom obtainedFrom = Bukkit.getServer().getOnlineMode() && player != null ? NPC.Skin.ObtainedFrom.GAME_PROFILE : NPC.Skin.ObtainedFrom.MOJANG_API;
            TextComponent giveHeadButton = ClickableText.get((giveHead ? "\u00a78" : "\u00a76") + "\u00a7l[\u2620]", new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Content[]{new Text(giveHead ? null : "\u00a76\u00a7l" + skin.getPlayerName() + "'s head\n\u00a77This will give you the player's head with it's texture.\n\n\u00a7eClick to receive player's head.")}), giveHead ? null : new ClickEvent(ClickEvent.Action.RUN_COMMAND, command.getCommand(skin.getPlayerName() + " -giveHead")));
            boolean updateButtonActivated = lastUpdateSeconds > 5 && skin.canBeUpdated();
            for (i = 0; i <= 7; ++i) {
                ((ClickableText)lines.get(i)).add("  ");
            }
            ((ClickableText)lines.get(0)).add("\u00a7l\u00a7nSkin information", mostCommonColor, data.getNpcLib().isDebug() ? "\u00a7e\u00a7lMost common color\n\u00a7c\u00a7lR: \u00a77" + mostCommonColor.getColor().getRed() + "\n\u00a7a\u00a7lG: \u00a77" + mostCommonColor.getColor().getGreen() + "\n\u00a79\u00a7lB: \u00a77" + mostCommonColor.getColor().getBlue() : null);
            ((ClickableText)lines.get(2)).add("\u00a77Player name: ").add("\u00a7e\u00a7l" + skin.getPlayerName(), "\u00a7e\u00a7lPlayer information\n\u00a7eName: \u00a77" + skin.getPlayerName() + "\n\u00a7eUUID: \u00a77" + skin.getPlayerUUID() + "\n\u00a7eObtained from: " + skin.getObtainedFrom().getTitle() + "\n\n\u00a77\u00a7oLast updated " + lastUpdateString + " ago.\n\n\u00a7eClick to copy UUID to clipboard", ClickEvent.Action.COPY_TO_CLIPBOARD, skin.getPlayerUUID());
            ((ClickableText)lines.get(3)).add("\u00a77Texture value: ").add("\u00a7a\u00a7l[COPY TO CLIPBOARD]", "\u00a7a\u00a7lTexture value\n\u00a77This texture value is " + skin.getTexture().length() + " characters length.\n\n\u00a7eClick to copy texture value to clipboard", ClickEvent.Action.COPY_TO_CLIPBOARD, skin.getTexture()).add(" ").add(giveHeadButton);
            ((ClickableText)lines.get(4)).add("\u00a77Texture signature: ").add("\u00a7a\u00a7l[COPY TO CLIPBOARD]", "\u00a7a\u00a7lTexture signature\n\u00a77This texture signature is " + skin.getSignature().length() + " characters length.\n\n\u00a7eClick to copy texture signature to clipboard", ClickEvent.Action.COPY_TO_CLIPBOARD, skin.getSignature());
            ((ClickableText)lines.get(6)).add("\u00a77Manage: ").add((updateButtonActivated ? "\u00a7b\u00a7l" : "\u00a78\u00a7l") + "[UPDATE SKIN]", (String)(updateButtonActivated ? "\u00a7b\u00a7lForce to update skin\n\u00a77This will update the skin, and depending on whether the player is online or not, the skin will be downloaded from the player's Game Profile, or else from the Mojang API.\n\n\u00a77Now the skin would be obtained from: \u00a7a" + obtainedFrom.getTitle() + "\n\n\u00a7eClick to update the skin" : ""), updateButtonActivated, ClickEvent.Action.RUN_COMMAND, command.getCommand(skin.getPlayerName() + " -forceDownload")).add(" ").add((skin.canBeDeleted() ? "\u00a74" : "\u00a78") + "\u00a7l[REMOVE SKIN]", skin.canBeDeleted() ? "\u00a74\u00a7lRemove skin from database\n\u00a77This will permanently remove the skin from the server, and it will not appear again until it is downloaded again.\n\n\u00a7eClick to remove skin from database" : null, skin.canBeDeleted(), ClickEvent.Action.RUN_COMMAND, command.getCommand(skin.getPlayerName() + " -delete"));
            ((ClickableText)lines.get(7)).add("\u00a77Apply to: ").add("\u00a76\u00a7l[GLOBAL NPC]", "\u00a76\u00a7lApply to Global NPC\n\u00a77This will apply the skin to the selected Global NPC.\n\n\u00a77Click to suggest the command\n\u00a7e/npcglobal setskin (id) " + skin.getPlayerName(), ClickEvent.Action.SUGGEST_COMMAND, "/npcglobal setskin (id) " + skin.getPlayerName()).add(" ").add("\u00a7c\u00a7l[PERSONAL NPC]", "\u00a7c\u00a7lApply to Personal NPC\n\u00a77This will apply the skin to the selected Personal NPC.\n\n\u00a77Click to suggest the command\n\u00a7e/npcpersonal setskin (player) (id) " + skin.getPlayerName(), ClickEvent.Action.SUGGEST_COMMAND, "/npcpersonal setskin (player) (id) " + skin.getPlayerName());
            for (i = 0; i < 100; ++i) {
                data.getPlayerSender().sendMessage("");
            }
            for (ClickableText line : lines) {
                line.send(data.getPlayerSender());
            }
            data.getPlayerSender().sendMessage("");
        });
    }, (command, data) -> {
        if (data.getCommandArgs().length > 1) {
            return null;
        }
        ArrayList strings = new ArrayList();
        String argLowerCase = data.getCommandArgs()[0].toLowerCase();
        NPC.Skin.getSuggestedSkinNames().stream().filter(x -> x.startsWith(argLowerCase)).forEach(x -> strings.add(x));
        return strings;
    });
    protected static final Command SETUPDATEGAZETICKS;
    protected static final Command SETUPDATEGAZETYPE;
    protected static final Command SETTICKSUNTILTABLISTHIDE;
    protected static final Command SETSKINUPDATEFREQUENCY;
    protected static final Command SETDEBUG;
    public static final String errorPrefix = "\u00a7c\u00a7lError \u00a78| \u00a77";

    public NPCLibCommand(SpigotPlugin plugin) {
        super(plugin, COMMAND_LABEL);
    }

    @Override
    public void onExecute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player playerSender;
        Player player = playerSender = sender instanceof Player ? (Player)sender : null;
        if (playerSender != null && !this.getPlugin().hasPermission(playerSender, "command")) {
            sender.sendMessage(NPCLibCommand.getPrefix() + "You don't have permission to do this.");
            return;
        }
        NPCLib npcLib = NPCLib.getInstance();
        if (args.length == 0) {
            if (playerSender != null) {
                NPCLibCommand.sendHelpList(playerSender, 1);
            } else {
                NPCLibCommand.error(sender);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("help")) {
            int asd = 1;
            if (args.length == 2 && MathUtils.isInteger(args[1])) {
                asd = Integer.valueOf(args[1]);
            }
            if (playerSender != null) {
                NPCLibCommand.sendHelpList(playerSender, asd);
            } else {
                NPCLibCommand.error(sender);
            }
            return;
        }
        Command commands = NPCLibCommand.getCommand(args[0]);
        if (commands == null) {
            new ClickableText("\u00a7c\u00a7lError \u00a78| \u00a77This command do not exist. Use \u00a7e/npclib help", NPCLibCommand.c(), ClickEvent.Action.SUGGEST_COMMAND, "/npclib help").send(sender);
            return;
        }
        if (!commands.isEnabled()) {
            NPCLibCommand.error(sender, "This command is not enabled.");
            return;
        }
        CommandData data = new CommandData(sender, args, npcLib);
        commands.getExecute().accept(commands, data);
    }

    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        Player playerSender;
        if (!this.isCommand(label)) {
            return null;
        }
        ArrayList<String> strings = new ArrayList<String>();
        Player player = playerSender = sender instanceof Player ? (Player)sender : null;
        if (playerSender != null && !this.getPlugin().hasPermission(playerSender, "command")) {
            return strings;
        }
        NPCLib npcLib = NPCLib.getInstance();
        if (args.length == 1) {
            commands.stream().filter(x -> x.getArgument().startsWith(args[0]) && x.isEnabled()).forEach(x -> strings.add(x.getArgument()));
            if ("help".startsWith(args[0]) || strings.isEmpty()) {
                strings.add("help");
            }
            return strings;
        }
        Command command = NPCLibCommand.getCommand(args[0]);
        if (command == null || !command.isEnabled()) {
            return strings;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("help")) {
            int maxpag = (commands.size() - 1) / 4 + 1;
            for (int i = 1; i <= maxpag; ++i) {
                strings.add("" + i);
            }
            return strings;
        }
        if (args.length >= 2) {
            CommandData data = new CommandData(sender, args, npcLib);
            List<String> suggested = command.getTabComplete().apply(command, data);
            if (suggested != null) {
                strings.addAll(suggested);
            }
            return strings;
        }
        return strings;
    }

    public static void error(CommandSender sender) {
        NPCLibCommand.error(sender, "Invalid command.");
    }

    public static void error(CommandSender sender, String text) {
        sender.sendMessage(errorPrefix + text);
    }

    public static void sendMessage(CommandSender sender, String text) {
        sender.sendMessage(NPCLibCommand.getPrefix() + text);
    }

    public static void error(CommandSender sender, String text, String hover, ClickEvent.Action action, String value) {
        new ClickableText(errorPrefix + text, hover, action, value).send(sender);
    }

    public static void errorSuggestCommand(CommandSender sender, String text, String suggestedCommand) {
        NPCLibCommand.error(sender, text, NPCLibCommand.c(), ClickEvent.Action.SUGGEST_COMMAND, suggestedCommand);
    }

    public static void notEnoughArguments(Command command, CommandData data) {
        NPCLibCommand.error(data.getCommandSender(), "Use " + command.getCommand());
    }

    private static String t() {
        return "\u00a7a\u2714 \u00a77";
    }

    private static String f() {
        return "\u00a7c\u2718 \u00a77";
    }

    private static String n() {
        return "\u00a7e\u2666 \u00a77";
    }

    private static String w() {
        return "\u00a7e\u26a0 \u00a7c";
    }

    private static String i(boolean b) {
        return b ? NPCLibCommand.t() : NPCLibCommand.f();
    }

    private static String b(boolean b) {
        return b ? "\u00a7aTrue" : "\u00a7cFalse";
    }

    private static String be(boolean b) {
        return b ? "\u00a7aEnabled" : "\u00a7cDisabled";
    }

    private static String ba(boolean b) {
        return b ? "\u00a7aOn" : "\u00a7cOff";
    }

    private static String c() {
        return "\u00a7eClick to write this command.";
    }

    private static String cN() {
        return "\u00a7cThis command is currently not enabled.";
    }

    private static String ts(List<String> lines) {
        String s = "";
        for (String a : lines) {
            s = s + "\u00a77, " + a;
        }
        s = s.replaceFirst("\u00a77, ", "");
        return s;
    }

    public static String getCommandColor(Command command) {
        return NPCLibCommand.getCommandColor(command, false);
    }

    public static String getCommandColor(Command command, boolean ignoreBold) {
        if (!command.isEnabled()) {
            return "\u00a78" + (command.isImportant() && !ignoreBold ? "\u00a7l" : "");
        }
        NPCLib.Command.Color color = command.getColor();
        if (command.isImportant()) {
            return color.getImportantSimple() + (!ignoreBold ? "\u00a7l" : "");
        }
        return "" + color.getNormal();
    }

    private static void sendHelpList(Player player, Integer pag) {
        int total = commands.size();
        int perpag = 4;
        int maxpag = (total - 1) / perpag + 1;
        if (pag > maxpag) {
            return;
        }
        for (int i = 0; i < 100; ++i) {
            player.sendMessage("");
        }
        player.sendMessage(new String[]{" \u00a7c\u00a7lNPCLib commands \u00a77(Page " + pag + "/" + maxpag + ")", ""});
        int aas = 0;
        for (int i = 0; i < perpag && total > i + (pag - 1) * perpag; ++i) {
            int id = i + (pag - 1) * perpag;
            Command npcCommands = commands.get(id);
            new ClickableText("\u00a78\u2022 ").add(NPCLibCommand.getCommandColor(npcCommands) + npcCommands.getTitle() + (String)(npcCommands.isCustom() ? " \u00a77(" + npcCommands.getPlugin().getName() + ")" : ""), npcCommands.isEnabled() ? NPCLibCommand.getCommandColor(npcCommands, true) + npcCommands.getCommand() + "\n\u00a78Command managed by " + npcCommands.getPluginName() + " plugin\n\n\u00a77" + npcCommands.getHover() + "\n\n" + NPCLibCommand.c() : null, npcCommands.isEnabled() ? new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, npcCommands.getCommand()) : null).send(player);
            ++aas;
        }
        int ass = perpag - aas;
        for (int i = 0; i < ass; ++i) {
            player.sendMessage("");
        }
        player.sendMessage("");
        boolean next = total > pag * perpag;
        boolean previous = pag > 1;
        new ClickableText("  ").add((previous ? "\u00a7c\u00a7l" : "\u00a78\u00a7l") + "[PREVIOUS]", previous ? "\u00a7eClick to go to the previous page." : null, previous ? new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/npclib help " + (pag - 1)) : null).add("    ").add((next ? "\u00a7a\u00a7l" : "\u00a78\u00a7l") + "[NEXT]", next ? "\u00a7eClick to go to the next page." : null, next ? new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/npclib help " + (pag + 1)) : null).send(player);
        player.sendMessage("");
    }

    public static Command getCommand(String argument) {
        return commands.stream().filter(x -> x.getArgument().equalsIgnoreCase(argument)).findAny().orElse(null);
    }

    public static Set<Command> getCommands(Plugin plugin) {
        HashSet<Command> commands = new HashSet<Command>();
        commands.stream().filter(x -> x.getPlugin().equals(plugin)).forEach(x -> commands.add((Command)x));
        return commands;
    }

    public static void addCommand(Plugin plugin, String argument, String arguments, boolean enabled, boolean important, String description, String hover, BiConsumer<Command, CommandData> execute, BiFunction<Command, CommandData, List<String>> tabComplete, NPCLib.Command.Color color) {
        if (plugin.equals((Object)PlayerNPCPlugin.getInstance())) {
            throw new IllegalArgumentException("Plugin must be yours.");
        }
        new Command(plugin, argument, arguments, enabled, important, description, hover, execute, tabComplete, color);
    }

    public static String getPrefix() {
        return PlayerNPCPlugin.getInstance().getPrefix();
    }

    static {
        SETSKINUPDATEFREQUENCY = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setskinupdatefrequency", "(number) (timeUnit)", true, false, "Set skin update frequency", "\u00a77With this command you can set the amount of time that the server will save the skin locally, and when it exceeds it, it will request it again from the Mojang API.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(number) \u00a77Amount of time it will take to update.\n\u00a78\u2022 \u00a7a(timeUnit) \u00a77Unit of time it will take to update.\n\u00a78TimeUnit: \u00a77seconds, minutes, hours, days", (command, data) -> {
            if (data.getCommandArgs().length < 2) {
                NPCLibCommand.notEnoughArguments(command, data);
                return;
            }
            if (!MathUtils.isInteger(data.getCommandArgs()[0])) {
                NPCLibCommand.error(data.getCommandSender(), "No valid number entered");
                return;
            }
            Integer number = Integer.valueOf(data.getCommandArgs()[0]);
            try {
                TimeUnit timeUnit = TimeUnit.valueOf(data.getCommandArgs()[1].toUpperCase());
                command.getPluginManager().setSkinUpdateFrequency(new NPCLib.SkinUpdateFrequency(number, timeUnit));
                NPCLibCommand.sendMessage(data.getCommandSender(), "Skin update frequency has been set as \u00a7a" + number + " " + timeUnit.name().toLowerCase() + "\u00a77.");
            }
            catch (Exception e) {
                NPCLibCommand.error(data.getCommandSender(), "No valid time unit entered");
            }
        }, (command, data) -> {
            if (data.getCommandArgs().length > 2) {
                return null;
            }
            ArrayList strings = new ArrayList();
            if (data.getCommandArgs().length == 2) {
                Arrays.asList("seconds", "minutes", "hours", "days").stream().filter(x -> x.startsWith(data.getCommandArgs()[1].toLowerCase())).forEach(x -> strings.add(x.toLowerCase()));
            }
            return strings;
        });
        SETUPDATEGAZETICKS = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setupdategazeticks", "(ticks)", true, false, "Set update gaze ticks", "\u00a77With this command you can set the ticks that the server will take to update the gaze of the NPCs.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(ticks) \u00a77Number of ticks it will take to update.", (command, data) -> {
            if (data.getCommandArgs().length < 1) {
                NPCLibCommand.notEnoughArguments(command, data);
                return;
            }
            if (!MathUtils.isInteger(data.getCommandArgs()[0])) {
                NPCLibCommand.error(data.getCommandSender(), "No valid number entered");
                return;
            }
            Integer ticks = Integer.valueOf(data.getCommandArgs()[0]);
            command.getPluginManager().setUpdateGazeTicks(ticks);
            NPCLibCommand.sendMessage(data.getCommandSender(), "Update gaze ticks has been set to \u00a7a" + command.getPluginManager().getUpdateGazeTicks() + " ticks\u00a77.");
        }, (command, data) -> null);
        SETUPDATEGAZETYPE = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setupdategazetype", "(type)", true, false, "Set update gaze type", "\u00a77With this command you can set the type of gaze update of the NPC's.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(type) \u00a77Gaze Update Type.\n\u00a78GazeUpdateType: \u00a77move_event, ticks", (command, data) -> {
            if (data.getCommandArgs().length < 1) {
                NPCLibCommand.notEnoughArguments(command, data);
                return;
            }
            try {
                NPCLib.UpdateGazeType type = NPCLib.UpdateGazeType.valueOf(data.getCommandArgs()[0].toUpperCase());
                data.getNpcLib().getPluginManager(command.plugin).setUpdateGazeType(type);
                NPCLibCommand.sendMessage(data.getCommandSender(), "Update gaze type has been set to \u00a7a" + type.toString().toLowerCase().replaceAll("_", " ") + "\u00a77.");
            }
            catch (Exception e) {
                NPCLibCommand.error(data.getCommandSender(), "No valid update gaze type entered");
            }
        }, (command, data) -> {
            if (data.getCommandArgs().length > 1) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.stream(NPCLib.UpdateGazeType.values()).toList().stream().filter(x -> x.name().toLowerCase().startsWith(data.getCommandArgs()[0].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            return strings;
        });
        SETTICKSUNTILTABLISTHIDE = new Command((Plugin)PlayerNPCPlugin.getInstance(), "settitcksuntiltablisthide", "(ticks)", true, false, "Set ticks until tablist hide", "\u00a77With this command you can set the ticks that it will take for the NPCs to disappear from the tablist.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(ticks) \u00a77Number of ticks it will take to hide.", (command, data) -> {
            if (data.getCommandArgs().length < 1) {
                NPCLibCommand.notEnoughArguments(command, data);
                return;
            }
            if (!MathUtils.isInteger(data.getCommandArgs()[0])) {
                NPCLibCommand.error(data.getCommandSender(), "No valid number entered");
                return;
            }
            Integer ticks = Integer.valueOf(data.getCommandArgs()[0]);
            command.getPluginManager().setTicksUntilTabListHide(ticks);
            NPCLibCommand.sendMessage(data.getCommandSender(), "Ticks until tablist hide has been set to \u00a7a" + command.getPluginManager().getTicksUntilTabListHide() + " ticks\u00a77.");
        }, (command, data) -> null);
        SETDEBUG = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setdebug", "(boolean)", true, false, "Set debug mode", "\u00a77With this command you can enable or disable debug mode.\n\u00a7cThis is only for developers.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false.", (command, data) -> {
            if (data.getCommandArgs().length < 1) {
                NPCLibCommand.notEnoughArguments(command, data);
                return;
            }
            Boolean debug = Boolean.parseBoolean(data.getCommandArgs()[0]);
            if (data.getNpcLib().isDebug() == debug.booleanValue()) {
                NPCLibCommand.error(data.getCommandSender(), "Debug mode was " + debug + " yet.");
                return;
            }
            data.getNpcLib().setDebug(debug);
            NPCLibCommand.sendMessage(data.getCommandSender(), "Debug mode has been set to " + NPCLibCommand.b(debug).toLowerCase() + "\u00a77.");
        }, (command, data) -> {
            if (data.getArgs().length != 2) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.asList("true", "false").stream().filter(x -> x.startsWith(data.getArgs()[1].toLowerCase())).forEach(x -> strings.add(x));
            return strings;
        });
    }

    public static class Command {
        private String argument;
        private String arguments;
        private boolean important;
        private boolean enabled;
        private String title;
        private String hover;
        private BiConsumer<Command, CommandData> execute;
        private BiFunction<Command, CommandData, List<String>> tabComplete;
        private Plugin plugin;
        private NPCLib.Command.Color color;

        protected Command(Plugin plugin, String argument, String arguments, boolean enabled, boolean important, String title, String hover, BiConsumer<Command, CommandData> execute, BiFunction<Command, CommandData, List<String>> tabComplete, NPCLib.Command.Color color) {
            if (NPCLibCommand.getCommand(argument) != null) {
                throw new IllegalArgumentException("Argument is already used.");
            }
            this.plugin = plugin;
            this.argument = argument;
            this.arguments = arguments;
            this.enabled = enabled;
            this.important = important;
            this.title = title;
            this.hover = hover;
            this.execute = execute;
            this.tabComplete = tabComplete;
            this.color = color;
            commands.add(this);
        }

        protected Command(Plugin plugin, String argument, String arguments, boolean enabled, boolean important, String title, String hover, BiConsumer<Command, CommandData> execute, BiFunction<Command, CommandData, List<String>> tabComplete) {
            this(plugin, argument, arguments, enabled, important, title, hover, execute, tabComplete, null);
        }

        public String getArgument() {
            return this.argument;
        }

        public String getArguments() {
            return this.arguments;
        }

        public boolean isImportant() {
            return this.important;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public boolean isCustom() {
            return !(this.plugin instanceof PlayerNPCPlugin);
        }

        public Plugin getPlugin() {
            return this.plugin;
        }

        public String getPluginName() {
            return this.getPlugin().getName();
        }

        public NPCLib.PluginManager getPluginManager() {
            return NPCLib.getInstance().getPluginManager(this.plugin);
        }

        public String getTitle() {
            return this.title;
        }

        public String getHover() {
            return this.hover;
        }

        public BiConsumer<Command, CommandData> getExecute() {
            return this.execute;
        }

        public BiFunction<Command, CommandData, List<String>> getTabComplete() {
            return this.tabComplete;
        }

        public NPCLib.Command.Color getColor() {
            return this.color == null ? this.getPluginManager().getCommandColor() : this.color;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getCommand() {
            return "/npclib " + this.argument + (String)(this.arguments != null ? " " + this.arguments : "");
        }

        public String getCommand(String arguments) {
            return "/npclib " + this.argument + (String)(arguments != null ? " " + arguments : "");
        }
    }

    public class CommandData {
        private final CommandSender commandSender;
        private final String[] args;
        private final String[] cmdArgs;
        private final NPCLib npcLib;

        public CommandData(CommandSender commandSender, String[] args, NPCLib npcLib) {
            this.commandSender = commandSender;
            this.args = args;
            this.npcLib = npcLib;
            this.cmdArgs = new String[args.length - 1];
            for (int i = 1; i < args.length; ++i) {
                this.cmdArgs[i - 1] = args[i];
            }
        }

        public String getCommand() {
            if (this.args.length < 1) {
                return null;
            }
            return this.args[0];
        }

        public NPCLib getNpcLib() {
            return this.npcLib;
        }

        public Player getPlayerSender() {
            if (this.commandSender instanceof Player) {
                return (Player)this.commandSender;
            }
            return null;
        }

        public CommandSender getCommandSender() {
            return this.commandSender;
        }

        public String[] getArgs() {
            return this.args;
        }

        public String[] getCommandArgs() {
            return this.cmdArgs;
        }
    }
}
