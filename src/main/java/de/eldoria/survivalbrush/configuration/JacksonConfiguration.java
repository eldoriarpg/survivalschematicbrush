/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.configuration;

import de.eldoria.eldoutilities.config.JacksonConfig;
import dev.chojo.ocular.key.Key;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class JacksonConfiguration extends JacksonConfig<ConfigFile> implements Configuration {
    public JacksonConfiguration(@NotNull Plugin plugin) {
        super(plugin, Key.builder(Path.of("config.yml"), ConfigFile::new).build());
    }


    @Override
    public BlockSettings blockSettings() {
        return main().blockSettings();
    }

    @Override
    public UpdateSettings updates() {
        return main().updates();
    }
}
