/*
 * Copyright (C) 2019-2021 C4
 *
 * Trinket of Undying is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Trinket of Undying is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Trinket of Undying.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.trinketofundying;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.List;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Pair;
import top.theillusivec4.trinketofundying.integration.DeadTotemsIntegration;

public class TrinketOfUndying implements ModInitializer {

  private static boolean deadTotemsLoaded = false;

  @Override
  public void onInitialize() {
    deadTotemsLoaded = FabricLoader.getInstance().isModLoaded("deadtotems");
  }

  public static boolean tryUseCurioTotem(LivingEntity livingEntity, DamageSource source) {

    if (source.isOutOfWorld()) {
      return false;
    } else {
      ItemStack stack = TrinketsApi.getTrinketComponent(livingEntity).map(component -> {
        List<Pair<SlotReference, ItemStack>> res = component.getEquipped(Items.TOTEM_OF_UNDYING);
        return res.size() > 0 ? res.get(0).getRight() : ItemStack.EMPTY;
      }).orElse(ItemStack.EMPTY);

      if (!stack.isEmpty()) {
        ItemStack stack2 = stack.copy();
        stack.decrement(1);

        if (deadTotemsLoaded) {
          DeadTotemsIntegration.giveDeadTotem(livingEntity);
        }

        if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
          serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
          Criteria.USED_TOTEM.trigger(serverPlayerEntity, stack2);
        }
        livingEntity.setHealth(1.0F);
        livingEntity.clearStatusEffects();
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
        livingEntity.world.sendEntityStatus(livingEntity, (byte) 35);
        return true;
      }
    }
    return false;
  }
}
