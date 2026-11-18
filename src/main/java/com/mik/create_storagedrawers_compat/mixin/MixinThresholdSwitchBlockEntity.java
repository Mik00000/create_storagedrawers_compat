package com.mik.create_storagedrawers_compat.mixin;

import com.jaquadro.minecraft.storagedrawers.capabilities.DrawerItemHandler;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ThresholdSwitchBlockEntity.class, remap = false)
public class MixinThresholdSwitchBlockEntity {
    @Inject(method = "lambda$updateCurrentLevel$2", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onFallbackSlotLimit(ItemStack stackInSlot, IItemHandler inv, int finalSlot, CallbackInfoReturnable<Long> cir) {
        if (inv instanceof DrawerItemHandler) {
            if (finalSlot == 0) {
                cir.setReturnValue(0L);
            } else {
                cir.setReturnValue((long) inv.getSlotLimit(finalSlot));
            }
        }
    }
}
