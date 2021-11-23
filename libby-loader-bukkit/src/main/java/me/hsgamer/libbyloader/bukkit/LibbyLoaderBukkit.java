package me.hsgamer.libbyloader.bukkit;

import me.hsgamer.libbyloader.api.LibbyLoaderAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class LibbyLoaderBukkit extends JavaPlugin {
    private final MainConfig mainConfig = new MainConfig(this);
    private final BukkitLibraryManagerWrapper manager = new BukkitLibraryManagerWrapper(this);

    public LibbyLoaderBukkit() {
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
