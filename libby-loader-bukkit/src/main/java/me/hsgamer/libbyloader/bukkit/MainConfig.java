package me.hsgamer.libbyloader.bukkit;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.config.*;
import me.hsgamer.libbyloader.settings.LibrarySettings;
import net.byteflux.libby.Library;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
                try {
                    Library.Builder builder = LibrarySettings.deserialize(value);
                    builder.id(key);
                    list.add(builder.build());
                } catch (Exception ignored) {
                    // IGNORED
                }
            });
            return list;
        }

        @Override
        public @NotNull Map<String, Map<String, Object>> convertToRaw(@NotNull List<Library> value) {
            Map<String, Map<String, Object>> map = new LinkedHashMap<>();
            value.forEach(library -> {
                Map<String, Object> options = LibrarySettings.serialize(library);
                map.put(library.getId(), options);
            });
            return map;
        }
    };

    public MainConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "config.yml"));
    }
}
