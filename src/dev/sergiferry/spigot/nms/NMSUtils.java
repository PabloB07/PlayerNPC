/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package dev.sergiferry.spigot.nms;

import dev.sergiferry.spigot.nms.craftbukkit.NMSCraftPlayer;
import dev.sergiferry.spigot.nms.craftbukkit.NMSCraftServer;
import dev.sergiferry.spigot.nms.craftbukkit.NMSCraftWorld;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;

public class NMSUtils {
    private static String version;

    public static void load() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String[] split = packageName.split("\\.");
        version = split.length > 3 ? split[3] : "";
        try {
            NMSUtils.loadNMS(NMSCraftPlayer.class);
            NMSUtils.loadNMS(NMSCraftWorld.class);
            NMSUtils.loadNMS(NMSCraftServer.class);
        }
        catch (Exception e) {
            throw new IllegalStateException("This NMS version (" + version + ") is not supported.");
        }
    }

    public static Class<?> getCraftBukkitClass(String nmsClassString) throws ClassNotFoundException {
        return NMSUtils.getNMSClass("org.bukkit.craftbukkit", nmsClassString);
    }

    public static Class<?> getMinecraftClass(String nmsClassString) throws ClassNotFoundException {
        return Class.forName("net.minecraft." + nmsClassString);
    }

    public static Class<?> getClass(String nmsClassString) throws ClassNotFoundException {
        return Class.forName(nmsClassString);
    }

    public static Class<?> getNMSClass(String prefix, String nmsClassString) throws ClassNotFoundException {
        if (version != null && !version.isBlank()) {
            String name = prefix + "." + version + "." + nmsClassString;
            try {
                return Class.forName(name);
            }
            catch (ClassNotFoundException ignored) {}
        }
        return Class.forName(prefix + "." + nmsClassString);
    }

    public static void loadNMS(Class<?> c) {
        try {
            Method method = c.getDeclaredMethod("load");
            method.setAccessible(true);
            method.invoke(null);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Error loading NMS " + c.getName());
        }
    }

    public static String getVersion() {
        return version;
    }

    public static Object getValue(Object instance, String name) {
        return NMSUtils.getValue(instance, name, false);
    }

    public static Object getValue(Object instance, String name, boolean printError) {
        Object result;
        block2: {
            result = null;
            try {
                Field field = instance.getClass().getDeclaredField(name);
                field.setAccessible(true);
                result = field.get(instance);
                field.setAccessible(false);
            }
            catch (Exception e) {
                if (!printError) break block2;
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void setValue(Object instance, String name, Object value) {
        NMSUtils.setValue(instance, name, value, false);
    }

    public static void setValue(Object obj, String name, Object value, boolean printError) {
        block2: {
            try {
                Field field = obj.getClass().getDeclaredField(name);
                field.setAccessible(true);
                field.set(obj, value);
            }
            catch (Exception e) {
                if (!printError) break block2;
                e.printStackTrace();
            }
        }
    }
}
