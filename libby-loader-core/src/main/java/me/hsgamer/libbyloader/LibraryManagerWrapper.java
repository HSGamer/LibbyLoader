package me.hsgamer.libbyloader;

import net.byteflux.libby.Library;
import net.byteflux.libby.LibraryManager;

import java.util.Collections;
import java.util.List;

public class LibraryManagerWrapper {
    private final LibraryManager libraryManager;

    public LibraryManagerWrapper(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
    }

    public void setup() {
        libraryManager.addMavenLocal();
        libraryManager.addMavenCentral();
        libraryManager.addJCenter();
        libraryManager.addJitPack();
        getExternalRepositories().forEach(libraryManager::addRepository);
        getPreloadLibraries().forEach(libraryManager::loadLibrary);
    }

    protected List<String> getExternalRepositories() {
        return Collections.emptyList();
    }

    protected List<Library> getPreloadLibraries() {
        return Collections.emptyList();
    }

    public void addLibrary(Library... libraries) {
        List<Library> preloadLibraries = getPreloadLibraries();
        for (Library library : libraries) {
            if (LibbyLoaderAPI.isLibraryNotExists(preloadLibraries, library)) {
                libraryManager.loadLibrary(library);
            }
        }
    }

    public void addRepository(String... repositories) {
        for (String repository : repositories) {
            libraryManager.addRepository(repository);
        }
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }
}
