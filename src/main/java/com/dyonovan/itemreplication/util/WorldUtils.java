package com.dyonovan.itemreplication.util;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class WorldUtils {
    protected static Random random = new Random();

    public static void expelItem(World worldObj, int xCoord, int yCoord, int zCoord, ItemStack itemstack) {
        if (itemstack != null) {
            EntityItem entityitem =
                    new EntityItem(worldObj,
                            (double) xCoord + random.nextFloat() * 0.8F + 0.1F,
                            (double) yCoord + random.nextFloat() * 0.8F + 0.1F,
                            (double) zCoord + random.nextFloat() * 0.8F + 0.1F,
                            itemstack
                    );

            if (itemstack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
            }

            float f3 = 0.05F;
            entityitem.motionX = (double) ((float) random.nextGaussian() * f3);
            entityitem.motionY = (double) ((float) random.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = (double) ((float) random.nextGaussian() * f3);
            worldObj.spawnEntityInWorld(entityitem);
        }
    }

    public static void breakBlock(World world, int x, int y, int z) {
        ItemStack drops = new ItemStack(Item.getItemFromBlock(world.getBlock(x, y, z)));
        expelItem(world, x, y, z, drops);
        world.getBlock(x, y, z).breakBlock(world, x, y, z, world.getBlock(x, y, z), 0);
        world.setBlockToAir(x, y, z);
    }

    public static void breakBlock(World world, Location loc) {
        breakBlock(world, loc.x, loc.y, loc.z);
    }

    public static Block getBlockInDirection(World world, Location loc, ForgeDirection dir) {
        loc.moveInDirection(dir);
        return world.getBlock(loc.x, loc.y, loc.z);
    }

    public static Block getBlockInLocation(World world, Location loc) {
        return world.getBlock(loc.x, loc.y, loc.z);
    }
}
