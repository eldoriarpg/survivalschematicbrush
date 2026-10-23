/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.payment;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Takes the price of a paste from an inventory.
 * <p>
 * Only plain stacks pay: a stack with a custom name, enchantments, contents or any other extra data is neither counted
 * nor taken, so a player never loses a special item and never passes the check with items that are not taken.
 * <p>
 * Filled buckets come back empty, like after placing water or lava by hand.
 */
public final class Payment {
    private static final Map<Material, Material> EMPTY_CONTAINERS =
            Map.of(Material.WATER_BUCKET, Material.BUCKET, Material.LAVA_BUCKET, Material.BUCKET);

    private final Inventory inventory;

    /**
     * @param inventory the inventory to pay from. Only its storage contents are used.
     */
    public Payment(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Finds the items the inventory lacks to pay a price.
     *
     * @param price the amount of each item to pay
     * @return the missing amount of each item that can not be paid in full. Empty when the price can be paid.
     */
    public Map<Material, Integer> missing(Map<Material, Integer> price) {
        var available = available();
        Map<Material, Integer> missing = new EnumMap<>(Material.class);
        for (var entry : price.entrySet()) {
            var lacking = entry.getValue() - available.getOrDefault(entry.getKey(), 0);
            if (lacking > 0) missing.put(entry.getKey(), lacking);
        }
        return missing;
    }

    /**
     * Takes a price from the inventory. Call it only after {@link #missing(Map)} returned an empty map.
     *
     * @param price the amount of each item to take
     */
    public void take(Map<Material, Integer> price) {
        var contents = inventory.getStorageContents();
        for (var entry : price.entrySet()) {
            var remaining = entry.getValue();
            for (var stack : contents) {
                if (remaining == 0) break;
                if (!isPlain(stack, entry.getKey())) continue;
                var taken = Math.min(stack.getAmount(), remaining);
                stack.setAmount(stack.getAmount() - taken);
                remaining -= taken;
            }
        }
        inventory.setStorageContents(contents);
    }

    /**
     * Gives back the empty containers of the filled items in a price. Call it after {@link #take(Map)}.
     *
     * @param price the price that was taken
     * @return the containers that did not fit into the inventory
     */
    public Collection<ItemStack> returnContainers(Map<Material, Integer> price) {
        List<ItemStack> containers = new ArrayList<>();
        for (var entry : price.entrySet()) {
            var container = EMPTY_CONTAINERS.get(entry.getKey());
            if (container == null) continue;
            var remaining = entry.getValue();
            while (remaining > 0) {
                var amount = Math.min(remaining, container.getMaxStackSize());
                containers.add(ItemStack.of(container, amount));
                remaining -= amount;
            }
        }
        return inventory.addItem(containers.toArray(ItemStack[]::new)).values();
    }

    private Map<Material, Integer> available() {
        Map<Material, Integer> available = new EnumMap<>(Material.class);
        for (var stack : inventory.getStorageContents()) {
            if (stack == null || stack.isEmpty() || !isPlain(stack, stack.getType())) continue;
            available.merge(stack.getType(), stack.getAmount(), Integer::sum);
        }
        return available;
    }

    private static boolean isPlain(ItemStack stack, Material material) {
        return stack != null && stack.getType() == material && stack.isSimilar(ItemStack.of(material));
    }
}
