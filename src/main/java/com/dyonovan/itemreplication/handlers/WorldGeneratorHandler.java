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
        if(ConfigHandler.generateOre) {
            switch (world.provider.dimensionId) {
                case -1:
                    generateNether(world, random, chunkX * 16, chunkZ * 16);
                    break;
                case 0:
                    generateSurface(world, random, chunkX * 16, chunkZ * 16);
                    break;
                case 1:
                    generateEnd(world, random, chunkX * 16, chunkZ * 16);
                    break;
            }
        }
    }

    private void generateEnd(World world, Random random, int i, int j) {}

    private void generateSurface(World world, Random random, int i, int j) {

        for(int k = 0; k < ConfigHandler.oreVeinsPerChunk; k++) {
            int x = i + random.nextInt(16);
            int y = random.nextInt(ConfigHandler.oreMaxLevel - ConfigHandler.oreMinLevel) + ConfigHandler.oreMinLevel;
            int z = j + random.nextInt(16);

            (new WorldGenMinable(BlockHandler.blockOreActinium, ConfigHandler.oreVeinSize)).
                generate(world, random, x, y, z);
        }
    }

    private void generateNether(World world, Random random, int i, int j) {}
}
