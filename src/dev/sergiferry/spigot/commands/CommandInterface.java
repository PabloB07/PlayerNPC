/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package dev.sergiferry.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

interface CommandInterface {
    public void onExecute(CommandSender var1, Command var2, String var3, String[] var4);
}

