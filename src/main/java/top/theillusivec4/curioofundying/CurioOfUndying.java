/*
 * Copyright (c) 2019-2020 C4
 *
 * This file is part of Curio Of Undying, a mod made for Minecraft.
 *
 * Curio Of Undying is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Curio Of Undying is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Curio Of Undying.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.curioofundying;

import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;
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
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curioofundying.integration.DeadTotemsIntegration;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosComponent;
import top.theillusivec4.curios.api.SlotTypeInfo.BuildScheme;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.component.ICurio;

public class CurioOfUndying implements ModInitializer {

  private static boolean deadTotemsLoaded = false;

  @Override
  public void onInitialize() {
    deadTotemsLoaded = FabricLoader.getInstance().isModLoaded("deadtotems");
    CuriosApi.enqueueSlotType(BuildScheme.REGISTER, SlotTypePreset.CHARM.getInfoBuilder().build());
    ItemComponentCallbackV2.event(Items.TOTEM_OF_UNDYING).register(
        ((item, itemStack, componentContainer) -> componentContainer
            .put(CuriosComponent.ITEM, new ICurio() {
              @Override
              public boolean canRightClickEquip() {
                return true;
              }
            })));
  }

  public static boolean tryUseCurioTotem(LivingEntity livingEntity, DamageSource source) {

    if (source.isOutOfWorld()) {
      return false;
    } else {
      ItemStack stack = CuriosApi.getCuriosHelper()
          .findEquippedCurio(Items.TOTEM_OF_UNDYING, livingEntity).map(ImmutableTriple::getRight)
          .orElse(ItemStack.EMPTY);

      if (!stack.isEmpty()) {
        ItemStack stack2 = stack.copy();
        stack.decrement(1);

        if (deadTotemsLoaded) {
          DeadTotemsIntegration.giveDeadTotem(livingEntity);
        }

        if (livingEntity instanceof ServerPlayerEntity) {
          ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) livingEntity;
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
