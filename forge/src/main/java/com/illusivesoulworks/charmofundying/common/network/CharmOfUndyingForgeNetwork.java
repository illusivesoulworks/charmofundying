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

import com.illusivesoulworks.charmofundying.CharmOfUndyingConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class CharmOfUndyingForgeNetwork {

  private static final int PTC_VERSION = 1;

  private static SimpleChannel instance;

  public static SimpleChannel get() {
    return instance;
  }

  public static void setup() {
    instance = ChannelBuilder.named(new ResourceLocation(CharmOfUndyingConstants.MOD_ID, "main"))
        .networkProtocolVersion(PTC_VERSION)
        .clientAcceptedVersions(Channel.VersionTest.exact(PTC_VERSION))
        .serverAcceptedVersions(Channel.VersionTest.exact(PTC_VERSION)).simpleChannel();

    instance.messageBuilder(SPacketUseTotem.class)
        .encoder(SPacketUseTotem::encode)
        .decoder(SPacketUseTotem::decode)
        .consumerNetworkThread((message, context) -> {
          context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
              () -> () -> SPacketUseTotem.handle(message)));
          context.setPacketHandled(true);
        })
        .add();
  }
}
