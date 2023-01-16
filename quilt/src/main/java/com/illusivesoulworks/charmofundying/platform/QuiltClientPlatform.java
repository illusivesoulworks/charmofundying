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

import com.illusivesoulworks.charmofundying.platform.services.IClientPlatform;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;

public class QuiltClientPlatform implements IClientPlatform {

  @SuppressWarnings("unchecked")
  @Override
  public void translateToPosition(LivingEntity livingEntity,
                                  EntityModel<? extends LivingEntity> model, PoseStack poseStack) {

    if (livingEntity instanceof AbstractClientPlayer player) {
      TrinketRenderer.translateToChest(poseStack, (PlayerModel<AbstractClientPlayer>) model,
          player);
    }
  }
}
