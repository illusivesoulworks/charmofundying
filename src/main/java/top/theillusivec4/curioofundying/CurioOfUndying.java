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

import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurio;

@Mod(CurioOfUndying.MODID)
public class CurioOfUndying {

  public static final String MODID = "curioofundying";

  public CurioOfUndying() {
    final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::setup);
    eventBus.addListener(this::enqueue);
  }

  private void setup(final FMLCommonSetupEvent evt) {
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void enqueue(final InterModEnqueueEvent evt) {
    InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
        () -> SlotTypePreset.CHARM.getMessageBuilder().build());
  }

  @SubscribeEvent
  public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {

    if (evt.getObject().getItem() != Items.TOTEM_OF_UNDYING) {
      return;
    }
    ICurio curio = new ICurio() {

      @Override
      public boolean canRightClickEquip() {
        return true;
      }

      @Override
      public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
        return true;
      }

      @Override
      public void render(String identifier, int index, MatrixStack matrixStack,
          IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing,
          float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
          float headPitch) {
        ICurio.RenderHelper.translateIfSneaking(matrixStack, livingEntity);
        ICurio.RenderHelper.rotateIfSneaking(matrixStack, livingEntity);
        matrixStack.scale(0.35F, 0.35F, 0.35F);
        matrixStack.translate(0.0F, 0.5F, -0.4F);
        matrixStack.rotate(Direction.DOWN.getRotation());
        Minecraft.getInstance().getItemRenderer()
            .renderItem(evt.getObject(), TransformType.NONE, light, OverlayTexture.NO_OVERLAY,
                matrixStack, renderTypeBuffer);
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

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingDeath(LivingDeathEvent evt) {

    if (hasTotemProtection(evt.getEntityLiving(), evt.getSource())) {
      evt.setCanceled(true);
    }
  }

  private boolean hasTotemProtection(LivingEntity livingEntity, DamageSource source) {

    if (source.canHarmInCreative()) {
      return false;
    }

    for (ItemStack held : livingEntity.getHeldEquipment()) {

      if (held.getItem() == Items.TOTEM_OF_UNDYING) {
        return false;
      }
    }
    return CuriosApi.getCuriosHelper().findEquippedCurio(Items.TOTEM_OF_UNDYING, livingEntity)
        .map(totem -> {
          this.activateTotem(livingEntity, totem.getRight());
          return true;
        }).orElse(false);
  }

  private void activateTotem(LivingEntity livingEntity, ItemStack totem) {
    ItemStack copy = totem.copy();
    totem.shrink(1);

    if (livingEntity instanceof ServerPlayerEntity) {
      ServerPlayerEntity serverPlayer = (ServerPlayerEntity) livingEntity;
      serverPlayer.addStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
      CriteriaTriggers.USED_TOTEM.trigger(serverPlayer, copy);
    }
    livingEntity.setHealth(1.0F);
    livingEntity.clearActivePotions();
    livingEntity.addPotionEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
    livingEntity.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
    livingEntity.world.setEntityState(livingEntity, (byte) 35);
  }
}
