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
import com.illusivesoulworks.charmofundying.common.ITotemEffectProvider;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public class MixinLivingEntity {

  @Unique
  Pair<ITotemEffectProvider, ItemStack> charmofundying$totem;

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At(value = "HEAD"), method = "checkTotemDeathProtection", cancellable = true)
  private void charmofundying$precheckTotemDeathProtection(DamageSource src,
                                                           CallbackInfoReturnable<Boolean> cir) {
    charmofundying$totem =
        CharmOfUndyingCommonMod.getEffectProvider((LivingEntity) (Object) this).orElse(null);

    if (charmofundying$totem != null && charmofundying$totem.getFirst().bypassInvul() &&
        CharmOfUndyingCommonMod.useTotem(charmofundying$totem, src, (LivingEntity) (Object) this)) {
      cir.setReturnValue(true);
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At(value = "INVOKE", target = "net/minecraft/world/InteractionHand.values()[Lnet/minecraft/world/InteractionHand;"), method = "checkTotemDeathProtection", cancellable = true)
  private void charmofundying$checkTotemDeathProtection(DamageSource src,
                                                        CallbackInfoReturnable<Boolean> cir) {
    if (charmofundying$totem != null && !charmofundying$totem.getFirst().bypassInvul() &&
        CharmOfUndyingCommonMod.useTotem(charmofundying$totem, src, (LivingEntity) (Object) this)) {
      cir.setReturnValue(true);
    }
  }
}
