package com.mik.create_storagedrawers_compat.mixin;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.capabilities.DrawerItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = DrawerItemHandler.class, remap = false)
public interface DrawerItemHandlerAccessor {
    @Accessor("group")
    IDrawerGroup create_storagedrawers_compat$getGroup();
}
