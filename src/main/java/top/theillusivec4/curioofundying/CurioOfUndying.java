package top.theillusivec4.curioofundying;

import net.minecraft.advancements.CriteriaTriggers;
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
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod(CurioOfUndying.MODID)
public class CurioOfUndying {

    public static final String MODID = "curioofundying";

    public CurioOfUndying() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::enqueue);
    }

    private void setup(final FMLCommonSetupEvent evt) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void enqueue(final InterModEnqueueEvent evt) {
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("charm"));
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {

        if (evt.getObject().getItem() == Items.TOTEM_OF_UNDYING) {
            ICurio curio = new ICurio() {

                @Override
                public boolean canRightClickEquip() {
                    return true;
                }
            };
            evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
                private final LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                    return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
                }
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingDeath(LivingDeathEvent evt) {

        if (checkTotemDeathProtection(evt.getEntityLiving(), evt.getSource())) {
            evt.setCanceled(true);
        }
    }

    private boolean checkTotemDeathProtection(LivingEntity livingBase, DamageSource source) {

        if (source.canHarmInCreative()) {
            return false;
        } else {

            for (ItemStack held : livingBase.getHeldEquipment()) {

                if (held.getItem() == Items.TOTEM_OF_UNDYING) {
                    return false;
                }
            }
            return CuriosAPI.getCurioEquipped(Items.TOTEM_OF_UNDYING, livingBase).map(totem -> {
                ItemStack stack = totem.getRight();
                ItemStack copy = stack.copy();
                stack.shrink(1);

                if (livingBase instanceof ServerPlayerEntity) {
                    ServerPlayerEntity entityplayermp = (ServerPlayerEntity) livingBase;
                    entityplayermp.addStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
                    CriteriaTriggers.USED_TOTEM.trigger(entityplayermp, copy);
                }
                livingBase.setHealth(1.0F);
                livingBase.clearActivePotions();
                livingBase.addPotionEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                livingBase.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                livingBase.world.setEntityState(livingBase, (byte)35);
                return true;
            }).orElse(false);
        }
    }
}
