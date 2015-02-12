package com.dyonovan.itemreplication.entities;

import com.dyonovan.itemreplication.blocks.BlockFrame;
import com.dyonovan.itemreplication.helpers.LogHelper;
import com.dyonovan.itemreplication.util.Location;
import com.dyonovan.itemreplication.util.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class EntityLaserNode extends Entity {


    public EntityLaserNode(World world) {
        super(world);
    }

    public EntityLaserNode(World world, double x, double y, double z, int from) {
        super(world);
        posX = x;
        posY = y;
        posZ = z;

        switch(from) {
        case 2 :
            posZ--;
            break;
        case 3 :
            posZ++;
            break;
        case 4 :
            posX--;
            break;
        case 5 :
            posX++;
            break;
        }
    }

    @Override
    protected void entityInit() {

        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if(dir == ForgeDirection.UP || dir == ForgeDirection.DOWN)
                continue;
            if(WorldUtils.getBlockInDirection(worldObj, new Location((int) posX, (int) posY, (int) posZ), dir) instanceof BlockFrame)
            {
                LogHelper.info("FOUND!");
            }
        }
    }

    public void moveInside() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {

    }
}
