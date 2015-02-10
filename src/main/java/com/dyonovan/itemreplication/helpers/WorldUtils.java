package com.dyonovan.itemreplication.helpers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

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
}
