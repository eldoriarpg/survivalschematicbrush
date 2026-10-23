---
sidebar_position: 2
---

# Permissions

Survival Schematic Brush has no commands. You control it with permissions.

| Permission | Default | What it allows |
|---|---|---|
| `survivalschematicbrush.limit.<number>` | nobody | Paste schematics with up to this many blocks, for example `survivalschematicbrush.limit.500`. |
| `survivalschematicbrush.limit.bypass` | nobody | Paste schematics of any size. Blocks are still taken from the inventory. |
| `survivalschematicbrush.paste.bypass` | nobody | Skip every check: no size limit, no blocks taken, no blacklist. |
| `survivalschematicbrush.updates` | operators | Hear about new versions on join and download them. |

The checks only apply to players in survival mode, see [What happens on a paste](./index.md#what-happens-on-a-paste).

## Size limit

The size of a schematic is the number of its blocks, without air.

Give each group of players a limit permission with the number you want:

```text
survivalschematicbrush.limit.100
survivalschematicbrush.limit.1000
```

If a player has several limit permissions, the highest number counts.

:::danger
A player without any limit permission has a limit of 0. They cannot paste anything in survival mode.
Operators are no exception. The limit has to be given to them as a permission too.
:::

`survivalschematicbrush.limit.bypass` removes the size limit, but the player still pays for the blocks.

## Paste bypass

`survivalschematicbrush.paste.bypass` lets a player paste in survival mode as if the add-on was not installed.
Use it for staff who build in survival worlds.

Both bypass permissions are given to nobody by default, not even to operators. Give them on purpose.

## Update notices

Players with `survivalschematicbrush.updates` get a chat message on join when a new version is out.
The message has buttons that run this command:

```text
/survivalschematicbrush:eldo-update <download|enable-auto|later|never>
```

The command is only there once a new version was found.
The console, operators and players with `survivalschematicbrush.updates` may use it.
See [updates](./configuration.md#updates) for the settings behind it.
