package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.lib.Constants;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    public static int oreVeinSize;
    public static int oreVeinsPerChunk;
    public static int oreMinLevel;
    public static int oreMaxLevel;

    public static void init(Configuration config) {

        config.load();

        oreVeinSize      = config.get(Constants.CONFIG_ORE_GENERATION, "Ore Vein Size", 3).getInt();
        oreVeinsPerChunk = config.get(Constants.CONFIG_ORE_GENERATION, "Ore Veins Per Chunk", 10).getInt();
        oreMinLevel      = config.get(Constants.CONFIG_ORE_GENERATION, "Ore Min Level", 0).getInt();
        oreMaxLevel      = config.get(Constants.CONFIG_ORE_GENERATION, "Ore Max Level", 64).getInt();

        config.save();
    }
}
