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

package com.illusivesoulworks.charmofundying.common.integration;

import com.illusivesoulworks.charmofundying.common.ITotemEffectProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BMEnchantedTotemEffectProvider implements ITotemEffectProvider {

  @Override
  public boolean applyEffects(LivingEntity livingEntity, DamageSource damageSource,
                              ItemStack stack) {
    livingEntity.setHealth(livingEntity.getMaxHealth() / 2F);
    livingEntity.removeAllEffects();
    livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 500, 1));
    livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 3));
    livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2000, 0));
    livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2000, 0));
    return true;
  }
}
