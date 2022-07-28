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

package com.illusivesoulworks.charmofundying.mixin;

import com.illusivesoulworks.charmofundying.CharmOfUndyingCommonMod;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public class MixinLivingEntity {

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("HEAD"), method = "checkTotemDeathProtection", cancellable = true)
  private void charmofundying$checkTotemDeathProtection(DamageSource src,
                                                        CallbackInfoReturnable<Boolean> cir) {
    if (CharmOfUndyingCommonMod.hasTotem((LivingEntity) (Object) this)) {
      if (src.isBypassInvul()) {
        cir.setReturnValue(false);
      } else {
        cir.setReturnValue(true);
      }
    }
  }
}
