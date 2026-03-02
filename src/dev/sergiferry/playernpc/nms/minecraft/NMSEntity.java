package dev.sergiferry.playernpc.nms.minecraft;

import dev.sergiferry.spigot.nms.NMSUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;

import java.lang.reflect.Method;

public class NMSEntity {
    private static Method setCustomNameMethod;
    private static Method setArmorStandMarkerMethod;

    public static void load() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> entityClass = NMSUtils.getMinecraftClass("world.entity.Entity");
        Class<?> armorStandClass = NMSUtils.getMinecraftClass("world.entity.decoration.ArmorStand");
        setCustomNameMethod = entityClass.getMethod("setCustomName", Component.class);
        setArmorStandMarkerMethod = armorStandClass.getDeclaredMethod("setMarker", boolean.class);
        setArmorStandMarkerMethod.setAccessible(true);
    }

    public static void setCustomName(Entity entity, String name) {
        try {
            setCustomNameMethod.invoke(entity, Component.literal(name));
        }
        catch (Exception e) {
            throw new IllegalStateException("Error setting custom name", e);
        }
    }

    public static void setArmorStandMarker(ArmorStand armorStand, boolean marker) {
        try {
            setArmorStandMarkerMethod.invoke(armorStand, marker);
        }
        catch (Exception e) {
            throw new IllegalStateException("Error setting armor stand marker", e);
        }
    }
}
