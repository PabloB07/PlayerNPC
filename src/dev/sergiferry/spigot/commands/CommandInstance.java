/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.command.TabCompleter
 */
package dev.sergiferry.spigot.commands;

import dev.sergiferry.spigot.SpigotPlugin;
import dev.sergiferry.spigot.commands.CommandInterface;
import dev.sergiferry.spigot.commands.EmptyTabCompleter;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public abstract class CommandInstance
implements CommandInterface,
CommandExecutor {
    private SpigotPlugin plugin;
    private String commandLabel;

    public CommandInstance(SpigotPlugin plugin, String commandLabel) {
        this.plugin = plugin;
        this.commandLabel = commandLabel.toLowerCase();
        this.getCommand().setExecutor((CommandExecutor)this);
    }

    public String getCommandLabel() {
        return this.commandLabel;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (this.isCommand(label)) {
            this.onExecute(sender, command, label, args);
        }
        return true;
    }

    public boolean isCommand(String s) {
        if ((s = s.toLowerCase()).equals(this.commandLabel) || s.equals(this.getCommand().getPlugin().getName().toLowerCase() + ":" + this.commandLabel)) {
            return true;
        }
        for (String aliases : this.getCommand().getAliases()) {
            String a = aliases.toLowerCase();
            if (s.equals(a)) {
                return true;
            }
            if (!s.equals(this.getCommand().getPlugin().getName().toLowerCase() + ":" + a)) continue;
            return true;
        }
        return false;
    }

    public void setAliases(String ... aliases) {
        this.getCommand().setAliases(Arrays.stream(aliases).toList());
    }

    public void setAliases(List<String> aliases) {
        this.getCommand().setAliases(aliases);
    }

    public PluginCommand getCommand() {
        return this.plugin.getCommand(this.getCommandLabel());
    }

    public SpigotPlugin getPlugin() {
        return this.plugin;
    }

    public CommandInstance setEmptyTabCompleter() {
        this.getCommand().setTabCompleter((TabCompleter)new EmptyTabCompleter(this));
        return this;
    }

    public CommandInstance setTabCompleter(TabCompleter tabCompleter) {
        this.getCommand().setTabCompleter(tabCompleter);
        return this;
    }
}

