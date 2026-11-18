package com.mik.create_storagedrawers_compat;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.capabilities.DrawerItemHandler;
import com.mik.create_storagedrawers_compat.mixin.DrawerItemHandlerAccessor;
import com.simibubi.create.api.packager.unpacking.UnpackingHandler;
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
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

public enum StorageDrawersUnpackingHandler implements UnpackingHandler {
    INSTANCE;

    private static class SimSlot {
        final IDrawer drawer;
        boolean empty;
        ItemStack protoStack;
        int count;
        int maxCapacity;

        SimSlot(IDrawer d) {
            this.drawer = d;
            ItemStack proto = d.getStoredItemPrototype();
            if (proto.isEmpty() || d.isEmpty()) {
                this.empty = true;
                this.protoStack = ItemStack.EMPTY;
                this.count = 0;
                this.maxCapacity = 0;
            } else {
                this.empty = false;
                this.protoStack = proto;
                this.count = d.getStoredItemCount();
                this.maxCapacity = d.getMaxCapacity();
            }
        }
    }

    @Override
    public boolean unpack(Level level, BlockPos pos, BlockState state, Direction side, List<ItemStack> items, PackageOrderWithCrafts orderContext, boolean simulate) {
        BlockEntity targetBE = level.getBlockEntity(pos);
        if (targetBE == null) {
            return false;
        }

        IItemHandler targetInv = Capabilities.ItemHandler.BLOCK.getCapability(level, pos, state, targetBE, side);
        if (targetInv == null) {
            return false;
        }

        IDrawerGroup group = null;
        if (targetInv instanceof DrawerItemHandlerAccessor accessor) {
            group = accessor.create_storagedrawers_compat$getGroup();
        } else if (targetBE instanceof IDrawerGroup dg) {
            group = dg;
        } else if (targetBE instanceof BlockEntityDrawers bed) {
            group = bed.getGroup();
        }

        if (group == null || !group.isGroupValid()) {
            return DefaultUnpackingHandler.INSTANCE.unpack(level, pos, state, side, items, orderContext, simulate);
        }

        if (!simulate) {
            for (ItemStack itemStack : items) {
                if (!itemStack.isEmpty()) {
                    ItemHandlerHelper.insertItemStacked(targetInv, itemStack.copy(), false);
                }
            }
            return true;
        }

        int drawerCount = group.getDrawerCount();
        List<SimSlot> simSlots = new ArrayList<>(drawerCount);
        for (int i = 0; i < drawerCount; i++) {
            IDrawer d = group.getDrawer(i);
            if (d == null || !d.isEnabled()) {
                continue;
            }
            IDrawerAttributes attrs = d.getAttributes();
            if (attrs != null && attrs.isSuspended()) {
                continue;
            }
            simSlots.add(new SimSlot(d));
        }

        for (ItemStack toInsert : items) {
            if (toInsert.isEmpty()) {
                continue;
            }
            int remaining = toInsert.getCount();

            // Pass 1: Existing drawers with matching item
            for (SimSlot slot : simSlots) {
                if (remaining <= 0) break;
                if (slot.empty) continue;
                if (!slot.drawer.canItemBeStored(toInsert)) continue;
                if (!ItemStack.isSameItemSameComponents(slot.protoStack, toInsert)) continue;

                int space = slot.maxCapacity - slot.count;
                if (space > 0) {
                    int added = Math.min(space, remaining);
                    slot.count += added;
                    remaining -= added;
                }
            }

            // Pass 2: Empty drawers that accept this item
            if (remaining > 0) {
                for (SimSlot slot : simSlots) {
                    if (remaining <= 0) break;
                    if (!slot.empty) continue;
                    if (!slot.drawer.canItemBeStored(toInsert)) continue;

                    slot.empty = false;
                    slot.protoStack = toInsert.copy();
                    slot.maxCapacity = slot.drawer.getAcceptingMaxCapacity(toInsert);
                    int space = slot.maxCapacity;
                    int added = Math.min(space, remaining);
                    slot.count = added;
                    remaining -= added;
                }
            }

            if (remaining > 0) {
                return false; // Not all items fit in simulation
            }
        }

        return true;
    }
}
