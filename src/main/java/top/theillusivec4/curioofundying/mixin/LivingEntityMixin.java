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

package top.theillusivec4.curioofundying.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curioofundying.CurioOfUndying;

@Mixin(value = LivingEntity.class)
public class LivingEntityMixin {

  @SuppressWarnings("ConstantConditions")
  @Inject(method = "tryUseTotem", at = @At(value = "HEAD"), cancellable = true)
  private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> ci) {
    LivingEntity livingEntity = (LivingEntity) (Object) this;

    if (CurioOfUndying.tryUseCurioTotem(livingEntity, source)) {
      ci.setReturnValue(true);
    }
  }
}
