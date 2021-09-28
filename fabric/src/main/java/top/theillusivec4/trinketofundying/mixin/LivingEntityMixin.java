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

package top.theillusivec4.trinketofundying.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.trinketofundying.TrinketOfUndying;

@Mixin(value = LivingEntity.class)
public class LivingEntityMixin {

  @SuppressWarnings("ConstantConditions")
  @Inject(method = "tryUseTotem", at = @At(value = "HEAD"), cancellable = true)
  private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> ci) {
    LivingEntity livingEntity = (LivingEntity) (Object) this;

    if (TrinketOfUndying.tryUseCurioTotem(livingEntity, source)) {
      ci.setReturnValue(true);
    }
  }
}
