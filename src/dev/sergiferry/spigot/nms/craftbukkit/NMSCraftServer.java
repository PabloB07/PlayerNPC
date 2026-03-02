/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.MinecraftServer
 *  org.bukkit.Bukkit
 *  org.bukkit.Server
 */
package dev.sergiferry.spigot.nms.craftbukkit;

import dev.sergiferry.spigot.nms.NMSUtils;
import java.lang.reflect.Method;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public class NMSCraftServer {
    private static Class<?> craftServerClass;
    private static Method craftServerGetServer;

    protected static void load() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException {
        craftServerClass = NMSUtils.getCraftBukkitClass("CraftServer");
        craftServerGetServer = craftServerClass.getMethod("getServer", new Class[0]);
    }

    public static Class<?> getCraftServerClass() {
        return craftServerClass;
    }

    public static Method getCraftServerGetServer() {
        return craftServerGetServer;
    }

    public static MinecraftServer getMinecraftServer(Server server) {
        try {
            return (MinecraftServer)NMSCraftServer.getCraftServerGetServer().invoke(NMSCraftServer.getCraftServerClass().cast(server), new Object[0]);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error at NMSCraftServer");
        }
    }

    public static MinecraftServer getMinecraftServer() {
        return NMSCraftServer.getMinecraftServer(Bukkit.getServer());
    }
}

