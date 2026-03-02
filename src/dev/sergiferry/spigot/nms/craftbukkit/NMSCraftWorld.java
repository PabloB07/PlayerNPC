package dev.sergiferry.spigot.nms.craftbukkit;

import dev.sergiferry.spigot.nms.NMSUtils;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.World;

import java.lang.reflect.Method;

public class NMSCraftWorld {
    private static Class<?> craftWorldClass;
    private static Method craftWorldGetHandle;

    protected static void load() throws ClassNotFoundException, NoSuchMethodException {
        craftWorldClass = NMSUtils.getCraftBukkitClass("CraftWorld");
        craftWorldGetHandle = craftWorldClass.getMethod("getHandle");
    }

    public static ServerLevel getWorldServer(World world) {
        try {
            return (ServerLevel) craftWorldGetHandle.invoke(craftWorldClass.cast(world));
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error at NMSCraftWorld", e);
        }
    }

    public static Class<?> getCraftWorldClass() {
        return craftWorldClass;
    }

    public static Method getCraftWorldGetHandle() {
        return craftWorldGetHandle;
    }
}
