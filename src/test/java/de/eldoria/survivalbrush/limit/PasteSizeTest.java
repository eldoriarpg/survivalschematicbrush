/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.limit;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasteSizeTest {
    @BeforeEach
    void setUp() {
        MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void everyKindOfAirIsLeftOut() {
        var size = PasteSize.of(Map.of(
                Material.AIR.createBlockData(), 10,
                Material.CAVE_AIR.createBlockData(), 20,
                Material.VOID_AIR.createBlockData(), 30,
                Material.STONE.createBlockData(), 4,
                Material.CHEST.createBlockData(), 2));

        assertEquals(6, size);
    }
}
