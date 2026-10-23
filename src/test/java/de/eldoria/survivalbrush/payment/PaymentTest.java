/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.payment;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentTest {
    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void namedItemsDoNotCountTowardsThePrice() {
        var inventory = inventoryWith(named(Material.STONE, 5), ItemStack.of(Material.STONE, 3));

        var missing = new Payment(inventory).missing(Map.of(Material.STONE, 8));

        assertEquals(Map.of(Material.STONE, 5), missing);
    }

    @Test
    void plainItemsPayThePrice() {
        var inventory = inventoryWith(ItemStack.of(Material.STONE, 64), ItemStack.of(Material.STONE, 6));

        var missing = new Payment(inventory).missing(Map.of(Material.STONE, 70));

        assertTrue(missing.isEmpty());
    }

    @Test
    void takingLeavesNamedItemsAlone() {
        var inventory = inventoryWith(named(Material.STONE, 5), ItemStack.of(Material.STONE, 10));
        var payment = new Payment(inventory);

        payment.take(Map.of(Material.STONE, 7));

        assertEquals(5, amount(inventory, true));
        assertEquals(3, amount(inventory, false));
    }

    @Test
    void takingSpansSeveralStacks() {
        var inventory = inventoryWith(ItemStack.of(Material.STONE, 64), ItemStack.of(Material.STONE, 64));

        new Payment(inventory).take(Map.of(Material.STONE, 100));

        assertEquals(28, amount(inventory, false));
    }

    private Inventory inventoryWith(ItemStack... stacks) {
        var inventory = server.addPlayer().getInventory();
        inventory.addItem(stacks);
        return inventory;
    }

    private static ItemStack named(Material material, int amount) {
        var stack = ItemStack.of(material, amount);
        stack.editMeta(meta -> meta.displayName(Component.text("Special")));
        return stack;
    }

    private static int amount(Inventory inventory, boolean named) {
        return Arrays.stream(inventory.getStorageContents())
                .filter(Objects::nonNull)
                .filter(stack -> stack.getItemMeta().hasDisplayName() == named)
                .mapToInt(ItemStack::getAmount)
                .sum();
    }
}
