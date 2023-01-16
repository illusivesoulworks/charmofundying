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
import com.illusivesoulworks.charmofundying.common.integration.FWaystonesVoidTotemEffectProvider;
import java.util.HashSet;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class CharmOfUndyingQuiltMod implements ModInitializer {

  @Override
  public void onInitialize(ModContainer modContainer) {
    CharmOfUndyingCommonMod.init();

    if (QuiltLoader.isModLoaded("fwaystones")) {
      FWaystonesVoidTotemEffectProvider.init();
    }
    boolean isClient = MinecraftQuiltLoader.getEnvironmentType() == EnvType.CLIENT;
    Set<String> totems = new HashSet<>(TotemProviders.getItems());

    if (isClient) {
      Set<String> remove = new HashSet<>();

      for (String name : totems) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(name));

        if (item != Items.AIR) {
          FabricClientHooks.registerTrinketRenderer(item);
          remove.add(name);
        }
      }
      totems.removeAll(remove);
    }
    RegistryEntryAddedCallback.event(BuiltInRegistries.ITEM).register((rawId, id, object) -> {

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
