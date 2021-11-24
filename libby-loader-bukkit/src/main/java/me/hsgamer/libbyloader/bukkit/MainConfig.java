package me.hsgamer.libbyloader.bukkit;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.config.*;
import me.hsgamer.hscore.config.path.BooleanConfigPath;
import me.hsgamer.libbyloader.LibbyLoaderAPI;
import me.hsgamer.libbyloader.settings.LibrarySettings;
import net.byteflux.libby.Library;
import net.byteflux.libby.Repositories;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MainConfig extends PathableConfig {
    public static final BooleanConfigPath ADD_LIBRARIES_TO_CONFIG = new BooleanConfigPath("add-libraries-to-config", false);
    public static final BooleanConfigPath ADD_REPOSITORIES_TO_CONFIG = new BooleanConfigPath("add-repositories-to-config", false);
    public static final ConfigPath<List<String>> EXTERNAL_REPOSITORIES = new BaseConfigPath<>(
            "external-repositories",
            Arrays.asList(Repositories.JCENTER, Repositories.JITPACK, Repositories.MAVEN_CENTRAL, Repositories.SONATYPE),
            o -> CollectionUtils.createStringListFromObject(o, true)
    );
    public static final ConfigPath<List<Library>> PRELOAD_LIBRARIES = new AdvancedConfigPath<List<Map<String, Object>>, List<Library>>(
            "preload-libraries", Collections.emptyList()
    ) {
        @Override
        public @NotNull List<Map<String, Object>> getFromConfig(@NotNull Config config) {
            List<Map<String, Object>> list = new ArrayList<>();
            List<?> rawValue = config.getInstance(getPath(), Collections.emptyList(), List.class);
            rawValue.forEach(o -> {
                if (o instanceof Map) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    ((Map<?, ?>) o).forEach((key, value) -> map.put(key.toString(), value));
                    list.add(map);
                } else if (o instanceof String) {
                    try {
                        list.add(LibrarySettings.serialize(LibbyLoaderAPI.getLibrary((String) o)));
                    } catch (Exception ignored) {
                        // IGNORED
                    }
                }
            });
            return list;
        }

        @Override
        public @NotNull List<Library> convert(@NotNull List<Map<String, Object>> rawValue) {
            List<Library> list = new ArrayList<>();
            rawValue.forEach(map -> {
                try {
                    Library.Builder builder = LibrarySettings.deserialize(map);
                    list.add(builder.build());
                } catch (Exception ignored) {
                    // IGNORED
                }
            });
            return list;
        }

        @Override
        public @NotNull List<Map<String, Object>> convertToRaw(@NotNull List<Library> value) {
            List<Map<String, Object>> list = new ArrayList<>();
            value.forEach(library -> {
                Map<String, Object> options = LibrarySettings.serialize(library);
                list.add(options);
            });
            return list;
        }
    };

    public MainConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "config.yml"));
    }

    public static void addRepository(String... repositories) {
        List<String> configRepositories = EXTERNAL_REPOSITORIES.getValue();
        for (String repository : repositories) {
            if (!configRepositories.contains(repository)) {
                configRepositories.add(repository);
            }
        }
        EXTERNAL_REPOSITORIES.setValue(configRepositories);
        EXTERNAL_REPOSITORIES.getConfig().save();
    }

    public static void addLibrary(Library... libraries) {
        List<Library> configLibraries = PRELOAD_LIBRARIES.getValue();
        if (LibbyLoaderAPI.combineLibraries(configLibraries, libraries)) {
            PRELOAD_LIBRARIES.setValue(configLibraries);
            PRELOAD_LIBRARIES.getConfig().save();
        }
    }
}
