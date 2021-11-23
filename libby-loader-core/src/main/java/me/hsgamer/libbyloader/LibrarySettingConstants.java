package me.hsgamer.libbyloader;

import net.byteflux.libby.Library;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class LibrarySettingConstants {
    public static final String GROUP_ID = "group-id";
    public static final String ARTIFACT_ID = "artifact-id";
    public static final String VERSION = "version";
    public static final String CHECKSUM = "checksum";
    public static final String CLASSIFIER = "classifier";
    public static final String ISOLATED = "isolated";

    private LibrarySettingConstants() {
        // EMPTY
    }

    public static Library.Builder deserialize(Map<String, Object> map) {
        if (!map.containsKey(GROUP_ID) || !map.containsKey(ARTIFACT_ID) || !map.containsKey(VERSION)) {
            throw new IllegalArgumentException("Missing required fields");
        }
        Library.Builder builder = Library.builder()
                .groupId(String.valueOf(map.get(GROUP_ID)))
                .artifactId(String.valueOf(map.get(ARTIFACT_ID)))
                .version(String.valueOf(map.get(VERSION)));
        if (map.containsKey(CLASSIFIER)) {
            builder.classifier(String.valueOf(map.get(CLASSIFIER)));
        }
        if (map.containsKey(CHECKSUM)) {
            builder.checksum(String.valueOf(map.get(CHECKSUM)));
        }
        Optional.ofNullable(map.get(ISOLATED))
                .map(Object::toString)
                .map(Boolean::parseBoolean)
                .ifPresent(builder::isolatedLoad);

        return builder;
    }

    public static Map<String, Object> serialize(Library library) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(GROUP_ID, library.getGroupId());
        map.put(ARTIFACT_ID, library.getArtifactId());
        map.put(VERSION, library.getVersion());
        if (library.hasClassifier()) {
            map.put(CLASSIFIER, library.getClassifier());
        }
        if (library.hasChecksum()) {
            map.put(CHECKSUM, library.getChecksum());
        }
        map.put(ISOLATED, library.isIsolatedLoad());
        return map;
    }
}
