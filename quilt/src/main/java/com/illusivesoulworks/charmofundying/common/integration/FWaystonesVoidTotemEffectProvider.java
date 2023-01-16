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

package com.illusivesoulworks.charmofundying.common.integration;

import com.illusivesoulworks.charmofundying.common.ITotemEffectProvider;
import com.illusivesoulworks.charmofundying.common.TotemProviders;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import wraith.fwaystones.FabricWaystones;
import wraith.fwaystones.access.PlayerEntityMixinAccess;
import wraith.fwaystones.block.WaystoneBlockEntity;
import wraith.fwaystones.item.VoidTotem;
import wraith.fwaystones.util.TeleportSources;
import wraith.fwaystones.util.Utils;
import wraith.fwaystones.util.WaystonePacketHandler;

public class FWaystonesVoidTotemEffectProvider implements ITotemEffectProvider {

  public static void init() {
    TotemProviders.putEffectProvider("fwaystones:void_totem",
        new FWaystonesVoidTotemEffectProvider());
  }

  @Override
  public boolean applyEffects(LivingEntity livingEntity, DamageSource damageSource,
                              ItemStack stack) {

    if (!(livingEntity instanceof Player player)) {
      return false;
    }
    livingEntity.setHealth(1.0F);
    livingEntity.removeAllEffects();
    livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
    livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
    livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
    boolean teleported = false;

    if (livingEntity instanceof ServerPlayer serverPlayer) {
      FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
      ServerPlayNetworking.send(serverPlayer, WaystonePacketHandler.VOID_REVIVE, packet);
      String hash = VoidTotem.getBoundWaystone(stack);

      if (hash == null) {
        Set<String> discovered = ((PlayerEntityMixinAccess) player).getDiscoveredWaystones();

        if (!discovered.isEmpty()) {
          List<String> list = new ArrayList<>(discovered);
          hash = list.get(Utils.random.nextInt(list.size()));
        }
      }
      if (hash != null) {
        WaystoneBlockEntity waystone = FabricWaystones.WAYSTONE_STORAGE.getWaystoneEntity(hash);

        if (waystone != null) {
          player.fallDistance = 0;
          waystone.teleportPlayer(player, false, TeleportSources.VOID_TOTEM);
          teleported = true;
        }
      }
    }
    return teleported || !damageSource.isBypassInvul();
  }
}
