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
        getExternalRepositories().forEach(libraryManager::addRepository);
        libraryManager.addJCenter();
        libraryManager.addJitPack();
        libraryManager.addMavenCentral();
        libraryManager.addMavenLocal();

        getPreloadLibraries().forEach(libraryManager::loadLibrary);
    }

    protected List<String> getExternalRepositories() {
        return Collections.emptyList();
    }

    protected List<Library> getPreloadLibraries() {
        return Collections.emptyList();
    }

    public void addLibrary(Library library) {
        libraryManager.loadLibrary(library);
    }

    public void addRepository(String repository) {
        libraryManager.addRepository(repository);
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }
}
