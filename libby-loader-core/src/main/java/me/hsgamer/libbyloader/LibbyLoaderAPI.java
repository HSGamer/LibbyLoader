package me.hsgamer.libbyloader;

import net.byteflux.libby.Library;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LibbyLoaderAPI {
    private static final Pattern COORDINATE_PATTERN = Pattern.compile("([^: ]+):([^: ]+)(:([^: ]*)(:([^: ]+))?)?:([^: ]+)");
    private static final BiPredicate<Library, Library> DEFAULT_CHECKER = (library1, library2) ->
            library1.getArtifactId().equals(library2.getArtifactId())
                    && library1.getGroupId().equals(library2.getGroupId())
                    && library1.getVersion().equals(library2.getVersion())
                    && library1.getClassifier().equals(library2.getClassifier());
    private static final BiPredicate<Library, Library> ISOLATED_CHECKER = (library1, library2) ->
            DEFAULT_CHECKER.test(library1, library2)
                    && library1.isIsolatedLoad() == library2.isIsolatedLoad()
                    && Objects.equals(library1.getId(), library2.getId());

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
        return Library.builder()
                .groupId(m.group(1))
                .artifactId(m.group(2))
                .classifier(Optional.ofNullable(m.group(6)).orElse(""))
                .version(m.group(7))
                .build();
    }

    public static boolean combineLibraries(List<Library> libraries, Library... librariesToAdd) {
        boolean changed = false;
        for (Library library : librariesToAdd) {
            if (libraries.parallelStream().noneMatch(library1 -> {
                if (library.isIsolatedLoad()) {
                    return ISOLATED_CHECKER.test(library1, library);
                } else {
                    return DEFAULT_CHECKER.test(library1, library);
                }
            })) {
                libraries.add(library);
                changed = true;
            }
        }
        return changed;
    }
}
