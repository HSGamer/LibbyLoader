package me.hsgamer.libbyloader.settings;

import net.byteflux.libby.Library;

import java.util.*;

public final class LibrarySettings {
    public static final String GROUP_ID = "group-id";
    public static final String ARTIFACT_ID = "artifact-id";
    public static final String VERSION = "version";
    public static final String CHECKSUM = "checksum";
    public static final String CLASSIFIER = "classifier";
    public static final String ISOLATED = "isolated";
    public static final String RELOCATIONS = "relocations";

    private LibrarySettings() {
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
        if (map.containsKey(RELOCATIONS)) {
            Object relocations = map.get(RELOCATIONS);
            if (relocations instanceof List) {
                List<?> list = (List<?>) relocations;
                for (Object o : list) {
                    if (o instanceof Map) {
                        Map<String, Object> mapped = new LinkedHashMap<>();
                        ((Map<?, ?>) o).forEach((k, v) -> mapped.put(String.valueOf(k), v));
                        builder.relocate(RelocationSettings.deserialize(mapped));
                    } else if (o instanceof String) {
                        String[] split = ((String) o).split(":", 2);
                        if (split.length == 2) {
                            builder.relocate(split[0], split[1]);
                        }
                    }
                }
            }
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
        if (library.hasRelocations()) {
            List<Object> list = new ArrayList<>();
            library.getRelocations().forEach(relocation -> list.add(RelocationSettings.serialize(relocation)));
            map.put(RELOCATIONS, list);
        }
        map.put(ISOLATED, library.isIsolatedLoad());
        return map;
    }
}
