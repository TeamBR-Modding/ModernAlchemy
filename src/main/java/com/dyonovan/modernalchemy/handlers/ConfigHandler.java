package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    public static String folderLocation;

    public static boolean generateActinium, machineExplodes, machineSounds, poisonDust, genCopper, debugMode, useDefault;
    public static int actiniumMaxLevel, actiniumVeinSize, actiniumVeinsPerChunk, actiniumMinLevel;
    public static int copperMaxLevel, copperVeinSize, copperVeinsPerChunk, copperMinLevel;
    public static int searchRange, defaultReplicationValue;
    public static int rfPerTesla, maxCoilGenerate, maxCoilTransfer;

    public static void init(Configuration config) {

        folderLocation = config.getConfigFile().getAbsolutePath();

        config.load();

        generateActinium        = config.get(Constants.CONFIG_ORE_GENERATION, "Generate Actinium", true).getBoolean();
        actiniumVeinSize        = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Vein Size", 5).getInt();
        actiniumVeinsPerChunk   = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Veins Per Chunk", 3).getInt();
        actiniumMinLevel        = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Min Level", 5).getInt();
        actiniumMaxLevel        = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Max Level", 20).getInt();

        rfPerTesla              = config.get(Constants.CONFIG_TESLA, "RFs per Tesla", 10).getInt();
        maxCoilTransfer         = config.get(Constants.CONFIG_TESLA, "Max Tesla a coil can transfer per tick", 1).getInt();
        maxCoilGenerate         = config.get(Constants.CONFIG_TESLA, "Max Tesla a coil can generate per tick", 1).getInt();
        searchRange             = config.get(Constants.CONFIG_TESLA, "How many blocks (cubed) to search for coils", 10).getInt();

        machineExplodes         = config.get(Constants.CONFIG_GENERAL, "Do Machines Explode?", true).getBoolean();
        poisonDust              = config.get(Constants.CONFIG_GENERAL, "Disable Poison from Actinium Dust?", false).getBoolean();
        machineSounds           = config.get(Constants.CONFIG_GENERAL, "Do Machines Make Sounds?", true).getBoolean();

        genCopper               = config.get(Constants.CONFIG_ORE_GENERATION, "Enable Copper Ore Gen (Only works if Copper Ore Doesn't Already Exist)", true).getBoolean();
        copperVeinSize          = config.get(Constants.CONFIG_ORE_GENERATION, "Copper Vein Size", 12).getInt();
        copperVeinsPerChunk     = config.get(Constants.CONFIG_ORE_GENERATION, "Copper Veins Per Chunk", 6).getInt();
        copperMinLevel          = config.get(Constants.CONFIG_ORE_GENERATION, "Copper Min Level", 40).getInt();
        copperMaxLevel          = config.get(Constants.CONFIG_ORE_GENERATION, "Copper Max Level", 75).getInt();

        useDefault              = config.get(Constants.CONFIG_DEFAULT_VALUE, "Assign Default Replication Value if not found?", true).getBoolean();
        defaultReplicationValue = config.get(Constants.CONFIG_DEFAULT_VALUE, "Default Replication Value", 10000).getInt();

        debugMode               = config.get("DEBUG" , "Enable Debug Mode?", false).getBoolean();

        config.save();
    }
}
