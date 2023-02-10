package com.talhanation.recruits.init;

import com.google.common.collect.ImmutableSet;
import com.talhanation.recruits.Main;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class ModPois {
    private static final Logger logger = LogManager.getLogger(Main.MOD_ID);
    public static final DeferredRegister<PoiType> POIS = DeferredRegister.create(ForgeRegistries.POI_TYPES, Main.MOD_ID);
    public static final RegistryObject<PoiType> POI_RECRUIT = makePoi("recruit", ModBlocks.RECRUIT_BLOCK);
    public static final RegistryObject<PoiType> POI_SHIELDMAN = makePoi("shieldman", ModBlocks.RECRUIT_SHIELD_BLOCK);
    public static final RegistryObject<PoiType> POI_BOWMAN = makePoi("bowman", ModBlocks.BOWMAN_BLOCK);

    private static RegistryObject<PoiType> makePoi(String name, RegistryObject<Block> block) {
        logger.info("ezo--Registering POI for " + block.getKey().toString());
        return POIS.register(name, () -> {
            Set<BlockState> blockStates = ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates());
            return new PoiType(blockStates, 1, 1);
        });
    }
}
