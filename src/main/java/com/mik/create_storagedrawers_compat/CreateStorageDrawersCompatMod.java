package com.mik.create_storagedrawers_compat;

import com.simibubi.create.api.packager.unpacking.UnpackingHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CreateStorageDrawersCompatMod.MOD_ID)
public class CreateStorageDrawersCompatMod {
    public static final String MOD_ID = "create_storagedrawers_compat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public CreateStorageDrawersCompatMod(IEventBus modEventBus) {
        LOGGER.info("Create & Storage Drawers Compat initialized");
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            UnpackingHandler.REGISTRY.registerProvider(block -> {
                ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
                if (key != null && "storagedrawers".equals(key.getNamespace())) {
                    return StorageDrawersUnpackingHandler.INSTANCE;
                }
                return null;
            });
            LOGGER.info("Registered StorageDrawersUnpackingHandler into UnpackingHandler.REGISTRY");
        });
    }
}
