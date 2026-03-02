/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  net.md_5.bungee.api.chat.hover.content.Content
 *  net.md_5.bungee.api.chat.hover.content.Text
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package dev.sergiferry.playernpc.command;

import dev.sergiferry.playernpc.PlayerNPCPlugin;
import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import dev.sergiferry.playernpc.utils.ClickableText;
import dev.sergiferry.playernpc.utils.MathUtils;
import dev.sergiferry.spigot.SpigotPlugin;
import dev.sergiferry.spigot.commands.CommandInstance;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class NPCPersonalCommand
extends CommandInstance
implements TabCompleter {
    public NPCPersonalCommand(SpigotPlugin plugin) {
        super(plugin, "npcpersonal");
    }

    @Override
    public void onExecute(CommandSender sender, Command command, String label, String[] args) {
        Player playerSender;
        Player player = playerSender = sender instanceof Player ? (Player)sender : null;
        if (playerSender != null && !this.getPlugin().hasPermission(playerSender, "command")) {
            sender.sendMessage(this.getPrefix() + "You don't have permission to do this.");
            return;
        }
        NPCLib npcLib = NPCLib.getInstance();
        if (args.length == 0) {
            if (playerSender != null) {
                this.sendHelpList(playerSender, 1);
            } else {
                this.error(sender);
            }
            return;
        }
        if (args.length < 3) {
            if (args[0].equals("help")) {
                int asd = 1;
                if (args.length == 2 && MathUtils.isInteger(args[1])) {
                    asd = Integer.valueOf(args[1]);
                }
                if (playerSender != null) {
                    this.sendHelpList(playerSender, asd);
                } else {
                    this.error(sender);
                }
                return;
            }
            NPCCommands commands = NPCCommands.getCommand(args[0]);
            if (commands != null) {
                new ClickableText("\u00a7c\u00a7lError \u00a78| \u00a77Use \u00a7e" + commands.getCommand(), "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, commands.getCommand()).send(sender);
            } else {
                this.error(sender, "Incorrect command. Use \u00a7e/npcpersonal help");
            }
            return;
        }
        Player playerTarget = this.getPlugin().getServer().getPlayerExact(args[1]);
        if (playerTarget == null || !playerTarget.isOnline()) {
            this.error(sender, "This player is not online.");
            return;
        }
        String id = args[2];
        if (args[0].equals(NPCCommands.GENERATE.getArgument())) {
            if (!NPCCommands.GENERATE.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
            if (npcLib.hasPersonalNPC(playerTarget, (Plugin)PlayerNPCPlugin.getInstance(), id)) {
                this.error(sender, "The NPC with id \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 is already generated.");
                return;
            }
            NPC.Personal npc = npcLib.generatePersonalNPC(playerTarget, (Plugin)PlayerNPCPlugin.getInstance(), id, playerSender != null ? playerSender.getLocation() : new Location(playerTarget.getWorld(), 0.0, 0.0, 0.0));
            sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been generated.");
            if (playerSender != null) {
                this.sendNPCProgress(playerSender, npc);
            }
            return;
        }
        NPC.Personal npc = npcLib.getPersonalNPC(playerTarget, id);
        if (npc == null) {
            new ClickableText("\u00a7c\u00a7lError \u00a78| \u00a77This NPC doesn't exist. To generate the NPC use \u00a7e" + NPCCommands.GENERATE.getCommand(), "\u00a7eClick to write the command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.GENERATE.getCommand()).send(sender);
            return;
        }
        if (args[0].equals(NPCCommands.CREATE.getArgument())) {
            if (!NPCCommands.CREATE.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
            if (npc.isCreated()) {
                this.error(sender, "This NPC is already created.");
                return;
            }
            if (npc.getSkin() == null) {
                this.error(sender, "You must set skin before creating.");
                return;
            }
            npc.create();
            sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been created.");
            new ClickableText("\u00a77To show the NPC to the player use \u00a7e" + NPCCommands.SHOW.getCommand(npc), "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.SHOW.getCommand(npc)).send(sender);
            return;
        }
        if (args[0].equals(NPCCommands.SHOW.getArgument())) {
            if (!NPCCommands.SHOW.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
            if (!npc.isCreated()) {
                this.errorCreated(sender, npc);
                return;
            }
            if (npc.isShown()) {
                this.error(sender, "This NPC is already visible.");
                return;
            }
            npc.show();
            sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been shown.");
        } else if (args[0].equals(NPCCommands.HIDE.getArgument())) {
            if (!NPCCommands.HIDE.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
            if (!npc.isCreated()) {
                this.errorCreated(sender, npc);
                return;
            }
            if (!npc.isShown()) {
                this.error(sender, "This NPC is not visible.");
                return;
            }
            npc.hide();
            sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been hidden.");
        } else if (args[0].equals(NPCCommands.UPDATE.getArgument())) {
            if (!NPCCommands.UPDATE.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
            if (!npc.isCreated()) {
                this.errorCreated(sender, npc);
                return;
            }
            npc.update();
            sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been updated.");
        } else if (args[0].equals(NPCCommands.FORCEUPDATE.getArgument())) {
            if (!NPCCommands.FORCEUPDATE.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
            if (!npc.isCreated()) {
                this.errorCreated(sender, npc);
                return;
            }
            npc.forceUpdate();
            sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been force updated.");
        } else if (args[0].equals(NPCCommands.UPDATETEXT.getArgument())) {
            if (!NPCCommands.UPDATETEXT.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
            if (!npc.isCreated()) {
                this.errorCreated(sender, npc);
                return;
            }
            npc.updateText();
            sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been updated text.");
        }
        if (args[0].equals(NPCCommands.FORCEUPDATETEXT.getArgument())) {
            if (!NPCCommands.FORCEUPDATETEXT.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
            if (!npc.isCreated()) {
                this.errorCreated(sender, npc);
                return;
            }
            npc.forceUpdateText();
            sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been force updated text.");
        } else if (args[0].equals(NPCCommands.DESTROY.getArgument())) {
            if (!NPCCommands.DESTROY.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
            if (!npc.isCreated()) {
                this.errorCreated(sender, npc);
                return;
            }
            npc.destroy();
            sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been destroyed.");
        } else if (args[0].equals(NPCCommands.REMOVE.getArgument())) {
            if (!NPCCommands.REMOVE.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
            npcLib.removePersonalNPC(npc);
            sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been removed.");
        } else if (args[0].equals(NPCCommands.TELEPORT.getArgument())) {
            if (!NPCCommands.TELEPORT.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
            if (!npc.isCreated()) {
                this.errorCreated(sender, npc);
                return;
            }
            if (args.length < 4) {
                this.error(sender, "Use " + NPCCommands.TELEPORT.getCommand(npc));
                return;
            }
            Location look = null;
            if (MathUtils.isInteger(args[3])) {
                if (args.length < 6 || !MathUtils.isInteger(args[4]) || !MathUtils.isInteger(args[5])) {
                    this.error(sender, "Use " + NPCCommands.TELEPORT.getCommand(npc));
                    return;
                }
                Integer x = Integer.valueOf(args[3]);
                Integer y = Integer.valueOf(args[4]);
                Integer z = Integer.valueOf(args[5]);
                look = new Location(npc.getWorld(), (double)x.intValue(), (double)y.intValue(), (double)z.intValue());
            } else {
                Player lookPlayer = Bukkit.getPlayerExact((String)args[3]);
                if (lookPlayer == null || !lookPlayer.isOnline()) {
                    this.error(sender, "That player is not online.");
                    return;
                }
                look = lookPlayer.getLocation();
            }
            if (look == null) {
                this.error(sender, "We can't find that location.");
                return;
            }
            npc.teleport(look);
            sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been teleported.");
        } else {
            if (args[0].equals(NPCCommands.LOOKAT.getArgument())) {
                if (!NPCCommands.LOOKAT.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (!npc.isCreated()) {
                    this.errorCreated(sender, npc);
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.LOOKAT.getCommand(npc));
                    return;
                }
                Location look = null;
                if (MathUtils.isInteger(args[3])) {
                    if (args.length < 6 || !MathUtils.isInteger(args[4]) || !MathUtils.isInteger(args[5])) {
                        this.error(sender, "Use " + NPCCommands.LOOKAT.getCommand(npc));
                        return;
                    }
                    Integer x = Integer.valueOf(args[3]);
                    Integer y = Integer.valueOf(args[4]);
                    Integer z = Integer.valueOf(args[5]);
                    look = new Location(npc.getWorld(), (double)x.intValue(), (double)y.intValue(), (double)z.intValue());
                } else {
                    Player lookPlayer = Bukkit.getPlayerExact((String)args[3]);
                    if (lookPlayer == null || !lookPlayer.isOnline()) {
                        this.error(sender, "That player is not online.");
                        return;
                    }
                    if (!lookPlayer.getWorld().getName().equals(npc.getWorld().getName())) {
                        this.error(sender, "That player is not in the same world as NPC.");
                        return;
                    }
                    look = lookPlayer.getLocation();
                }
                if (look == null) {
                    this.error(sender, "We can't find that location.");
                    return;
                }
                npc.lookAt(look);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set look at.");
                new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.UPDATE.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.UPDATE.getCommand(npc)).send(sender);
                return;
            }
            if (args[0].equals(NPCCommands.SETGLOWING.getArgument())) {
                if (!NPCCommands.SETGLOWING.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETGLOWING.getCommand(npc));
                    return;
                }
                Boolean bo = Boolean.valueOf(args[3]);
                if (npc.isGlowing() == bo.booleanValue()) {
                    this.error(sender, "The glowing attribute was \u00a7e" + bo + "\u00a77 yet");
                    return;
                }
                npc.setGlowing(bo);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set glowing as \u00a7e" + bo.toString().toLowerCase());
                if (npc.isCreated()) {
                    new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.UPDATE.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.UPDATE.getCommand(npc)).send(sender);
                }
                return;
            }
            if (args[0].equals(NPCCommands.SETPOSE.getArgument())) {
                if (!NPCCommands.SETPOSE.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETPOSE.getCommand(npc));
                    return;
                }
                try {
                    NPC.Pose npcPose = NPC.Pose.valueOf(args[3].toUpperCase());
                    if (npc.getPose() == npcPose) {
                        this.error(sender, "The pose was \u00a7e" + npcPose.name().toLowerCase() + "\u00a77 yet");
                        return;
                    }
                    if (npcPose.isDeprecated()) {
                        this.error(sender, "This pose is deprecated, only for developers.");
                        return;
                    }
                    npc.setPose(npcPose);
                    sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set pose as \u00a7e" + npcPose.name().toLowerCase());
                    if (npc.isCreated()) {
                        new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.UPDATE.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.UPDATE.getCommand(npc)).send(sender);
                    }
                }
                catch (Exception e) {
                    this.error(sender, "The pose is not valid");
                }
            } else if (args[0].equals(NPCCommands.SETSKIN.getArgument())) {
                if (!NPCCommands.SETSKIN.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETSKIN.getCommand(npc));
                    return;
                }
                sender.sendMessage(this.getPrefix() + "\u00a77Trying to fetch skin of " + args[3]);
                NPC.Skin.fetchSkinAsync(args[3], skin -> {
                    if (skin == null) {
                        this.error(sender, "There was an error trying to fetch that skin.");
                        return;
                    }
                    npc.setSkin((NPC.Skin)skin);
                    sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set skin to " + args[3]);
                    if (npc.isCreated()) {
                        new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.FORCEUPDATE.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.FORCEUPDATE.getCommand(npc)).send(sender);
                        return;
                    }
                    if (playerSender != null) {
                        this.sendNPCProgress(playerSender, npc);
                    }
                });
            } else if (args[0].equals(NPCCommands.SETTEXT.getArgument())) {
                if (!NPCCommands.SETTEXT.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETTEXT.getCommand(npc));
                    return;
                }
                ArrayList<String> list = new ArrayList<String>();
                for (int i = 3; i < args.length; ++i) {
                    list.add(args[i].replaceAll("_", " ").replaceAll("&", "\u00a7"));
                }
                int as = npc.getText().size();
                npc.setText(list);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set Text to \u00a7e" + list.size() + "\u00a77 lines.");
                if (npc.isCreated()) {
                    boolean same = as == npc.getText().size();
                    new ClickableText("\u00a77You need to do \u00a7e" + (same ? NPCCommands.UPDATETEXT.getCommand(npc) : NPCCommands.FORCEUPDATETEXT.getCommand(npc)) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, same ? NPCCommands.UPDATETEXT.getCommand(npc) : NPCCommands.FORCEUPDATETEXT.getCommand(npc)).send(sender);
                    return;
                }
                if (playerSender != null) {
                    this.sendNPCProgress(playerSender, npc);
                }
            } else if (args[0].equals(NPCCommands.SETCUSTOMTABLISTNAME.getArgument())) {
                if (!NPCCommands.SETCUSTOMTABLISTNAME.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETCUSTOMTABLISTNAME.getCommand(npc));
                    return;
                }
                try {
                    npc.setCustomTabListName(args[3].replaceAll("_", " ").replaceAll("&", "\u00a7"));
                }
                catch (Exception e) {
                    playerTarget.sendMessage(this.getPrefix() + "\u00a7cThis name is not valid. Remember that cannot be larger than 16 characters, and it can't be 2 NPCs with the same custom tab list name.");
                    return;
                }
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set custom tab list name to \u00a7e" + npc.getCustomTabListName());
                if (!npc.isShowOnTabList()) {
                    new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.SETSHOWONTABLIST.getCommand(npc, "true") + " \u00a77to show the custom tab list name on the tab list.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.SETSHOWONTABLIST.getCommand(npc, "true")).send(playerTarget);
                } else {
                    if (npc.isCreated()) {
                        new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.FORCEUPDATE.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.FORCEUPDATE.getCommand(npc)).send(sender);
                        return;
                    }
                    if (playerSender != null) {
                        this.sendNPCProgress(playerSender, npc);
                    }
                }
            } else if (args[0].equals(NPCCommands.SETCOLLIDABLE.getArgument())) {
                if (!NPCCommands.SETCOLLIDABLE.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETCOLLIDABLE.getCommand(npc));
                    return;
                }
                Boolean bo = Boolean.valueOf(args[3]);
                if (npc.isCollidable() == bo.booleanValue()) {
                    this.error(sender, "The collidable attribute was \u00a7e" + bo + "\u00a77 yet");
                    return;
                }
                npc.setCollidable(bo);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set collidable as \u00a7e" + bo);
                if (npc.isCreated()) {
                    new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.FORCEUPDATE.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.FORCEUPDATE.getCommand(npc)).send(sender);
                    return;
                }
                if (playerSender != null) {
                    this.sendNPCProgress(playerSender, npc);
                }
            } else if (args[0].equals(NPCCommands.SETGLOWCOLOR.getArgument())) {
                if (!NPCCommands.SETGLOWCOLOR.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETGLOWCOLOR.getCommand(npc));
                    return;
                }
                ChatColor color = null;
                try {
                    color = ChatColor.valueOf((String)args[3].toUpperCase());
                }
                catch (Exception as) {
                    // empty catch block
                }
                if (color == null) {
                    this.error(sender, "This color is not valid.");
                    return;
                }
                npc.setGlowingColor(color);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set glow color as \u00a7f" + color + color.name().toLowerCase());
                if (npc.isCreated()) {
                    new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.FORCEUPDATE.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.FORCEUPDATE.getCommand(npc)).send(sender);
                    return;
                }
                if (playerSender != null) {
                    this.sendNPCProgress(playerSender, npc);
                }
            } else if (args[0].equals(NPCCommands.SETGAZETRACKINGTYPE.getArgument())) {
                if (!NPCCommands.SETGAZETRACKINGTYPE.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETGAZETRACKINGTYPE.getCommand(npc));
                    return;
                }
                NPC.GazeTrackingType followLookType = null;
                try {
                    followLookType = NPC.GazeTrackingType.valueOf(args[3].toUpperCase());
                }
                catch (Exception as) {
                    // empty catch block
                }
                if (followLookType == null) {
                    this.error(sender, "This gaze tracking type is not valid.");
                    return;
                }
                npc.setGazeTrackingType(followLookType);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set gaze tracking type as \u00a7e" + followLookType.name().toLowerCase());
                if (playerSender != null) {
                    this.sendNPCProgress(playerSender, npc);
                }
            } else if (args[0].equals(NPCCommands.SETTEXTOPACITY.getArgument())) {
                if (!NPCCommands.SETTEXTOPACITY.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETTEXTOPACITY.getCommand(npc));
                    return;
                }
                NPC.Hologram.Opacity textOpacity = null;
                try {
                    textOpacity = NPC.Hologram.Opacity.valueOf(args[3].toUpperCase());
                }
                catch (Exception as) {
                    // empty catch block
                }
                if (textOpacity == null) {
                    this.error(sender, "This text opacity type is not valid.");
                    return;
                }
                npc.setTextOpacity(textOpacity);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set text opacity as \u00a7e" + textOpacity.name().toLowerCase());
                if (npc.isCreated()) {
                    new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.FORCEUPDATETEXT.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.FORCEUPDATETEXT.getCommand(npc)).send(sender);
                }
            } else if (args[0].equals(NPCCommands.SETLINEOPACITY.getArgument())) {
                if (!NPCCommands.SETLINEOPACITY.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 5) {
                    this.error(sender, "Use " + NPCCommands.SETLINEOPACITY.getCommand(npc));
                    return;
                }
                if (!MathUtils.isInteger(args[3])) {
                    this.error(sender, "This line is not valid.");
                    return;
                }
                Integer line = Integer.parseInt(args[3]);
                if (args[4].equalsIgnoreCase("reset")) {
                    npc.resetLineOpacity(line);
                    sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been reset line opacity for the line \u00a7e" + line);
                    if (npc.isCreated()) {
                        new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.FORCEUPDATETEXT.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.FORCEUPDATETEXT.getCommand(npc)).send(sender);
                    }
                    return;
                }
                NPC.Hologram.Opacity textOpacity = null;
                try {
                    textOpacity = NPC.Hologram.Opacity.valueOf(args[4].toUpperCase());
                }
                catch (Exception same) {
                    // empty catch block
                }
                if (textOpacity == null) {
                    this.error(sender, "This text opacity type is not valid.");
                    return;
                }
                npc.setLineOpacity(line, textOpacity);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set line opacity as \u00a7e" + textOpacity.name().toLowerCase() + " \u00a77for the line \u00a7e" + line);
                if (npc.isCreated()) {
                    new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.FORCEUPDATETEXT.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.FORCEUPDATETEXT.getCommand(npc)).send(sender);
                }
            } else if (args[0].equals(NPCCommands.SETSHOWONTABLIST.getArgument())) {
                if (!NPCCommands.SETSHOWONTABLIST.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETSHOWONTABLIST.getCommand(npc));
                    return;
                }
                Boolean bo = Boolean.valueOf(args[3]);
                if (npc.isShowOnTabList() == bo.booleanValue()) {
                    this.error(sender, "The show on tab list attribute was \u00a7e" + bo + "\u00a77 yet");
                    return;
                }
                npc.setShowOnTabList(bo);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set show on tab list as \u00a7e" + bo.toString().toLowerCase());
                if (npc.isCreated()) {
                    new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.FORCEUPDATE.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.FORCEUPDATE.getCommand(npc)).send(sender);
                    return;
                }
                this.sendNPCProgress(playerTarget, npc);
            } else if (args[0].equals(NPCCommands.SETTEXTALIGNMENT.getArgument())) {
                if (!NPCCommands.SETTEXTALIGNMENT.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETTEXTALIGNMENT.getCommand(npc));
                    return;
                }
                Vector look = null;
                if (MathUtils.isDouble(args[3])) {
                    if (args.length < 6 || !MathUtils.isDouble(args[4]) || !MathUtils.isDouble(args[5])) {
                        this.error(sender, "Use " + NPCCommands.SETTEXTALIGNMENT.getCommand(npc));
                        return;
                    }
                    Double x = Double.valueOf(args[3]);
                    Double y = Double.valueOf(args[4]);
                    Double z = Double.valueOf(args[5]);
                    look = new Vector(x.doubleValue(), y.doubleValue(), z.doubleValue());
                } else if (args[3].equalsIgnoreCase("reset")) {
                    look = NPC.Attributes.getDefaultTextAlignment();
                }
                if (look == null) {
                    this.error(sender, "This value is not valid.");
                    return;
                }
                npc.setTextAlignment(look);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set text alignment as (\u00a7e" + look.getX() + "\u00a77, \u00a7e" + look.getY() + "\u00a77, \u00a7e" + look.getZ() + "\u00a77)");
                if (npc.isCreated()) {
                    new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.FORCEUPDATETEXT.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.FORCEUPDATETEXT.getCommand(npc)).send(sender);
                }
            } else if (args[0].equals(NPCCommands.SETLINESPACING.getArgument())) {
                if (!NPCCommands.SETLINESPACING.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETLINESPACING.getCommand(npc));
                    return;
                }
                Double d = null;
                if (MathUtils.isDouble(args[3])) {
                    d = Double.parseDouble(args[3]);
                } else if (args[3].equalsIgnoreCase("reset")) {
                    d = NPC.Attributes.getDefaultLineSpacing();
                }
                if (d == null) {
                    this.error(sender, "This value is not valid.");
                    return;
                }
                npc.setLineSpacing(d);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set line spacing as \u00a7e" + npc.getLineSpacing());
                if (npc.isCreated()) {
                    new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.FORCEUPDATETEXT.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.FORCEUPDATETEXT.getCommand(npc)).send(sender);
                }
            } else if (args[0].equals(NPCCommands.SETHIDETEXT.getArgument())) {
                if (!NPCCommands.SETHIDETEXT.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (!npc.isCreated()) {
                    this.errorCreated(sender, npc);
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETHIDETEXT.getCommand(npc));
                    return;
                }
                Boolean bo = Boolean.valueOf(args[3]);
                if (npc.isHiddenText() == bo.booleanValue()) {
                    this.error(sender, "The hide text attribute was " + bo + " yet");
                    return;
                }
                npc.setHideText(bo);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set hide text as \u00a7e" + bo);
            } else if (args[0].equals(NPCCommands.SETITEM.getArgument())) {
                if (!NPCCommands.SETITEM.isEnabled()) {
                    this.error(sender, "This command is not enabled.");
                    return;
                }
                if (args.length < 4) {
                    this.error(sender, "Use " + NPCCommands.SETITEM.getCommand(npc));
                    return;
                }
                ItemStack itemStack = playerTarget.getInventory().getItemInMainHand();
                NPC.Slot npcSlot = null;
                try {
                    npcSlot = NPC.Slot.valueOf(args[3].toUpperCase());
                }
                catch (Exception y) {
                    // empty catch block
                }
                if (npcSlot == null) {
                    this.error(sender, "Incorrect slot. Use one of the suggested.");
                    return;
                }
                if (args.length > 4) {
                    Material material = null;
                    try {
                        material = Material.valueOf((String)args[4]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (material == null) {
                        this.error(sender, "This material is not recognized.");
                        return;
                    }
                    itemStack = new ItemStack(material);
                }
                npc.setItem(NPC.Slot.valueOf(args[3].toUpperCase()), itemStack);
                sender.sendMessage(this.getPrefix() + "\u00a77The NPC \u00a7a" + id + "\u00a77 for the player \u00a7b" + playerTarget.getName() + "\u00a77 has been set item on \u00a7e" + args[3] + "\u00a77 as \u00a7c" + itemStack.getType().name());
                if (npc.isCreated()) {
                    new ClickableText("\u00a77You need to do \u00a7e" + NPCCommands.UPDATE.getCommand(npc) + " \u00a77to show it to the player.", "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.UPDATE.getCommand(npc)).send(sender);
                    return;
                }
                if (playerSender != null) {
                    this.sendNPCProgress(playerSender, npc);
                }
            } else if (args[0].equals(NPCCommands.ACTION.getArgument()) && !NPCCommands.ACTION.isEnabled()) {
                this.error(sender, "This command is not enabled.");
                return;
            }
        }
    }

    private void error(CommandSender sender) {
        this.error(sender, "Invalid command.");
    }

    private void error(CommandSender sender, String text) {
        sender.sendMessage("\u00a7c\u00a7lError \u00a78| \u00a77" + text);
    }

    private void errorCreated(CommandSender sender, NPC.Personal npc) {
        this.error(sender, "This NPC is not created yet.");
        if (sender instanceof Player) {
            new ClickableText("\u00a77To create it use \u00a7e" + NPCCommands.CREATE.getCommand(npc), "\u00a7eClick to write command.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.CREATE.getCommand(npc)).send(sender);
        }
    }

    private String t() {
        return "\u00a7a\u2714 \u00a77";
    }

    private String f() {
        return "\u00a7c\u2718 \u00a77";
    }

    private String n() {
        return "\u00a7e\u2666 \u00a77";
    }

    private String w() {
        return "\u00a7e\u26a0 \u00a7c";
    }

    private String i(boolean b) {
        return b ? this.t() : this.f();
    }

    private String c() {
        return "\u00a7eClick to write this command.";
    }

    private String ts(List<String> lines) {
        String s = "";
        for (String a : lines) {
            s = s + "\u00a77, " + a;
        }
        s = s.replaceFirst("\u00a77, ", "");
        return s;
    }

    private void sendHelpList(Player player, Integer pag) {
        TextComponent componentChatMessage;
        boolean previous;
        int total = NPCCommands.values().length;
        int perpag = 4;
        int maxpag = (total - 1) / perpag + 1;
        if (pag > maxpag) {
            return;
        }
        for (int i = 0; i < 100; ++i) {
            player.sendMessage("");
        }
        player.sendMessage("\u00a7e\u00a7lPersistent Global NPCs are now available through /npcglobal command");
        player.sendMessage("");
        player.sendMessage("\u00a7cThis command is only for experimental purposes, changes will not be saved on the server restart.");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage("");
        player.sendMessage(" \u00a7c\u00a7lPersonal NPC commands \u00a77(Page " + pag + "/" + maxpag + ")");
        player.sendMessage("");
        int aas = 0;
        for (int i = 0; i < perpag && total > i + (pag - 1) * perpag; ++i) {
            int id = i + (pag - 1) * perpag;
            NPCCommands npcCommands = NPCCommands.values()[id];
            new ClickableText("\u00a78\u2022 ").add((String)(npcCommands.isEnabled() ? (npcCommands.isImportant() ? "\u00a76\u00a7l" : "\u00a7e") : "\u00a78" + (npcCommands.isImportant() ? "\u00a7l" : "")) + npcCommands.getDescription(), npcCommands.isEnabled() ? (npcCommands.isImportant() ? "\u00a76" : "\u00a7e") + npcCommands.getCommand() + "\n" + npcCommands.getHover() + "\n\n" + this.c() : null, npcCommands.isEnabled() ? new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, npcCommands.getCommand()) : null).send(player);
            ++aas;
        }
        int ass = perpag - aas;
        for (int i = 0; i < ass; ++i) {
            player.sendMessage("");
        }
        player.sendMessage("");
        boolean next = total > pag * perpag;
        boolean bl = previous = pag > 1;
        if (!next && !previous) {
            return;
        }
        TextComponent baseComponent = new TextComponent("  ");
        if (previous) {
            componentChatMessage = new TextComponent("\u00a7c\u00a7l[PREVIOUS]");
            componentChatMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/npcpersonal help " + (pag - 1)));
            componentChatMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Content[]{new Text("\u00a7eClick to go to the previous page.")}));
            baseComponent.addExtra((BaseComponent)componentChatMessage);
        } else {
            baseComponent.addExtra("\u00a78\u00a7l[PREVIOUS]");
        }
        baseComponent.addExtra("    ");
        if (next) {
            componentChatMessage = new TextComponent("\u00a7a\u00a7l[NEXT]");
            componentChatMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/npcpersonal help " + (pag + 1)));
            componentChatMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Content[]{new Text("\u00a7eClick to go to the next page.")}));
            baseComponent.addExtra((BaseComponent)componentChatMessage);
        } else {
            baseComponent.addExtra("\u00a78\u00a7l[NEXT]");
        }
        player.spigot().sendMessage(ChatMessageType.CHAT, (BaseComponent)baseComponent);
        player.sendMessage("");
    }

    private void sendNPCProgress(Player sender, NPC.Personal npc) {
        Player player = npc.getPlayer();
        String id = npc.getCode();
        if (npc == null) {
            return;
        }
        if (npc.isCreated()) {
            return;
        }
        String equip = "\u00a7cNone";
        HashMap<NPC.Slot, ItemStack> equipment = new HashMap<>();
        Arrays.stream(NPC.Slot.values()).filter(x -> npc.getEquipment(x) != null && !npc.getEquipment(x).getType().equals(Material.AIR)).forEach(x -> equipment.put(x, npc.getEquipment(x)));
        if (!equipment.isEmpty()) {
            equip = "";
            for (NPC.Slot slot : equipment.keySet()) {
                equip = equip + "\n    \u00a78\u2022 \u00a77" + slot.name().substring(0, 1).toUpperCase() + slot.name().substring(1).toLowerCase() + ": \u00a7e" + equipment.get(slot).getType().name();
            }
        }
        NPC.Color color = npc.getGlowingColor();
        String colorName = color.name();
        new ClickableText("\u00a7eHover to see " + player.getName() + "'s " + id + " NPC creation progress.", "\u00a7e" + player.getName() + "'s " + id + " NPC creation progress:\n\n" + this.i(npc.getLocation() != null) + "Location: \u00a7e" + npc.getWorld().getName() + ", x:" + MathUtils.getFormat(npc.getX()) + ", y:" + MathUtils.getFormat(npc.getY()) + ", z:" + MathUtils.getFormat(npc.getZ()) + "\n" + this.n() + "Text: " + (npc.getText().isEmpty() ? "\u00a7cNone" : this.ts(npc.getText())) + "\n" + this.i(npc.getSkin() != null) + "Skin: " + (String)(npc.getSkin() != null ? "\u00a7a" + (npc.getSkin().getPlayerName() != null ? npc.getSkin().getPlayerName() : "Setted") : "\u00a7cNone") + "\n" + this.n() + "Items: " + (String)equip + "\n" + this.n() + "Glow color: \u00a7f" + color + colorName.substring(0, 1) + colorName.substring(1) + "\n" + this.t() + "Collision: " + (npc.isCollidable() ? "\u00a7aTrue" : "\u00a7cFalse") + "\n\n" + (String)(npc.canBeCreated() ? "\u00a7aYou can create it with " + NPCCommands.CREATE.getCommand(npc) : "\u00a7cYou still can't create the NPC.")).send(player);
        if (!npc.canBeCreated()) {
            String remain = "\u00a7cAttributes you must set before creating the NPC\n\u00a78\u2022 \u00a77Skin: \u00a7e" + NPCCommands.SETSKIN.getCommand(npc);
            new ClickableText(this.f() + "\u00a77You must set some attributes before you can create it.", remain).send(sender);
            return;
        }
        new ClickableText(this.t() + "\u00a7aClick here to create the NPC " + id + " for " + player.getName(), "\u00a7eClick to create it.", ClickEvent.Action.SUGGEST_COMMAND, NPCCommands.CREATE.getCommand(npc)).send(sender);
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        Player playerSender;
        if (!this.isCommand(label)) {
            return null;
        }
        ArrayList<String> strings = new ArrayList<String>();
        Player player = playerSender = sender instanceof Player ? (Player)sender : null;
        if (!sender.isOp()) {
            return strings;
        }
        if (args.length == 1) {
            Arrays.asList(NPCCommands.values()).stream().filter(x -> x.getArgument().startsWith(args[0])).forEach(x -> strings.add(x.getArgument()));
            if ("help".startsWith(args[0]) || strings.isEmpty()) {
                strings.add("help");
            }
            return strings;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("help")) {
                int maxpag = (NPCCommands.values().length - 1) / 4 + 1;
                for (int i = 1; i <= maxpag; ++i) {
                    strings.add("" + i);
                }
                return strings;
            }
            Bukkit.getOnlinePlayers().stream().filter(x -> x.getName().toLowerCase().startsWith(args[1].toLowerCase())).forEach(x -> strings.add(x.getName()));
            return strings;
        }
        Player to = this.getPlugin().getServer().getPlayer(args[1]);
        if (to == null) {
            return strings;
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase(NPCCommands.GENERATE.getArgument())) {
                return strings;
            }
            NPCLib.getInstance().getAllPersonalNPCs(to).stream().filter(x -> x.getCode().startsWith(args[2])).forEach(x -> strings.add(x.getCode()));
            return strings;
        }
        if (args.length >= 4) {
            NPC.Personal npc = NPCLib.getInstance().getPersonalNPC(to, args[2]);
            if (npc == null) {
                return strings;
            }
            String arg0 = args[0];
            if ((arg0.equalsIgnoreCase(NPCCommands.SETHIDETEXT.getArgument()) || arg0.equalsIgnoreCase(NPCCommands.SETCOLLIDABLE.getArgument()) || arg0.equalsIgnoreCase(NPCCommands.SETGLOWING.getArgument()) || arg0.equalsIgnoreCase(NPCCommands.SETSHOWONTABLIST.getArgument())) && args.length == 4) {
                Set.of("true", "false").stream().filter(x -> x.toLowerCase().startsWith(args[3].toLowerCase())).forEach(x -> strings.add((String)x));
                return strings;
            }
            if (arg0.equalsIgnoreCase(NPCCommands.SETGLOWCOLOR.getArgument()) && args.length == 4) {
                Arrays.stream(ChatColor.values()).filter(x -> x.isColor() && x.name().toLowerCase().startsWith(args[3].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
                return strings;
            }
            if (arg0.equalsIgnoreCase(NPCCommands.SETSKIN.getArgument()) && args.length == 4) {
                Bukkit.getOnlinePlayers().stream().filter(x -> x.getName().toLowerCase().startsWith(args[3].toLowerCase())).forEach(x -> strings.add(x.getName()));
                return strings;
            }
            if (arg0.equalsIgnoreCase(NPCCommands.SETGAZETRACKINGTYPE.getArgument()) && args.length == 4) {
                Arrays.stream(NPC.GazeTrackingType.values()).filter(x -> x.name().toLowerCase().startsWith(args[3].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            }
            if (arg0.equalsIgnoreCase(NPCCommands.SETTEXTOPACITY.getArgument()) && args.length == 4) {
                Arrays.stream(NPC.Hologram.Opacity.values()).filter(x -> x.name().toLowerCase().startsWith(args[3].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            }
            if (arg0.equalsIgnoreCase(NPCCommands.SETLINEOPACITY.getArgument())) {
                if (args.length == 4) {
                    ArrayList<Integer> lines = new ArrayList<Integer>();
                    for (int i = 1; i <= npc.getText().size(); ++i) {
                        lines.add(i);
                    }
                    lines.stream().filter(x -> x.toString().startsWith(args[3])).forEach(x -> strings.add(x.toString()));
                }
                if (args.length == 5) {
                    Arrays.stream(NPC.Hologram.Opacity.values()).filter(x -> x.name().toLowerCase().startsWith(args[4].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
                    if ("reset".startsWith(args[4].toLowerCase())) {
                        strings.add("reset");
                    }
                }
            }
            if (arg0.equalsIgnoreCase(NPCCommands.SETPOSE.getArgument()) && args.length == 4) {
                Arrays.stream(NPC.Pose.values()).filter(x -> !x.isDeprecated() && x.name().toLowerCase().startsWith(args[3].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
            }
            if (arg0.equalsIgnoreCase(NPCCommands.LOOKAT.getArgument()) || arg0.equalsIgnoreCase(NPCCommands.TELEPORT.getArgument())) {
                String x2;
                if (args.length == 4) {
                    npc.getWorld().getPlayers().stream().filter(x -> x.getName().toLowerCase().startsWith(args[3].toLowerCase())).forEach(x -> strings.add(x.getName()));
                    String string = x2 = playerSender != null ? "" + playerSender.getLocation().getBlockX() : "0";
                    if (x2.startsWith(args[3])) {
                        strings.add(x2);
                    }
                    return strings;
                }
                if (args.length == 5 && MathUtils.isInteger(args[3])) {
                    String string = x2 = playerSender != null ? "" + playerSender.getLocation().getBlockY() : "0";
                    if (x2.startsWith(args[4])) {
                        strings.add(x2);
                    }
                }
                if (args.length == 6 && MathUtils.isInteger(args[3]) && MathUtils.isInteger(args[4])) {
                    String string = x2 = playerSender != null ? "" + playerSender.getLocation().getBlockZ() : "0";
                    if (x2.startsWith(args[5])) {
                        strings.add(x2);
                    }
                }
            }
            if (arg0.equalsIgnoreCase(NPCCommands.SETTEXTALIGNMENT.getArgument())) {
                if (args.length == 4) {
                    if ("reset".startsWith(args[3])) {
                        strings.add("reset");
                    }
                    if (Double.valueOf(npc.getTextAlignment().getX()).toString().startsWith(args[3])) {
                        strings.add("" + npc.getTextAlignment().getX());
                    }
                    return strings;
                }
                if (args.length == 5 && MathUtils.isDouble(args[3]) && Double.valueOf(npc.getTextAlignment().getY()).toString().startsWith(args[4])) {
                    strings.add("" + npc.getTextAlignment().getY());
                }
                if (args.length == 6 && MathUtils.isDouble(args[3]) && MathUtils.isDouble(args[4]) && Double.valueOf(npc.getTextAlignment().getZ()).toString().startsWith(args[5])) {
                    strings.add("" + npc.getTextAlignment().getZ());
                }
            }
            if (arg0.equalsIgnoreCase(NPCCommands.SETITEM.getArgument()) && args.length == 4) {
                Arrays.stream(NPC.Slot.values()).filter(x -> x.name().toLowerCase().startsWith(args[3].toLowerCase())).forEach(x -> strings.add(x.name().toLowerCase()));
                return strings;
            }
        }
        return strings;
    }

    public String getPrefix() {
        return PlayerNPCPlugin.getInstance().getPrefix();
    }

    public static enum NPCCommands {
        GENERATE("generate", "(player) (id)", true, true, "Generate an NPC", "\n\u00a77It generates the NPC object with the id for the player.\n\u00a77This will not create the EntityPlayer or show it to player.\n\u00a7cThis is the first step to spawn an NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID is a custom string you decide."),
        SETTEXT("settext", "(player) (id) (text)...", true, false, "Set the text of an NPC", "\n\u00a77This sets the text above the NPC. No need to set it.\n\u00a77Use \"_\" for the spaces and \" \" for a new line.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(text) \u00a77The text will see above the NPC."),
        SETSKIN("setskin", "(player) (id) (skin)", true, false, "Set the skin of an NPC", "\n\u00a77This sets the NPC skin. \u00a78By default is Steve skin.\n\u00a77You can set both online or offline player's skin.\n\u00a77With the API you can set any skin texture.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(skin) \u00a77The name of the player skin"),
        SETITEM("setitem", "(player) (id) (slot) [material]", true, false, "Set the equipment of an NPC", "\n\u00a77This sets the equipment of NPC. \u00a77No need to set it.\n\u00a7cThis will use the item on your main hand.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(slot) \u00a77The slot of the NPC will have the item.\n\u00a78Slots: \u00a77helmet, chestplate, leggings, boots, mainhand, offhand\n\u00a78\u2022 \u00a7a[material] \u00a77The material of the item (if not will use your hand)."),
        SETCOLLIDABLE("setcollidable", "(player) (id) (boolean)", true, false, "Set the collision of an NPC", "\n\u00a77This sets if the NPC will be collidable or not.\n\u00a77No need to set it. By default will not have collission.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false"),
        SETGLOWCOLOR("setglowcolor", "(player) (id) (color)", true, false, "Set the glow color of an NPC", "\n\u00a77This sets the glow color of an NPC.\n\u00a77No need to set it. By default will be white.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(color) \u00a77The name of the color."),
        SETCUSTOMTABLISTNAME("setcustomtablistname", "(player) (id) (text)", true, false, "Set custom tab list name of an NPC", "\n\u00a77This sets the custom tab list name of an NPC.\n\u00a77No need to set it. By default will be \u00a78[NPC] UUID\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(text) \u00a77Name that will show on tab list."),
        SETSHOWONTABLIST("setshowontablist", "(player) (id) (boolean)", true, false, "Set show on tab list of an NPC", "\n\u00a77This sets if the NPC will be shown on tab list.\n\u00a77No need to set it. By default will not be visible.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false"),
        SETLINESPACING("setlinespacing", "(player) (id) (double/reset)", true, false, "Set line spacing of an NPC", "\n\u00a77This sets the line spacing of the Hologram of an NPC.\n\u00a77No need to set it. By default will be 0.27\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(double) \u00a77Value of the line spacing for an NPC"),
        SETTEXTALIGNMENT("settextalignment", "(player) (id) (vector/reset)", true, false, "Set text alignment of an NPC", "\n\u00a77This sets the text alignment of the Hologram of an NPC.\n\u00a77No need to set it. By default will be (0.0, 1.75, 0.0)\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(vector) \u00a77The vector added to NPC location."),
        SETTEXTOPACITY("settextopacity", "(player) (id) (textopacity)", true, false, "Set text opacity of an NPC", "\n\u00a77This sets the text opacity of the Hologram of an NPC.\n\u00a77No need to set it. By default will be LOWEST\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(textopacity) \u00a77Value that can be one of the suggested.\n\u00a78TextOpacity: \u00a77lowest, low, medium, hard, harder, full"),
        SETLINEOPACITY("setlineopacity", "(player) (id) (line) (textopacity)", true, false, "Set line opacity of an NPC", "\n\u00a77This sets the text opacity of a line at the Hologram of an NPC.\n\u00a77No need to set it. By default will be LOWEST\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(line) \u00a77The line number that will be affected.\n\u00a78\u2022 \u00a7a(textopacity) \u00a77Value that can be one of the suggested.\n\u00a78TextOpacity: \u00a77lowest, low, medium, hard, harder, full"),
        SETGAZETRACKINGTYPE("setgazetrackingtype", "(player) (id) (gazetrackingtype)", true, false, "Set follow look type of an NPC", "\n\u00a77This sets the NPC follow look type.\n\u00a77No need to set it. By default will be NONE.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(gazetrackingtype) \u00a77Value that can be one of the suggested\n\u00a78GazeTrackingType: \u00a77none, player, nearest_player, nearest_entity"),
        ACTION("action", "(player) (id) (righ/left) (add/clear) [type] [value]", false, false, "Manage NPC actions", "\n\u00a77This sets the NPC actions will be executed.\n\u00a77when player interacts at it.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(right/left) \u00a77Type of player's click\n\u00a78\u2022 \u00a7a(add/clear) \u00a77Add an action, or clear actions\n\u00a78\u2022 \u00a7a[type] \u00a77Type of NPC.Actions\n\u00a78\u2022 \u00a7a[value] \u00a77Value of the action\n\u00a78NPC.Actions: \u00a77console_command, player_command"),
        CREATE("create", "(player) (id)", true, true, "Create an NPC", "\n\u00a77This will create the EntityPlayer object, but will not show it\n\u00a77to the player. \u00a7cBefore doing this, you must generate and set the NPC attributes.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation."),
        SHOW("show", "(player) (id)", true, true, "Show an NPC", "\n\u00a77This will show the EntityPlayer to the Player.\n\u00a7cBefore doing this you must create the NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation."),
        TELEPORT("teleport", "(player) (id) (player/location)", true, false, "Teleport an NPC", "\n\u00a77This will teleport the NPC to your location.\n\u00a7cBefore doing this you must create the NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(player/location) \u00a77The name of the player or location (x y z)"),
        LOOKAT("lookat", "(player) (id) (player/location)", true, false, "Set look at of an NPC", "\n\u00a77This will change the NPC look direction.\n\u00a7cBefore doing this you must create the NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(player/location) \u00a77The name of the player or location (x y z)"),
        SETGLOWING("setglowing", "(player) (id) (boolean)", true, false, "Set glowing of an NPC", "\n\u00a77This sets whether if the NPC will be glowing or not.\n\u00a77No need to set it. By default will be false.\n\u00a7cIf EntityPlayer is created, you must do force update.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false."),
        SETPOSE("setpose", "(player) (id) (npcpose)", true, false, "Set pose of an NPC", "\n\u00a77This sets the pose of an NPC\n\u00a77No need to set it. By default will be STANDING.\n\u00a77Poses: standing, swimming, crouching, sleeping\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(npcpose) \u00a77The pose of the NPC.\n\u00a78NPCPose: \u00a77standing, gliding, sleeping, swimming, crouching"),
        HIDE("hide", "(player) (id)", true, true, "Hide an NPC", "\n\u00a77This will hide the EntityPlayer from the Player.\n\u00a7cBefore doing this you must create the NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation."),
        SETHIDETEXT("sethidetext", "(player) (id) (boolean)", true, false, "Hide the NPC Text", "\n\u00a77This will hide or show the text above the NPC.\n\u00a7cBefore doing this you must create the NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.\n\u00a78\u2022 \u00a7a(boolean) \u00a77Value that can be true or false"),
        DESTROY("destroy", "(player) (id)", true, true, "Destroy an NPC", "\n\u00a77This will destroy the EntityPlayer,\n\u00a77but it can be created after.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation."),
        REMOVE("remove", "(player) (id)", true, true, "Remove an NPC", "\n\u00a77This will destroy the NPC object.\n\u00a7cAll the NPC info will be removed.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation."),
        UPDATE("update", "(player) (id)", true, true, "Update an NPC", "\n\u00a77This will update the client of the player.\n\u00a7cSome changes will need this to be visible.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation."),
        FORCEUPDATE("forceupdate", "(player) (id)", true, true, "Force update an NPC", "\n\u00a77This will update the client of the player.\n\u00a7cSome changes will need this to be visible.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation."),
        UPDATETEXT("updatetext", "(player) (id)", true, true, "Update the NPC Text", "\n\u00a77This will update the text above the NPC.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation."),
        FORCEUPDATETEXT("forceupdatetext", "(player) (id)", true, true, "Force update the NPC Text", "\n\u00a77This will update the text above the NPC.\n\u00a7cIf the text have different amount of lines you must do this.\n\n\u00a77Variables:\n\u00a78\u2022 \u00a7a(player) \u00a77The name of the player that will see the NPC\n\u00a78\u2022 \u00a7a(id) \u00a77The ID of the NPC you decided on generation.");

        private String argument;
        private String arguments;
        private boolean important;
        private boolean enabled;
        private String description;
        private String hover;

        private NPCCommands(String argument, String arguments, boolean enabled, boolean important, String description, String hover) {
            this.argument = argument;
            this.arguments = arguments;
            this.enabled = enabled;
            this.important = important;
            this.description = description;
            this.hover = hover;
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

        public String getDescription() {
            return this.description;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public String getHover() {
            return this.hover;
        }

        public String getCommand() {
            return "/npcpersonal " + this.argument + " " + this.arguments;
        }

        public String getCommand(NPC.Personal npc) {
            String args = this.arguments;
            args = args.replaceAll("\\(player\\)", npc.getPlayer().getName()).replaceAll("\\(id\\)", npc.getCode());
            return "/npcpersonal " + this.argument + " " + args;
        }

        public String getCommand(NPC.Personal npc, String arguments) {
            return "/npcpersonal " + this.argument + " " + npc.getPlayer().getName() + " " + npc.getCode() + " " + arguments;
        }

        public static NPCCommands getCommand(String argument) {
            return Arrays.stream(NPCCommands.values()).filter(x -> x.getArgument().equalsIgnoreCase(argument)).findAny().orElse(null);
        }
    }
}
