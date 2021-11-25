package me.hsgamer.libbyloader;

import net.byteflux.libby.Library;
import net.byteflux.libby.LibraryManager;
import net.byteflux.libby.classloader.IsolatedClassLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibraryManagerWrapper {
    private final List<Library> loadedLibraries = new ArrayList<>();
    private final LibraryManager libraryManager;

    public LibraryManagerWrapper(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
    }

    public void setup() {
        getExternalRepositories().forEach(this::addRepository);
        getPreloadLibraries().forEach(this::addLibrary);
    }

    protected List<String> getExternalRepositories() {
        return Collections.emptyList();
    }

    protected List<Library> getPreloadLibraries() {
        return Collections.emptyList();
    }

    private void addLibrary(Library library) {
        if (LibbyLoaderAPI.isLibraryNotExists(loadedLibraries, library)) {
            libraryManager.loadLibrary(library);
            loadedLibraries.add(library);
        }
    }

    private void addRepository(String repository) {
        libraryManager.addRepository(repository);
    }

    public void loadLibrary(Library... libraries) {
        for (Library library : libraries) {
            this.addLibrary(library);
        }
    }

    public void loadRepository(String... repositories) {
        for (String repository : repositories) {
            this.addRepository(repository);
        }
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

    public List<Library> getLoadedLibraries() {
        return loadedLibraries;
    }

    public IsolatedClassLoader getIsolatedClassLoader(String id) {
        return libraryManager.getIsolatedClassLoaderOf(id);
    }
}
