package com.dyonovan.itemreplication.handlers;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.Random;

/**
 * Created by Tim on 2/4/2015.
 */
public class WorldGeneratorHandler implements IWorldGenerator {

    public WorldGeneratorHandler() {}

    public static void init()
    {
        GameRegistry.registerWorldGenerator(new WorldGeneratorHandler(), 0); // TODO:: the second number is the weight - larger goes later
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

        if(world.provider.dimensionId == 0) // Overworld only
            generateActinium(world, random, chunkX * 16, chunkZ * 16);
    }

    private void generateActinium(World world, Random random, int i, int j) {

        if(ConfigHandler.generateActinium) {
            if(ConfigHandler.actiniumMinLevel < 0) ConfigHandler.actiniumMinLevel = 0;
            if(ConfigHandler.actiniumMaxLevel < 0) ConfigHandler.actiniumMaxLevel = 0;
            if(ConfigHandler.actiniumMinLevel > ConfigHandler.actiniumMaxLevel) ConfigHandler.actiniumMinLevel = ConfigHandler.actiniumMaxLevel;
            if(ConfigHandler.actiniumVeinsPerChunk < 0) ConfigHandler.actiniumVeinsPerChunk = 0;
            if(ConfigHandler.actiniumVeinSize < 0) ConfigHandler.actiniumVeinSize = 0;

            for (int k = 0; k < ConfigHandler.actiniumVeinsPerChunk; k++) {
                int x = i + random.nextInt(16);
                int y = random.nextInt(ConfigHandler.actiniumMaxLevel - ConfigHandler.actiniumMinLevel) + ConfigHandler.actiniumMinLevel;
                int z = j + random.nextInt(16);

                (new WorldGenMinable(BlockHandler.blockOreActinium, ConfigHandler.actiniumVeinSize)).
                        generate(world, random, x, y, z);
            }
        }
    }
}
