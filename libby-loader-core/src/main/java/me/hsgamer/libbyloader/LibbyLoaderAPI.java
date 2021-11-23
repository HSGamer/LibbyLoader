package me.hsgamer.libbyloader;

public final class LibbyLoaderAPI {
    private static LibraryManagerWrapper manager;

    private LibbyLoaderAPI() {
        // EMPTY
    }

    public static LibraryManagerWrapper getManager() {
        return manager;
    }

    public static void setManager(LibraryManagerWrapper manager) {
        LibbyLoaderAPI.manager = manager;
    }
}
