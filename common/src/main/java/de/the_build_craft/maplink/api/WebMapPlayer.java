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

import org.jetbrains.annotations.Nullable;

/**
 * A player as the web map last showed them.
 *
 * @param name        the player's name, as the web map gives it
 * @param x           the position the web map gives (whole blocks on some web maps)
 * @param y           the position the web map gives
 * @param z           the position the web map gives
 * @param webMapWorld the web map's name for the world the player is in
 * @param dimension   the id of the client's dimension (like "minecraft:overworld") when the web map
 *                    places the player in the same world as the client, null otherwise
 * @param updateTime  {@link System#currentTimeMillis()} when Map Link read this position
 * @author clixmods
 * @version 29.10.2026
 */
public record WebMapPlayer(String name, double x, double y, double z, String webMapWorld,
                           @Nullable String dimension, long updateTime) {
}
