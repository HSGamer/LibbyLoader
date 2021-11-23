package me.hsgamer.libbyloader.settings;

import me.hsgamer.hscore.common.CollectionUtils;
import net.byteflux.libby.relocation.Relocation;

import java.util.LinkedHashMap;
import java.util.Map;

public final class RelocationSettings {
    public static final String PATTERN = "pattern";
    public static final String RELOCATED_PATTERN = "relocated-pattern";
    public static final String INCLUDES = "includes";
    public static final String EXCLUDES = "excludes";

    private RelocationSettings() {
        // EMPTY
    }

    public static Relocation deserialize(Map<String, Object> map) {
        if (!map.containsKey(PATTERN) || !map.containsKey(RELOCATED_PATTERN)) {
            throw new IllegalArgumentException("Missing pattern or relocated-pattern");
        }
        Relocation.Builder builder = Relocation.builder()
                .pattern(String.valueOf(map.get(PATTERN)))
                .relocatedPattern(String.valueOf(map.get(RELOCATED_PATTERN)));
        if (map.containsKey(INCLUDES)) {
            for (String include : CollectionUtils.createStringListFromObject(map.get(INCLUDES), true)) {
                builder.include(include);
            }
        }
        if (map.containsKey(EXCLUDES)) {
            for (String exclude : CollectionUtils.createStringListFromObject(map.get(EXCLUDES), true)) {
                builder.exclude(exclude);
            }
        }
        return builder.build();
    }

    public static Map<String, Object> serialize(Relocation relocation) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(PATTERN, relocation.getPattern());
        map.put(RELOCATED_PATTERN, relocation.getRelocatedPattern());
        map.put(INCLUDES, relocation.getIncludes());
        map.put(EXCLUDES, relocation.getExcludes());
        return map;
    }
}
