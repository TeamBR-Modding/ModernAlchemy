package com.dyonovan.itemreplication.entities;

import com.dyonovan.itemreplication.blocks.replicator.BlockReplicatorFrame;
import com.dyonovan.itemreplication.blocks.replicator.BlockReplicatorStand;
import com.dyonovan.itemreplication.handlers.ItemHandler;
import com.dyonovan.itemreplication.util.Location;
import com.dyonovan.itemreplication.util.RenderUtils;
import com.dyonovan.itemreplication.util.WorldUtils;
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

        if(coolDown < 0) {
            for (int i = -5; i <= 5; i++) {
                for (int j = -5; j <= 5; j++) {
                    for (int k = -5; k <= 5; k++) {
                        if (worldObj.getBlock((int) posX + i, (int) posY + j, (int) posZ + k) instanceof BlockReplicatorStand) {
                            RenderUtils.sendBoltToClient(worldObj.provider.dimensionId, posX - 0.5, posY - 0.5, posZ - 0.5, ((int) posX) + i + 0.5, ((int) posY) + j + 0.7, ((int) posZ) + k + 0.5, 0.01, 0.005, 15, 255, 0, 0);
                            rotateToPosition(posX + i + 0.5, posY + j + 0.7, posZ + k + 0.5);
                            coolDown = 10;
                        }
                    }
                }
            }
        }

        if(!hasFrame())
            motionY -= 0.01;
        else
            motionY = 0;

        if(!worldObj.isAirBlock((int)Math.floor(posX), (int)Math.floor(posY), (int)Math.floor(posZ)))
            setDead();

        coolDown--;
        setPosition(posX + motionX, posY + motionY, posZ + motionZ);
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
