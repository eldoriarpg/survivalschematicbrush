package de.eldoria.survivalbrush.configuration;

import de.eldoria.eldoutilities.config.ConfigKey;
import de.eldoria.eldoutilities.config.JacksonConfig;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class JacksonConfiguration extends JacksonConfig<ConfigFile> implements Configuration {
    public JacksonConfiguration(@NotNull Plugin plugin) {
        super(plugin, ConfigKey.defaultConfig(ConfigFile.class, ConfigFile::new));
    }


    @Override
    public BlockSettings blockSettings() {
        return main().blockSettings();
    }
}
