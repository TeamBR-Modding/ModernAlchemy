package com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies;

import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.BaseCore;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import net.minecraft.nbt.NBTTagCompound;

public class TileDummy extends BaseTile {
    private Location coreLocation = new Location(-100, -100, -100);

    /**
     * Used to get the current associated core
     * @return Parent core, null if not found
     */
    public BaseCore getCore() {
        if(worldObj.getTileEntity(coreLocation.X(), coreLocation.Y(), coreLocation.Z()) instanceof BaseCore)
            return (BaseCore) worldObj.getTileEntity(coreLocation.X(), coreLocation.Y(), coreLocation.Z());
        else
            return null;
    }

    /**
     * Associate the core to a location
     * @param loc Location of the core to associate with
     */
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
