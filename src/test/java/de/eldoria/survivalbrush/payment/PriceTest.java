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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class PriceTest {
    private final BlockSettings settings = new BlockSettings();

    @BeforeEach
    void setUp() {
        MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void everyKindOfAirIsFree() {
        var price = Price.of(
                Map.of(
                        block(Material.AIR, Material.AIR), 10,
                        block(Material.CAVE_AIR, Material.AIR), 20,
                        block(Material.VOID_AIR, Material.AIR), 30),
                settings);

        assertTrue(price.isEmpty());
    }

    @Test
    void blacklistedBlocksAreFree() {
        var price = Price.of(Map.of(block(Material.CHEST, Material.CHEST), 4), settings);

        assertTrue(price.isEmpty());
    }

    @Test
    void blocksCostTheItemThatPlacesThem() {
        var price = Price.of(
                Map.of(
                        block(Material.WALL_TORCH, Material.TORCH), 3,
                        block(Material.TORCH, Material.TORCH), 2,
                        block(Material.REDSTONE_WIRE, Material.REDSTONE), 5),
                settings);

        assertEquals(Map.of(Material.TORCH, 5, Material.REDSTONE, 5), price);
    }

    @Test
    void waterAndLavaCostAFilledBucket() {
        var price = Price.of(
                Map.of(block(Material.WATER, Material.AIR), 2, block(Material.LAVA, Material.AIR), 1), settings);

        assertEquals(Map.of(Material.WATER_BUCKET, 2, Material.LAVA_BUCKET, 1), price);
    }

    @Test
    void blocksNoItemPlacesCostThemselves() {
        var price = Price.of(Map.of(block(Material.END_PORTAL, Material.AIR), 9), settings);

        assertEquals(Map.of(Material.END_PORTAL, 9), price);
    }

    @Test
    void twoPartBlocksCostOneItem() {
        var price = Price.of(
                Map.of(
                        block(Material.OAK_DOOR, Material.OAK_DOOR, half(Bisected.Half.BOTTOM)), 1,
                        block(Material.OAK_DOOR, Material.OAK_DOOR, half(Bisected.Half.TOP)), 1,
                        block(Material.RED_BED, Material.RED_BED, part(Bed.Part.FOOT)), 1,
                        block(Material.RED_BED, Material.RED_BED, part(Bed.Part.HEAD)), 1,
                        block(Material.TALL_GRASS, Material.TALL_GRASS, half(Bisected.Half.BOTTOM)), 1,
                        block(Material.TALL_GRASS, Material.TALL_GRASS, half(Bisected.Half.TOP)), 1,
                        block(Material.PISTON_HEAD, Material.AIR), 1,
                        block(Material.PISTON, Material.PISTON), 1),
                settings);

        assertEquals(
                Map.of(Material.OAK_DOOR, 1, Material.RED_BED, 1, Material.TALL_GRASS, 1, Material.PISTON, 1), price);
    }

    @Test
    void upsideDownStairsAndTrapdoorsCostAnItemEach() {
        var price = Price.of(
                Map.of(
                        block(Material.OAK_STAIRS, Material.OAK_STAIRS, half(Bisected.Half.TOP)), 2,
                        block(Material.OAK_STAIRS, Material.OAK_STAIRS, half(Bisected.Half.BOTTOM)), 3,
                        block(Material.OAK_TRAPDOOR, Material.OAK_TRAPDOOR, half(Bisected.Half.TOP)), 4),
                settings);

        assertEquals(Map.of(Material.OAK_STAIRS, 5, Material.OAK_TRAPDOOR, 4), price);
    }

    private static BlockData block(Material material, Material placement) {
        return block(material, placement, data -> {});
    }

    private static BlockData block(Material material, Material placement, Consumer<BlockData> state) {
        var data = material.createBlockData();
        state.accept(data);
        var block = spy(data);
        doReturn(placement).when(block).getPlacementMaterial();
        return block;
    }

    private static Consumer<BlockData> half(Bisected.Half half) {
        return data -> ((Bisected) data).setHalf(half);
    }

    private static Consumer<BlockData> part(Bed.Part part) {
        return data -> ((Bed) data).setPart(part);
    }
}
