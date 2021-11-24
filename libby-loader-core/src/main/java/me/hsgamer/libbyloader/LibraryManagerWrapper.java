package me.hsgamer.libbyloader;

import net.byteflux.libby.Library;
import net.byteflux.libby.LibraryManager;

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
        getExternalRepositories().forEach(this::loadRepository);
        getPreloadLibraries().forEach(this::loadLibrary);
    }

    protected List<String> getExternalRepositories() {
        return Collections.emptyList();
    }

    protected List<Library> getPreloadLibraries() {
        return Collections.emptyList();
    }

    private void loadLibrary(Library library) {
        if (LibbyLoaderAPI.isLibraryNotExists(loadedLibraries, library)) {
            libraryManager.loadLibrary(library);
            loadedLibraries.add(library);
        }
    }

    private void loadRepository(String repository) {
        libraryManager.addRepository(repository);
    }

    public void addLibrary(Library... libraries) {
        for (Library library : libraries) {
            this.loadLibrary(library);
        }
    }

    public void addRepository(String... repositories) {
        for (String repository : repositories) {
            this.loadRepository(repository);
        }
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }
}
