/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package dev.sergiferry.spigot;

import dev.sergiferry.spigot.metrics.Metrics;
import dev.sergiferry.spigot.nms.NMSUtils;
import dev.sergiferry.spigot.server.ServerVersion;
import dev.sergiferry.spigot.updater.UpdateChecker;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SpigotPlugin
extends JavaPlugin
implements Listener {
    public static ServerVersion serverVersion;
    private String prefix;
    private int spigotResourceID;
    private int bStatsResourceID;
    private String lastSpigotVersion;
    private Metrics metrics;
    private List<ServerVersion> supportedVersions;

    public SpigotPlugin(int spigotResourceID, ServerVersion ... serverVersion) {
        this.spigotResourceID = spigotResourceID;
        this.supportedVersions = new ArrayList<ServerVersion>();
        this.supportedVersions.addAll(List.of(serverVersion));
        this.prefix = "\u00a7b\u00a7l" + this.getDescription().getName() + " \u00a78| \u00a77";
        if (SpigotPlugin.serverVersion == null) {
            SpigotPlugin.serverVersion = ServerVersion.getVersion(Bukkit.getBukkitVersion());
            this.getLogger().log(Level.INFO, "Your server is using Bukkit version: " + Bukkit.getBukkitVersion());
        }
    }

    public abstract void enable();

    public abstract void disable();

    public void onEnable() {
        boolean unsafeByPass = false;
        String minecraftVersion = Bukkit.getBukkitVersion().split("-")[0];
        if (!this.supportedVersions.contains((Object)serverVersion)) {
            Object vs = "";
            for (ServerVersion serverVersion : this.supportedVersions) {
                vs = (String)vs + ", " + serverVersion.getMinecraftVersion();
                if (!serverVersion.getMinecraftVersion().equals(minecraftVersion)) continue;
                unsafeByPass = true;
            }
            if (!unsafeByPass) {
                vs = ((String)vs).replaceFirst(", ", "");
                Bukkit.getConsoleSender().sendMessage("\u00a7cThis bukkit version (" + Bukkit.getBukkitVersion() + ") is not supported by this plugin (" + this.getDescription().getName() + " v" + this.getDescription().getVersion() + ")");
                Bukkit.getConsoleSender().sendMessage("\u00a7cSupported bukkit versions: " + (String)vs);
                this.getServer().getPluginManager().disablePlugin((Plugin)this);
                return;
            }
        }
        if (unsafeByPass) {
            Bukkit.getConsoleSender().sendMessage("\u00a7eThis bukkit compilation (" + Bukkit.getBukkitVersion() + ") is not supported, but the Minecraft Server version (" + minecraftVersion + ") is supported in another compilation.");
            Bukkit.getConsoleSender().sendMessage("\u00a7eEnabling " + this.getDescription().getName() + " v" + this.getDescription().getVersion() + " anyways, errors may appear.");
        }
        try {
            NMSUtils.load();
            this.enable();
            this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
            new UpdateChecker(this).getLatestVersion(version -> {
                if (this.getDescription().getVersion().equalsIgnoreCase((String)version)) {
                    return;
                }
                Integer higher = this.isHigherVersion(this.getDescription().getVersion(), (String)version);
                if (higher != -1) {
                    return;
                }
                this.lastSpigotVersion = version;
                this.getServer().getConsoleSender().sendMessage("\u00a7e" + this.getDescription().getName() + " version " + version + " is available (currently running " + this.getDescription().getVersion() + ").\u00a77\n\u00a7ePlease download it at " + this.getSpigotResource());
            });
            Bukkit.getConsoleSender().sendMessage(this.getPrefix() + "The plugin has been enabled successfully");
            Bukkit.getConsoleSender().sendMessage(this.getPrefix() + "Plugin created by \u00a76SergiFerry");
        }
        catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("\u00a7cThere was an error enabling the plugin " + this.getDescription().getName() + " v" + this.getDescription().getVersion());
            e.printStackTrace();
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
        }
    }

    public void onDisable() {
        try {
            this.disable();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().isOp()) {
            return;
        }
        if (this.getLastSpigotVersion() == null) {
            return;
        }
        Bukkit.getScheduler().runTaskLater((Plugin)this, () -> event.getPlayer().sendMessage(this.getPrefix() + "\u00a77" + this.getDescription().getName() + " version \u00a7e" + this.getLastSpigotVersion() + "\u00a77 is available (currently running " + this.getDescription().getVersion() + "). Please download it at: \u00a7e" + this.getSpigotResource()), 20L);
    }

    public int isHigherVersion(String version1, String version2) {
        String[] string1 = version1.split("\\.");
        String[] string2 = version2.split("\\.");
        int length = Math.max(string1.length, string2.length);
        for (int i = 0; i < length; ++i) {
            Integer v1 = i < string1.length ? Integer.parseInt(string1[i]) : 0;
            Integer v2 = i < string2.length ? Integer.parseInt(string2[i]) : 0;
            if (v1 > v2) {
                return 1;
            }
            if (v1 >= v2) continue;
            return -1;
        }
        return 0;
    }

    public void setupMetrics(int bStatsResourceID) {
        this.bStatsResourceID = bStatsResourceID;
        this.metrics = new Metrics(this, bStatsResourceID);
    }

    public boolean hasPermission(Player player, String s) {
        return player.isOp() || player.hasPermission(this.getDescription().getName().toLowerCase() + ".*") || s != null && player.hasPermission(this.getDescription().getName().toLowerCase() + "." + s);
    }

    public Metrics getMetrics() {
        return this.metrics;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSpigotResource() {
        return "https://www.spigotmc.org/resources/" + this.getDescription().getName().toLowerCase() + "." + this.spigotResourceID + "/";
    }

    public String getLastSpigotVersion() {
        return this.lastSpigotVersion;
    }

    public static ServerVersion getServerVersion() {
        return serverVersion;
    }

    public List<ServerVersion> getSupportedVersions() {
        return this.supportedVersions;
    }

    public int getSpigotResourceID() {
        return this.spigotResourceID;
    }
}

