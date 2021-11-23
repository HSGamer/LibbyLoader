package me.hsgamer.libbyloader.bukkit;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.config.*;
import net.byteflux.libby.Library;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static me.hsgamer.libbyloader.LibrarySettingConstants.*;

public class MainConfig extends PathableConfig {
    public static final ConfigPath<List<String>> EXTERNAL_REPOSITORIES = new BaseConfigPath<>(
            "external-repositories", Collections.emptyList(),
            o -> CollectionUtils.createStringListFromObject(o, true)
    );
    public static final ConfigPath<List<Library>> PRELOAD_LIBRARIES = new AdvancedConfigPath<Map<String, Map<String, Object>>, List<Library>>(
            "preload-libraries", Collections.emptyList()
    ) {
        @Override
        public @NotNull Map<String, Map<String, Object>> getFromConfig(@NotNull Config config) {
            Map<String, Map<String, Object>> map = new LinkedHashMap<>();
            config.getKeys(getPath(), false).forEach(key -> map.put(key, config.getNormalizedValues(getPath() + "." + key, false)));
            return map;
        }

        @Override
        public @NotNull List<Library> convert(@NotNull Map<String, Map<String, Object>> rawValue) {
            List<Library> list = new ArrayList<>();
            rawValue.forEach((key, value) -> {
                if (!value.containsKey(GROUP_ID) || !value.containsKey(ARTIFACT_ID) || !value.containsKey(VERSION)) {
                    return;
                }
                Library.Builder builder = Library.builder().id(key)
                        .groupId(String.valueOf(value.get(GROUP_ID)))
                        .artifactId(String.valueOf(value.get(ARTIFACT_ID)))
                        .version(String.valueOf(value.get(VERSION)));
                if (value.containsKey(CLASSIFIER)) {
                    builder.classifier(String.valueOf(value.get(CLASSIFIER)));
                }
                if (value.containsKey(CHECKSUM)) {
                    builder.checksum(String.valueOf(value.get(CHECKSUM)));
                }

                boolean isolated = Optional.ofNullable(value.get(ISOLATED))
                        .map(Object::toString)
                        .map(Boolean::parseBoolean)
                        .orElse(false);
                builder.isolatedLoad(isolated);
                list.add(builder.build());
            });
            return list;
        }

        @Override
        public @NotNull Map<String, Map<String, Object>> convertToRaw(@NotNull List<Library> value) {
            Map<String, Map<String, Object>> map = new LinkedHashMap<>();
            value.forEach(library -> {
                Map<String, Object> options = new LinkedHashMap<>();
                options.put(GROUP_ID, library.getGroupId());
                options.put(ARTIFACT_ID, library.getArtifactId());
                options.put(VERSION, library.getVersion());
                if (library.hasClassifier()) {
                    options.put(CLASSIFIER, library.getClassifier());
                }
                if (library.hasChecksum()) {
                    options.put(CHECKSUM, library.getChecksum());
                }
                options.put(ISOLATED, library.isIsolatedLoad());
                map.put(library.getId(), options);
            });
            return map;
        }
    };

    public MainConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "config.yml"));
    }
}
