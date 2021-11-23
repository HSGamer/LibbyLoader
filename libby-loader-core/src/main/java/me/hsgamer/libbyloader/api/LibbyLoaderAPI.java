package me.hsgamer.libbyloader.api;

import me.hsgamer.libbyloader.LibraryManagerWrapper;

public class LibbyLoaderAPI {
    private static LibraryManagerWrapper manager;

    public static LibraryManagerWrapper getManager() {
        return manager;
    }

    public static void setManager(LibraryManagerWrapper manager) {
        LibbyLoaderAPI.manager = manager;
    }
}
