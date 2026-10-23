---
sidebar_position: 1
---

# Survival Schematic Brush

Survival Schematic Brush is an add-on for [Schematic Brush Reborn](/schematic-brush-reborn).
It makes survival players pay for what they paste: the blocks of a schematic are taken from their inventory.

## Why use it

Schematic brushes place many blocks at once and cost nothing.
That is fine for builders in creative mode, but it breaks the game for survival players.

With this add-on, survival players can still use schematic brushes, but:

- They need the blocks of the schematic in their inventory. The blocks are taken when they paste.
- They can only paste schematics up to a size you allow.
- Chests, shulker boxes and other blocks you choose are never pasted, so nobody can copy their contents.

## Requirements

| What | Version |
|---|---|
| Schematic Brush Reborn | 3 |
| FastAsyncWorldEdit | required |
| Paper | 1.21.11 or newer |
| Java | 25 |

See [Getting started](/schematic-brush-reborn/getting-started) to set up the core plugin.
Players still need the Schematic Brush Reborn permissions to use brushes, see the [permission reference](/schematic-brush-reborn/reference/permissions).

## Installation

1. Make sure Schematic Brush Reborn 3 and FastAsyncWorldEdit are installed.
2. Download the Survival Schematic Brush jar from [Lyna](https://lyna.eldoria.de/products/7) and put it into your server's `plugins` folder.
3. Restart the server.
4. Give your survival players a size limit permission, for example `survivalschematicbrush.limit.500`.

:::danger Give a size limit
Without a limit permission, the limit is 0. Survival players then cannot paste anything at all.
See [Permissions](./permissions.md).
:::

The add-on has no commands. It works on its own as soon as it is installed.

### Updating from version 1

Version 2 needs Schematic Brush Reborn 3.
If your config was written by a version older than 1.1.0, update to the latest 1.1 release first and start the server once.
Otherwise version 2 stops with an error on startup.

## What happens on a paste

The add-on only acts on players in **survival** mode. Players in creative, adventure or spectator mode paste as usual.
Players with `survivalschematicbrush.paste.bypass` also paste as usual.

For everyone else, each paste goes through these checks:

1. **Size.** The schematic may have at most as many blocks as the player's limit. Air does not count.
   If it is bigger, the paste is stopped with "The schematic is too large."
2. **Materials.** The add-on counts the items needed to place the blocks of the schematic.
   Air of every kind, including cave air and void air, and blacklisted blocks are not counted.
   Water and lava count as water buckets and lava buckets.
3. **Inventory.** It compares the count with the player's inventory.
   If something is missing, the paste is stopped and the player sees a list of the missing items and amounts.
4. **Paste.** If everything is there, the items are taken from the inventory and the schematic is pasted.
   Empty buckets go back into the inventory.
   Blacklisted blocks are replaced with air.

Only the main inventory and hotbar count. Armor slots and the off hand do not.
Items with a custom name, enchantments or other extra data do not count and are not taken, so a player never loses a special item.

:::note Things to know
- A block costs the item you would place it with. A wall torch costs a torch, redstone wire costs redstone and wheat costs wheat seeds.
- A block made of two parts costs one item. A door, a bed or a tall flower costs one door, one bed or one flower.
- A water or lava block costs a full bucket. The player gets the empty bucket back, just like placing water or lava by hand. If the inventory is full, the empty buckets drop at the player's feet.
- A few blocks cannot be placed with any item, for example end portals or nether portals. A schematic with such blocks cannot be paid for.
- The blacklist protects against copying chest contents. See [Configuration](./configuration.md).
:::

## Next steps

- [Permissions](./permissions.md): the size limit and the bypass permissions.
- [Configuration](./configuration.md): the paste blacklist and update settings.
