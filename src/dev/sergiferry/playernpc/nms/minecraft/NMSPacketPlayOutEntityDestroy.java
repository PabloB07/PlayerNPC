package dev.sergiferry.playernpc.nms.minecraft;

import dev.sergiferry.spigot.nms.NMSUtils;
import net.minecraft.network.protocol.Packet;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class NMSPacketPlayOutEntityDestroy {
    private static Constructor<?> constructor;
    private static boolean usesIntArray;

    public static void load() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> packetClass = resolvePacketClass();
        constructor = Arrays.stream(packetClass.getConstructors())
                .filter(x -> x.getParameterCount() == 1)
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Could not resolve destroy packet constructor for " + packetClass.getName()));
        Class<?> parameter = constructor.getParameterTypes()[0];
        usesIntArray = parameter.equals(int[].class);
    }

    @SuppressWarnings("unchecked")
    public static Packet<?> createPacket(int id) {
        try {
            Object argument = usesIntArray ? new int[]{id} : id;
            return (Packet<?>) constructor.newInstance(argument);
        }
        catch (Exception e) {
            throw new IllegalStateException("Error creating entity destroy packet", e);
        }
    }

    public static boolean isSingleConstructor() {
        return !usesIntArray;
    }

    private static Class<?> resolvePacketClass() throws ClassNotFoundException {
        try {
            return NMSUtils.getMinecraftClass("network.protocol.game.PacketPlayOutEntityDestroy");
        }
        catch (ClassNotFoundException ignored) {}
        return NMSUtils.getMinecraftClass("network.protocol.game.ClientboundRemoveEntitiesPacket");
    }
}
