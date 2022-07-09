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
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class ForgeClientPlatform implements IClientPlatform {

  @Override
  public void translateToPosition(LivingEntity livingEntity,
                                  EntityModel<? extends LivingEntity> model, PoseStack poseStack) {
    ICurioRenderer.translateIfSneaking(poseStack, livingEntity);
    ICurioRenderer.rotateIfSneaking(poseStack, livingEntity);
    poseStack.translate(0.0F, 0.4F, -0.15F);
  }
}
