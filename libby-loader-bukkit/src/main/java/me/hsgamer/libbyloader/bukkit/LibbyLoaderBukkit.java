package me.hsgamer.libbyloader.bukkit;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.libbyloader.LibbyLoaderAPI;

public class LibbyLoaderBukkit extends BasePlugin {
    private MainConfig mainConfig;
    private BukkitLibraryManagerWrapper manager;

    @Override
    public void preLoad() {
        mainConfig = new MainConfig(this);
        manager = new BukkitLibraryManagerWrapper(this);
        mainConfig.setup();
        manager.setup();
        LibbyLoaderAPI.setManager(manager);
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public BukkitLibraryManagerWrapper getManager() {
        return manager;
    }
}
