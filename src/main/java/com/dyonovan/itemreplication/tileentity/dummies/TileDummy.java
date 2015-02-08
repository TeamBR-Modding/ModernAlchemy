package com.dyonovan.itemreplication.tileentity.dummies;

import com.dyonovan.itemreplication.helpers.Location;
import com.dyonovan.itemreplication.tileentity.BaseCore;
import com.dyonovan.itemreplication.tileentity.BaseTile;
import net.minecraft.nbt.NBTTagCompound;

public class TileDummy extends BaseTile {
    private Location coreLocation = new Location(-100, -100, -100);

    public TileDummy() {};

    public BaseCore getCore() {
        if(worldObj.getTileEntity(coreLocation.getX(), coreLocation.getY(), coreLocation.getZ()) instanceof BaseCore)
            return (BaseCore) worldObj.getTileEntity(coreLocation.getX(), coreLocation.getY(), coreLocation.getZ());
        else
            return null;
    }

    public void setCoreLocation(Location loc) {
        coreLocation = loc;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        coreLocation.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        coreLocation.writeToNBT(tagCompound);
    }
}
