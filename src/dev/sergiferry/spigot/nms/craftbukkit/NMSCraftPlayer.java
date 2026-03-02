package dev.sergiferry.spigot.nms.craftbukkit;

import dev.sergiferry.spigot.nms.NMSUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class NMSCraftPlayer {
    private static Class<?> craftPlayerClass;
    private static Method craftPlayerGetHandle;
    private static Field playerConnectionField;
    private static Method sendPacketMethod;

    protected static void load() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException {
        craftPlayerClass = NMSUtils.getCraftBukkitClass("entity.CraftPlayer");
        craftPlayerGetHandle = craftPlayerClass.getMethod("getHandle");
        Class<?> handleClass = craftPlayerGetHandle.getReturnType();
        playerConnectionField = Arrays.stream(handleClass.getFields())
                .filter(field -> ServerGamePacketListenerImpl.class.isAssignableFrom(field.getType()))
                .findFirst()
                .orElseThrow(() -> new NoSuchFieldException("Could not resolve ServerPlayer.connection"));
        sendPacketMethod = Arrays.stream(playerConnectionField.getType().getMethods())
                .filter(method -> method.getParameterCount() == 1 && Packet.class.isAssignableFrom(method.getParameterTypes()[0]))
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Could not resolve send packet method"));
    }

    public static ServerGamePacketListenerImpl getPlayerConnection(Player player) {
        try {
            return (ServerGamePacketListenerImpl) playerConnectionField.get(getEntityPlayer(player));
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error at NMSCraftPlayer", e);
        }
    }

    public static ServerPlayer getEntityPlayer(Player player) {
        try {
            return (ServerPlayer) craftPlayerGetHandle.invoke(craftPlayerClass.cast(player));
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error at NMSCraftPlayer", e);
        }
    }

    public static void sendPacket(Player player, Packet<?> packet) {
        try {
            sendPacketMethod.invoke(getPlayerConnection(player), packet);
        }
        catch (Exception e) {
            throw new IllegalStateException("Error sending packet", e);
        }
    }

    public static Class<?> getCraftPlayerClass() {
        return craftPlayerClass;
    }

    public static Method getCraftPlayerGetHandle() {
        return craftPlayerGetHandle;
    }

    public static Field getPlayerConnectionField() {
        return playerConnectionField;
    }

    public static Method getSendPacketMethod() {
        return sendPacketMethod;
    }
}
