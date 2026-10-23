/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.configuration;

import org.bukkit.Material;

import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
public class BlockSettings {
    private static final List<Material> DEFAULT_BLACKLIST = List.of(
            Material.SHULKER_BOX,
            Material.WHITE_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX,
            Material.PINK_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX,
            Material.RED_SHULKER_BOX,
            Material.BLACK_SHULKER_BOX,
            Material.CHEST,
            Material.TRAPPED_CHEST,
            Material.FURNACE);
    private List<Material> pasteBlacklist;

    public BlockSettings() {
        pasteBlacklist = DEFAULT_BLACKLIST;
    }

    public List<Material> pasteBlacklist() {
        return pasteBlacklist;
    }

    public boolean isBlacklisted(Material material) {
        return pasteBlacklist.contains(material);
    }
}
