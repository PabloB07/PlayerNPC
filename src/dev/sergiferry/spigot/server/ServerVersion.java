/*
 * Decompiled with CFR 0.152.
 */
package dev.sergiferry.spigot.server;

import java.util.Arrays;
import java.util.Comparator;

public enum ServerVersion {
    VERSION_1_17(1, "1.17-R0.1-SNAPSHOT"),
    VERSION_1_17_1(2, "1.17.1-R0.1-SNAPSHOT"),
    VERSION_1_18(3, "1.18-R0.1-SNAPSHOT"),
    VERSION_1_18_1(4, "1.18.1-R0.1-SNAPSHOT"),
    VERSION_1_18_2(5, "1.18.2-R0.1-SNAPSHOT"),
    VERSION_1_19(6, "1.19-R0.1-SNAPSHOT"),
    VERSION_1_19_1(7, "1.19.1-R0.1-SNAPSHOT"),
    VERSION_1_19_2(8, "1.19.2-R0.1-SNAPSHOT"),
    VERSION_1_19_3(9, "1.19.3-R0.1-SNAPSHOT"),
    VERSION_1_19_4(10, "1.19.4-R0.1-SNAPSHOT"),
    VERSION_1_20(11, "1.20-R0.1-SNAPSHOT"),
    VERSION_1_20_1(12, "1.20.1-R0.1-SNAPSHOT"),
    VERSION_1_20_2(13, "1.20.2-R0.1-SNAPSHOT"),
    VERSION_1_20_3(14, "1.20.3-R0.1-SNAPSHOT"),
    VERSION_1_20_4(15, "1.20.4-R0.1-SNAPSHOT"),
    VERSION_1_20_5(16, "1.20.5-R0.1-SNAPSHOT"),
    VERSION_1_20_6(17, "1.20.6-R0.1-SNAPSHOT"),
    VERSION_1_21(18, "1.21-R0.1-SNAPSHOT"),
    VERSION_1_21_1(19, "1.21.1-R0.1-SNAPSHOT"),
    VERSION_1_21_3(20, "1.21.3-R0.1-SNAPSHOT"),
    VERSION_1_21_4(21, "1.21.4-R0.1-SNAPSHOT"),
    VERSION_1_21_5(22, "1.21.5-R0.1-SNAPSHOT"),
    VERSION_1_21_6(23, "1.21.6-R0.1-SNAPSHOT"),
    VERSION_1_21_7(24, "1.21.7-R0.1-SNAPSHOT"),
    VERSION_1_21_8(25, "1.21.8-R0.1-SNAPSHOT"),
    VERSION_1_21_9(26, "1.21.9-R0.1-SNAPSHOT"),
    VERSION_1_21_10(27, "1.21.10-R0.1-SNAPSHOT"),
    VERSION_1_21_11(28, "1.21.11-R0.1-SNAPSHOT");

    private String bukkitVersion;
    private int versionOrder;

    private ServerVersion(Integer versionOrder, String bukkitVersion) {
        this.bukkitVersion = bukkitVersion;
        this.versionOrder = versionOrder;
    }

    public String toString() {
        return this.getBukkitVersion();
    }

    public String getBukkitVersion() {
        return this.bukkitVersion;
    }

    public boolean isNewerThan(ServerVersion version) {
        return this.versionOrder > version.versionOrder;
    }

    public boolean isNewerThanOrEqual(ServerVersion version) {
        return this.versionOrder >= version.versionOrder;
    }

    public boolean isOlderThanOrEqual(ServerVersion version) {
        return this.versionOrder <= version.versionOrder;
    }

    public boolean isOlderThan(ServerVersion version) {
        return this.versionOrder < version.versionOrder;
    }

    public int getOrder() {
        return this.versionOrder;
    }

    public String getMinecraftVersion() {
        return this.bukkitVersion.split("-")[0];
    }

    public static ServerVersion getVersion(String version) {
        ServerVersion exact = Arrays.stream(ServerVersion.values()).filter(x -> x.getBukkitVersion().equals(version)).findAny().orElse(null);
        if (exact != null) {
            return exact;
        }
        String minecraftVersion = version.split("-")[0];
        return Arrays.stream(ServerVersion.values())
                .filter(x -> x.getMinecraftVersion().equals(minecraftVersion))
                .max(Comparator.comparingInt(ServerVersion::getOrder))
                .orElse(null);
    }
}
