/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.prison.spigot;

import io.github.prison.Prison;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The plugin class for the Spigot implementation.
 *
 * @author SirFaizdat
 */
public class SpigotPrison extends JavaPlugin {

    @Override
    public void onEnable() {
        Prison.getInstance().init(new SpigotPlatform(this));
    }

    @Override
    public void onDisable() {
        Prison.getInstance().deinit();
    }

}