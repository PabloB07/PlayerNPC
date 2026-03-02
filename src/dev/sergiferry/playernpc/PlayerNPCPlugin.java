/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package dev.sergiferry.playernpc;

import dev.sergiferry.playernpc.api.NPCLib;
import dev.sergiferry.playernpc.command.NPCGlobalCommand;
import dev.sergiferry.playernpc.command.NPCLibCommand;
import dev.sergiferry.playernpc.command.NPCPersonalCommand;
import dev.sergiferry.playernpc.nms.craftbukkit.NMSCraftItemStack;
import dev.sergiferry.playernpc.nms.craftbukkit.NMSCraftScoreboard;
import dev.sergiferry.playernpc.nms.minecraft.NMSEntity;
import dev.sergiferry.playernpc.nms.minecraft.NMSEntityPlayer;
import dev.sergiferry.playernpc.nms.minecraft.NMSNetworkManager;
import dev.sergiferry.playernpc.nms.minecraft.NMSPacketPlayOutEntityDestroy;
import dev.sergiferry.playernpc.nms.minecraft.NMSPacketPlayOutSpawnEntity;
import dev.sergiferry.spigot.SpigotPlugin;
import dev.sergiferry.spigot.metrics.Metrics;
import dev.sergiferry.spigot.nms.NMSUtils;
import dev.sergiferry.spigot.server.ServerVersion;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerNPCPlugin
extends SpigotPlugin {
    private static PlayerNPCPlugin instance;
    private NPCLib npcLib;
    private NPCGlobalCommand npcGlobalCommand;
    private NPCPersonalCommand npcPersonalCommand;
    private NPCLibCommand npcLibCommand;

    public PlayerNPCPlugin() {
        super(
                93625,
                ServerVersion.VERSION_1_18,
                ServerVersion.VERSION_1_18_1,
                ServerVersion.VERSION_1_18_2,
                ServerVersion.VERSION_1_19,
                ServerVersion.VERSION_1_19_1,
                ServerVersion.VERSION_1_19_2,
                ServerVersion.VERSION_1_19_3,
                ServerVersion.VERSION_1_19_4,
                ServerVersion.VERSION_1_20,
                ServerVersion.VERSION_1_20_1,
                ServerVersion.VERSION_1_20_2,
                ServerVersion.VERSION_1_20_3,
                ServerVersion.VERSION_1_20_4,
                ServerVersion.VERSION_1_20_5,
                ServerVersion.VERSION_1_20_6,
                ServerVersion.VERSION_1_21,
                ServerVersion.VERSION_1_21_1,
                ServerVersion.VERSION_1_21_3,
                ServerVersion.VERSION_1_21_4,
                ServerVersion.VERSION_1_21_5,
                ServerVersion.VERSION_1_21_6,
                ServerVersion.VERSION_1_21_7,
                ServerVersion.VERSION_1_21_8,
                ServerVersion.VERSION_1_21_9,
                ServerVersion.VERSION_1_21_10,
                ServerVersion.VERSION_1_21_11
        );
        instance = this;
    }

    @Override
    public void enable() {
        NMSUtils.loadNMS(NMSEntityPlayer.class);
        NMSUtils.loadNMS(NMSEntity.class);
        NMSUtils.loadNMS(NMSCraftItemStack.class);
        NMSUtils.loadNMS(NMSCraftScoreboard.class);
        NMSUtils.loadNMS(NMSPacketPlayOutEntityDestroy.class);
        NMSUtils.loadNMS(NMSPacketPlayOutSpawnEntity.class);
        NMSUtils.loadNMS(NMSNetworkManager.class);
        this.setPrefix("\u00a76\u00a7lPlayerNPC \u00a78| \u00a77");
        try {
            Constructor constructor = NPCLib.class.getDeclaredConstructor(PlayerNPCPlugin.class);
            constructor.setAccessible(true);
            this.npcLib = (NPCLib)constructor.newInstance(new Object[]{this});
            constructor.setAccessible(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.npcGlobalCommand = new NPCGlobalCommand(this);
        this.npcPersonalCommand = new NPCPersonalCommand(this);
        this.npcLibCommand = new NPCLibCommand(this);
        this.callPrivate("onEnable");
        this.setupMetrics(11918);
        super.getMetrics().addCustomChart(new Metrics.SingleLineChart("npcs", () -> {
            HashSet npcSet = new HashSet();
            this.getServer().getOnlinePlayers().forEach(x -> npcSet.addAll(this.npcLib.getAllPersonalNPCs((Player)x)));
            return npcSet.size();
        }));
        super.getMetrics().addCustomChart(new Metrics.SingleLineChart("global_npcs", () -> this.npcLib.getAllGlobalNPCs().size()));
        super.getMetrics().addCustomChart(new Metrics.SingleLineChart("personal_npcs", () -> {
            HashSet npcSet = new HashSet();
            for (Player player : this.getServer().getOnlinePlayers()) {
                this.npcLib.getAllPersonalNPCs(player).stream().filter(x -> !x.hasGlobal()).forEach(x -> npcSet.add(x));
            }
            return npcSet.size();
        }));
        super.getMetrics().addCustomChart(new Metrics.AdvancedPie("dependents", () -> {
            HashMap valueMap = new HashMap();
            this.npcLib.getRegisteredPlugins().stream().filter(x -> !x.equals((Object)this)).forEach(x -> valueMap.put(x.getName(), 1));
            return valueMap;
        }));
    }

    @Override
    public void disable() {
        this.callPrivate("onDisable");
    }

    private void callPrivate(String m) {
        try {
            Method method = NPCLib.class.getDeclaredMethod(m, PlayerNPCPlugin.class);
            method.setAccessible(true);
            method.invoke((Object)this.npcLib, new Object[]{this});
            method.setAccessible(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NPCLib getNPCLib() {
        return this.npcLib;
    }

    public NPCGlobalCommand getNPCGlobalCommand() {
        return this.npcGlobalCommand;
    }

    public NPCPersonalCommand getNPCPersonalCommand() {
        return this.npcPersonalCommand;
    }

    public static PlayerNPCPlugin getInstance() {
        return instance;
    }

    public static void sendConsoleMessage(boolean prefix, String message) {
        Bukkit.getConsoleSender().sendMessage((prefix ? PlayerNPCPlugin.getInstance().getPrefix() : "") + message);
    }

    public static void sendConsoleMessage(String message) {
        PlayerNPCPlugin.sendConsoleMessage(true, message);
    }
}
