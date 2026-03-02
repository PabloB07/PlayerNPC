package dev.sergiferry.playernpc.nms.minecraft;

import dev.sergiferry.spigot.nms.NMSUtils;
import dev.sergiferry.spigot.nms.craftbukkit.NMSCraftPlayer;
import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;

public class NMSNetworkManager {
    private static Class<?> networkManagerClass;
    private static Field channelField;
    private static Field connectionField;

    public static void load() throws ClassNotFoundException, NoSuchFieldException {
        networkManagerClass = NMSUtils.getMinecraftClass("network.Connection");
        channelField = Arrays.stream(networkManagerClass.getDeclaredFields())
                .filter(field -> Channel.class.isAssignableFrom(field.getType()))
                .findFirst()
                .orElseThrow(() -> new NoSuchFieldException("Could not resolve Connection.channel"));
        channelField.setAccessible(true);
        connectionField = findConnectionField(ServerGamePacketListenerImpl.class);
    }

    public static Connection getNetworkManager(Player player) {
        ServerGamePacketListenerImpl packetListener = NMSCraftPlayer.getPlayerConnection(player);
        try {
            return (Connection) connectionField.get(packetListener);
        }
        catch (Exception e) {
            throw new IllegalStateException("Error resolving network manager", e);
        }
    }

    public static Channel getChannel(Connection connection) {
        try {
            return (Channel) channelField.get(connection);
        }
        catch (Exception e) {
            return null;
        }
    }

    private static Field findConnectionField(Class<?> packetListenerClass) throws NoSuchFieldException {
        Class<?> current = packetListenerClass;
        while (current != null) {
            for (Field field : current.getDeclaredFields()) {
                if (Connection.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    return field;
                }
            }
            current = current.getSuperclass();
        }
        throw new NoSuchFieldException("Could not resolve ServerGamePacketListenerImpl.connection");
    }
}
