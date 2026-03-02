/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 */
package dev.sergiferry.spigot.commands;

import dev.sergiferry.spigot.commands.CommandInstance;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

class EmptyTabCompleter
implements TabCompleter {
    private CommandInstance commandInstance;

    public EmptyTabCompleter(CommandInstance commandInstance) {
        this.commandInstance = commandInstance;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!this.commandInstance.isCommand(label)) {
            return null;
        }
        return new ArrayList<String>();
    }

    public CommandInstance getCommandInstance() {
        return this.commandInstance;
    }
}

