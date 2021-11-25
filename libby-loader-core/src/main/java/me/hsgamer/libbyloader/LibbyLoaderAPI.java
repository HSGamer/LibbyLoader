package me.hsgamer.libbyloader;

import net.byteflux.libby.Library;
import net.byteflux.libby.LibraryManager;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The API for library loader & manager
 */
public final class LibbyLoaderAPI {
    private static final Pattern COORDINATE_PATTERN = Pattern.compile("([^: ]+):([^: ]+)(:([^: ]*)(:([^: ]+))?)?:([^: ]+)");
    private static final BiPredicate<Library, Library> DEFAULT_CHECKER = (library1, library2) ->
            library1.getArtifactId().equals(library2.getArtifactId())
                    && library1.getGroupId().equals(library2.getGroupId())
                    && library1.getVersion().equals(library2.getVersion())
                    && Objects.equals(library1.getClassifier(), library2.getClassifier());
    private static final BiPredicate<Library, Library> ISOLATED_CHECKER = (library1, library2) ->
            DEFAULT_CHECKER.test(library1, library2)
                    && library1.isIsolatedLoad() == library2.isIsolatedLoad()
                    && Objects.equals(library1.getId(), library2.getId());

    private static LibraryManagerWrapper wrapper;

    private LibbyLoaderAPI() {
        // EMPTY
    }

    /**
     * Get the library manager wrapper
     *
     * @return the library manager wrapper
     */
    public static LibraryManagerWrapper getWrapper() {
        return wrapper;
    }

    /**
     * Set the library manager wrapper
     *
     * @param wrapper the library manager wrapper
     */
    public static void setWrapper(LibraryManagerWrapper wrapper) {
        LibbyLoaderAPI.wrapper = wrapper;
    }

    /**
     * Load the libraries
     *
     * @param libraries the libraries
     * @see LibraryManagerWrapper#loadLibrary(Library...)
     */
    public static void loadLibrary(Library... libraries) {
        if (wrapper == null) {
            throw new IllegalStateException("The manager is not set");
        }
        wrapper.loadLibrary(libraries);
    }

    /**
     * Load the repositories
     *
     * @param repositories the repositories
     * @see LibraryManagerWrapper#loadRepository(String...)
     */
    public static void loadRepository(String... repositories) {
        if (wrapper == null) {
            throw new IllegalStateException("The manager is not set");
        }
        wrapper.loadRepository(repositories);
    }

    /**
     * Get the library by the coordinate
     *
     * @param coords the coordinate
     * @return the library
     * @see <a href="https://github.com/apache/maven-resolver/blob/master/maven-resolver-api/src/main/java/org/eclipse/aether/artifact/DefaultArtifact.java">Default Artifact</a>
     */
    public static Library getLibrary(String coords) {
        Matcher m = COORDINATE_PATTERN.matcher(coords);
        if (!m.matches()) {
            throw new IllegalArgumentException("Bad artifact coordinates " + coords
                    + ", expected format is <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>");
        }
        Library.Builder builder = Library.builder()
                .groupId(m.group(1))
                .artifactId(m.group(2))
                .version(m.group(7));
        Optional.ofNullable(m.group(6)).ifPresent(builder::classifier);
        return builder.build();
    }

    /**
     * Check if the library does not exist in the list
     *
     * @param libraries the list of libraries
     * @param library   the library
     * @return true if the library does not exist in the list
     */
    public static boolean isLibraryNotExists(List<Library> libraries, Library library) {
        return libraries.parallelStream().noneMatch(library1 -> {
            if (library.isIsolatedLoad()) {
                return ISOLATED_CHECKER.test(library1, library);
            } else {
                return DEFAULT_CHECKER.test(library1, library);
            }
        });
    }

    /**
     * Add the libraries to the list
     *
     * @param libraries      the list of libraries
     * @param librariesToAdd the libraries to add
     * @return true if any library is added
     */
    public static boolean combineLibraries(List<Library> libraries, Library... librariesToAdd) {
        boolean changed = false;
        for (Library library : librariesToAdd) {
            if (isLibraryNotExists(libraries, library)) {
                libraries.add(library);
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Get the library manager
     *
     * @return the library manager
     */
    public LibraryManager getManager() {
        if (wrapper == null) {
            throw new IllegalStateException("The manager is not set");
        }
        return wrapper.getLibraryManager();
    }
}
