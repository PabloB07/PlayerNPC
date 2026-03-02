package dev.sergiferry.playernpc.nms.minecraft;

import com.mojang.authlib.GameProfile;
import dev.sergiferry.spigot.nms.NMSUtils;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class NMSEntityPlayer {
    private static Constructor<?> entityPlayerConstructor;
    private static Method getGameProfile;
    private static Method getId;
    private static Method getEntityData;
    private static Method createDefaultClientInformation;

    public static void load() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> entityPlayerClass = NMSUtils.getMinecraftClass("server.level.ServerPlayer");
        Class<?> entityHumanClass = NMSUtils.getMinecraftClass("world.entity.player.Player");
        Class<?> clientInformationClass = NMSUtils.getMinecraftClass("server.level.ClientInformation");

        getGameProfile = entityHumanClass.getMethod("getGameProfile");
        getId = entityPlayerClass.getMethod("getId");
        getEntityData = entityPlayerClass.getMethod("getEntityData");
        createDefaultClientInformation = clientInformationClass.getMethod("createDefault");
        entityPlayerConstructor = entityPlayerClass.getConstructor(
                MinecraftServer.class,
                ServerLevel.class,
                GameProfile.class,
                clientInformationClass
        );
    }

    public static ServerPlayer newEntityPlayer(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile) {
        try {
            Object clientInformation = createDefaultClientInformation.invoke(null);
            return (ServerPlayer) entityPlayerConstructor.newInstance(minecraftServer, serverLevel, gameProfile, clientInformation);
        }
        catch (Exception e) {
            throw new IllegalStateException("Error creating ServerPlayer", e);
        }
    }

    public static GameProfile getGameProfile(ServerPlayer player) {
        try {
            return (GameProfile) getGameProfile.invoke(player);
        }
        catch (Exception e) {
            throw new IllegalStateException("Error reading GameProfile", e);
        }
    }

    public static Integer getEntityID(ServerPlayer entityPlayer) {
        try {
            return (Integer) getId.invoke(entityPlayer);
        }
        catch (Exception e) {
            throw new IllegalStateException("Error reading entity id", e);
        }
    }

    public static SynchedEntityData getDataWatcher(ServerPlayer entityPlayer) {
        try {
            return (SynchedEntityData) getEntityData.invoke(entityPlayer);
        }
        catch (Exception e) {
            throw new IllegalStateException("Error reading entity data", e);
        }
    }
}
