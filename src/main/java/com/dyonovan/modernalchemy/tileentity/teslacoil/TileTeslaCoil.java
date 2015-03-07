package com.dyonovan.modernalchemy.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.handlers.PacketHandler;
import com.dyonovan.teambrcore.helpers.GuiHelper;
import com.dyonovan.modernalchemy.network.UpdateServerCoilLists;
import com.dyonovan.modernalchemy.tileentity.BaseMachine;
import com.dyonovan.modernalchemy.util.Location;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.LinkedList;
import java.util.List;

public class TileTeslaCoil extends BaseMachine implements IEnergyHandler {

    protected EnergyStorage energyRF;
    public LinkedList<Location> rangeMachines;
    public LinkedList<Location> linkedMachines;


    public TileTeslaCoil() {
        super();
        energyRF = new EnergyStorage(10000, 1000, 0);
        energyTank = new TeslaBank(1000);
        rangeMachines = new LinkedList<Location>();
        linkedMachines = new LinkedList<Location>();
    }

    /*******************************************************************************************************************
     ************************************* Tesla Coil Functions ********************************************************
     *******************************************************************************************************************/

    public void searchMachines(EntityPlayer player) {
        rangeMachines.clear();
        for (int x = -ConfigHandler.searchRange; x <= ConfigHandler.searchRange; x++) {
            for (int y = -ConfigHandler.searchRange; y <= ConfigHandler.searchRange; y++) {
                for (int z = -ConfigHandler.searchRange; z <= ConfigHandler.searchRange; z++) {
                    TileEntity te = worldObj.getTileEntity(xCoord + x, yCoord + y, zCoord + z);
                    if (te instanceof BaseMachine && !(te instanceof TileTeslaCoil)) {
                        rangeMachines.add(new Location(xCoord + x, yCoord + y, zCoord + z));
                    }
                }
            }
        }
        for (int i = 0; i < linkedMachines.size(); i++) {
            if (rangeMachines.contains(linkedMachines.get(i))) {
                rangeMachines.remove(linkedMachines.get(i));
            } else {
                linkedMachines.remove(i);
                i--;
            }
        }
        //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (EntityPlayerMP pr : players) {
            if (pr.getDisplayName().equals(player.getDisplayName())) {
                PacketHandler.net.sendTo(new UpdateServerCoilLists.UpdateMessage(xCoord, yCoord, zCoord,
                        "linkedMachines", linkedMachines), pr);
                PacketHandler.net.sendTo(new UpdateServerCoilLists.UpdateMessage(xCoord, yCoord, zCoord,
                        "rangeMachines", rangeMachines), pr);
            }
        }

    }

    private boolean doConvert() {
        if (energyRF.getEnergyStored() > 0 && energyTank.getEnergyLevel()  < energyTank.getMaxCapacity()) {
            isActive = true;
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
        isActive = false;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
        if (worldObj.isRemote) return;
        searchMachines(player);
        player.openGui(ModernAlchemy.instance, GuiHandler.TESLA_COIL_LINKS_GUI_ID, worldObj, xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energyRF.readFromNBT(tag);
        if (tag.hasKey("Links")) {
            NBTTagList nbtList = tag.getTagList("Links", 10);
            linkedMachines.clear();
            for (int i = 0; i < nbtList.tagCount(); i++) {
                NBTTagCompound tag1 = nbtList.getCompoundTagAt(i);
                int x = tag1.getInteger("X");
                int y = tag1.getInteger("Y");
                int z = tag1.getInteger("Z");
                linkedMachines.add(new Location(x, y, z));
            }
        }
        if (tag.hasKey("Found")) {
            NBTTagList nbtList = tag.getTagList("Found", 10);
            rangeMachines.clear();
            for (int i = 0; i < nbtList.tagCount(); i++) {
                NBTTagCompound tag1 = nbtList.getCompoundTagAt(i);
                int x = tag1.getInteger("X");
                int y = tag1.getInteger("Y");
                int z = tag1.getInteger("Z");
                rangeMachines.add(new Location(x, y, z));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energyRF.writeToNBT(tag);
        if (linkedMachines.size() > 0) {
            NBTTagList nbtList = new NBTTagList();
            for (Location loc : linkedMachines) {
                NBTTagCompound tag1 = new NBTTagCompound();
                tag1.setInteger("X", loc.x);
                tag1.setInteger("Y", loc.y);
                tag1.setInteger("Z", loc.z);
                nbtList.appendTag(tag1);
            }
            tag.setTag("Links", nbtList);
        }
        if (rangeMachines.size() > 0) {
            NBTTagList nbtList = new NBTTagList();
            for (Location loc : rangeMachines) {
                NBTTagCompound tag1 = new NBTTagCompound();
                tag1.setInteger("X", loc.x);
                tag1.setInteger("Y", loc.y);
                tag1.setInteger("Z", loc.z);
                nbtList.appendTag(tag1);
            }
            tag.setTag("Found", nbtList);
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;
        if(doConvert())
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    /*******************************************************************************************************************
     ********************************************** Misc Functions *****************************************************
     *******************************************************************************************************************/
    @Override
    public void returnWailaHead(List<String> head) {
        head.add(GuiHelper.GuiColor.YELLOW + "Tesla Energy: " + GuiHelper.GuiColor.WHITE + energyTank.getEnergyLevel() + "/" + energyTank.getMaxCapacity() + GuiHelper.GuiColor.TURQUISE + "T");
        head.add(GuiHelper.GuiColor.YELLOW + "RF Energy: " + GuiHelper.GuiColor.WHITE + energyRF.getEnergyStored() + "/" + energyRF.getMaxEnergyStored() + GuiHelper.GuiColor.TURQUISE + "RF");
        if(linkedMachines.size() > 0) {
            head.add(GuiHelper.GuiColor.YELLOW + "Link Mode: " + GuiHelper.GuiColor.RED + "Restricted");
        }
        else
            head.add(GuiHelper.GuiColor.YELLOW + "Link Mode: " + GuiHelper.GuiColor.GREEN + "All");

    }
}
