/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.configuration;

@SuppressWarnings("FieldMayBeFinal")
public class ConfigFile {
    private BlockSettings blockSettings = new BlockSettings();

    public BlockSettings blockSettings() {
        return blockSettings;
    }

    public void blockSettings(BlockSettings blockSettings) {
        this.blockSettings = blockSettings;
    }
}
