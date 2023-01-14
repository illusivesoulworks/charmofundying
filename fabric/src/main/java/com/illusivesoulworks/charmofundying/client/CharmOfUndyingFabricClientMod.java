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

import com.illusivesoulworks.charmofundying.CharmOfUndyingConstants;
import com.illusivesoulworks.charmofundying.common.network.SPacketUseTotem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class CharmOfUndyingFabricClientMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    ClientPlayNetworking.registerGlobalReceiver(CharmOfUndyingConstants.TOTEM_EVENT,
        (client, listener, buf, responseSender) -> {
          SPacketUseTotem msg = SPacketUseTotem.decode(buf);
          client.execute(() -> SPacketUseTotem.handle(msg));
        });
  }
}
