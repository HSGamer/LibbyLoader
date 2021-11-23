package me.hsgamer.libbyloader;

import net.byteflux.libby.Library;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LibbyLoaderAPI {
    private static final Pattern COORDINATE_PATTERN = Pattern.compile("([^: ]+):([^: ]+)(:([^: ]*)(:([^: ]+))?)?:([^: ]+)");
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
}
