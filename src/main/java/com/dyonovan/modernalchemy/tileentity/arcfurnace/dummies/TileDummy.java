package com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies;

import com.dyonovan.modernalchemy.blocks.arcfurnace.dummies.BlockDummy;
import com.dyonovan.modernalchemy.tileentity.TileModernAlchemy;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.BaseCore;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.IHasGui;
import openmods.api.IIconProvider;

import java.util.List;

public class TileDummy extends TileModernAlchemy implements IIconProvider, IHasGui {
    private Location coreLocation;

    @Override
    public void onWrench(EntityPlayer player, int side) {

    }

    /*
    @Override
    public void returnWailaHead(List<String> head) {
      //  if(getCore() != null)
            //getCore().returnWailaHead(head);
    }
*/
    /**
     * Used to get the current associated core
     * @return Parent core, null if not found
     */
    public TileArcFurnaceCore getCore() {
        if(worldObj.getTileEntity(coreLocation.X(), coreLocation.Y(), coreLocation.Z()) instanceof TileArcFurnaceCore)
            return (TileArcFurnaceCore) worldObj.getTileEntity(coreLocation.X(), coreLocation.Y(), coreLocation.Z());
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

    protected void updateCore() {
        worldObj.markBlockForUpdate(coreLocation.x, coreLocation.y, coreLocation.z);
        getCore().sync();
    }

    @Override
    protected void createSyncedFields() {
        coreLocation = new Location();
    }

    @Override
    public IIcon getIcon(ForgeDirection rotatedDir) {
        return getCore() != null ? BlockDummy.Icons.active : BlockDummy.Icons.inActive;
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return getCore() != null ? getCore().getServerGui(player) : null;
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return getCore() != null ? getCore().getClientGui(player) : null;
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return getCore() != null;
    }
}
