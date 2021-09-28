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

package top.theillusivec4.curioofundying.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class TotemRenderer implements ICurioRenderer {

  @Override
  public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack,
                                                                        SlotContext slotContext,
                                                                        PoseStack matrixStack,
                                                                        RenderLayerParent<T, M> renderLayerParent,
                                                                        MultiBufferSource renderTypeBuffer,
                                                                        int light, float limbSwing,
                                                                        float limbSwingAmount,
                                                                        float partialTicks,
                                                                        float ageInTicks,
                                                                        float netHeadYaw,
                                                                        float headPitch) {
    LivingEntity livingEntity = slotContext.entity();
    ICurioRenderer.translateIfSneaking(matrixStack, livingEntity);
    ICurioRenderer.rotateIfSneaking(matrixStack, livingEntity);
    matrixStack.scale(0.35F, 0.35F, 0.35F);
    matrixStack.translate(0.0F, 0.5F, -0.4F);
    matrixStack.mulPose(Direction.DOWN.getRotation());
    Minecraft.getInstance().getItemRenderer()
        .renderStatic(stack, ItemTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY,
            matrixStack, renderTypeBuffer, 0);
  }
}
