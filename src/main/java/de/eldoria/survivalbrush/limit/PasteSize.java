/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.limit;

import org.bukkit.block.data.BlockData;

import java.util.Map;

/**
 * Measures a paste against the size limit of a player.
 */
public final class PasteSize {
    private PasteSize() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Counts the blocks of a paste. Air of every kind, including cave air and void air, does not count.
     *
     * @param blocks the amount of each block state
     * @return the number of blocks that are not air
     */
    public static int of(Map<BlockData, Integer> blocks) {
        return blocks.entrySet().stream()
                .filter(entry -> !entry.getKey().getMaterial().isAir())
                .mapToInt(Map.Entry::getValue)
                .sum();
    }
}
