package com.mik.create_storagedrawers_compat.mixin;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityController;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityControllerIO;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawers;
import com.simibubi.create.compat.thresholdSwitch.StorageDrawers;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StorageDrawers.class, remap = false)
public class MixinStorageDrawersThresholdCompat {
    @Inject(method = "isFromThisMod", at = @At("HEAD"), cancellable = true, remap = false)
    public void isFromThisMod(BlockEntity blockEntity, CallbackInfoReturnable<Boolean> cir) {
        if (blockEntity instanceof BlockEntityDrawers || blockEntity instanceof BlockEntityController || blockEntity instanceof BlockEntityControllerIO || blockEntity instanceof IDrawerGroup) {
            cir.setReturnValue(true);
        }
    }
}
