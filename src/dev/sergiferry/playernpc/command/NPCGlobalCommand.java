/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.util.Vector
 */
package dev.sergiferry.playernpc.command;

import dev.sergiferry.playernpc.PlayerNPCPlugin;
import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import dev.sergiferry.playernpc.command.NPCLibCommand;
import dev.sergiferry.playernpc.utils.ClickableText;
import dev.sergiferry.playernpc.utils.MathUtils;
import dev.sergiferry.playernpc.utils.StringUtils;
import dev.sergiferry.playernpc.utils.TimerUtils;
import dev.sergiferry.spigot.SpigotPlugin;
import dev.sergiferry.spigot.commands.CommandInstance;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.Vector;

public class NPCGlobalCommand
extends CommandInstance
implements TabCompleter {
    protected static final String COMMAND_LABEL = "npcglobal";
    private static final List<Command> commands;
    private static final Command GENERATE;
    private static final Command SETPERSISTENT;
    private static final Command SETVISIBILITY;
    private static final Command SETVISIBILITYREQUIREMENT;
    private static final Command ADDPLAYER;
    private static final Command REMOVEPLAYER;
    private static final Command SETAUTOCREATE;
    private static final Command SETAUTOSHOW;
    private static final Command SETTEXT;
    private static final Command SETSKIN;
    private static final Command SETSKINVISIBLEPART;
    private static final Command SETOWNPLAYERSKIN;
    private static final Command SETITEM;
    private static final Command SETCOLLIDABLE;
    private static final Command SETGLOWCOLOR;
    private static final Command SETSHOWONTABLIST;
    private static final Command SETCUSTOMTABLISTNAME;
    private static final Command SETLINESPACING;
    private static final Command SETTEXTALIGNMENT;
    private static final Command SETTEXTOPACITY;
    private static final Command SETLINEOPACITY;
    private static final Command SETGAZETRACKINGTYPE;
    private static final Command SETGLOWING;
    private static final Command SETPOSE;
    private static final Command PLAYANIMATION;
    private static final Command CUSTOMDATA;
    private static final Command ACTION;
    private static final Command TELEPORT;
    private static final Command LOOKAT;
    private static final Command SHOW;
    private static final Command HIDE;
    private static final Command SETHIDETEXT;
    private static final Command REMOVE;
    private static final Command UPDATE;
    private static final Command FORCEUPDATE;
    private static final Command UPDATETEXT;
    private static final Command FORCEUPDATETEXT;
    private static final Command LOADPERSISTENT;
    private static final Command SAVEPERSISTENT;
    private static final Command DISABLEPERSISTENTSAVE;
    private static final Command GOTO;
    private static final Command INFO;
    public static final String errorPrefix = "\u00a7c\u00a7lError \u00a78| \u00a77";

    public NPCGlobalCommand(SpigotPlugin plugin) {
        super(plugin, COMMAND_LABEL);
    }

    @Override
    public void onExecute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player playerSender;
        Player player = playerSender = sender instanceof Player ? (Player)sender : null;
        if (playerSender != null && !this.getPlugin().hasPermission(playerSender, "command")) {
            sender.sendMessage(NPCGlobalCommand.getPrefix() + "You don't have permission to do this.");
            return;
        }
        NPCLib npcLib = NPCLib.getInstance();
        if (args.length == 0) {
            if (playerSender != null) {
                NPCGlobalCommand.sendHelpList(playerSender, 1);
            } else {
                NPCGlobalCommand.error(sender);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("help")) {
            int asd = 1;
            if (args.length == 2 && MathUtils.isInteger(args[1])) {
                asd = Integer.valueOf(args[1]);
            }
            if (playerSender != null) {
                NPCGlobalCommand.sendHelpList(playerSender, asd);
            } else {
                NPCGlobalCommand.error(sender);
            }
            return;
        }
        if (args.length < 2) {
            Command commands = NPCGlobalCommand.getCommand(args[0]);
            if (commands != null) {
                new ClickableText("\u00a7c\u00a7lError \u00a78| \u00a77Use \u00a7e" + commands.getCommand(), NPCGlobalCommand.c(), ClickEvent.Action.SUGGEST_COMMAND, commands.getCommand()).send(sender);
            } else {
                NPCGlobalCommand.errorSuggestCommand(sender, "Incorrect command. Use \u00a7e/npcglobal help", "/npcglobal help");
            }
            return;
        }
        String id = args[1];
        Command commands = NPCGlobalCommand.getCommand(args[0]);
        if (commands == null) {
            new ClickableText("\u00a7c\u00a7lError \u00a78| \u00a77This command do not exist. Use \u00a7e/npcglobal help", NPCGlobalCommand.c(), ClickEvent.Action.SUGGEST_COMMAND, "/npcglobal help").send(sender);
            return;
        }
        if (!commands.isEnabled()) {
            NPCGlobalCommand.error(sender, "This command is not enabled.");
            return;
        }
        CommandData data = new CommandData(sender, args, npcLib);
        if (commands.getArgument().equals(GENERATE.getArgument())) {
            GENERATE.getExecute().accept(GENERATE, data);
            return;
        }
        NPC.Global npc = npcLib.getGlobalNPC(id);
        if (npc == null) {
            NPCGlobalCommand.errorSuggestCommand(sender, "\u00a77This NPC doesn't exist. To generate the NPC use \u00a7e" + GENERATE.getCommand(), GENERATE.getCommand());
            return;
        }
        data.setGlobal(npc);
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
        Command command = NPCGlobalCommand.getCommand(args[0]);
        if (command == null || !command.isEnabled()) {
            return strings;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("help")) {
                int maxpag = (commands.size() - 1) / 4 + 1;
                for (int i = 1; i <= maxpag; ++i) {
                    strings.add("" + i);
                }
                return strings;
            }
            if (args[0].equalsIgnoreCase(GENERATE.getArgument())) {
                return strings;
            }
            boolean onlyPersistent = args[0].equalsIgnoreCase(SAVEPERSISTENT.getArgument()) || args[0].equalsIgnoreCase(LOADPERSISTENT.getArgument()) || args[0].equalsIgnoreCase(DISABLEPERSISTENTSAVE.getArgument());
            boolean onlyCanBePersistent = args[0].equalsIgnoreCase(SETPERSISTENT.getArgument());
            Stream<NPC.Global> npcs = NPCLib.getInstance().getAllGlobalNPCs().stream().filter(x -> x.getCode().toLowerCase().startsWith(args[1].toLowerCase()));
            if (onlyPersistent) {
                npcs = npcs.filter(x -> x.isPersistent());
            }
            if (onlyCanBePersistent) {
                npcs = npcs.filter(x -> x.canBePersistent());
            }
            npcs.forEach(x -> strings.add(x.getCode()));
            return strings;
        }
        if (args.length >= 3) {
            CommandData data = new CommandData(sender, args, npcLib);
            NPC.Global npc = npcLib.getGlobalNPC(data.getID());
            if (npc == null) {
                if (args[0].equalsIgnoreCase(GENERATE.getArgument())) {
                    return NPCGlobalCommand.GENERATE.tabComplete.apply(command, data);
                }
                return strings;
            }
            data.setGlobal(npc);
            List<String> suggested = command.getTabComplete().apply(command, data);
            if (suggested != null) {
                strings.addAll(suggested);
            }
            return strings;
        }
        return strings;
    }

    public static void error(CommandSender sender) {
        NPCGlobalCommand.error(sender, "Invalid command.");
    }

    public static void error(CommandSender sender, String text) {
        sender.sendMessage(errorPrefix + text);
    }

    public static void sendMessage(CommandSender sender, String text) {
        sender.sendMessage(NPCGlobalCommand.getPrefix() + text);
    }

    public static void error(CommandSender sender, String text, String hover, ClickEvent.Action action, String value) {
        new ClickableText(errorPrefix + text, hover, action, value).send(sender);
    }

    public static void errorSuggestCommand(CommandSender sender, String text, String suggestedCommand) {
        NPCGlobalCommand.error(sender, text, NPCGlobalCommand.c(), ClickEvent.Action.SUGGEST_COMMAND, suggestedCommand);
    }

    public static void notEnoughArguments(Command command, CommandData data) {
        NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
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
        return b ? NPCGlobalCommand.t() : NPCGlobalCommand.f();
    }

    private static String b(boolean b) {
        return b ? "\u00a7aTrue" : "\u00a7cFalse";
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

    public static void needUpdate(CommandSender sender, NPC.Global global, boolean forceUpdate) {
        new ClickableText("\u00a77You need to do \u00a7e" + (!forceUpdate ? UPDATE.getCommand(global) : FORCEUPDATE.getCommand(global)) + " \u00a77to show it to the players.", NPCGlobalCommand.c(), ClickEvent.Action.SUGGEST_COMMAND, !forceUpdate ? UPDATE.getCommand(global) : FORCEUPDATE.getCommand(global)).send(sender);
    }

    public static void needUpdateText(CommandSender sender, NPC.Global global, boolean forceUpdate) {
        new ClickableText("\u00a77You need to do \u00a7e" + (!forceUpdate ? UPDATETEXT.getCommand(global) : FORCEUPDATETEXT.getCommand(global)) + " \u00a77to show it to the players.", NPCGlobalCommand.c(), ClickEvent.Action.SUGGEST_COMMAND, !forceUpdate ? UPDATETEXT.getCommand(global) : FORCEUPDATETEXT.getCommand(global)).send(sender);
    }

    public static String getCommandColor(Command command) {
        return NPCGlobalCommand.getCommandColor(command, false);
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
        player.sendMessage(new String[]{" \u00a7e\u00a7lPERSISTENT NPCs ARE NOW AVAILABLE!", "", " \u00a77All changes made through the command will be saved if the NPC is persistent.", "", "", "", ""});
        player.sendMessage(new String[]{" \u00a7c\u00a7lGlobal NPC commands \u00a77(Page " + pag + "/" + maxpag + ")", ""});
        int aas = 0;
        for (int i = 0; i < perpag && total > i + (pag - 1) * perpag; ++i) {
            int id = i + (pag - 1) * perpag;
            Command npcCommands = commands.get(id);
            new ClickableText("\u00a78\u2022 ").add(NPCGlobalCommand.getCommandColor(npcCommands) + npcCommands.getTitle() + (String)(npcCommands.isCustom() ? " \u00a77(" + npcCommands.getPlugin().getName() + ")" : ""), npcCommands.isEnabled() ? NPCGlobalCommand.getCommandColor(npcCommands, true) + npcCommands.getCommand() + "\n\u00a78Command managed by " + npcCommands.getPluginName() + " plugin\n\n\u00a77" + npcCommands.getHover() + "\n\n" + NPCGlobalCommand.c() : null, npcCommands.isEnabled() ? new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, npcCommands.getCommand()) : null).send(player);
            ++aas;
        }
        int ass = perpag - aas;
        for (int i = 0; i < ass; ++i) {
            player.sendMessage("");
        }
        player.sendMessage("");
        boolean next = total > pag * perpag;
        boolean previous = pag > 1;
        new ClickableText("  ").add((previous ? "\u00a7c\u00a7l" : "\u00a78\u00a7l") + "[PREVIOUS]", previous ? "\u00a7eClick to go to the previous page." : null, previous ? new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/npcglobal help " + (pag - 1)) : null).add("    ").add((next ? "\u00a7a\u00a7l" : "\u00a78\u00a7l") + "[NEXT]", next ? "\u00a7eClick to go to the next page." : null, next ? new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/npcglobal help " + (pag + 1)) : null).send(player);
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
        SETAUTOCREATE = null;
        SETAUTOSHOW = null;
        SETHIDETEXT = null;
        commands = new ArrayList<Command>();
        GENERATE = new Command((Plugin)PlayerNPCPlugin.getInstance(), "generate", "(id) [persistent]", true, true, "Generate an NPC", "\u00a77It generates the Global NPC object with the id. You can also set if the NPC will be persistent, or not (You can change this after generation).\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID is a custom string you decide.\n\u00a78\u2022 \u00a7a[persistent] \u00a77Value that can be true or false", (command, data) -> {
            if (data.getNpcLib().getGlobalNPC((Plugin)PlayerNPCPlugin.getInstance(), data.getID()) != null) {
                NPCGlobalCommand.error(data.getCommandSender(), "The NPC with id \u00a7e" + data.getID() + " \u00a77is already generated.");
                return;
            }
            Boolean bo = null;
            if (data.getArgs().length > 2) {
                if (!data.getArgs()[2].equalsIgnoreCase("true") && !data.getArgs()[2].equalsIgnoreCase("false")) {
                    NPCGlobalCommand.error(data.getCommandSender(), "Boolean can only be true or false.");
                    return;
                }
                bo = Boolean.valueOf(data.getArgs()[2]);
            }
            NPC.Global npc = data.getNpcLib().generateGlobalNPC((Plugin)PlayerNPCPlugin.getInstance(), data.getID(), NPC.Global.Visibility.EVERYONE, null, data.getPlayerSender() != null ? data.getPlayerSender().getLocation() : new Location((World)Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0));
            npc.setPersistent(bo != null ? bo : true);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC " + (bo != null && bo != false ? "(Persistent) " : "") + "with ID: \u00a7a" + npc.getCode() + "\u00a77 has been generated.");
        }, (command, data) -> {
            if (data.getCommandArgs().length != 1) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.asList("true", "false").stream().filter(x -> x.startsWith(data.getCommandArgs()[0].toLowerCase())).forEach(x -> strings.add(x));
            return strings;
        });
        SETPERSISTENT = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setpersistent", "(id) (boolean)", true, true, "Set NPC persistent", "\u00a77This sets if the NPC will be persistent or not.\n\u00a77That means that if the NPC is persistent, after a server restart it will appear, otherwise not.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            if (!data.getGlobal().canBePersistent()) {
                NPCGlobalCommand.error(data.getCommandSender(), "This NPC cannot be Persistent because it's not managed by PlayerNPC plugin.");
                return;
            }
            if (!data.getArgs()[2].equalsIgnoreCase("true") && !data.getArgs()[2].equalsIgnoreCase("false")) {
                NPCGlobalCommand.error(data.getCommandSender(), "Boolean can only be true or false.");
                return;
            }
            boolean bo = Boolean.valueOf(data.getArgs()[2]);
            if (data.getGlobal().isPersistent() == bo) {
                NPCGlobalCommand.error(data.getCommandSender(), "Persistent attribute was \u00a7e" + bo + "\u00a77 yet");
                return;
            }
            data.getGlobal().setPersistent(bo);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77persistent has been set as \u00a7e" + bo);
        }, (command, data) -> {
            if (data.getArgs().length != 3) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.asList("true", "false").stream().filter(x -> x.startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x));
            return strings;
        });
        SETVISIBILITY = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setvisibility", "(id) (visibility)", true, false, "Set NPC visibility", "\u00a77With this command you can set if the NPC will be visible for everyone, or for selected players.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID is a custom string you decide.\n\u00a78\u2022 \u00a7a(visibility) \u00a77Who can see the Global NPC.\n\u00a78Visibility: \u00a77everyone, selected_players", (command, data) -> {
            NPC.Global.Visibility visibility;
            if (data.getCommandArgs().length < 1) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            String vis = data.getCommandArgs()[0];
            if (vis.equalsIgnoreCase("everyone")) {
                visibility = NPC.Global.Visibility.EVERYONE;
            } else if (vis.equalsIgnoreCase("selected_players")) {
                visibility = NPC.Global.Visibility.SELECTED_PLAYERS;
            } else {
                NPCGlobalCommand.error(data.getCommandSender(), "This visibility option do not exist.");
                return;
            }
            data.getGlobal().setVisibility(visibility);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77visibility has been set to \u00a7e" + visibility.name().toLowerCase().replaceAll("_", " ") + "\u00a77.");
        }, (command, data) -> {
            if (data.getCommandArgs().length != 1) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.asList("everyone", "selected_players").stream().filter(x -> x.startsWith(data.getCommandArgs()[0].toLowerCase())).forEach(x -> strings.add(x));
            return strings;
        });
        SETVISIBILITYREQUIREMENT = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setvisibilityrequirement", "(id) (permission/clear)", true, false, "Set NPC visibility requirement", "\u00a77With this command you can establish a permission requirement to be able to see the NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID is a custom string you decide.\n\u00a78\u2022 \u00a7a(permission/clear) \u00a77Only players with this permission can see the NPC. You must set visibility to everyone, in order to show this NPC to all players with an specific permission.", (command, data) -> {
            if (data.getCommandArgs().length < 1) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            String vis = data.getCommandArgs()[0];
            if (vis.equalsIgnoreCase("clear")) {
                if (data.getGlobal().getVisibilityRequirement() == null) {
                    NPCGlobalCommand.error(data.getCommandSender(), "This Global NPC doesn't have any visibility requirement yet.");
                    return;
                }
                data.getGlobal().setVisibilityRequirement(null);
                data.getGlobal().setCustomData("visibilityrequirementpermission", null);
                data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77visibility requirement permission has been cleared.");
                return;
            }
            data.getGlobal().setVisibilityRequirement(player -> player.hasPermission(vis));
            data.getGlobal().setCustomData("visibilityrequirementpermission", vis.toLowerCase());
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77visibility requirement permission has been set to \u00a7e" + vis.toLowerCase() + "\u00a77.");
        }, (command, data) -> {
            if (data.getCommandArgs().length != 1) {
                return null;
            }
            ArrayList strings = new ArrayList();
            boolean b = data.getGlobal().getCustomData("visibilityrequirementpermission") != null;
            Arrays.asList(b ? "clear" : "", b ? data.getGlobal().getCustomData("visibilityrequirementpermission") : "").stream().filter(x -> x.startsWith(data.getCommandArgs()[0].toLowerCase())).forEach(x -> strings.add(x));
            return strings;
        });
        ADDPLAYER = new Command((Plugin)PlayerNPCPlugin.getInstance(), "addplayer", "(id) (player) [ignoreVisibilityRequirement]", true, false, "Add player to see an NPC", "\u00a77This adds a player to the selected players to see a Global NPC. If the Global NPC already has visibility for everyone, this is not useful.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(player) \u00a77Player's name that will see the NPC.\n\u00a78\u2022 \u00a7a[ignoreVisibilityRequirement] \u00a77Value can be true or false.", (command, data) -> {
            if (data.getCommandArgs().length < 1) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            String playerName = data.getCommandArgs()[0];
            Player player = Bukkit.getPlayerExact((String)playerName);
            if (player == null) {
                Player suggested = Bukkit.getPlayer((String)playerName);
                if (suggested != null) {
                    NPCGlobalCommand.errorSuggestCommand(data.getCommandSender(), "This player cannot be found. Maybe you're meaning \u00a7e" + suggested.getName(), command.getCommand(data.getGlobal(), suggested.getName()));
                } else {
                    NPCGlobalCommand.error(data.getCommandSender(), "This player cannot be found.");
                }
                return;
            }
            if (data.getGlobal().hasPlayer(player)) {
                NPCGlobalCommand.error(data.getCommandSender(), "This player is already added to this Global NPC.");
                return;
            }
            boolean ignoreRequirement = false;
            if (data.getCommandArgs().length > 1) {
                if (!data.getCommandArgs()[1].equalsIgnoreCase("true") && !data.getCommandArgs()[1].equalsIgnoreCase("false")) {
                    NPCGlobalCommand.error(data.getCommandSender(), "Boolean can only be true or false.");
                    return;
                }
                ignoreRequirement = Boolean.valueOf(data.getCommandArgs()[1]);
            }
            if (!ignoreRequirement && !data.getGlobal().meetsVisibilityRequirement(player)) {
                NPCGlobalCommand.errorSuggestCommand(data.getCommandSender(), "This player do not meet visibility requirement. Instead use ignoreVisibilityRequirement as true", command.getCommand(data.getGlobal(), player.getName() + " true"));
                return;
            }
            data.getGlobal().addPlayer(player, ignoreRequirement);
            NPCGlobalCommand.sendMessage(data.getCommandSender(), "\u00a7e" + player.getName() + " \u00a77has been added to Global NPC \u00a7a" + data.getID());
        }, (command, data) -> {
            if (data.getCommandArgs().length > 2) {
                return null;
            }
            ArrayList strings = new ArrayList();
            if (data.getCommandArgs().length == 1) {
                Bukkit.getOnlinePlayers().stream().filter(x -> x.getName().toLowerCase().startsWith(data.getCommandArgs()[0].toLowerCase()) && !data.getGlobal().hasPlayer((Player)x)).forEach(x -> strings.add(x.getName()));
            }
            if (data.getCommandArgs().length == 2) {
                Arrays.asList("true", "false").stream().filter(x -> x.startsWith(data.getCommandArgs()[1].toLowerCase())).forEach(x -> strings.add(x));
            }
            return strings;
        });
        REMOVEPLAYER = new Command((Plugin)PlayerNPCPlugin.getInstance(), "removeplayer", "(id) (player)", true, false, "Remove player from seeing an NPC", "\u00a77This removes a player from the selected players to see a Global NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(player) \u00a77Player's name that will stop seeing the NPC.", (command, data) -> {
            if (data.getCommandArgs().length < 1) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            String playerName = data.getCommandArgs()[0];
            Player player = Bukkit.getPlayerExact((String)playerName);
            if (player == null) {
                Player suggested = Bukkit.getPlayer((String)playerName);
                if (!data.getGlobal().hasPlayer(suggested)) {
                    suggested = null;
                }
                if (suggested != null) {
                    NPCGlobalCommand.errorSuggestCommand(data.getCommandSender(), "This player cannot be found. Maybe you're meaning \u00a7e" + suggested.getName(), command.getCommand(data.getGlobal(), suggested.getName()));
                } else {
                    NPCGlobalCommand.error(data.getCommandSender(), "This player cannot be found.");
                }
                return;
            }
            if (!data.getGlobal().hasPlayer(player)) {
                NPCGlobalCommand.error(data.getCommandSender(), "This player is not added to this Global NPC yet.");
                return;
            }
            NPCGlobalCommand.sendMessage(data.getCommandSender(), "\u00a7e" + player.getName() + " \u00a77has been removed from Global NPC \u00a7a" + data.getID());
            data.getGlobal().removePlayer(player);
        }, (command, data) -> {
            if (data.getCommandArgs().length > 1) {
                return null;
            }
            ArrayList strings = new ArrayList();
            if (data.getCommandArgs().length == 1) {
                Bukkit.getOnlinePlayers().stream().filter(x -> x.getName().toLowerCase().startsWith(data.getCommandArgs()[0].toLowerCase()) && data.getGlobal().hasPlayer((Player)x)).forEach(x -> strings.add(x.getName()));
            }
            return strings;
        });
        SETTEXT = new Command((Plugin)PlayerNPCPlugin.getInstance(), "settext", "(id) (text)...", true, false, "Set the text of an NPC", "\u00a77This sets the text above the NPC. No need to set it.\n\u00a77Use \" \" for the spaces and \"\\n\" for a new line.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(text) \u00a77The text will see above the NPC.", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            String msg = "";
            for (int i = 2; i < data.getArgs().length; ++i) {
                msg = msg + " " + data.getArgs()[i];
            }
            msg = msg.replaceFirst(" ", "");
            msg = ChatColor.translateAlternateColorCodes((char)'&', msg);
            ArrayList<String> list = new ArrayList<String>();
            if (msg.contains("\\n")) {
                for (String s : msg.split("\\\\n")) {
                    list.add(s);
                }
            } else {
                list.add(msg);
            }
            int as = data.getGlobal().getText().size();
            data.getGlobal().setText(list);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77text has been set to \u00a7e" + list.size() + "\u00a77 lines.");
            boolean same = as == list.size();
            NPCGlobalCommand.needUpdateText(data.getCommandSender(), data.getGlobal(), !same);
        }, (command, data) -> null);
        SETSKIN = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setskin", "(id) (skin)", true, false, "Set the skin of an NPC", "\u00a77This sets the NPC skin. \u00a78By default is Steve skin.\n\u00a77You can set both online or offline player's skin.\n\u00a77With the API you can set any skin texture.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(skin) \u00a77The name of the player skin", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Trying to fetch skin of " + data.getArgs()[2]);
            NPC.Skin.fetchSkinAsync(data.getArgs()[2], skin -> {
                if (skin == null) {
                    NPCGlobalCommand.error(data.getCommandSender(), "There was an error trying to fetch that skin.");
                    return;
                }
                data.getGlobal().setSkin((NPC.Skin)skin);
                data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 skin has been set to " + skin.getPlayerName());
                NPCGlobalCommand.needUpdate(data.getCommandSender(), data.getGlobal(), true);
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
        SETSKINVISIBLEPART = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setskinvisiblepart", "(id) (part) (boolean)", true, false, "Set the skin's visible parts of an NPC", "\u00a77With this command you can manage which parts of the NPC's skin will be visible and which will not.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(part) \u00a77Type of NPC.Skin.Part\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false.\n\u00a78NPC.Skin.Part: \u00a77cape, jacket, left_sleeve, right_sleeve, left_pants, right_pants, hat", (command, data) -> {
            if (data.getArgs().length < 4) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            try {
                NPC.Skin.Part part = NPC.Skin.Part.valueOf(data.getCommandArgs()[0].toUpperCase());
                Boolean b = Boolean.valueOf(data.getCommandArgs()[1]);
                if (data.getGlobal().getSkinParts().isVisible(part) == b.booleanValue()) {
                    NPCGlobalCommand.error(data.getCommandSender(), "The visibility for \u00a7e" + part.name().toLowerCase().replaceAll("_", " ") + " \u00a77part was already " + NPCGlobalCommand.b(b).toLowerCase());
                    return;
                }
                data.getGlobal().setSkinVisiblePart(part, b);
                data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 skin part \u00a7a" + part.name().toLowerCase().replaceAll("_", " ") + " \u00a77has been set it's visibility as " + NPCGlobalCommand.b(b).toLowerCase());
                NPCGlobalCommand.needUpdate(data.getCommandSender(), data.getGlobal(), false);
            }
            catch (Exception e) {
                NPCGlobalCommand.error(data.getCommandSender(), "\u00a77Skin part was not found with that name.");
            }
        }, (command, data) -> {
            if (data.getArgs().length > 4) {
                return null;
            }
            ArrayList strings = new ArrayList();
            if (data.getArgs().length == 3) {
                Arrays.stream(NPC.Skin.Part.values()).filter(x -> x.name().toLowerCase().startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            }
            if (data.getArgs().length == 4) {
                Arrays.asList("true", "false").stream().filter(x -> x.startsWith(data.getArgs()[3].toLowerCase())).forEach(x -> strings.add(x));
            }
            return strings;
        });
        SETOWNPLAYERSKIN = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setownplayerskin", "(id) (boolean)", true, false, "Set own player skin to an NPC", "\u00a77This sets if the NPC skin will be as the own player skin who looks at.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false", (command, data) -> {
            if (data.getCommandArgs().length < 1) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            if (!data.getCommandArgs()[0].equalsIgnoreCase("true") && !data.getCommandArgs()[0].equalsIgnoreCase("false")) {
                NPCGlobalCommand.error(data.getCommandSender(), "Boolean can only be true or false.");
                return;
            }
            boolean b = Boolean.valueOf(data.getCommandArgs()[0]);
            if (data.getGlobal().isOwnPlayerSkin() == b) {
                NPCGlobalCommand.error(data.getCommandSender(), "Own Player skin variable was already \u00a7e" + b);
                return;
            }
            data.getGlobal().setOwnPlayerSkin(b);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 own player skin has been set to \u00a7e" + b);
            NPCGlobalCommand.needUpdate(data.getCommandSender(), data.getGlobal(), true);
        }, (command, data) -> {
            if (data.getCommandArgs().length != 1) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.asList("true", "false").stream().filter(x -> x.startsWith(data.getCommandArgs()[0].toLowerCase())).forEach(x -> strings.add(x));
            return strings;
        });
        SETITEM = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setitem", "(id) (slot) [material]", true, false, "Set the equipment of an NPC", "\u00a77This sets the equipment of an NPC. \u00a77No need to set it.\n\u00a7cThis will use the item on your main hand or the variable [material] in case you set it.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(slot) \u00a77The slot of the NPC will have the item.\n\u00a78Slots: \u00a77helmet, chestplate, leggings, boots, mainhand, offhand\n\u00a78\u2022 \u00a7a[material] \u00a77The material of the item (if not, will use your hand).", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            ItemStack itemStack = null;
            if (data.getPlayerSender() != null) {
                itemStack = data.getPlayerSender().getInventory().getItemInMainHand();
            }
            NPC.Slot npcSlot = null;
            try {
                npcSlot = NPC.Slot.valueOf(data.getArgs()[2].toUpperCase());
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (npcSlot == null) {
                NPCGlobalCommand.error(data.getCommandSender(), "Incorrect slot. Use one of the suggested.");
                return;
            }
            if (data.getArgs().length > 3) {
                Material material = null;
                try {
                    material = Material.valueOf((String)data.getArgs()[3].toUpperCase());
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (material == null) {
                    NPCGlobalCommand.error(data.getCommandSender(), "This material is not recognized.");
                    return;
                }
                itemStack = new ItemStack(material);
            }
            data.getGlobal().setItem(npcSlot, itemStack);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77item on slot \u00a7e" + npcSlot.name().toLowerCase() + " \u00a77has been set as \u00a7c" + itemStack.getType().name().toLowerCase().replaceAll("_", " "));
            NPCGlobalCommand.needUpdate(data.getCommandSender(), data.getGlobal(), false);
        }, (command, data) -> {
            if (data.getArgs().length != 3 && data.getArgs().length != 4) {
                return null;
            }
            ArrayList strings = new ArrayList();
            if (data.getArgs().length == 3) {
                Arrays.stream(NPC.Slot.values()).filter(x -> x.name().toLowerCase().startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            }
            if (data.getArgs().length == 4 && data.getArgs()[3].length() >= 1) {
                Arrays.stream(Material.values()).filter(x -> x.name().toLowerCase().startsWith(data.getArgs()[3].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            }
            return strings;
        });
        SETCOLLIDABLE = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setcollidable", "(id) (boolean)", true, false, "Set the collision of an NPC", "\u00a77This sets if the NPC will be collidable or not.\n\u00a77No need to set it. By default will not have collission.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            if (!data.getArgs()[2].equalsIgnoreCase("true") && !data.getArgs()[2].equalsIgnoreCase("false")) {
                NPCGlobalCommand.error(data.getCommandSender(), "Boolean can only be true or false.");
                return;
            }
            boolean bo = Boolean.valueOf(data.getArgs()[2]);
            if (data.getGlobal().isCollidable() == bo) {
                NPCGlobalCommand.error(data.getCommandSender(), "The collidable attribute was \u00a7e" + bo + "\u00a77 yet");
                return;
            }
            data.getGlobal().setCollidable(bo);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77collidable has been set as \u00a7e" + bo);
            NPCGlobalCommand.needUpdate(data.getCommandSender(), data.getGlobal(), true);
        }, (command, data) -> {
            if (data.getArgs().length != 3) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.asList("true", "false").stream().filter(x -> x.startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x));
            return strings;
        });
        SETGLOWCOLOR = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setglowcolor", "(id) (color)", true, false, "Set the glow color of an NPC", "\u00a77This sets the glow color of an NPC.\n\u00a77No need to set it. By default will be white.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(color) \u00a77The name of the color.", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            ChatColor color = null;
            try {
                color = ChatColor.valueOf((String)data.getArgs()[2].toUpperCase());
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (color == null) {
                NPCGlobalCommand.error(data.getCommandSender(), "This color is not valid.");
                return;
            }
            data.getGlobal().setGlowingColor(color);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77glow color has been set as \u00a7f" + color + color.name().toLowerCase());
            NPCGlobalCommand.needUpdate(data.getCommandSender(), data.getGlobal(), true);
        }, (command, data) -> {
            if (data.getArgs().length != 3) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.stream(ChatColor.values()).filter(x -> x.isColor() && x.name().toLowerCase().startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            return strings;
        });
        SETSHOWONTABLIST = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setshowontablist", "(id) (boolean)", true, false, "Set show on tab list of an NPC", "\u00a77This sets if the NPC will be shown on tab list.\n\u00a77No need to set it. By default will not be visible.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            if (!data.getArgs()[2].equalsIgnoreCase("true") && !data.getArgs()[2].equalsIgnoreCase("false")) {
                NPCGlobalCommand.error(data.getCommandSender(), "Boolean can only be true or false.");
                return;
            }
            boolean bo = Boolean.valueOf(data.getArgs()[2]);
            if (data.getGlobal().isShowOnTabList() == bo) {
                NPCGlobalCommand.error(data.getCommandSender(), "Show on tab list attribute was \u00a7e" + bo + "\u00a77 yet");
                return;
            }
            data.getGlobal().setShowOnTabList(bo);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77show on tab list has been set as \u00a7e" + bo);
            NPCGlobalCommand.needUpdate(data.getCommandSender(), data.getGlobal(), true);
        }, (command, data) -> {
            if (data.getArgs().length != 3) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.asList("true", "false").stream().filter(x -> x.startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x));
            return strings;
        });
        SETCUSTOMTABLISTNAME = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setcustomtablistname", "(id) (text)", true, false, "Set custom tab list name of an NPC", "\u00a77This sets the custom tab list name of an NPC.\n\u00a77No need to set it. By default will be \u00a78[NPC] UUID\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(text) \u00a77Name that will show on tab list.", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            try {
                data.getGlobal().setCustomTabListName(data.getArgs()[2].replaceAll("_", " ").replaceAll("&", "\u00a7"));
            }
            catch (Exception e) {
                NPCGlobalCommand.error(data.getCommandSender(), "\u00a7cThis name is not valid. Remember that cannot be larger than 16 characters, and it can't be 2 NPCs with the same custom tab list name.");
                return;
            }
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77custom tab list name has been set to \u00a7e" + data.getGlobal().getCustomTabListName());
            if (!data.getGlobal().isShowOnTabList()) {
                new ClickableText("\u00a77You need to do \u00a7e" + SETSHOWONTABLIST.getCommand(data.getGlobal(), "true") + " \u00a77to show the custom tab list name on the tab list.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, SETSHOWONTABLIST.getCommand(data.getGlobal(), "true")).send(data.getCommandSender());
            } else {
                NPCGlobalCommand.needUpdate(data.getCommandSender(), data.getGlobal(), true);
            }
        }, (command, data) -> null);
        SETLINESPACING = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setlinespacing", "(id) (double/reset)", true, false, "Set line spacing of an NPC", "\u00a77This sets the line spacing of the Hologram of an NPC.\n\u00a77No need to set it. By default will be 0.27\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(double) \u00a77Value of the line spacing for an NPC", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            Double d = null;
            if (MathUtils.isDouble(data.getArgs()[2])) {
                d = Double.parseDouble(data.getArgs()[2]);
            } else if (data.getArgs()[2].equalsIgnoreCase("reset")) {
                d = NPC.Attributes.getDefaultLineSpacing();
            }
            if (d == null) {
                NPCGlobalCommand.error(data.getCommandSender(), "This value is not valid.");
                return;
            }
            data.getGlobal().setLineSpacing(d);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77The NPC \u00a7a" + data.getID() + " \u00a77line spacing has been set as \u00a7e" + data.getGlobal().getLineSpacing());
            NPCGlobalCommand.needUpdateText(data.getCommandSender(), data.getGlobal(), true);
        }, (command, data) -> {
            if (data.getArgs().length != 3) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.asList("reset", "" + data.getGlobal().getLineSpacing()).stream().filter(x -> x.startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x));
            return strings;
        });
        SETTEXTALIGNMENT = new Command((Plugin)PlayerNPCPlugin.getInstance(), "settextalignment", "(id) (vector/reset)", true, false, "Set text alignment of an NPC", "\u00a77This sets the text alignment of the Hologram of an NPC.\n\u00a77No need to set it. By default will be (0.0, 1.75, 0.0)\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(vector) \u00a77The vector added to NPC location.", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            Vector look = null;
            if (MathUtils.isDouble(data.getArgs()[2])) {
                if (data.getArgs().length < 5 || !MathUtils.isDouble(data.getArgs()[3]) || !MathUtils.isDouble(data.getArgs()[4])) {
                    NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                    return;
                }
                Double x = Double.valueOf(data.getArgs()[2]);
                Double y = Double.valueOf(data.getArgs()[3]);
                Double z = Double.valueOf(data.getArgs()[4]);
                look = new Vector(x.doubleValue(), y.doubleValue(), z.doubleValue());
            } else if (data.getArgs()[2].equalsIgnoreCase("reset")) {
                look = NPC.Attributes.getDefaultTextAlignment();
            }
            if (look == null) {
                NPCGlobalCommand.error(data.getCommandSender(), "This value is not valid.");
                return;
            }
            data.getGlobal().setTextAlignment(look);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77The NPC \u00a7a" + data.getID() + " \u00a77text alignment has been set as (\u00a7e" + look.getX() + "\u00a77, \u00a7e" + look.getY() + "\u00a77, \u00a7e" + look.getZ() + "\u00a77)");
            NPCGlobalCommand.needUpdateText(data.getCommandSender(), data.getGlobal(), true);
        }, (command, data) -> {
            if (data.getArgs().length > 5) {
                return null;
            }
            ArrayList<String> strings = new ArrayList<String>();
            if (data.getArgs().length == 3) {
                if ("reset".startsWith(data.getArgs()[2])) {
                    strings.add("reset");
                }
                if (Double.valueOf(data.getGlobal().getTextAlignment().getX()).toString().startsWith(data.getArgs()[2])) {
                    strings.add("" + data.getGlobal().getTextAlignment().getX());
                }
                return strings;
            }
            if (data.getArgs().length == 4 && MathUtils.isDouble(data.getArgs()[2]) && Double.valueOf(data.getGlobal().getTextAlignment().getY()).toString().startsWith(data.getArgs()[3])) {
                strings.add("" + data.getGlobal().getTextAlignment().getY());
            }
            if (data.getArgs().length == 5 && MathUtils.isDouble(data.getArgs()[2]) && MathUtils.isDouble(data.getArgs()[3]) && Double.valueOf(data.getGlobal().getTextAlignment().getZ()).toString().startsWith(data.getArgs()[4])) {
                strings.add("" + data.getGlobal().getTextAlignment().getZ());
            }
            return strings;
        });
        SETTEXTOPACITY = new Command((Plugin)PlayerNPCPlugin.getInstance(), "settextopacity", "(id) (textOpacity)", true, false, "Set text opacity of an NPC", "\u00a77This sets the text opacity of the Hologram of an NPC.\n\u00a77No need to set it. By default will be LOWEST\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(textOpacity) \u00a77Value that can be one of the suggested.\n\u00a78TextOpacity: \u00a77lowest, low, medium, hard, harder, full", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            NPC.Hologram.Opacity textOpacity = null;
            try {
                textOpacity = NPC.Hologram.Opacity.valueOf(data.getArgs()[2].toUpperCase());
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (textOpacity == null) {
                NPCGlobalCommand.error(data.getCommandSender(), "This text opacity type is not valid.");
                return;
            }
            data.getGlobal().setTextOpacity(textOpacity);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77text opacity has been set as \u00a7e" + textOpacity.name().toLowerCase());
            NPCGlobalCommand.needUpdateText(data.getCommandSender(), data.getGlobal(), true);
        }, (command, data) -> {
            if (data.getArgs().length != 3) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.stream(NPC.Hologram.Opacity.values()).filter(x -> x.name().toLowerCase().startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            return strings;
        });
        SETLINEOPACITY = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setlineopacity", "(id) (line) (textOpacity)", true, false, "Set line opacity of an NPC", "\u00a77This sets the text opacity of a line at the Hologram of an NPC.\n\u00a77No need to set it. By default will be LOWEST\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(line) \u00a77The line number that will be affected.\n\u00a78\u2022 \u00a7a(textOpacity) \u00a77Value that can be one of the suggested.\n\u00a78TextOpacity: \u00a77lowest, low, medium, hard, harder, full", (command, data) -> {
            if (data.getArgs().length < 4) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            if (!MathUtils.isInteger(data.getArgs()[2])) {
                NPCGlobalCommand.error(data.getCommandSender(), "This line is not valid.");
                return;
            }
            Integer line = Integer.parseInt(data.getArgs()[2]);
            if (data.getArgs()[3].equalsIgnoreCase("reset")) {
                data.getGlobal().resetLineOpacity(line);
                data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77line opacity for line \u00a7e" + line + " \u00a77has been reset.");
                NPCGlobalCommand.needUpdateText(data.getCommandSender(), data.getGlobal(), true);
                return;
            }
            NPC.Hologram.Opacity textOpacity = null;
            try {
                textOpacity = NPC.Hologram.Opacity.valueOf(data.getArgs()[3].toUpperCase());
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (textOpacity == null) {
                NPCGlobalCommand.error(data.getCommandSender(), "This text opacity type is not valid.");
                return;
            }
            data.getGlobal().setLineOpacity(line, textOpacity);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77line opacity for line \u00a7e" + line + " \u00a77has been set as \u00a7e" + textOpacity.name().toLowerCase());
            NPCGlobalCommand.needUpdateText(data.getCommandSender(), data.getGlobal(), true);
        }, (command, data) -> {
            if (data.getArgs().length > 4) {
                return null;
            }
            ArrayList<String> strings = new ArrayList<String>();
            if (data.getArgs().length == 3) {
                ArrayList<Integer> lines = new ArrayList<Integer>();
                for (int i = 1; i <= data.getGlobal().getText().size(); ++i) {
                    lines.add(i);
                }
                lines.stream().filter(x -> x.toString().startsWith(data.getArgs()[2])).forEach(x -> strings.add(x.toString()));
            }
            if (data.getArgs().length == 4) {
                Arrays.stream(NPC.Hologram.Opacity.values()).filter(x -> x.name().toLowerCase().startsWith(data.getArgs()[3].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
                if ("reset".startsWith(data.getArgs()[3].toLowerCase())) {
                    strings.add("reset");
                }
            }
            return strings;
        });
        SETGAZETRACKINGTYPE = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setgazetrackingtype", "(id) (gazetrackingtype)", true, false, "Set gaze tracking type of an NPC", "\u00a77This sets the NPC gaze tracking type.\n\u00a77No need to set it. By default will be NONE.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(gazetrackingtype) \u00a77Value that can be one of the suggested\n\u00a78GazeTrackingType: \u00a77none, player, nearest_player, nearest_entity", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            NPC.GazeTrackingType gazeTrackingType = null;
            try {
                gazeTrackingType = NPC.GazeTrackingType.valueOf(data.getArgs()[2].toUpperCase());
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (gazeTrackingType == null) {
                NPCGlobalCommand.error(data.getCommandSender(), "This gaze tracking type is not valid.");
                return;
            }
            data.getGlobal().setGazeTrackingType(gazeTrackingType);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77gaze tracking type has been set as \u00a7e" + gazeTrackingType.name().toLowerCase().replaceAll("_", " "));
            NPCGlobalCommand.needUpdate(data.getCommandSender(), data.getGlobal(), false);
        }, (command, data) -> {
            if (data.getArgs().length != 3) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.stream(NPC.GazeTrackingType.values()).filter(x -> x.name().toLowerCase().startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            return strings;
        });
        SETGLOWING = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setglowing", "(id) (boolean)", true, false, "Set glowing of an NPC", "\u00a77This sets whether if the NPC will be glowing or not.\n\u00a77No need to set it. By default will be false.\n\u00a7cIf EntityPlayer is created, you must do force update.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false.", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            Boolean bo = Boolean.valueOf(data.getArgs()[2]);
            if (data.getGlobal().isGlowing() == bo.booleanValue()) {
                NPCGlobalCommand.error(data.getCommandSender(), "The glowing attribute was \u00a7e" + bo + "\u00a77 yet");
                return;
            }
            data.getGlobal().setGlowing(bo);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77glowing has been set as \u00a7e" + bo.toString());
            NPCGlobalCommand.needUpdate(data.getCommandSender(), data.getGlobal(), false);
        }, (command, data) -> {
            if (data.getArgs().length != 3) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.asList("true", "false").stream().filter(x -> x.startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x));
            return strings;
        });
        SETPOSE = new Command((Plugin)PlayerNPCPlugin.getInstance(), "setpose", "(id) (npcpose)", true, false, "Set pose of an NPC", "\u00a77This sets the pose of an NPC\n\u00a77No need to set it. By default will be STANDING.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(npcpose) \u00a77The pose of the NPC.\n\u00a78NPC.Pose: \u00a77standing, gliding, sleeping, swimming, crouching", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            try {
                NPC.Pose npcPose = NPC.Pose.valueOf(data.getArgs()[2].toUpperCase());
                if (data.getGlobal().getPose() == npcPose) {
                    NPCGlobalCommand.error(data.getCommandSender(), "The pose was \u00a7e" + npcPose.name().toLowerCase() + "\u00a77 yet");
                    return;
                }
                if (npcPose.isDeprecated()) {
                    NPCGlobalCommand.error(data.getCommandSender(), "This pose is deprecated, only for developers.");
                    return;
                }
                data.getGlobal().setPose(npcPose);
                data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 pose has been set as \u00a7e" + npcPose.name().toLowerCase());
                NPCGlobalCommand.needUpdate(data.getCommandSender(), data.getGlobal(), false);
            }
            catch (Exception e) {
                NPCGlobalCommand.error(data.getCommandSender(), "This pose is not valid");
            }
        }, (command, data) -> {
            if (data.getArgs().length != 3) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.stream(NPC.Pose.values()).filter(x -> !x.isDeprecated() && x.name().toLowerCase().startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            return strings;
        });
        PLAYANIMATION = new Command((Plugin)PlayerNPCPlugin.getInstance(), "playanimation", "(id) (animation)", true, false, "Play animation to an NPC", "\u00a77Plays an animation to the NPC\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(animation) \u00a77Animation that will be played.\n\u00a78NPC.Animation: \u00a77take_damage, swing_main_arm, swing_off_hand, critical_effect, magical_critical_effect", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            try {
                NPC.Animation animation = NPC.Animation.valueOf(data.getArgs()[2].toUpperCase());
                if (animation.isDeprecated()) {
                    NPCGlobalCommand.error(data.getCommandSender(), "This animation is deprecated, only for developers.");
                    return;
                }
                data.getGlobal().playAnimation(animation);
                data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 played animation \u00a7e" + animation.name().toLowerCase().replaceAll("_", " "));
            }
            catch (Exception e) {
                NPCGlobalCommand.error(data.getCommandSender(), "This animation is not valid");
            }
        }, (command, data) -> {
            if (data.getArgs().length != 3) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.stream(NPC.Animation.values()).filter(x -> !x.isDeprecated() && x.name().toLowerCase().startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            return strings;
        });
        CUSTOMDATA = new Command((Plugin)PlayerNPCPlugin.getInstance(), "customdata", "(id) (get/set/clear) (key) [value]", true, false, "Manage custom data of an NPC", "\u00a77Manage the custom data of an NPC. Custom data is a map of a value assigned to a key.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(get/set/clear) \u00a77Action will be performed.\n\u00a78\u2022 \u00a7a(key) \u00a77Key to find the value.\n\u00a78\u2022 \u00a7a[value] \u00a77Value assigned to the key.", (command, data) -> {
            if (data.getArgs().length < 4) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            String action = data.getCommandArgs()[0].toLowerCase();
            String key = data.getCommandArgs()[1].toLowerCase();
            if (!action.equals("set") && !data.getGlobal().hasCustomData(key)) {
                NPCGlobalCommand.error(data.getCommandSender(), "Custom data do not contain this key.");
                return;
            }
            if (action.equals("get")) {
                NPCGlobalCommand.sendMessage(data.commandSender, "\u00a77Global NPC \u00a7a" + data.global.getCode() + " \u00a77custom data value for key \u00a7a" + key + " \u00a77is: \u00a7e" + data.global.getCustomData(key));
            } else if (action.equals("set")) {
                if (data.getCommandArgs().length < 3) {
                    NPCGlobalCommand.notEnoughArguments(command, data);
                }
                data.getGlobal().setCustomData(key, data.getCommandArgs()[2]);
                NPCGlobalCommand.sendMessage(data.commandSender, "\u00a77Global NPC \u00a7a" + data.global.getCode() + " \u00a77custom data value for key \u00a7a" + key + " \u00a77has been set to: \u00a7e" + data.global.getCustomData(key));
            } else if (action.equals("clear")) {
                data.getGlobal().setCustomData(key, null);
                NPCGlobalCommand.sendMessage(data.commandSender, "\u00a77Global NPC \u00a7a" + data.global.getCode() + " \u00a77custom data value for key \u00a7a" + key + " \u00a77has been cleared.");
            } else {
                NPCGlobalCommand.error(data.commandSender, "This action is not recognized. Use get, set or clear.");
            }
        }, (command, data) -> {
            ArrayList<String> strings = new ArrayList<String>();
            if (data.getCommandArgs().length == 1) {
                Arrays.asList("get", "set", "clear").stream().filter(x -> x.startsWith(data.getCommandArgs()[0].toLowerCase())).forEach(x -> strings.add((String)x));
            }
            if (data.getCommandArgs().length == 2) {
                data.getGlobal().getCustomDataKeys().stream().filter(x -> x.startsWith(data.getCommandArgs()[1].toLowerCase())).forEach(x -> strings.add((String)x));
            }
            if (data.getCommandArgs().length == 3 && data.getCommandArgs()[0].equalsIgnoreCase("set") && data.getGlobal().hasCustomData(data.getCommandArgs()[1]) && data.getGlobal().getCustomData(data.getCommandArgs()[1]).startsWith(data.getCommandArgs()[2])) {
                strings.add(data.getGlobal().getCustomData(data.getCommandArgs()[1]));
            }
            return strings;
        });
        ACTION = new Command((Plugin)PlayerNPCPlugin.getInstance(), "action", "(id) (right/left/both) (add/clear) [type] [value]", true, false, "Manage NPC actions", "\u00a77This sets the NPC actions will be executed when player interacts at it.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(right/left/both) \u00a77Type of player's click\n\u00a78\u2022 \u00a7a(add/clear) \u00a77Add an action, or clear actions\n\u00a78\u2022 \u00a7a[type] \u00a77Type of NPC.Actions\n\u00a78\u2022 \u00a7a[value] \u00a77Value of the action\n\u00a78NPC.Actions: \u00a77console_command, player_command, actionbar_message, title_message, chat_message, teleport_player, connect_server", (command, data) -> {
            if (data.getArgs().length < 4) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            NPC.Interact.ClickType clickType = null;
            if (data.getArgs()[2].equalsIgnoreCase("right")) {
                clickType = NPC.Interact.ClickType.RIGHT_CLICK;
            }
            if (data.getArgs()[2].equalsIgnoreCase("left")) {
                clickType = NPC.Interact.ClickType.LEFT_CLICK;
            }
            if (data.getArgs()[2].equalsIgnoreCase("both")) {
                clickType = NPC.Interact.ClickType.EITHER;
            }
            if (clickType == null) {
                NPCGlobalCommand.error(data.getCommandSender(), "Incorrect argument \"" + data.getArgs()[2] + "\". Click type can only be right, left or both.");
                return;
            }
            if (!data.getArgs()[3].equalsIgnoreCase("add") && !data.getArgs()[3].equalsIgnoreCase("clear")) {
                NPCGlobalCommand.error(data.getCommandSender(), "Incorrect argument \"" + data.getArgs()[3] + "\". Need to specify if add or clear actions.");
                return;
            }
            if (data.getArgs()[3].equalsIgnoreCase("clear")) {
                data.getGlobal().resetClickActions(clickType);
                data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77click actions for \u00a7e" + data.getArgs()[2] + " click \u00a77have been cleaned.");
                return;
            }
            if (data.getArgs().length < 6) {
                NPCGlobalCommand.error(data.getCommandSender(), "Use " + command.getCommand(data.getGlobal()));
                return;
            }
            String type = data.getArgs()[4];
            List<String> types = Arrays.asList("console_command", "player_command", "actionbar_message", "title_message", "chat_message", "teleport_player", "connect_server");
            if (!types.contains(type.toLowerCase())) {
                NPCGlobalCommand.error(data.getCommandSender(), "Incorrect argument \"" + data.getArgs()[4] + "\". Need to specify a valid NPC.Actions");
                return;
            }
            String msg = "";
            for (int i = 5; i < data.getArgs().length; ++i) {
                msg = msg + " " + data.getArgs()[i];
            }
            msg = msg.replaceFirst(" ", "");
            msg = ChatColor.translateAlternateColorCodes((char)'&', msg);
            if (type.equalsIgnoreCase("console_command")) {
                data.getGlobal().addRunConsoleCommandClickAction(clickType, msg);
            }
            if (type.equalsIgnoreCase("player_command")) {
                data.getGlobal().addRunPlayerCommandClickAction(clickType, msg);
            }
            if (type.equalsIgnoreCase("actionbar_message")) {
                data.getGlobal().addActionBarMessageClickAction(clickType, msg);
            }
            if (type.equalsIgnoreCase("chat_message")) {
                data.getGlobal().addMessageClickAction(clickType, new String[]{msg});
            }
            if (type.equalsIgnoreCase("connect_server")) {
                data.getGlobal().addConnectBungeeServerClickAction(clickType, data.getArgs()[5]);
            }
            if (type.equalsIgnoreCase("title_message")) {
                String[] lines = new String[2];
                if (msg.contains("\\n")) {
                    lines = msg.split("\\\\n", 2);
                } else {
                    lines[0] = msg;
                }
                data.getGlobal().addTitleMessageClickAction(clickType, lines[0], lines.length > 1 ? lines[1] : "", 10, 30, 10);
            }
            if (type.equalsIgnoreCase("teleport_player")) {
                Location tp = null;
                if (data.getArgs()[5].equalsIgnoreCase("here")) {
                    if (data.getPlayerSender() != null) {
                        tp = data.getPlayerSender().getLocation();
                    } else {
                        NPCGlobalCommand.error(data.getCommandSender(), "You cannot use 'here' value in console.");
                    }
                } else {
                    World world = Bukkit.getWorld((String)data.getArgs()[5]);
                    Integer x = Integer.valueOf(data.getArgs()[6]);
                    Integer y = Integer.valueOf(data.getArgs()[7]);
                    Integer z = Integer.valueOf(data.getArgs()[8]);
                    tp = new Location(world, (double)x.intValue(), (double)y.intValue(), (double)z.intValue());
                }
                if (tp == null) {
                    NPCGlobalCommand.error(data.getCommandSender(), "We can't find that location.");
                    return;
                }
                data.getGlobal().addTeleportToLocationClickAction(clickType, tp);
            }
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77click action for \u00a7e" + data.getArgs()[2] + " click \u00a77have been added for \u00a7e" + type.replaceAll("_", " ") + " \u00a77as: \u00a7r" + msg);
        }, (command, data) -> {
            ArrayList<String> strings = new ArrayList<String>();
            if (data.getArgs().length == 3) {
                Arrays.asList("right", "left", "both").stream().filter(x -> x.startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add((String)x));
            }
            if (data.getArgs().length == 4) {
                Arrays.asList("add", "clear").stream().filter(x -> x.startsWith(data.getArgs()[3].toLowerCase())).forEach(x -> strings.add((String)x));
            }
            if (data.getArgs().length == 5) {
                if (data.getArgs()[3].equalsIgnoreCase("clear")) {
                    return strings;
                }
                List<String> types = Arrays.asList("console_command", "player_command", "actionbar_message", "title_message", "chat_message", "teleport_player", "connect_server");
                types.stream().filter(x -> x.startsWith(data.getArgs()[4].toLowerCase())).forEach(x -> strings.add((String)x));
            }
            if (data.getArgs().length <= 5) {
                return strings;
            }
            if (data.getArgs()[4].contains("_message") || data.getArgs()[4].contains("_command")) {
                NPC.Placeholders.getAllPlaceholders(data.global).stream().filter(x -> ("{" + x + "}").startsWith(data.getArgs()[data.getArgs().length - 1].toLowerCase())).forEach(x -> strings.add("{" + x + "}"));
            } else if (data.getArgs()[4].equalsIgnoreCase("teleport_player")) {
                if (data.getArgs().length == 6) {
                    Bukkit.getWorlds().stream().filter(x -> x.getName().toLowerCase().startsWith(data.getArgs()[5].toLowerCase())).forEach(x -> strings.add(x.getName().toLowerCase()));
                    if (data.getPlayerSender() != null && "here".startsWith(data.getArgs()[5].toLowerCase())) {
                        strings.add("here");
                    }
                }
                if (data.getArgs().length > 6 && data.getArgs()[5].equalsIgnoreCase("here")) {
                    return strings;
                }
                if (data.getArgs().length == 7) {
                    Arrays.asList("" + (Serializable)(data.getPlayerSender() != null ? Integer.valueOf(data.getPlayerSender().getLocation().getBlockX()) : "0")).stream().filter(x -> x.toLowerCase().startsWith(data.getArgs()[6].toLowerCase())).forEach(x -> strings.add(x.toLowerCase()));
                }
                if (data.getArgs().length == 8) {
                    Arrays.asList("" + (Serializable)(data.getPlayerSender() != null ? Integer.valueOf(data.getPlayerSender().getLocation().getBlockY()) : "0")).stream().filter(x -> x.toLowerCase().startsWith(data.getArgs()[7].toLowerCase())).forEach(x -> strings.add(x.toLowerCase()));
                }
                if (data.getArgs().length == 9) {
                    Arrays.asList("" + (Serializable)(data.getPlayerSender() != null ? Integer.valueOf(data.getPlayerSender().getLocation().getBlockZ()) : "0")).stream().filter(x -> x.toLowerCase().startsWith(data.getArgs()[8].toLowerCase())).forEach(x -> strings.add(x.toLowerCase()));
                }
            }
            return strings;
        });
        TELEPORT = new Command((Plugin)PlayerNPCPlugin.getInstance(), "teleport", "(id) (player/location)", true, false, "Teleport an NPC", "\u00a77This will teleport the NPC to your location.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(player/location) \u00a77The name of the player or location (world, x, y, z)", (command, data) -> {
            if (data.getCommandArgs().length < 1) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            Location look = null;
            Player lookPlayer = null;
            if (data.getCommandArgs().length > 1) {
                World lookWorld = Bukkit.getWorld((String)data.getCommandArgs()[0]);
                if (lookWorld == null) {
                    NPCGlobalCommand.error(data.getCommandSender(), "That world do not exist.");
                    return;
                }
                if (!(data.getCommandArgs().length >= 4 && MathUtils.isDouble(data.getCommandArgs()[1]) && MathUtils.isDouble(data.getCommandArgs()[2]) && MathUtils.isDouble(data.getCommandArgs()[3]))) {
                    NPCGlobalCommand.notEnoughArguments(command, data);
                    return;
                }
                Double x = Double.valueOf(data.getCommandArgs()[1]);
                Double y = Double.valueOf(data.getCommandArgs()[2]);
                Double z = Double.valueOf(data.getCommandArgs()[3]);
                look = new Location(data.getGlobal().getWorld(), x.doubleValue(), y.doubleValue(), z.doubleValue());
            } else {
                lookPlayer = Bukkit.getPlayerExact((String)data.getCommandArgs()[0]);
                if (lookPlayer == null || !lookPlayer.isOnline()) {
                    NPCGlobalCommand.error(data.getCommandSender(), "That player is not online.");
                    return;
                }
                look = lookPlayer.getLocation();
            }
            if (look == null) {
                NPCGlobalCommand.error(data.getCommandSender(), "We can't find that location.");
                return;
            }
            data.getGlobal().teleport(look);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77has been teleported to \u00a7f" + (String)(lookPlayer != null ? lookPlayer.getName() : look.getWorld().getName() + ", " + look.getX() + ", " + look.getY() + ", " + look.getZ()));
        }, (command, data) -> {
            String x2;
            ArrayList<String> strings = new ArrayList<String>();
            if (data.getArgs().length == 3) {
                Bukkit.getOnlinePlayers().stream().filter(x -> x.getName().toLowerCase().startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x.getName()));
                Bukkit.getWorlds().stream().filter(x -> x.getName().toLowerCase().startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x.getName()));
                return strings;
            }
            if (data.getArgs().length == 4 && Bukkit.getWorld((String)data.getArgs()[2]) != null) {
                String string = x2 = data.getPlayerSender() != null ? MathUtils.getSimpleFormat(data.getPlayerSender().getLocation().getX(), 1) : "0.0";
                if (x2.startsWith(data.getArgs()[3])) {
                    strings.add(x2);
                }
            }
            if (data.getArgs().length == 5 && MathUtils.isDouble(data.getArgs()[3])) {
                String string = x2 = data.getPlayerSender() != null ? MathUtils.getSimpleFormat(data.getPlayerSender().getLocation().getY(), 1) : "0.0";
                if (x2.startsWith(data.getArgs()[4])) {
                    strings.add(x2);
                }
            }
            if (data.getArgs().length == 6 && MathUtils.isDouble(data.getArgs()[3]) && MathUtils.isDouble(data.getArgs()[4])) {
                String string = x2 = data.getPlayerSender() != null ? MathUtils.getSimpleFormat(data.getPlayerSender().getLocation().getZ(), 1) : "0.0";
                if (x2.startsWith(data.getArgs()[5])) {
                    strings.add(x2);
                }
            }
            return strings;
        });
        LOOKAT = new Command((Plugin)PlayerNPCPlugin.getInstance(), "lookat", "(id) (player/location)", true, false, "Set look direction of an NPC", "\u00a77This will change the NPC look direction.\n\u00a7cBefore doing this you must create the NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(player/location) \u00a77The name of the player or location (x y z)", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            Location look = null;
            if (MathUtils.isInteger(data.getArgs()[2])) {
                if (data.getArgs().length < 5 || !MathUtils.isInteger(data.getArgs()[3]) || !MathUtils.isInteger(data.getArgs()[4])) {
                    NPCGlobalCommand.notEnoughArguments(command, data);
                    return;
                }
                Integer x = Integer.valueOf(data.getArgs()[2]);
                Integer y = Integer.valueOf(data.getArgs()[3]);
                Integer z = Integer.valueOf(data.getArgs()[4]);
                look = new Location(data.getGlobal().getWorld(), (double)x.intValue(), (double)y.intValue(), (double)z.intValue());
            } else {
                Player lookPlayer = Bukkit.getPlayerExact((String)data.getArgs()[2]);
                if (lookPlayer == null || !lookPlayer.isOnline()) {
                    NPCGlobalCommand.error(data.getCommandSender(), "That player is not online.");
                    return;
                }
                if (!lookPlayer.getWorld().getName().equals(data.getGlobal().getWorld().getName())) {
                    NPCGlobalCommand.error(data.getCommandSender(), "That player is not in the same world as NPC.");
                    return;
                }
                look = lookPlayer.getLocation();
            }
            if (look == null) {
                NPCGlobalCommand.error(data.getCommandSender(), "We can't find that location.");
                return;
            }
            data.getGlobal().lookAt(look);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 look direction has been set.");
            NPCGlobalCommand.needUpdate(data.commandSender, data.getGlobal(), false);
        }, (command, data) -> {
            String x2;
            ArrayList<String> strings = new ArrayList<String>();
            if (data.getArgs().length == 3) {
                String x3;
                data.getGlobal().getWorld().getPlayers().stream().filter(x -> x.getWorld().getName().equals(data.getGlobal().getWorld().getName()) && x.getName().toLowerCase().startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x.getName()));
                String string = x3 = data.getPlayerSender() != null ? "" + data.getPlayerSender().getLocation().getBlockX() : "0";
                if (x3.startsWith(data.getArgs()[2])) {
                    strings.add(x3);
                }
                return strings;
            }
            if (data.getArgs().length == 4 && MathUtils.isInteger(data.getArgs()[2])) {
                String string = x2 = data.getPlayerSender() != null ? "" + data.getPlayerSender().getLocation().getBlockY() : "0";
                if (x2.startsWith(data.getArgs()[3])) {
                    strings.add(x2);
                }
            }
            if (data.getArgs().length == 5 && MathUtils.isInteger(data.getArgs()[2]) && MathUtils.isInteger(data.getArgs()[3])) {
                String string = x2 = data.getPlayerSender() != null ? "" + data.getPlayerSender().getLocation().getBlockZ() : "0";
                if (x2.startsWith(data.getArgs()[4])) {
                    strings.add(x2);
                }
            }
            return strings;
        });
        SHOW = new Command((Plugin)PlayerNPCPlugin.getInstance(), "show", "(id)", true, true, "Show an NPC", "\u00a77This will show the EntityPlayer to the Player.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.", (command, data) -> {
            data.getGlobal().show();
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77has been shown.");
        }, (command, data) -> null);
        HIDE = new Command((Plugin)PlayerNPCPlugin.getInstance(), "hide", "(id)", true, true, "Hide an NPC", "\"\u00a77This will hide the EntityPlayer from the Player.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.", (command, data) -> {
            data.getGlobal().hide();
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77has been hidden.");
        }, (command, data) -> null);
        REMOVE = new Command((Plugin)PlayerNPCPlugin.getInstance(), "remove", "(id)", true, true, "Remove an NPC", "\u00a77This will destroy the NPC object.\n\u00a7cAll the NPC info will be removed.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.", (command, data) -> {
            if (data.getGlobal().isPersistent()) {
                data.getGlobal().setPersistent(false);
            }
            NPCLib.getInstance().removeGlobalNPC(data.getGlobal());
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77 has been removed.");
        }, (command, data) -> null);
        UPDATE = new Command((Plugin)PlayerNPCPlugin.getInstance(), "update", "(id)", true, true, "Update an NPC", "\u00a77This will update the client of the player.\n\u00a7cSome changes will need this to be visible.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.", (command, data) -> {
            data.getGlobal().update();
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 has been updated.");
        }, (command, data) -> null);
        FORCEUPDATE = new Command((Plugin)PlayerNPCPlugin.getInstance(), "forceupdate", "(id)", true, true, "Force update an NPC", "\u00a77This will update the client of the player.\n\u00a7cSome changes will need this to be visible.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.", (command, data) -> {
            data.getGlobal().forceUpdate();
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 has been force updated.");
        }, (command, data) -> null);
        UPDATETEXT = new Command((Plugin)PlayerNPCPlugin.getInstance(), "updatetext", "(id)", true, true, "Update the NPC Text", "\u00a77This will update the text above the NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.", (command, data) -> {
            data.getGlobal().updateText();
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 text has been updated.");
        }, (command, data) -> null);
        FORCEUPDATETEXT = new Command((Plugin)PlayerNPCPlugin.getInstance(), "forceupdatetext", "(id)", true, true, "Force update the NPC Text", "\u00a77This will update the text above the NPC.\n\u00a7cIf the text have different amount of lines you must do this.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.", (command, data) -> {
            data.getGlobal().forceUpdateText();
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 text has been force updated.");
        }, (command, data) -> null);
        LOADPERSISTENT = new Command((Plugin)PlayerNPCPlugin.getInstance(), "loadpersistent", "(id)", true, false, "Load persistent data of an NPC", "\u00a77This will load the persistent data of an NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.", (command, data) -> {
            if (!data.getGlobal().isPersistent()) {
                NPCGlobalCommand.error(data.getCommandSender(), "This NPC is not persistent.");
                return;
            }
            data.getGlobal().getPersistentManager().load();
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 persistent data has been loaded.");
        }, (command, data) -> null);
        SAVEPERSISTENT = new Command((Plugin)PlayerNPCPlugin.getInstance(), "savepersistent", "(id)", true, false, "Save persistent data of an NPC", "\u00a77This will save the persistent data of an NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.", (command, data) -> {
            if (!data.getGlobal().isPersistent()) {
                NPCGlobalCommand.error(data.getCommandSender(), "This NPC is not persistent.");
                return;
            }
            data.getGlobal().getPersistentManager().save();
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + "\u00a77 persistent data has been saved.");
        }, (command, data) -> null);
        DISABLEPERSISTENTSAVE = new Command((Plugin)PlayerNPCPlugin.getInstance(), "disablepersistentsave", "(id) (boolean)", true, false, "Disable persistent data save of an NPC", "\u00a77This sets whether if the NPC Persistent will save or not it's data on the future. Useful if you want to stay the NPC exactly how it's right now.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false.", (command, data) -> {
            if (data.getArgs().length < 3) {
                NPCGlobalCommand.notEnoughArguments(command, data);
                return;
            }
            if (!data.getGlobal().isPersistent()) {
                NPCGlobalCommand.error(data.getCommandSender(), "This NPC is not persistent.");
                return;
            }
            Boolean bo = Boolean.valueOf(data.getArgs()[2]);
            if (((Boolean)data.getGlobal().getPersistentManager().get("disableSaving")).booleanValue() == bo.booleanValue()) {
                NPCGlobalCommand.error(data.getCommandSender(), "Disable saving was \u00a7e" + bo + "\u00a77 yet");
                return;
            }
            data.getGlobal().getPersistentManager().setDisableSaving(bo);
            data.getCommandSender().sendMessage(NPCGlobalCommand.getPrefix() + "\u00a77Global NPC \u00a7a" + data.getID() + " \u00a77disable saving has been set as " + NPCGlobalCommand.b(bo).toLowerCase());
        }, (command, data) -> {
            if (data.getArgs().length != 3) {
                return null;
            }
            ArrayList strings = new ArrayList();
            Arrays.asList("true", "false").stream().filter(x -> x.startsWith(data.getArgs()[2].toLowerCase())).forEach(x -> strings.add(x));
            return strings;
        });
        GOTO = new Command((Plugin)PlayerNPCPlugin.getInstance(), "goto", "(id)", true, false, "Teleport you to an NPC", "\u00a77This will teleport you to the NPC location.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.", (command, data) -> {
            if (data.getPlayerSender() == null) {
                NPCGlobalCommand.error(data.getCommandSender(), "You cannot do this command through console.");
                return;
            }
            data.getPlayerSender().teleport(data.getGlobal().getLocation());
            NPCGlobalCommand.sendMessage((CommandSender)data.getPlayerSender(), "\u00a77You've been teleported to \u00a7a" + data.getID() + " \u00a77Global NPC.");
        }, (command, data) -> null);
        INFO = new Command((Plugin)PlayerNPCPlugin.getInstance(), "info", "(id)", true, true, "Get info of an NPC", "\u00a77This will give you all the information about an NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.", (command, data) -> {
            Player player = data.getPlayerSender();
            if (player == null) {
                NPCGlobalCommand.error(data.commandSender);
            }
            NPC.Global global = data.getGlobal();
            Integer page = 1;
            Integer maxPage = 3;
            if (data.getCommandArgs().length >= 1 && MathUtils.isInteger(data.getCommandArgs()[0])) {
                page = Integer.valueOf(data.getCommandArgs()[0]);
            }
            for (int i = 0; i < 100; ++i) {
                player.sendMessage("");
            }
            player.sendMessage(new String[]{" \u00a7c\u00a7lInformation about the NPC \u00a77(Page " + page + "/" + maxPage + ")", ""});
            if (page == 1) {
                Set<NPC.Global> pluginNPCs = data.npcLib.getAllGlobalNPCs(global.getPlugin());
                int pluginNPCsSize = pluginNPCs.size();
                StringBuilder pluginNPCsBuilder = new StringBuilder("\u00a7e\u00a7lGlobal NPCs managed by " + global.getPlugin().getName() + ": \n");
                pluginNPCs.forEach(x -> pluginNPCsBuilder.append(", \u00a77" + x.getCode()));
                String pluginNPCsHover = pluginNPCsBuilder.toString().replaceFirst(", ", "");
                PluginDescriptionFile pluginDescriptionFile = global.getPlugin().getDescription();
                StringBuilder pluginDescriptionHover = new StringBuilder("\u00a7e\u00a7l" + global.getPlugin().getName());
                pluginDescriptionHover.append("\n\u00a7eMain: \u00a77" + pluginDescriptionFile.getMain());
                List authors = pluginDescriptionFile.getAuthors();
                pluginDescriptionHover.append("\n\u00a7eAuthor: \u00a77" + (authors.size() > 0 ? (String)authors.get(0) : "\u00a7cUnknown"));
                pluginDescriptionHover.append("\n\u00a7eVersion: \u00a77" + pluginDescriptionFile.getVersion());
                String persistentHover = "\u00a7c\u00a7lThis Global NPC is not persistent\n\u00a77This means that when you restart your server, the PlayerNPC plugin will not save the NPC data, and it won't appear, unless your plugin creates it again.\n\n" + (String)(global.canBePersistent() ? "\u00a77In order to make it persistent use:\n\u00a7e" + SETPERSISTENT.getCommand(global, "true") : "\u00a7cYou cannot make it persistent because this NPC is not created by \u00a7lPlayerNPC \u00a7cplugin.");
                if (global.isPersistent()) {
                    String persistentLastUpdate = TimerUtils.getCRCounterSimple(TimerUtils.getBetweenDatesString(global.getPersistentManager().getLastUpdate().getTime(), TimerUtils.getCurrentDate(), TimerUtils.DATE_FORMAT_LARGE, TimeUnit.SECONDS), true);
                    String action = "";
                    if (global.getPersistentManager().getLastUpdate().getType().equals((Object)NPC.Global.PersistentManager.LastUpdate.Type.SAVE)) {
                        action = "Saved";
                    }
                    if (global.getPersistentManager().getLastUpdate().getType().equals((Object)NPC.Global.PersistentManager.LastUpdate.Type.LOAD)) {
                        action = "Loaded";
                    }
                    persistentHover = "\u00a7a\u00a7lThis Global NPC is persistent\n\u00a77This means that the PlayerNPC plugin will store all the NPC data when the server restarts, and will be re-generated.\n\n\u00a77\u00a7o" + action + " " + persistentLastUpdate + " ago." + ((Boolean)global.getPersistentManager().get("disableSaving") != false ? "\n\u00a7cThis NPC has persistent data saving disabled." : "") + "\n\n\u00a77In order to make it not persistent use:\n\u00a7e" + SETPERSISTENT.getCommand(global, "false");
                }
                new ClickableText("\u00a78\u2022 \u00a76\u00a7lID: ").add("\u00a77" + global.getCode(), "\u00a7e\u00a7lNPC Identification\n\u00a7eCode: \u00a77" + global.getCode() + "\n\u00a7eSimple Code: \u00a77" + global.getSimpleCode()).add(" ").add("\u00a7e\u00a7l\u24d8", "\u00a7e\u00a7l\u24d8 Information about NPC ID\n\u00a77The code is used to execute commands to a specific NPC, while the simple code is used by plugins to find the instance of the NPC object.").send(player);
                new ClickableText("\u00a78\u2022 \u00a76\u00a7lType: ").add("\u00a77NPC.Global").add(" ").add(global.isPersistent() ? "\u00a7a(Persistent)" : "\u00a7c(Not Persistent)", persistentHover, ClickEvent.Action.SUGGEST_COMMAND, global.canBePersistent() ? SETPERSISTENT.getCommand(global, "" + !global.isPersistent()) : "").add(" ").add("\u00a7e\u00a7l\u24d8", "\u00a7e\u00a7l\u24d8 Information about NPC Type\n\u00a77This means that this NPC is visible for every player selected, or for everyone, depending on it's visibility. The Global NPC object is not like an NPC itself, but it's like a manager of Personal NPCs of every player.").send(player);
                new ClickableText("\u00a78\u2022 \u00a76\u00a7lPlugin: ").add("\u00a77" + global.getPlugin().getName(), pluginDescriptionHover.toString()).add(" ").add("\u00a7e(" + pluginNPCsSize + " Global NPCs)", pluginNPCsHover).add(" ").add("\u00a7e\u00a7l\u24d8", "\u00a7e\u00a7l\u24d8 Information about NPC Plugin\n\u00a77This plugin is the manager for this NPC, that means that in their code, it has been created this NPC, and it's managed also.").send(player);
                new ClickableText("\u00a78\u2022 \u00a76\u00a7lLocation: ").add("\u00a77" + global.getWorld().getName() + ", " + MathUtils.getFormat(global.getX(), 1) + ", " + MathUtils.getFormat(global.getY(), 1) + ", " + MathUtils.getFormat(global.getZ(), 1) + ", " + MathUtils.getFormat(global.getYaw().floatValue(), 1) + ", " + MathUtils.getFormat(global.getPitch().floatValue(), 1), "\u00a7eClick to teleport", ClickEvent.Action.RUN_COMMAND, "/npcglobal goto " + global.getCode()).send(player);
            }
            if (page == 2) {
                StringBuilder playersHoverBuilder = new StringBuilder("\u00a7eCurrent players (\u00a7a\u00a7lSHOWN\u00a7e/\u00a7c\u00a7lHIDDEN\u00a7e): \n");
                Set<Player> players = global.getPlayers();
                int playersSize = players.size();
                players.forEach(x -> playersHoverBuilder.append("\u00a77, " + (global.getPersonal((Player)x).isShown() ? "\u00a7a" : "\u00a7c") + x.getName()));
                String playersHover = playersHoverBuilder.toString().replaceFirst("\u00a77, ", "");
                String skinName = "Custom texture";
                if (global.getSkin() != null && global.getSkin().getPlayerName() != null) {
                    skinName = global.getSkin().getPlayerName();
                }
                if (global.isOwnPlayerSkin()) {
                    skinName = "Own Player Skin";
                }
                Object hoverSkin = "";
                hoverSkin = global.isOwnPlayerSkin() ? "\u00a7e\u00a7lOwn Player Skin\n\u00a77This means that each player will see their own skin on the NPC." : (global.getSkin().getType().equals((Object)NPC.Skin.Type.CUSTOM_TEXTURE) ? "\u00a7e\u00a7lCustom Texture Skin\n\u00a77This means that this skin does not belong to any identified player." : "\u00a7e\u00a7lSkin information\n\u00a7eName: " + (String)(skinName != null ? "\u00a77" + skinName : "\u00a7cUnknown") + "\n\u00a7eUUID: " + (String)(global.getSkin().getPlayerUUID() != null ? "\u00a77" + global.getSkin().getPlayerUUID() : "\u00a7cUnknown") + "\n\u00a7eObtained from: \u00a77" + global.getSkin().getObtainedFrom().getTitle() + "\n\n\u00a77\u00a7oLast updated " + TimerUtils.getCRCounterSimple(TimerUtils.getBetweenDatesString(global.getSkin().getLastUpdate(), TimerUtils.getCurrentDate(), TimerUtils.DATE_FORMAT_LARGE, TimeUnit.SECONDS), true) + " ago.\n\n\u00a7eClick to see more information.");
                StringBuilder hoverPartsBuilder = new StringBuilder("\u00a7e\u00a7lSkin parts");
                Arrays.stream(NPC.Skin.Part.values()).forEach(x -> hoverPartsBuilder.append("\n\u00a77" + StringUtils.getFirstCharUpperCase(x.name().replaceAll("_", " "), true) + ": " + NPCGlobalCommand.i(global.getSkinParts().isVisible((NPC.Skin.Part)((Object)((Object)x))))));
                StringBuilder hoverCustomDataBuilder = new StringBuilder("\u00a7e\u00a7lCustom data");
                global.getCustomDataKeys().forEach(x -> hoverCustomDataBuilder.append("\n\u00a78\u2022 \u00a7e" + x + ": \u00a77" + global.getCustomData((String)x)));
                new ClickableText("\u00a78\u2022 \u00a76\u00a7lVisibility: ").add("\u00a77" + StringUtils.getFirstCharUpperCase(global.getVisibility().name().replaceAll("_", " "), true) + " ").add("\u00a7e(" + playersSize + " player" + (playersSize != 1 ? "s" : "") + ")", playersSize > 0 ? playersHover : "").add(" ").add("\u00a7e\u00a7l\u24d8", "\u00a7e\u00a7l\u24d8 Information about visibility\n\u00a77The visibility can be for all or for selected players, this means that if it is for all, any person (that meets the visibility requirements) will be able to see the NPC, and if it is for selected players, only those players who have been previously selected will be able to see it.").send(player);
                new ClickableText("\u00a78\u2022 \u00a76\u00a7lVisibility requirement: ").add("\u00a77" + (String)(global.hasVisibilityRequirement() ? (global.hasCustomData("visibilityRequirementPermission") ? "\u00a7e" + global.getCustomData("visibilityRequirementPermission") + " \u00a77permission" : "Custom visibility requirement") : "\u00a7cNone"), (String)(global.hasVisibilityRequirement() && !global.hasCustomData("visibilityRequirementPermission") ? "\u00a7eCustom visibility requirement: \u00a77" + global.getVisibilityRequirement().toString().split("/")[0] : "")).add(" ").add("\u00a7e\u00a7l\u24d8", "\u00a7e\u00a7l\u24d8 Information about visibility requirement\n\u00a77This requirement will be validated in order to add the player to the NPC's visibility. In case the requirement is not met, the player will not be added and therefore will not be able to see the NPC.").send(player);
                new ClickableText("\u00a78\u2022 \u00a76\u00a7lSkin: ").add("\u00a77" + skinName, (String)hoverSkin, global.getSkin().getPlayerName() != null, ClickEvent.Action.RUN_COMMAND, NPCLibCommand.GETSKININFO.getCommand(global.getSkin().getPlayerName())).add(" ").add("\u00a7e(Hover to see skin parts)", hoverPartsBuilder.toString()).add(" ").add("\u00a7e\u00a7l\u24d8", "\u00a7e\u00a7l\u24d8 Information about skin\n\u00a77The skin is the image that is displayed on the body of the NPC. This can be from a specific player, and will be downloaded directly from the Mojang servers, or from the GameProfile if the player is connected, but it can also be a custom texture that does not correspond to any player.").send(player);
                ClickableText customData = new ClickableText("\u00a78\u2022 \u00a76\u00a7lCustom data: ").add(global.getCustomDataKeys().size() > 0 ? "\u00a77Data available" : "\u00a7cEmpty");
                if (global.getCustomDataKeys().size() > 0) {
                    customData.add(" ").add("\u00a7e(Hover to see custom data)", hoverCustomDataBuilder.toString());
                }
                customData.add(" ").add("\u00a7e\u00a7l\u24d8", "\u00a7e\u00a7l\u24d8 Information about custom data\n\u00a77Custom data is a map of a String to a String, this means that plugins will be able to attribute a value to a key, and that it will be stored inside the NPC object.").send(player);
            }
            if (page == 3) {
                new ClickableText("\u00a78\u2022 \u00a76\u00a7lAuto-create: ").add("\u00a77" + NPCGlobalCommand.b(global.isAutoCreate())).add(" ").send(player);
                new ClickableText("\u00a78\u2022 \u00a76\u00a7lAuto-show: ").add("\u00a77" + NPCGlobalCommand.b(global.isAutoShow())).add(" ").send(player);
                List<NPC.Interact.ClickAction> left = global.getClickActions(NPC.Interact.ClickType.LEFT_CLICK);
                List<NPC.Interact.ClickAction> right = global.getClickActions(NPC.Interact.ClickType.RIGHT_CLICK);
                List<NPC.Interact.ClickAction> either = global.getClickActions(NPC.Interact.ClickType.EITHER);
                StringBuilder leftClickActionsBuilder = new StringBuilder("\u00a7e\u00a7lLeft click actions");
                left.forEach(x -> leftClickActionsBuilder.append("\n\u00a78\u2022 \u00a77" + StringUtils.getFirstCharUpperCase(x.getActionType().name().replaceAll("_", " "), true)));
                StringBuilder rightClickActionsBuilder = new StringBuilder("\u00a7e\u00a7lRight click actions");
                right.forEach(x -> rightClickActionsBuilder.append("\n\u00a78\u2022 \u00a77" + StringUtils.getFirstCharUpperCase(x.getActionType().name().replaceAll("_", " "), true)));
                StringBuilder eitherClickActionsBuilder = new StringBuilder("\u00a7e\u00a7lEither click actions");
                either.forEach(x -> eitherClickActionsBuilder.append("\n\u00a78\u2022 \u00a77" + StringUtils.getFirstCharUpperCase(x.getActionType().name().replaceAll("_", " "), true)));
                new ClickableText("\u00a78\u2022 \u00a76\u00a7lClick actions: ").add((left.size() > 0 ? "\u00a7e" : "\u00a7c") + "(Left)", left.size() > 0 ? leftClickActionsBuilder.toString() : "\u00a7c\u00a7lLeft click actions\n\u00a77No action found for this type of click.").add(" ").add((either.size() > 0 ? "\u00a7e" : "\u00a7c") + "(Either)", either.size() > 0 ? eitherClickActionsBuilder.toString() : "\u00a7c\u00a7lEither click actions\n\u00a77No action found for this type of click.").add(" ").add((right.size() > 0 ? "\u00a7e" : "\u00a7c") + "(Right)", right.size() > 0 ? rightClickActionsBuilder.toString() : "\u00a7c\u00a7lRight click actions\n\u00a77No action found for this type of click.").add(" ").send(player);
                StringBuilder textBuilder = new StringBuilder("");
                global.getText().forEach(x -> textBuilder.append("\n" + x));
                textBuilder.replace(0, 1, "");
                new ClickableText("\u00a78\u2022 \u00a76\u00a7lText: ").add((String)(global.getText().size() == 0 ? "\u00a7cNone" : "\u00a77" + global.getText().size() + " lines ")).add(global.getText().size() > 0 ? "\u00a7e(Hover to see the text)" : "", global.getText().size() > 0 ? textBuilder.toString() : "").send(player);
            }
            player.sendMessage("");
            boolean next = page < maxPage;
            boolean previous = page > 1;
            ClickableText clickableText = new ClickableText("  ");
            if (previous) {
                clickableText.add("\u00a7c\u00a7l[PREVIOUS]", "\u00a7eClick to go to the previous page.", ClickEvent.Action.RUN_COMMAND, "/npcglobal info " + global.getCode() + " " + (page - 1));
            } else {
                clickableText.add("\u00a78\u00a7l[PREVIOUS]");
            }
            clickableText.add("    ");
            if (next) {
                clickableText.add("\u00a7a\u00a7l[NEXT]", "\u00a7eClick to go to the next page.", ClickEvent.Action.RUN_COMMAND, "/npcglobal info " + global.getCode() + " " + (page + 1));
            } else {
                clickableText.add("\u00a78\u00a7l[NEXT]");
            }
            clickableText.add("    ");
            clickableText.add("\u00a7c\u00a7l[CLOSE INFO]", "\u00a7eClick to close information.", ClickEvent.Action.RUN_COMMAND, "/npcglobal help");
            clickableText.send(player);
            player.sendMessage("");
        }, (command, data) -> null);
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
            if (NPCGlobalCommand.getCommand(argument) != null) {
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

        public NPCLib.PluginManager getPluginManager() {
            return NPCLib.getInstance().getPluginManager(this.plugin);
        }

        public NPCLib.Command.Color getColor() {
            return this.color == null ? this.getPluginManager().getCommandColor() : this.color;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getCommand() {
            return "/npcglobal " + this.argument + " " + this.arguments;
        }

        public String getCommand(NPC.Global npc) {
            if (npc == null) {
                return this.getCommand();
            }
            String args = this.arguments;
            args = args.replaceAll("\\(id\\)", npc.getCode());
            return "/npcglobal " + this.argument + " " + args;
        }

        public String getCommand(NPC.Global npc, String arguments) {
            return "/npcglobal " + this.argument + " " + npc.getCode() + " " + arguments;
        }
    }

    public class CommandData {
        private final CommandSender commandSender;
        private final String[] args;
        private final String[] cmdArgs;
        private final NPCLib npcLib;
        private NPC.Global global;

        public CommandData(CommandSender commandSender, String[] args, NPCLib npcLib) {
            this.commandSender = commandSender;
            this.args = args;
            this.npcLib = npcLib;
            this.cmdArgs = new String[args.length - 2];
            for (int i = 2; i < args.length; ++i) {
                this.cmdArgs[i - 2] = args[i];
            }
        }

        public String getCommand() {
            if (this.args.length < 1) {
                return null;
            }
            return this.args[0];
        }

        public String getID() {
            if (this.args.length < 2) {
                return null;
            }
            return this.args[1];
        }

        public NPCLib getNpcLib() {
            return this.npcLib;
        }

        public void setGlobal(NPC.Global global) {
            this.global = global;
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

        public NPC.Global getGlobal() {
            return this.global;
        }

        public String[] getArgs() {
            return this.args;
        }

        public String[] getCommandArgs() {
            return this.cmdArgs;
        }
    }
}
