package com.dyonovan.modernalchemy.common.tileentity.replicator;

import com.dyonovan.modernalchemy.common.tileentity.TileModernAlchemy;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import openmods.inventory.GenericInventory;
import openmods.inventory.IInventoryProvider;
import openmods.inventory.TileEntityInventory;

public class TileReplicatorStand extends TileModernAlchemy implements IInventoryProvider {

    public EntityItem entityItem = null;

    public GenericInventory inventory = registerInventoryCallback(new TileEntityInventory(this, "replicatorStand", true, 1));

    @Override
    protected void createSyncedFields() {}

    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        inventory.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.readFromNBT(tag);
    }

    @Override
    public void onWrench(EntityPlayer player, int side) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (inventory.getStackInSlot(0) != null) {
            if (entityItem == null || entityItem.getEntityItem() != inventory.getStackInSlot(0)) {
                entityItem = new EntityItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, inventory.getStackInSlot(0));
                entityItem.hoverStart = 0;
                entityItem.rotationYaw = 0;
                entityItem.motionX = 0;
                entityItem.motionY = 0;
                entityItem.motionZ = 0;
            }
        } else {
            entityItem = null;
        }
        if(!worldObj.isRemote)
            sync();
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }
}
