package com.dyonovan.modernalchemy.common.tileentity.teslacoil;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.client.gui.machines.GuiSuperTeslaCoil;
import com.dyonovan.modernalchemy.common.container.ContainerSuperTeslaCoil;
import com.dyonovan.modernalchemy.common.tileentity.BaseMachine;
import com.dyonovan.modernalchemy.common.tileentity.TileModernAlchemy;
import com.dyonovan.modernalchemy.energy.ITeslaProvider;
import com.dyonovan.modernalchemy.energy.SyncableRF;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.handlers.PacketHandler;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.network.UpdateServerSuperCoilLists;
import com.dyonovan.modernalchemy.util.Location;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.IHasGui;
import openmods.api.IValueProvider;
import openmods.sync.SyncableBoolean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TileSuperTeslaCoil extends TileModernAlchemy implements IEnergyHandler, ITeslaProvider, IHasGui {

    protected SyncableRF energyRF;
    public TeslaBank energyTank;
    protected SyncableBoolean isActive;
    public LinkedList<Location> rangeMachines;
    public LinkedList<Location> linkedMachines;

    protected void createSyncedFields() {
        energyRF = new SyncableRF(new EnergyStorage(10000*8, 1000*8, 0));
        energyTank = new TeslaBank(1000*8);
        isActive = new SyncableBoolean();
    }

    public TileSuperTeslaCoil() {
        super();
        rangeMachines = new LinkedList<>();
        linkedMachines = new LinkedList<>();
    }

    public void searchMachines(EntityPlayer player) {
        rangeMachines.clear();
        for (int x = -ConfigHandler.searchRange; x <= ConfigHandler.searchRange; x++) {
            for (int y = -ConfigHandler.searchRange; y <= ConfigHandler.searchRange; y++) {
                for (int z = -ConfigHandler.searchRange; z <= ConfigHandler.searchRange; z++) {
                    TileEntity te = worldObj.getTileEntity(xCoord + x, yCoord + y, zCoord + z);
                    if (te instanceof BaseMachine && !(te instanceof ITeslaProvider)) {
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
        @SuppressWarnings("unchecked") List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (EntityPlayerMP pr : players) {
            if (pr.getDisplayName().equals(player.getDisplayName())) {
                PacketHandler.net.sendTo(new UpdateServerSuperCoilLists.UpdateMessage(xCoord, yCoord, zCoord,
                        "linkedMachines", linkedMachines), pr);
                PacketHandler.net.sendTo(new UpdateServerSuperCoilLists.UpdateMessage(xCoord, yCoord, zCoord,
                        "rangeMachines", rangeMachines), pr);
            }
        }

    }

    /*******************************************************************************************************************
     ******************************************** Energy Functions *****************************************************
     *******************************************************************************************************************/

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

    @Override
    public int getEnergyProvided() {
        return ConfigHandler.maxCoilTransfer * 8;
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

    protected boolean doConvert() {
        if (energyRF.getValue().getEnergyStored() > 0 && energyTank.getEnergyLevel()  < energyTank.getMaxCapacity()) {
            isActive.set(true);
            int actualRF = Math.min((ConfigHandler.maxCoilGenerate*8) * (ConfigHandler.rfPerTesla*8), energyRF.getValue().getEnergyStored()*8);
            int actualTesla = Math.min(ConfigHandler.maxCoilGenerate*8, (energyTank.getMaxCapacity() - energyTank.getEnergyLevel())*8);

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
            sync();
            return true;
        }
        isActive.set(false);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return false;
    }

    public IValueProvider<TeslaBank> getTeslaBankProvider() {
        return this.energyTank;
    }

    public IValueProvider<EnergyStorage> getRFEnergyStorageProvider() {
        return this.energyRF;
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
        return new ContainerSuperTeslaCoil(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiSuperTeslaCoil(new ContainerSuperTeslaCoil(player.inventory, this));
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return true;
    }

    @Override
    public void onWrench(EntityPlayer player, int side) {
        if (worldObj.isRemote) return;
        searchMachines(player);
        player.openGui(ModernAlchemy.instance, GuiHandler.TESLA_SUPER_COIL_LINKS_GUI_ID, worldObj, xCoord, yCoord, zCoord);
    }

    @Override
    public boolean isActive() {
        return isActive.get();
    }

    @Override
    public void returnWailaHead(List<String> head) {
        head.add(GuiHelper.GuiColor.YELLOW + "Tesla Energy: " + GuiHelper.GuiColor.WHITE + energyTank.getEnergyLevel() + "/" + energyTank.getMaxCapacity() + GuiHelper.GuiColor.TURQUISE + "T");
        head.add(GuiHelper.GuiColor.YELLOW + "RF Energy: " + GuiHelper.GuiColor.WHITE + energyRF.getValue().getEnergyStored() + "/" + energyRF.getValue().getMaxEnergyStored() + GuiHelper.GuiColor.TURQUISE + "RF");
        if(linkedMachines.size() > 0) {
            head.add(GuiHelper.GuiColor.YELLOW + "Link Mode: " + GuiHelper.GuiColor.RED + "Restricted");
        }
        else
            head.add(GuiHelper.GuiColor.YELLOW + "Link Mode: " + GuiHelper.GuiColor.GREEN + "All");
    }
}
