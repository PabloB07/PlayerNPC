/*
 * Decompiled with CFR 0.152.
 */
package dev.sergiferry.playernpc.nms.craftbukkit;

import dev.sergiferry.spigot.nms.NMSUtils;
import java.lang.reflect.Method;

public class NMSCraftScoreboard {
    private static Class<?> craftScoreBoardClass;
    private static Method craftScoreBoardGetHandle;

    public static void load() throws ClassNotFoundException, NoSuchMethodException {
        craftScoreBoardClass = NMSUtils.getCraftBukkitClass("scoreboard.CraftScoreboard");
        craftScoreBoardGetHandle = craftScoreBoardClass.getMethod("getHandle", new Class[0]);
    }

    public static Class<?> getCraftScoreBoardClass() {
        return craftScoreBoardClass;
    }

    public static Method getCraftScoreBoardGetHandle() {
        return craftScoreBoardGetHandle;
    }
}

