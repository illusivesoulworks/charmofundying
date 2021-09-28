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

package top.theillusivec4.trinketofundying;

import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;

public class TrinketOfUndyingClient implements ClientModInitializer {

  @SuppressWarnings("unchecked")
  @Override
  public void onInitializeClient() {
    TrinketRendererRegistry.registerRenderer(Items.TOTEM_OF_UNDYING,
        (stack, slotReference, contextModel, matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch) -> {
          if (entity instanceof AbstractClientPlayerEntity player) {
            TrinketRenderer.translateToChest(matrices,
                (PlayerEntityModel<AbstractClientPlayerEntity>) contextModel, player);
            matrices.scale(0.35F, 0.35F, 0.35F);
            matrices.multiply(Direction.DOWN.getRotationQuaternion());
            MinecraftClient.getInstance().getItemRenderer()
                .renderItem(stack, Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices,
                    vertexConsumers, 0);
          }
        });
  }
}
