package com.dyonovan.itemreplication.entities;

import com.dyonovan.itemreplication.blocks.replicator.BlockFrame;
import com.dyonovan.itemreplication.util.Location;
import com.dyonovan.itemreplication.util.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class EntityLaserNode extends Entity {

    public EntityLaserNode(World world) {
        super(world);
        setSize(0.61F, 0.61F);
    }

    public EntityLaserNode(World world, double x, double y, double z, int from) {
        this(world);
        posX = x;
        posY = y + 0.2;
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
        moveToFrame();
    }

    public AxisAlignedBB getCollisionBox(Entity p_70114_1_)
    {
        return p_70114_1_.boundingBox;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public boolean attackEntityFrom(DamageSource source, float f)
    {
        this.setDead();
        return true;
    }

    @Override
    protected void entityInit() {

    }

    public void moveToFrame() {
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (dir == ForgeDirection.UP || dir == ForgeDirection.DOWN)
                continue;
            if (WorldUtils.getBlockInDirection(worldObj, getLocation(), dir) instanceof BlockFrame) {
                posX += (double) dir.offsetX / 8;
                posZ += (double) dir.offsetZ / 8;
                break;
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(worldObj.isRemote) return;

        setPosition(posX + motionX, posY, posZ + motionZ);
    }

    public Location getLocation() {
        return new Location((int)Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ));
    }
}
