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
import com.illusivesoulworks.charmofundying.platform.Services;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CharmOfUndyingCommonMod {

  public static void init() {
    TotemProviders.init();
  }

  public static boolean hasTotem(LivingEntity livingEntity) {
    ItemStack stack = Services.PLATFORM.findTotem(livingEntity);

    if (!stack.isEmpty()) {
      ItemStack copy = stack.copy();
      stack.shrink(1);

      if (livingEntity instanceof ServerPlayer player) {
        player.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
        CriteriaTriggers.USED_TOTEM.trigger(player, copy);
      }
      TotemProviders.getEffectProvider(copy.getItem())
          .ifPresent(effectProvider -> effectProvider.applyEffects(livingEntity));
      livingEntity.level.broadcastEntityEvent(livingEntity, (byte) 35);
      return true;
    }
    return false;
  }
}