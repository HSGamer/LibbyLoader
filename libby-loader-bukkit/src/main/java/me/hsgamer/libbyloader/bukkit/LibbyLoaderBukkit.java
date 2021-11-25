package me.hsgamer.libbyloader.bukkit;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.libbyloader.LibbyLoaderAPI;

public class LibbyLoaderBukkit extends BasePlugin {
    @Override
    public void preLoad() {
        MainConfig mainConfig = new MainConfig(this);
        mainConfig.setup();
        BukkitLibraryManagerWrapper manager = new BukkitLibraryManagerWrapper(this);
        manager.setup();
        LibbyLoaderAPI.setWrapper(manager);
    }
}
