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
import com.illusivesoulworks.charmofundying.common.network.SPacketUseTotemPayload;
import com.illusivesoulworks.charmofundying.platform.services.IPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class NeoForgePlatform implements IPlatform {

  @Override
  public ItemStack findTotem(LivingEntity livingEntity) {
    return CuriosApi.getCuriosInventory(livingEntity).map(
        inv -> inv.findFirstCurio(stack -> TotemProviders.IS_TOTEM.test(stack.getItem()))
            .map(SlotResult::stack).orElse(ItemStack.EMPTY)).orElse(ItemStack.EMPTY);
  }

  @Override
  public String getRegistryName(Item item) {
    ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
    return rl.toString();
  }

  @Override
  public boolean isModLoaded(String name) {
    return ModList.get().isLoaded(name);
  }

  @Override
  public void broadcastTotemEvent(LivingEntity livingEntity) {
    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(livingEntity)
        .send(new SPacketUseTotemPayload(livingEntity.getId()));
  }
}
