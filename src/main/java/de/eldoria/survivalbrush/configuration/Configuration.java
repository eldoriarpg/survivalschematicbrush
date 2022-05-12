/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.configuration;

import de.eldoria.eldoutilities.configuration.EldoConfig;
import org.bukkit.plugin.Plugin;

public class Configuration extends EldoConfig {
    private BlockSettings blockSettings;

    public Configuration(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected void reloadConfigs() {
        blockSettings = getConfig().getObject("blockSettings", BlockSettings.class, new BlockSettings());
    }

    @Override
    protected void saveConfigs() {
        getConfig().set("blockSettings", blockSettings);
    }

    public BlockSettings blockSettings() {
        return blockSettings;
    }
}
