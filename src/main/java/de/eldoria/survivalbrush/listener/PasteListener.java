/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.listener;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.schematicbrush.event.PrePasteEvent;
import de.eldoria.survivalbrush.configuration.Configuration;
import de.eldoria.survivalbrush.payment.Payment;
import de.eldoria.survivalbrush.payment.Price;
import de.eldoria.survivalbrush.util.Permissions;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PasteListener implements Listener {
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

        var clipboard = event.paste().clipboard();
        var price = Price.of(blockStates(clipboard), configuration.blockSettings());

        var payment = new Payment(player.getInventory());
        var missing = payment.missing(price);

        if (missing.isEmpty()) {
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

            payment.take(price);
            for (var leftover : payment.returnContainers(price)) {
                player.getWorld().dropItem(player.getLocation(), leftover);
            }
            return;
        }

        var missingItems = missing.entrySet().stream()
                                  .map(e -> String.format("  <gold>- %s: <dark_aqua>%s", e.getKey(), e.getValue()))
                                  .collect(Collectors.joining("\n"));

        messageSender.sendMessage(player, "<red>Missing items to paste schematic:\n" + missingItems);
        event.setCancelled(true);
    }

    private static Map<BlockData, Integer> blockStates(Clipboard clipboard) {
        Map<BlockData, Integer> blocks = new HashMap<>();
        for (var countable : clipboard.getBlockDistributionWithData(clipboard.getRegion())) {
            blocks.merge(BukkitAdapter.adapt(countable.getID()), countable.getAmount(), Integer::sum);
        }
        return blocks;
    }
}
