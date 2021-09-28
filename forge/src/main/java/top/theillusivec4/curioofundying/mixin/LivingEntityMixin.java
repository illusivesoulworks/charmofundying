/*
 * Copyright (C) 2019-2021 C4
 *
 * This file is part of Curio of Undying.
 *
 * Curio of Undying is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Curio of Undying is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with Curio of Undying.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 */

package top.theillusivec4.curioofundying.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curioofundying.CurioOfUndyingMod;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("HEAD"), method = "checkTotemDeathProtection", cancellable = true)
  private void curioofundying$checkTotemDeathProtection(DamageSource src,
                                                        CallbackInfoReturnable<Boolean> cir) {
    if (src.isBypassInvul()) {
      cir.setReturnValue(false);
    } else if (CurioOfUndyingMod.hasTotem((LivingEntity) (Object) this)) {
      cir.setReturnValue(true);
    }
  }
}
