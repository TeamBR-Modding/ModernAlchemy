package com.dyonovan.modernalchemy.common.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.client.gui.machines.GuiTeslaCoil;
import com.dyonovan.modernalchemy.common.container.ContainerTeslaCoil;
import com.dyonovan.modernalchemy.common.tileentity.TileModernAlchemy;
import com.dyonovan.modernalchemy.energy.ITeslaProvider;
import com.dyonovan.modernalchemy.energy.SyncableRF;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.handlers.PacketHandler;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.network.UpdateServerCoilLists;
import com.dyonovan.modernalchemy.common.tileentity.BaseMachine;
import com.dyonovan.modernalchemy.util.Location;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.IHasGui;
import openmods.api.IValueProvider;
import openmods.sync.SyncableBoolean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TileTeslaCoil extends TileModernAlchemy implements IEnergyHandler, ITeslaProvider, IHasGui {

    protected SyncableRF energyRF;
    public TeslaBank energyTank;
    protected SyncableBoolean isActive;
    public LinkedList<Location> rangeMachines;
    public LinkedList<Location> linkedMachines;

    public TileTeslaCoil() {
        super();
        rangeMachines = new LinkedList<>();
        linkedMachines = new LinkedList<>();
    }

    @Override
    protected void createSyncedFields() {
        energyRF = new SyncableRF(new EnergyStorage(10000, 1000, 0));
        energyTank = new TeslaBank(1000);
        isActive = new SyncableBoolean();
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

    protected boolean doConvert() {
        if (energyRF.getValue().getEnergyStored() > 0 && energyTank.getEnergyLevel()  < energyTank.getMaxCapacity()) {
            isActive.set(true);
            int actualRF = Math.min(ConfigHandler.maxCoilGenerate * ConfigHandler.rfPerTesla, energyRF.getValue().getEnergyStored());
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
        isActive.set(false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return false;
    }

    /*******************************************************************************************************************
     ******************************************** Energy Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public int getEnergyProvided() {
        return ConfigHandler.maxCoilTransfer;
    }

    @Override
    public int receiveEnergy(ForgeDirection side, int maxReceive, boolean simulate) {
        return energyRF.getValue().receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate) {
        return energyRF.getValue().extractEnergy(maxReceive, simulate);
    }

    public void removeEnergy(int amount) {
        energyRF.getValue().setEnergyStored(energyRF.getValue().getEnergyStored() - amount);
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getValue().getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getValue().getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection side) {
        return side == ForgeDirection.DOWN;
    }

    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void onWrench(EntityPlayer player, int side) {
        if (worldObj.isRemote) return;
        searchMachines(player);
        player.openGui(ModernAlchemy.instance, GuiHandler.TESLA_COIL_LINKS_GUI_ID, worldObj, xCoord, yCoord, zCoord);
    }

    @Override
    public boolean isActive() {
        return isActive.get();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
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
        if(doConvert()) {
            energyTank.markDirty();
            energyRF.markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            sync();
        }
    }

    public IValueProvider<TeslaBank> getTeslaBankProvider() {
        return energyTank;
    }

    public IValueProvider<EnergyStorage> getRFEnergyStorageProvider() {
        return energyRF;
    }

    public List<String> getTeslaEnergyToolTip() {
        List<String> toolTip = new ArrayList<>();
        toolTip.add(GuiHelper.GuiColor.WHITE + "Energy Stored");
        toolTip.add("" + GuiHelper.GuiColor.YELLOW + energyTank.getValue().getEnergyLevel() + "/" + energyTank.getValue().getMaxCapacity() + GuiHelper.GuiColor.BLUE + "T");
        return toolTip;
    }

    public List<String> getEnergyToolTip() {
        List<String> toolTip = new ArrayList<>();
        toolTip.add(GuiHelper.GuiColor.WHITE + "RF Energy");
        toolTip.add("" + GuiHelper.GuiColor.YELLOW + energyRF.getValue().getEnergyStored() + "/" + energyRF.getValue().getMaxEnergyStored() + GuiHelper.GuiColor.RED + "RF");
        return toolTip;
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerTeslaCoil(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiTeslaCoil(new ContainerTeslaCoil(player.inventory, this));
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return true;
    }

    /*******************************************************************************************************************
     ********************************************** Misc Functions *****************************************************
     *******************************************************************************************************************/
   /* @Override
    public void returnWailaHead(List<String> head) {
        head.add(GuiHelper.GuiColor.YELLOW + "Tesla Energy: " + GuiHelper.GuiColor.WHITE + energyTank.getEnergyLevel() + "/" + energyTank.getMaxCapacity() + GuiHelper.GuiColor.TURQUISE + "T");
        head.add(GuiHelper.GuiColor.YELLOW + "RF Energy: " + GuiHelper.GuiColor.WHITE + energyRF.getEnergyStored() + "/" + energyRF.getMaxEnergyStored() + GuiHelper.GuiColor.TURQUISE + "RF");
        if(linkedMachines.size() > 0) {
            head.add(GuiHelper.GuiColor.YELLOW + "Link Mode: " + GuiHelper.GuiColor.RED + "Restricted");
        }
        else
            head.add(GuiHelper.GuiColor.YELLOW + "Link Mode: " + GuiHelper.GuiColor.GREEN + "All");

    }*/
}
