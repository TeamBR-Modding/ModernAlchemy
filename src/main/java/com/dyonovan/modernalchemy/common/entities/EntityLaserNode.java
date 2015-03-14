package com.dyonovan.modernalchemy.common.entities;

import com.dyonovan.modernalchemy.common.blocks.replicator.BlockReplicatorFrame;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.RenderUtils;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
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
            if (WorldUtils.getBlockInDirection(worldObj, getLocation(), dir) instanceof BlockReplicatorFrame) {
                posX += (double) dir.offsetX / 8;
                posZ += (double) dir.offsetZ / 8;
                break;
            }
        }
    }

    public boolean hasFrame() {
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (dir == ForgeDirection.UP || dir == ForgeDirection.DOWN)
                continue;
            if (WorldUtils.getBlockInDirection(worldObj, getLocation(), dir) instanceof BlockReplicatorFrame) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setDead() {
        if(!worldObj.isRemote)
            WorldUtils.expelItem(worldObj, posX, posY, posZ, new ItemStack(ItemHandler.laserNode, 1));
        super.setDead();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {

    }

    int coolDown = 10;
    @Override
    public void onUpdate() {
        super.onUpdate();
        if(worldObj.isRemote) return;

        if(!hasFrame())
            motionY -= 0.01;
        else
            motionY = 0;

        if(!worldObj.isAirBlock((int)Math.floor(posX), (int)Math.floor(posY), (int)Math.floor(posZ)))
            setDead();

        coolDown--;
        setPosition(posX + motionX, posY + motionY, posZ + motionZ);
    }

    public void fireLaser(double x, double y, double z) {
        RenderUtils.sendBoltToClient(worldObj.provider.dimensionId, //Dimension
                posX - 0.5, posY - 0.5, posZ - 0.5, //Position
                x, y, z,  //Destination
                0.01, 0.005, 2, //Lightning things
                255, 0, 0); //Color
        rotateToPosition(x, y, z);
    }

    public void rotateToPosition(double x, double y, double z) {
        //TODO: Figure this stuff out
        //this.rotationPitch = 45;
        float angle = (float)Math.toDegrees(Math.atan2((posX - x), (posZ - z)));
        angle = (angle + 180.0f) % 360.0f;
        //this.rotationYaw = 360.0f - angle + 45;
    }

    public Location getLocation() {
        return new Location((int)Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ));
    }
}
