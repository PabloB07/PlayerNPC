package dev.sergiferry.playernpc.api;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Locale;

final class NPCStoragePaths {

    private static final String ROOT_FOLDER = "plugins/PlayerNPC";

    private NPCStoragePaths() {}

    static File configFile(){
        return new File(ROOT_FOLDER + "/config.yml");
    }

    static File globalPersistentFolder(Plugin plugin){
        return new File(ROOT_FOLDER + "/persistent/global/" + normalize(plugin.getName()) + "/");
    }

    static String globalPersistentFolderPath(Plugin plugin, String id){
        return ROOT_FOLDER + "/persistent/global/" + normalize(plugin.getName()) + "/" + id;
    }

    static String skinsFolderPath(){
        return ROOT_FOLDER + "/persistent/skins/";
    }

    private static String normalize(String value){
        return value.toLowerCase(Locale.ROOT);
    }
}
