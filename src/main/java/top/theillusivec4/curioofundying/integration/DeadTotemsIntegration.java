package top.theillusivec4.curioofundying.integration;

import dzwdz.deadtotems.DeadTotems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class DeadTotemsIntegration {

  public static void giveDeadTotem(LivingEntity livingEntity) {
    ItemStack stack = new ItemStack(DeadTotems.DEAD_TOTEM);

    if (livingEntity instanceof PlayerEntity) {
      ((PlayerEntity) livingEntity).giveItemStack(stack);
    } else {
      livingEntity.dropStack(stack);
    }
  }
}
