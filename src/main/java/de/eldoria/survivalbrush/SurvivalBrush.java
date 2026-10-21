/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush;

import de.eldoria.eldoutilities.config.template.PluginBaseConfiguration;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.survivalbrush.configuration.JacksonConfiguration;
import de.eldoria.survivalbrush.listener.PasteListener;
import de.eldoria.survivalbrush.updates.Updates;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

public class SurvivalBrush extends EldoPlugin {
    private @Nullable Updates updates;

    @Override
    public void onPluginEnable() throws Throwable {
        if (isPreJacksonInstall()) {
            throw new IllegalStateException("The configuration was written by a version older than 1.1.0. Update to the latest 1.1 release first, start the server once, then update to this version.");
        }

        var configuration = new JacksonConfiguration(this);
        PluginBaseConfiguration base = configuration.secondary(PluginBaseConfiguration.KEY);
        if (base.version() == 0) {
            base.version(1);
            base.lastInstalledVersion(this);
        }
        writeConfigs(configuration);

        MessageSender.builder(this)
                     .register();

        updates = new Updates(this, configuration);
        updates.apply();

        registerListener(new PasteListener(this, configuration));
    }

    @Override
    public void onPluginDisable() {
        if (updates != null) updates.stop();
    }

    /**
     * Checks whether the data folder still holds a configuration in the Bukkit serialization format used before 1.1.0.
     *
     * @return true if the main configuration contains Bukkit serialization markers
     * @throws IOException if the configuration file can not be read
     */
    private boolean isPreJacksonInstall() throws IOException {
        var config = getDataFolder().toPath().resolve("config.yml");
        return Files.exists(config) && Files.readString(config).contains("==:");
    }

    @Override
    public Level getLogLevel() {
        return Level.INFO;
    }

    /**
     * Writes the configuration files back, so keys added by this version appear in existing files. Only loaded files
     * are saved, so the main configuration is loaded first.
     *
     * @param configuration configuration of the plugin
     */
    private static void writeConfigs(JacksonConfiguration configuration) {
        configuration.main();
        configuration.save();
    }
}
