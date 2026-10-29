/*
 *    This file is part of the Map Link mod
 *    licensed under the GNU GPL v3 License.
 *
 *    Copyright (C) 2026  Leander Knüttel and contributors
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.the_build_craft.maplink.api;

import de.the_build_craft.maplink.common.AbstractModInitializer;
import de.the_build_craft.maplink.common.connections.MapConnection;
import de.the_build_craft.maplink.common.waypoints.PlayerPosition;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Read-only access for other mods to the player positions Map Link reads from the web map.
 * <p>
 * Works with or without Xaero's mods. Only players the web map shows are listed, never players
 * the client sees itself, and never the local player. All methods may be called from any thread.
 *
 * @author clixmods
 * @version 29.10.2026
 */
public final class MapLinkApi {
    /** Raised when a method is added or changes. */
    public static final int API_VERSION = 1;

    private static volatile List<WebMapPlayer> players = List.of();
    private static volatile long lastUpdateTime = 0;

    private MapLinkApi() {
    }

    /**
     * @return whether Map Link is connected to the web map of the server the client is on
     */
    public static boolean isConnected() {
        return AbstractModInitializer.connected && AbstractModInitializer.getConnection() != null;
    }

    /**
     * @return the players the web map showed at its last update, empty when not connected.
     * The list does not change: call again for newer positions.
     */
    public static List<WebMapPlayer> getWebMapPlayers() {
        return isConnected() ? players : List.of();
    }

    /**
     * @return {@link System#currentTimeMillis()} of the last web map update, 0 if there was none
     * since the client joined the server
     */
    public static long getLastUpdateTime() {
        return lastUpdateTime;
    }

    @ApiStatus.Internal
    public static void publish(Collection<PlayerPosition> positions, MapConnection connection) {
        long now = System.currentTimeMillis();
        Minecraft mc = Minecraft.getInstance();
        String clientName = mc.player != null ? mc.player.getName().getString() : null;
        String dimension = clientDimension(mc);
        List<WebMapPlayer> list = new ArrayList<>(positions.size());
        for (PlayerPosition p : positions) {
            // debug mode keeps the local player in the list
            if (Objects.equals(p.name, clientName)) continue;
            boolean sameWorld = dimension != null && Objects.equals(p.world, connection.currentDimension);
            list.add(new WebMapPlayer(p.name, p.pos.x, p.pos.y, p.pos.z, p.world,
                    sameWorld ? dimension : null, now));
        }
        players = List.copyOf(list);
        lastUpdateTime = now;
    }

    @ApiStatus.Internal
    public static void clear() {
        players = List.of();
        lastUpdateTime = 0;
    }

    private static @Nullable String clientDimension(Minecraft mc) {
        if (mc.level == null) return null;
        #if MC_VER >= MC_1_21_11
        return mc.level.dimension().identifier().toString();
        #else
        return mc.level.dimension().location().toString();
        #endif
    }
}
