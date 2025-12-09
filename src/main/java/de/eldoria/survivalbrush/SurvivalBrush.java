/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush;

import de.eldoria.eldoutilities.config.template.PluginBaseConfiguration;
import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.eldoutilities.updater.lynaupdater.LynaUpdateChecker;
import de.eldoria.eldoutilities.updater.lynaupdater.LynaUpdateData;
import de.eldoria.survivalbrush.configuration.BlockSettings;
import de.eldoria.survivalbrush.configuration.JacksonConfiguration;
import de.eldoria.survivalbrush.configuration.LegacyConfiguration;
import de.eldoria.survivalbrush.listener.PasteListener;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;
import java.util.logging.Level;

public class SurvivalBrush extends EldoPlugin {
    @Override
    public void onPluginEnable() throws Throwable {
        var configuration = new JacksonConfiguration(this);
        PluginBaseConfiguration base = configuration.secondary(PluginBaseConfiguration.KEY);
        if (base.version() == 0) {
            var legacyConfiguration = new LegacyConfiguration(this);
            getLogger().log(Level.INFO, "Migrating configuration to jackson.");
            configuration.main().blockSettings(legacyConfiguration.blockSettings());
            base.version(1);
            base.lastInstalledVersion(this);
            configuration.save();
        }

        LynaUpdateChecker.lyna(LynaUpdateData.builder(this, 7).build()).start();

        registerListener(new PasteListener(this, configuration));
    }

    @Override
    public Level getLogLevel() {
        return Level.INFO;
    }

    @Override
    public List<Class<? extends ConfigurationSerializable>> getConfigSerialization() {
        return List.of(BlockSettings.class);
    }
}
