package dev.sergiferry.playernpc.nms.minecraft;

import dev.sergiferry.spigot.SpigotPlugin;
import dev.sergiferry.spigot.nms.NMSUtils;
import dev.sergiferry.spigot.nms.craftbukkit.NMSCraftPlayer;
import dev.sergiferry.spigot.server.ServerVersion;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class NMSPacketPlayOutSpawnEntity {
    private static Constructor<?> constructor;
    private static boolean modernEntityPacket;

    public static void load() throws ClassNotFoundException, NoSuchMethodException {
        ServerVersion serverVersion = SpigotPlugin.getServerVersion();
        Class<?> packetClass = serverVersion != null && serverVersion.isOlderThanOrEqual(ServerVersion.VERSION_1_18_2)
                ? NMSUtils.getMinecraftClass("network.protocol.game.PacketPlayOutSpawnEntityLiving")
                : resolveModernPacketClass();

        constructor = Arrays.stream(packetClass.getConstructors())
                .filter(x -> x.getParameterCount() == 3
                        && Entity.class.isAssignableFrom(x.getParameterTypes()[0])
                        && x.getParameterTypes()[1].equals(int.class)
                        && x.getParameterTypes()[2].equals(BlockPos.class))
                .findFirst()
                .orElse(null);
        if(constructor != null){
            modernEntityPacket = true;
            return;
        }

        Class<?> entityClass = resolveLivingEntityClass();
        constructor = packetClass.getConstructor(entityClass);
        modernEntityPacket = false;
    }

    @SuppressWarnings("unchecked")
    public static void sendPacket(Player player, Object livingEntity) {
        try {
            Packet<?> packet;
            if(modernEntityPacket){
                Entity entity = (Entity) livingEntity;
                packet = (Packet<?>) constructor.newInstance(
                        entity,
                        0,
                        BlockPos.containing(entity.getX(), entity.getY(), entity.getZ())
                );
            } else {
                packet = (Packet<?>) constructor.newInstance(livingEntity);
            }
            NMSCraftPlayer.sendPacket(player, packet);
        }
        catch (Exception e) {
            throw new IllegalStateException("Error sending spawn entity packet", e);
        }
    }

    private static Class<?> resolveLivingEntityClass() throws ClassNotFoundException {
        try {
            return NMSUtils.getMinecraftClass("world.entity.EntityLiving");
        }
        catch (ClassNotFoundException ignored) {}
        return NMSUtils.getMinecraftClass("world.entity.LivingEntity");
    }

    private static Class<?> resolveModernPacketClass() throws ClassNotFoundException {
        try {
            return NMSUtils.getMinecraftClass("network.protocol.game.PacketPlayOutSpawnEntity");
        }
        catch (ClassNotFoundException ignored) {}
        return NMSUtils.getMinecraftClass("network.protocol.game.ClientboundAddEntityPacket");
    }
}
