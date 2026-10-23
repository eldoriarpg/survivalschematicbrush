/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.survivalbrush.util;

import org.bukkit.entity.Player;

public class Permissions {
    private static final String BASE = "survivalschematicbrush";

    private Permissions() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    private static String perm(String... perms) {
        return String.join(".", perms);
    }

    /**
     * The permission to hear about new versions on join and to download them.
     */
    public static final String UPDATES = perm(BASE, "updates");

    public static final class Paste {
        private Paste() {
            throw new UnsupportedOperationException("This is a utility class.");
        }

        private static final String PASTE = perm(BASE, "paste");

        public static final String BYPASS = perm(PASTE, "bypass");
    }

    public static final class Limit {
        private Limit() {
            throw new UnsupportedOperationException("This is a utility class.");
        }

        private static final String LIMIT = perm(BASE, "limit");

        public static final String BYPASS = perm(LIMIT, "bypass");

        public static int limit(Player player) {
            if (player.hasPermission(BYPASS)) return Integer.MAX_VALUE;
            return de.eldoria.eldoutilities.utils.Permissions.findHighestIntPermission(player, LIMIT + ".", 0);
        }
    }
}
