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
    public void addRepository(String repository) {
        super.addRepository(repository);
        List<String> repositories = MainConfig.EXTERNAL_REPOSITORIES.getValue();
        if (!repositories.contains(repository)) {
            repositories.add(repository);
        }
        MainConfig.EXTERNAL_REPOSITORIES.setValue(repositories);
        MainConfig.EXTERNAL_REPOSITORIES.getConfig().save();
    }
}
