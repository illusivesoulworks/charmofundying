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

package com.illusivesoulworks.charmofundying.common.network;

import com.illusivesoulworks.charmofundying.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;

public record SPacketUseTotem(int id) {

  public static void encode(SPacketUseTotem packet, FriendlyByteBuf buffer) {
    buffer.writeInt(packet.id());
  }

  public static SPacketUseTotem decode(FriendlyByteBuf buffer) {
    return new SPacketUseTotem(buffer.readInt());
  }

  public static void handle(SPacketUseTotem msg) {
    Minecraft mc = Minecraft.getInstance();
    ClientLevel level = mc.level;

    if (level != null) {
      Entity entity = mc.level.getEntity(msg.id());

      if (entity != null) {
        mc.particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
        entity.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(),
            SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);

        if (entity == mc.player) {
          mc.gameRenderer.displayItemActivation(Services.PLATFORM.findTotem(mc.player));
        }
      }
    }
  }
}
