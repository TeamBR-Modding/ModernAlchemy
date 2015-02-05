package com.dyonovan.itemreplication.handlers;

import com.dyonovan.itemreplication.lib.Constants;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    public static boolean generateActinium;
    public static int actiniumVeinSize;
    public static int actiniumVeinsPerChunk;
    public static int actiniumMinLevel;
    public static int actiniumMaxLevel;

    public static void init(Configuration config) {

        config.load();

        generateActinium      = config.get(Constants.CONFIG_ORE_GENERATION, "Generate Actinium", true).getBoolean();
        actiniumVeinSize      = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Vein Size", 3).getInt();
        actiniumVeinsPerChunk = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Veins Per Chunk", 10).getInt();
        actiniumMinLevel      = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Min Level", 5).getInt();
        actiniumMaxLevel      = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Max Level", 64).getInt();

        config.save();
    }
}
