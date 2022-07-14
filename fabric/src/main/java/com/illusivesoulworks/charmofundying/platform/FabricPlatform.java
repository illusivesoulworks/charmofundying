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

package com.illusivesoulworks.charmofundying.platform;

import com.illusivesoulworks.charmofundying.common.TotemProviders;
import com.illusivesoulworks.charmofundying.platform.services.IPlatform;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FabricPlatform implements IPlatform {

  @Override
  public ItemStack findTotem(LivingEntity livingEntity) {
    return TrinketsApi.getTrinketComponent(livingEntity).map(component -> {
      List<Tuple<SlotReference, ItemStack>> res =
          component.getEquipped(stack -> TotemProviders.IS_TOTEM.test(stack.getItem()));
      return res.size() > 0 ? res.get(0).getB() : ItemStack.EMPTY;
    }).orElse(ItemStack.EMPTY);
  }

  @Override
  public String getRegistryName(Item item) {
    return Registry.ITEM.getKey(item).toString();
  }

  @Override
  public boolean isModLoaded(String name) {
    return FabricLoader.getInstance().isModLoaded(name);
  }
}
