/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.payment;

import de.eldoria.survivalbrush.configuration.BlockSettings;
import org.bukkit.Material;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.TrapDoor;

import java.util.EnumMap;
import java.util.Map;

/**
 * Works out which items pay for the blocks of a paste, the way a player would place them by hand.
 * <ul>
 *     <li>Air of every kind and blacklisted blocks are free.</li>
 *     <li>A block costs the item that places it, so a wall torch costs a torch and redstone wire costs redstone.</li>
 *     <li>A block made of two parts, like a door, a bed, a tall flower or an extended piston, costs one item.</li>
 *     <li>Water and lava cost a filled bucket.</li>
 *     <li>A block that no item places costs itself, which no player can pay.</li>
 * </ul>
 */
public final class Price {
    private Price() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Calculates the price of a set of blocks.
     *
     * @param blocks   the amount of each block state
     * @param settings the settings holding the paste blacklist
     * @return the amount of each item to pay
     */
    public static Map<Material, Integer> of(Map<BlockData, Integer> blocks, BlockSettings settings) {
        Map<Material, Integer> price = new EnumMap<>(Material.class);
        for (var entry : blocks.entrySet()) {
            var block = entry.getKey();
            if (isFree(block, settings)) continue;
            price.merge(item(block), entry.getValue(), Integer::sum);
        }
        return price;
    }

    private static boolean isFree(BlockData block, BlockSettings settings) {
        var material = block.getMaterial();
        return material.isAir() || settings.isBlacklisted(material) || isSecondPart(block);
    }

    /**
     * Checks whether a block is the part of a two-part block that is placed together with the first one.
     *
     * @param block the block
     * @return true for the upper half of a door or tall plant, the head of a bed and the head of an extended piston
     */
    private static boolean isSecondPart(BlockData block) {
        return switch (block) {
            case Bed bed -> bed.getPart() == Bed.Part.HEAD;
            case PistonHead ignored -> true;
            case Stairs ignored -> false;
            case TrapDoor ignored -> false;
            case Bisected bisected -> bisected.getHalf() == Bisected.Half.TOP;
            default -> false;
        };
    }

    private static Material item(BlockData block) {
        var material = block.getMaterial();
        return switch (material) {
            case WATER -> Material.WATER_BUCKET;
            case LAVA -> Material.LAVA_BUCKET;
            default -> {
                var placement = block.getPlacementMaterial();
                yield placement.isAir() ? material : placement;
            }
        };
    }
}
