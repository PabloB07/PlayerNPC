/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  org.bukkit.inventory.ItemStack
 */
package dev.sergiferry.playernpc.nms.craftbukkit;

import dev.sergiferry.spigot.nms.NMSUtils;
import java.lang.reflect.Method;
import net.minecraft.world.item.ItemStack;

public class NMSCraftItemStack {
    private static Class<?> craftItemStackClass;
    private static Method craftItemStackAsNMSCopy;

    public static void load() throws ClassNotFoundException, NoSuchMethodException {
        craftItemStackClass = NMSUtils.getCraftBukkitClass("inventory.CraftItemStack");
        craftItemStackAsNMSCopy = craftItemStackClass.getDeclaredMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
    }

    public static ItemStack asNMSCopy(org.bukkit.inventory.ItemStack itemStack) {
        try {
            return (ItemStack)NMSCraftItemStack.getCraftItemStackAsNMSCopy().invoke(null, itemStack);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getCraftItemStackClass() {
        return craftItemStackClass;
    }

    public static Method getCraftItemStackAsNMSCopy() {
        return craftItemStackAsNMSCopy;
    }
}

