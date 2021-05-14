package top.theillusivec4.curioofundying.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curioofundying.CurioOfUndying;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("HEAD"), method = "checkTotemDeathProtection", cancellable = true)
  private void _curioofundying_checkTotemDeathProtection(DamageSource src,
                                                         CallbackInfoReturnable<Boolean> cir) {
    if (src.canHarmInCreative()) {
      cir.setReturnValue(false);
    } else if (CurioOfUndying.hasTotem((LivingEntity) (Object) this)) {
      cir.setReturnValue(true);
    }
  }
}
