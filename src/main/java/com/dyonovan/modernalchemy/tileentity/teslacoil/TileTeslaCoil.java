package com.dyonovan.modernalchemy.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.tileentity.BaseMachine;
import com.dyonovan.modernalchemy.util.Location;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TileTeslaCoil extends BaseMachine implements IEnergyHandler {

    protected EnergyStorage energyRF;
    public LinkedHashMap<String, Location> link;

    public TileTeslaCoil() {
        super();
        energyRF = new EnergyStorage(10000, 1000, 0);
        energyTank = new TeslaBank(1000);
        link = new LinkedHashMap<String, Location>();
        link.put("Any", new Location(0,0,0));
    }

    /*******************************************************************************************************************
     ************************************* Tesla Coil Functions ********************************************************
     *******************************************************************************************************************/

    public void searchMachines() {
        link.clear();
        for (int x = -ConfigHandler.searchRange; x <= ConfigHandler.searchRange; x++) {
            for (int y = -ConfigHandler.searchRange; y <= ConfigHandler.searchRange; y++) {
                for (int z = -ConfigHandler.searchRange; z <= ConfigHandler.searchRange; z++) {
                    TileEntity te = worldObj.getTileEntity(xCoord + x, yCoord + y, zCoord + z);
                    if (te instanceof BaseMachine && !(te instanceof TileTeslaCoil)) {
                        link.put(te.getBlockType().getLocalizedName(), new Location(xCoord + x, yCoord + y, zCoord + z));
                    }
                }
            }
        }
        if (link.size() < 1) {
            link.put("Any", new Location(0, 0, 0));
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private boolean doConvert() {
        if (energyRF.getEnergyStored() > 0 && energyTank.getEnergyLevel()  < energyTank.getMaxCapacity()) {
            int actualRF = Math.min(ConfigHandler.maxCoilGenerate * ConfigHandler.rfPerTesla, energyRF.getEnergyStored());
            int actualTesla = Math.min(ConfigHandler.maxCoilGenerate, energyTank.getMaxCapacity() - energyTank.getEnergyLevel());

            if (actualTesla * ConfigHandler.rfPerTesla < actualRF) {
                removeEnergy(actualTesla * 10);
                energyTank.addEnergy(actualTesla);
            } else if (actualTesla * ConfigHandler.rfPerTesla > actualRF && actualRF > 100) {
                removeEnergy(actualRF);
                energyTank.addEnergy(actualRF / ConfigHandler.rfPerTesla);
            } else if (actualTesla * ConfigHandler.rfPerTesla == actualRF) {
                removeEnergy(actualRF);
                energyTank.addEnergy(actualTesla);
            }
            return true;
        }
        return false;
    }

    /*******************************************************************************************************************
     ******************************************** Energy Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energyRF.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate) {
        return energyRF.extractEnergy(maxReceive, simulate);
    }

    public void removeEnergy(int amount) {
        energyRF.setEnergyStored(energyRF.getEnergyStored() - amount);
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getEnergyStored();
    }

    public int getRFEnergyStored() {
        return energyRF.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getMaxEnergyStored();
    }

    public int getRFMaxEnergyStored() {
        return energyRF.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection side) {
        return side == ForgeDirection.DOWN; //TODO Why BC Pipes dont update on load
    }

    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void onWrench(EntityPlayer player) {
        searchMachines();
        player.openGui(ModernAlchemy.instance, GuiHandler.TESLA_COIL_LINKS_GUI_ID, worldObj, xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energyRF.readFromNBT(tag);
        NBTTagList nbtList = tag.getTagList("Links", 10);
        link.clear();
        for (int i = 0; i < nbtList.tagCount(); i++) {
            NBTTagCompound tag1 = nbtList.getCompoundTagAt(i);
            link.put(tag1.getString("Machine"), new Location(
                    tag1.getInteger("X"), tag1.getInteger("Y"), tag1.getInteger("Z")));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energyRF.writeToNBT(tag);
        NBTTagList nbtList = new NBTTagList();
        for (Map.Entry<String, Location> entry : link.entrySet()) {
            NBTTagCompound tag1 = new NBTTagCompound();
            tag1.setString("Machine", entry.getKey());
            tag1.setInteger("X", entry.getValue().x);
            tag1.setInteger("Y", entry.getValue().y);
            tag1.setInteger("Z", entry.getValue().z);
            nbtList.appendTag(tag1);
        }
        tag.setTag("Links", nbtList);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;
        if(doConvert())
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
