package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.lib.Constants;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    public static boolean generateActinium;
    public static int actiniumMaxLevel, actiniumVeinSize, actiniumVeinsPerChunk, actiniumMinLevel, tickTesla, tickRF;
    public static int searchRange;

    public static void init(Configuration config) {

        config.load();

        generateActinium      = config.get(Constants.CONFIG_ORE_GENERATION, "Generate Actinium", true).getBoolean();
        actiniumVeinSize      = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Vein Size", 3).getInt();
        actiniumVeinsPerChunk = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Veins Per Chunk", 10).getInt();
        actiniumMinLevel      = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Min Level", 5).getInt();
        actiniumMaxLevel      = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Max Level", 64).getInt();

        tickTesla             = config.get(Constants.CONFIG_TESLA, "# of Tesla per Tick", 10).getInt();
        tickRF                = config.get(Constants.CONFIG_TESLA, "# of RF's per Tick per Tesla", 10).getInt();
        searchRange           = config.get(Constants.CONFIG_TESLA, "How many blocks (cubed) to search for coils", 5).getInt();

        config.save();
    }
}
