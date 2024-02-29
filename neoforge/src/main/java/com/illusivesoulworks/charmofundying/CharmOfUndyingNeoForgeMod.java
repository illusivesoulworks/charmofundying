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

package com.illusivesoulworks.charmofundying;

import com.illusivesoulworks.charmofundying.client.CurioTotemRenderer;
import com.illusivesoulworks.charmofundying.common.TotemProviders;
import com.illusivesoulworks.charmofundying.common.network.CharmOfUndyingClientPayloadHandler;
import com.illusivesoulworks.charmofundying.common.network.SPacketUseTotemPayload;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICurio;

@Mod(CharmOfUndyingConstants.MOD_ID)
public class CharmOfUndyingNeoForgeMod {

  public CharmOfUndyingNeoForgeMod(IEventBus eventBus) {
    CharmOfUndyingCommonMod.init();
    CharmOfUndyingConfig.setup();
    eventBus.addListener(this::registerCapabilities);
    eventBus.addListener(this::clientSetup);
    eventBus.addListener(this::registerPayloadHandler);
  }

  private void clientSetup(final FMLClientSetupEvent evt) {

    for (String name : TotemProviders.getItems()) {
      Item item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(name));

      if (item != Items.AIR) {
        CuriosRendererRegistry.register(item, CurioTotemRenderer::new);
      }
    }
  }

  private void registerPayloadHandler(final RegisterPayloadHandlerEvent evt) {
    evt.registrar(CharmOfUndyingConstants.MOD_ID)
        .play(SPacketUseTotemPayload.ID, SPacketUseTotemPayload::new,
            handler -> handler.client(
                CharmOfUndyingClientPayloadHandler.getInstance()::handleUseTotem));
  }

  private void registerCapabilities(final RegisterCapabilitiesEvent evt) {

    for (Item item : BuiltInRegistries.ITEM) {

      if (TotemProviders.IS_TOTEM.test(item)) {
        evt.registerItem(CuriosCapability.ITEM, (stack, ctx) -> new ICurio() {

          @Override
          public ItemStack getStack() {
            return stack;
          }

          @Override
          public boolean canEquipFromUse(SlotContext ctx) {
            return true;
          }
        });
      }
    }
  }
}