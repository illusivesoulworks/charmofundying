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

package com.illusivesoulworks.charmofundying.client;

import com.illusivesoulworks.charmofundying.CharmOfUndyingConfig;
import com.illusivesoulworks.charmofundying.platform.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TotemRenderer {

  public static void render(LivingEntity livingEntity, EntityModel<? extends LivingEntity> model,
                            PoseStack poseStack, ItemStack stack, MultiBufferSource buffer,
                            int light) {

    if (CharmOfUndyingConfig.SERVER.renderTotem.get()) {
      poseStack.pushPose();
      Services.CLIENT_PLATFORM.translateToPosition(livingEntity, model, poseStack);
      poseStack.translate(0.0F, -0.2F + CharmOfUndyingConfig.SERVER.yOffset.get(),
          0.0F + CharmOfUndyingConfig.SERVER.xOffset.get());
      poseStack.scale(0.35F, 0.35F, 0.35F);
      poseStack.mulPose(Direction.DOWN.getRotation());
      Minecraft.getInstance().getItemRenderer()
          .renderStatic(stack, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY,
              poseStack, buffer, livingEntity.level(), 0);
      poseStack.popPose();
    }
  }
}
