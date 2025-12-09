/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.listener;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import de.eldoria.eldoutilities.container.Pair;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.schematicbrush.event.PrePasteEvent;
import de.eldoria.survivalbrush.configuration.Configuration;
import de.eldoria.survivalbrush.util.Permissions;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PasteListener implements Listener {
    private static final List<Pair<Material, Material>> itemBlockMappings = List.of(
            Pair.of(Material.LAVA, Material.LAVA_BUCKET),
            Pair.of(Material.WATER, Material.WATER_BUCKET)
    );

    private final Configuration configuration;
    private final MessageSender messageSender;

    public PasteListener(Plugin plugin, Configuration configuration) {
        this.messageSender = MessageSender.getPluginMessageSender(plugin);
        this.configuration = configuration;
    }

    @EventHandler
    public void onPaste(PrePasteEvent event) {
        var player = event.player();

        if (player.getGameMode() != GameMode.SURVIVAL) return;

        if (player.hasPermission(Permissions.Paste.BYPASS)) return;

        var limit = Permissions.Limit.limit(player);

        if (event.schematic().effectiveSize() > limit) {
            messageSender.sendMessage(player, "<red>The schematic is too large.");
            event.setCancelled(true);
            return;
        }

        var blockCount = new HashMap<>(event.schematic().blockCount());
        blockCount.remove(Material.AIR);

        for (var mapping : itemBlockMappings) {
            var amount = blockCount.remove(mapping.first);
            if (amount == null) continue;
            blockCount.put(mapping.second, amount);
        }

        for (var material : configuration.blockSettings().pasteBlacklist()) {
            blockCount.remove(material);
        }

        var inv = player.getInventory();

        Map<Material, Integer> missing = new EnumMap<>(Material.class);
        Map<Material, Integer> invCount = new EnumMap<>(Material.class);


        for (var content : inv.getStorageContents()) {
            if (content == null) continue;
            invCount.compute(content.getType(), (k, v) -> v == null ? content.getAmount() : v + content.getAmount());
        }

        for (var block : blockCount.entrySet()) {
            var invAmount = invCount.getOrDefault(block.getKey(), 0);
            if (invAmount < block.getValue()) {
                missing.put(block.getKey(), block.getValue() - invAmount);
            }
        }

        if (missing.isEmpty()) {
            var clipboard = event.paste().clipboard();
            for (var current : clipboard.getRegion()) {
                var block = clipboard.getBlock(current);
                if (configuration.blockSettings().isBlacklisted(BukkitAdapter.adapt(block).getMaterial())) {
                    try {
                        clipboard.setBlock(current, BukkitAdapter.adapt(Material.AIR.createBlockData()));
                    } catch (WorldEditException e) {
                        // ignore
                    }
                }
            }

            for (var entry : blockCount.entrySet()) {
                var slots = inv.all(entry.getKey());
                var toTake = entry.getValue();
                var contained = 0;
                for (var slot : slots.entrySet()) {
                    var stack = slot.getValue();
                    // We skip not normal items like filled shulker boxes in order to avoid item loss
                    if (!stack.isSimilar(new ItemStack(entry.getKey()))) continue;
                    var take = Math.min(stack.getAmount(), toTake - contained);
                    stack.setAmount(stack.getAmount() - take);
                    contained += take;
                    if (contained == toTake) {
                        break;
                    }
                }
            }
            return;
        }

        var missingItems = missing.entrySet().stream()
                                  .map(e -> String.format("  <gold>- %s: <dark_aqua>%s", e.getKey(), e.getValue()))
                                  .collect(Collectors.joining("\n"));

        messageSender.sendMessage(player, "<red>Missing items to paste schematic:\n" + missingItems);
        event.setCancelled(true);
    }
}
