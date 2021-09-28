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

package top.theillusivec4.curioofundying;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curioofundying.client.TotemRenderer;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICurio;

@Mod(CurioOfUndyingMod.MOD_ID)
public class CurioOfUndyingMod {

  public static final String MOD_ID = "curioofundying";

  public CurioOfUndyingMod() {
    final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::setup);
    eventBus.addListener(this::clientSetup);
    eventBus.addListener(this::enqueue);
  }

  private void setup(final FMLCommonSetupEvent evt) {
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    CuriosRendererRegistry.register(Items.TOTEM_OF_UNDYING, TotemRenderer::new);
  }

  private void enqueue(final InterModEnqueueEvent evt) {
    InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
        () -> SlotTypePreset.CHARM.getMessageBuilder().build());
  }

  @SuppressWarnings("unused")
  @SubscribeEvent
  public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {

    if (evt.getObject().getItem() != Items.TOTEM_OF_UNDYING) {
      return;
    }
    ICurio curio = new ICurio() {
      @Override
      public ItemStack getStack() {
        return evt.getObject();
      }

      @Override
      public boolean canEquipFromUse(SlotContext ctx) {
        return true;
      }
    };
    ICapabilityProvider provider = new ICapabilityProvider() {
      private final LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);

      @Nonnull
      @Override
      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
                                               @Nullable Direction side) {
        return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
      }
    };
    evt.addCapability(CuriosCapability.ID_ITEM, provider);
  }

  public static boolean hasTotem(LivingEntity livingEntity) {
    ItemStack stack =
        CuriosApi.getCuriosHelper().findEquippedCurio(Items.TOTEM_OF_UNDYING, livingEntity)
            .map(ImmutableTriple::getRight).orElse(ItemStack.EMPTY);

    if (!stack.isEmpty()) {
      stack.shrink(1);

      if (livingEntity instanceof ServerPlayer player) {
        player.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
        CriteriaTriggers.USED_TOTEM.trigger(player, stack);
      }
      livingEntity.setHealth(1.0F);
      livingEntity.removeAllEffects();
      livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
      livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
      livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
      livingEntity.level.broadcastEntityEvent(livingEntity, (byte) 35);
      return true;
    }
    return false;
  }
}
