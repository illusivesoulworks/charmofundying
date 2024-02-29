package com.illusivesoulworks.charmofundying.common.network;

import com.illusivesoulworks.charmofundying.CharmOfUndyingConstants;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class SPacketUseTotemPayload extends SPacketUseTotem implements CustomPacketPayload {

  public static final ResourceLocation ID =
      new ResourceLocation(CharmOfUndyingConstants.MOD_ID, "use_totem");

  public SPacketUseTotemPayload(int id) {
    super(id);
  }

  public SPacketUseTotemPayload(FriendlyByteBuf buf) {
    this(buf.readInt());
  }

  @Override
  public void write(@Nonnull FriendlyByteBuf buf) {
    SPacketUseTotem.encode(this, buf);
  }

  @Nonnull
  @Override
  public ResourceLocation id() {
    return ID;
  }
}
