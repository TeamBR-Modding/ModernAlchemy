package com.dyonovan.modernalchemy.tileentity;

import com.dyonovan.modernalchemy.energy.ITeslaHandler;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.RenderUtils;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BaseMachine extends BaseTile implements ITeslaHandler {

    //Energy
    protected TeslaBank energyTank;

    protected boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    @Override
    public void onWrench(EntityPlayer player) {

    }

    @Override
    public List<String> returnWailaHead() {
        return null;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        tag.setBoolean("isActive", isActive);
        energyTank.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        isActive = tag.getBoolean("isActive");
        energyTank.readFromNBT(tag);
    }

    /*******************************************************************************************************************
     ******************************************** Energy Functions *****************************************************
     *******************************************************************************************************************/

    public List<TileTeslaCoil> findCoils(World world) {

        List<TileTeslaCoil> list = new ArrayList<TileTeslaCoil>();

        int tileX = xCoord;
        int tileY = yCoord;
        int tileZ = zCoord;

        for (int x = -ConfigHandler.searchRange; x <= ConfigHandler.searchRange; x++) {
            for (int y = -ConfigHandler.searchRange; y <= ConfigHandler.searchRange; y++) {
                for (int z = -ConfigHandler.searchRange; z <= ConfigHandler.searchRange; z++) {
                    if (world.getTileEntity(tileX + x, tileY + y, tileZ + z) instanceof TileTeslaCoil) {
                        if(hasClearPath(tileX, tileY, tileZ, tileX + x + 0.5, tileY + y + 0.5, tileZ + z + 0.5))
                            list.add((TileTeslaCoil) world.getTileEntity(tileX + x, tileY + y, tileZ + z));
                    }
                }
            }
        }
        return list;
    }

    public boolean hasClearPath(double x1, double y1, double z1, double x2, double y2, double z2) {
        float t = 0.1F;
        while(t < 1.0F) {
            double checkX = x1 + ((x2 - x1) * t);
            double checkY = y1 + ((y2 - y1) * t);
            double checkZ = z1 + ((z2 - z1) * t);
            if(!worldObj.isAirBlock((int)Math.floor(checkX), (int)Math.floor(checkY), (int)Math.floor(checkZ)) &&
                    (worldObj.getBlock((int)Math.floor(checkX), (int)Math.floor(checkY), (int)Math.floor(checkZ)) != this.getBlockType()) &&
                    (worldObj.getBlock((int)Math.floor(checkX), (int)Math.floor(checkY), (int)Math.floor(checkZ)) != BlockHandler.blockCoil)) {
                return false;
            }
            t += 0.01F;
        }
        return true;
    }

    public void chargeFromCoils() {
        int maxFill = energyTank.getMaxCapacity() - energyTank.getEnergyLevel();
        List<TileTeslaCoil> coils = findCoils(worldObj);
        int currentDrain = 0;
        for(TileTeslaCoil coil : coils) {
            if (coil.linkedMachines.contains(new Location(xCoord, yCoord, zCoord)) || coil.linkedMachines.size() == 0) {
                if (coil.getEnergyLevel() <= 0) continue; //fixes looking like its working when coil is empty
                int fill = coil.getEnergyLevel() > ConfigHandler.maxCoilTransfer ? ConfigHandler.maxCoilTransfer : coil.getEnergyLevel();
                if (currentDrain + fill > maxFill)
                    fill = maxFill - currentDrain;
                currentDrain += fill;
                coil.drainEnergy(fill);

                RenderUtils.sendBoltToClient(xCoord, yCoord, zCoord, coil, fill);
                WorldUtils.hurtEntitiesInRange(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, coil.xCoord + 0.5, coil.yCoord + 0.5, coil.zCoord + 0.5);

                if (currentDrain >= maxFill) //Don't want to drain other coils we don't need to
                    break;
            }
        }
        while(currentDrain > 0) {
            energyTank.addEnergy(1);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            currentDrain--;
        }
    }

    @Override
    public void addEnergy(int maxAmount) {
        energyTank.addEnergy(maxAmount);
    }

    @Override
    public int drainEnergy(int maxAmount) {
        return energyTank.drainEnergy(maxAmount);
    }

    @Override
    public int getEnergyLevel() {
        return energyTank.getEnergyLevel();
    }

    @Override
    public TeslaBank getEnergyBank() {
        return energyTank;
    }

}
