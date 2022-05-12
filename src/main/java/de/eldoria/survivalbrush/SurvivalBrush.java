/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush;

import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.survivalbrush.configuration.BlockSettings;
import de.eldoria.survivalbrush.configuration.Configuration;
import de.eldoria.survivalbrush.listener.PasteListener;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;

public class SurvivalBrush extends EldoPlugin {
    @Override
    public void onPluginEnable() throws Throwable {
        Configuration configuration = new Configuration(this);
        registerListener(new PasteListener(this, configuration));
    }

    @Override
    public List<Class<? extends ConfigurationSerializable>> getConfigSerialization() {
        return List.of(BlockSettings.class);
    }

    @Override
    public void onPluginDisable() throws Throwable {
        super.onPluginDisable();
    }
}
