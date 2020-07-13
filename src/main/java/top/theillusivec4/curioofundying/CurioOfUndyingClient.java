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

package top.theillusivec4.curioofundying;

import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import top.theillusivec4.curios.api.CuriosComponent;
import top.theillusivec4.curios.api.type.component.IRenderableCurio;

public class CurioOfUndyingClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    ItemComponentCallbackV2.event(Items.TOTEM_OF_UNDYING).register(
        ((item, itemStack, componentContainer) -> componentContainer
            .put(CuriosComponent.ITEM_RENDER, new IRenderableCurio() {
              @Override
              public void render(String identifier, int index, MatrixStack matrixStack,
                  VertexConsumerProvider vertexConsumerProvider, int light,
                  LivingEntity livingEntity, float limbSwing, float limbSwingAmount,
                  float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                IRenderableCurio.RenderHelper.translateIfSneaking(matrixStack, livingEntity);
                IRenderableCurio.RenderHelper.rotateIfSneaking(matrixStack, livingEntity);
                matrixStack.scale(0.35F, 0.35F, 0.35F);
                matrixStack.translate(0.0F, 0.5F, -0.4F);
                matrixStack.multiply(Direction.DOWN.getRotationQuaternion());
                MinecraftClient.getInstance().getItemRenderer()
                    .renderItem(itemStack, Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrixStack,
                        vertexConsumerProvider);
              }
            })));
  }
}
