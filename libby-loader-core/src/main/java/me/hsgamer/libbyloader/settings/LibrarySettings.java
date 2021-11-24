package me.hsgamer.libbyloader.settings;

import me.hsgamer.hscore.common.CollectionUtils;
import net.byteflux.libby.Library;

import java.util.*;

public final class LibrarySettings {
    public static final String ID = "id";
    public static final String GROUP_ID = "group-id";
    public static final String ARTIFACT_ID = "artifact-id";
    public static final String VERSION = "version";
    public static final String CHECKSUM = "checksum";
    public static final String CLASSIFIER = "classifier";
    public static final String ISOLATED = "isolated";
    public static final String RELOCATIONS = "relocations";
    public static final String REPOSITORIES = "repositories";
    public static final String URLS = "urls";

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
        Optional.ofNullable(map.get(ID))
                .map(String::valueOf)
                .ifPresent(builder::id);
        Optional.ofNullable(map.get(CLASSIFIER))
                .map(String::valueOf)
                .ifPresent(builder::classifier);
        Optional.ofNullable(map.get(CHECKSUM))
                .map(String::valueOf)
                .ifPresent(builder::checksum);
        Optional.ofNullable(map.get(ISOLATED))
                .map(String::valueOf)
                .map(Boolean::parseBoolean)
                .ifPresent(builder::isolatedLoad);
        Optional.ofNullable(map.get(RELOCATIONS))
                .filter(List.class::isInstance)
                .map(List.class::cast)
                .ifPresent(list -> {
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
                });
        Optional.ofNullable(map.get(REPOSITORIES))
                .map(o -> CollectionUtils.createStringListFromObject(o, true))
                .ifPresent(repositories -> repositories.forEach(builder::repository));
        Optional.ofNullable(map.get(URLS))
                .map(o -> CollectionUtils.createStringListFromObject(o, true))
                .ifPresent(urls -> urls.forEach(builder::url));

        return builder;
    }

    public static Map<String, Object> serialize(Library library) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(ID, library.getId());
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
        if (library.isIsolatedLoad()) {
            map.put(ISOLATED, library.isIsolatedLoad());
        }
        Collection<String> repositories = library.getRepositories();
        if (!repositories.isEmpty()) {
            map.put(REPOSITORIES, repositories);
        }
        Collection<String> urls = library.getUrls();
        if (!urls.isEmpty()) {
            map.put(URLS, urls);
        }
        return map;
    }
}
