package com.mik.create_storagedrawers_compat.mixin;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.capabilities.DrawerItemHandler;
import com.mik.create_storagedrawers_compat.StorageDrawersUnpackingHandler;
import com.simibubi.create.content.logistics.stockTicker.PackageOrderWithCrafts;
import com.simibubi.create.impl.unpacking.DefaultUnpackingHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = DefaultUnpackingHandler.class, remap = false)
public class MixinDefaultUnpackingHandler {
    @Inject(method = "unpack", at = @At("HEAD"), cancellable = true, remap = false)
    private void injectUnpack(Level level, BlockPos pos, BlockState state, Direction side, List<ItemStack> items, PackageOrderWithCrafts orderContext, boolean simulate, CallbackInfoReturnable<Boolean> cir) {
        BlockEntity targetBE = level.getBlockEntity(pos);
        if (targetBE == null) {
            return;
        }
        IItemHandler targetInv = Capabilities.ItemHandler.BLOCK.getCapability(level, pos, state, targetBE, side);
        if (targetInv instanceof DrawerItemHandler || targetBE instanceof IDrawerGroup || targetBE instanceof BlockEntityDrawers) {
            cir.setReturnValue(StorageDrawersUnpackingHandler.INSTANCE.unpack(level, pos, state, side, items, orderContext, simulate));
        }
    }
}
