package com.illusivesoulworks.charmofundying.common.network;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class CharmOfUndyingClientPayloadHandler {

  private static final CharmOfUndyingClientPayloadHandler INSTANCE =
      new CharmOfUndyingClientPayloadHandler();

  public static CharmOfUndyingClientPayloadHandler getInstance() {
    return INSTANCE;
  }

  public void handleUseTotem(SPacketUseTotemPayload msg, PlayPayloadContext ctx) {
    ctx.workHandler().submitAsync(() -> SPacketUseTotem.handle(msg))
        .exceptionally(e -> {
          ctx.packetHandler()
              .disconnect(
                  Component.translatable("charmofundying.networking.failed", e.getMessage()));
          return null;
        });
  }
}
