/*
 * Copyright (C) 2019-2022 Illusive Soulworks
 *
 * Charm of Undying is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Charm of Undying is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with Charm of Undying.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.charmofundying;

import com.illusivesoulworks.charmofundying.common.TotemProviders;
import java.util.HashSet;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CharmOfUndyingFabricMod implements ModInitializer {

  @Override
  public void onInitialize() {
    CharmOfUndyingCommonMod.init();
    boolean isClient = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    Set<String> totems = new HashSet<>(TotemProviders.getItems());

    if (isClient) {
      Set<String> remove = new HashSet<>();

      for (String name : totems) {
        Item item = Registry.ITEM.get(ResourceLocation.tryParse(name));

        if (item != Items.AIR) {
          FabricClientHooks.registerTrinketRenderer(item);
          remove.add(name);
        }
      }
      totems.removeAll(remove);
    }
    RegistryEntryAddedCallback.event(Registry.ITEM).register((rawId, id, object) -> {

      if (isClient && !totems.isEmpty()) {
        String name = id.toString();

        if (totems.contains(name)) {
          FabricClientHooks.registerTrinketRenderer(object);
          totems.remove(name);
        }
      }
    });
  }
}
