package com.dyonovan.modernalchemy.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldUtils {
    protected static Random random = new Random();

    public static void hurtEntitiesInRange(World worldObj, double x1, double y1, double z1, double x2, double y2, double z2) {
        float t = 0.1F;
        double range = 0.5F;
        while(t < 1.0F) {
            double checkX = x1 + ((x2 - x1) * t);
            double checkY = y1 + ((y2 - y1) * t);
            double checkZ = z1 + ((z2 - z1) * t);
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(checkX - range, checkY - range, checkZ - range, checkX + range, checkY + range, checkZ + range);
            List<Entity> entities = new ArrayList<Entity>();
            List<EntityLiving> livings = worldObj.getEntitiesWithinAABB(EntityLiving.class, bb);
            List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb);
            List<EntityCreeper> creepers = worldObj.getEntitiesWithinAABB(EntityCreeper.class, bb);
            entities.addAll(livings);
            entities.addAll(players);
            if(entities.size() > 0) {
                for(Entity entityLiving : entities) {
                    if(entityLiving instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer)entityLiving;
                        if(player.capabilities.isCreativeMode)
                            continue;
                    }
                    entityLiving.attackEntityFrom(DamageSource.inFire, 2.0F);
                    entityLiving.setFire(1);
                }
            }

            for(EntityCreeper creeper : creepers)
                creeper.getDataWatcher().updateObject(17, Byte.valueOf((byte)1));

            t += 0.01F;
        }
    }

    public static void expelItem(World worldObj, double xCoord, double yCoord, double zCoord, ItemStack itemstack) {
        if (itemstack != null) {
            EntityItem entityitem =
                    new EntityItem(worldObj,
                            xCoord + random.nextFloat() * 0.8F + 0.1F,
                            yCoord + random.nextFloat() * 0.8F + 0.1F,
                            zCoord + random.nextFloat() * 0.8F + 0.1F,
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
