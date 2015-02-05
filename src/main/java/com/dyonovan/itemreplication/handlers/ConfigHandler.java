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
        actiniumMinLevel      = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Min Level", 0).getInt();
        actiniumMaxLevel      = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Max Level", 64).getInt();

        if(actiniumMinLevel < 0) actiniumMinLevel = 0;
        if(actiniumMaxLevel < 0) actiniumMaxLevel = 0;
        if(actiniumMinLevel > actiniumMaxLevel) actiniumMinLevel = actiniumMaxLevel;
        if(actiniumVeinsPerChunk < 0) actiniumVeinsPerChunk = 0;
        if(actiniumVeinSize < 0) actiniumVeinSize = 0;

        config.save();
    }
}
