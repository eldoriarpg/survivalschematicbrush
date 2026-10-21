/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.configuration;

@SuppressWarnings("FieldMayBeFinal")
public class ConfigFile {
    private BlockSettings blockSettings = new BlockSettings();
    private UpdateSettings updates = new UpdateSettings();

    public BlockSettings blockSettings() {
        return blockSettings;
    }

    public UpdateSettings updates() {
        return updates;
    }
}
