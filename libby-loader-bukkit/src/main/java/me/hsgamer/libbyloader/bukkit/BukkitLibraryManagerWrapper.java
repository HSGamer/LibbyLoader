package me.hsgamer.libbyloader.bukkit;

import me.hsgamer.libbyloader.LibraryManagerWrapper;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class BukkitLibraryManagerWrapper extends LibraryManagerWrapper {
    public BukkitLibraryManagerWrapper(Plugin plugin) {
        super(new BukkitLibraryManager(plugin));
    }

    @Override
    protected List<String> getExternalRepositories() {
        return MainConfig.EXTERNAL_REPOSITORIES.getValue();
    }

    @Override
    protected List<Library> getPreloadLibraries() {
        return MainConfig.PRELOAD_LIBRARIES.getValue();
    }

    @Override
    public void addLibrary(Library... libraries) {
        super.addLibrary(libraries);
        if (MainConfig.ADD_LIBRARIES_TO_CONFIG.getValue()) {
            MainConfig.addLibrary(libraries);
        }
    }

    @Override
    public void addRepository(String... repositories) {
        super.addRepository(repositories);
        if (MainConfig.ADD_REPOSITORIES_TO_CONFIG.getValue()) {
            MainConfig.addRepository(repositories);
        }
    }
}
