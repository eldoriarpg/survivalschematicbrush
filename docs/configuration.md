---
sidebar_position: 3
---

# Configuration

Survival Schematic Brush keeps its settings in `plugins/SurvivalSchematicBrush/config.yml`.
The file is created with default values on the first start.

The add-on reads the file on startup. Stop the server, edit the file, then start the server again.

## config.yml

This is the file with all defaults:

```yaml
blockSettings:
  pasteBlacklist:
    - SHULKER_BOX
    - WHITE_SHULKER_BOX
    - ORANGE_SHULKER_BOX
    - MAGENTA_SHULKER_BOX
    - LIGHT_BLUE_SHULKER_BOX
    - YELLOW_SHULKER_BOX
    - LIME_SHULKER_BOX
    - PINK_SHULKER_BOX
    - GRAY_SHULKER_BOX
    - LIGHT_GRAY_SHULKER_BOX
    - CYAN_SHULKER_BOX
    - PURPLE_SHULKER_BOX
    - BROWN_SHULKER_BOX
    - GREEN_SHULKER_BOX
    - RED_SHULKER_BOX
    - BLACK_SHULKER_BOX
    - CHEST
    - TRAPPED_CHEST
    - FURNACE
updates:
  notice: true
  autoUpdate: false
  askForAutoUpdate: true
```

### blockSettings

| Key | Default | What it does |
|---|---|---|
| `pasteBlacklist` | all shulker boxes, `CHEST`, `TRAPPED_CHEST`, `FURNACE` | Blocks that survival players never paste. |

A schematic can contain a full chest or shulker box. Pasting it would give the player the items inside for free.
The blacklist stops that:

- Blacklisted blocks are not taken from the inventory.
- Blacklisted blocks are replaced with air when the schematic is pasted.

Use the Bukkit material names in upper case, like `BARREL` or `HOPPER`.

:::tip
Add every block that can hold items, for example `BARREL`, `HOPPER`, `DISPENSER`, `DROPPER`, `BLAST_FURNACE` and `SMOKER`.
The default list does not include them.
:::

The blacklist only applies to players the add-on checks: survival players without `survivalschematicbrush.paste.bypass`.

### updates

The add-on looks for new versions every six hours.

| Key | Default | What it does |
|---|---|---|
| `notice` | `true` | Tell the console and admins when a new version is out. |
| `autoUpdate` | `false` | Download new stable versions on its own. They take effect on the next start. A new major version is never downloaded on its own. |
| `askForAutoUpdate` | `true` | Ask admins in chat whether to switch `autoUpdate` on. |

When both `notice` and `autoUpdate` are `false`, the add-on does not look for new versions at all.

Admins with `survivalschematicbrush.updates` see the notice when they join, see [Permissions](./permissions.md).
Choosing "enable-auto" in the chat message sets `autoUpdate` to `true`. Choosing "never" sets `askForAutoUpdate` to `false`.

## base_configuration.yml

The add-on also writes a `base_configuration.yml` for its own bookkeeping.
Leave `version` and `lastInstalledVersion` as they are.

| Key | Default | What it does |
|---|---|---|
| `logLevel` | `INFO` | How much the add-on writes to the console. Use a Java log level such as `WARNING`, `INFO`, `CONFIG` or `FINE`. It takes effect on the next start. |
