package com.dyonovan.modernalchemy.common.tileentity.machines;

import com.dyonovan.modernalchemy.client.gui.machines.GuiElectricBellows;
import com.dyonovan.modernalchemy.common.container.machines.ContainerElectricBellows;
import com.dyonovan.modernalchemy.common.tileentity.TileModernAlchemy;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.PacketHandler;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.network.MachineSoundPacket;
import com.dyonovan.modernalchemy.common.tileentity.BaseMachine;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import openmods.api.IHasGui;
import openmods.api.IValueProvider;
import openmods.api.IValueReceiver;
import openmods.gui.misc.IConfigurableGuiSlots;
import openmods.include.IncludeInterface;
import openmods.liquids.SidedFluidHandler;
import openmods.sync.*;
import openmods.utils.MiscUtils;
import openmods.utils.bitmap.BitMapUtils;
import openmods.utils.bitmap.IRpcDirectionBitMap;
import openmods.utils.bitmap.IRpcIntBitMap;
import openmods.utils.bitmap.IWriteableBitMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TileElectricBellows extends TileModernAlchemy implements IHasGui, IConfigurableGuiSlots<TileElectricBellows.AutoSlots>{

    public static final int TANK_CAPACITY = FluidContainerRegistry.BUCKET_VOLUME * 10;
    public SyncableTank tank;
    public SyncableSides fluidOutput;
    private SyncableInt currentSpeed;
    private TeslaBank energyTank;
    private SyncableBoolean isActive;
    private SyncableFlags automaticSlots;

    public enum AutoSlots {
        fluid
    }

    @IncludeInterface
    private final IFluidHandler tankWrapper = new SidedFluidHandler.Source(fluidOutput, tank);

    @Override
    protected void createSyncedFields() {
        this.energyTank = new TeslaBank(1000);
        this.tank = new SyncableTank(TANK_CAPACITY, new FluidStack(BlockHandler.fluidCompressedAir, 0));
        currentSpeed = new SyncableInt();
        isActive = new SyncableBoolean();
        fluidOutput = new SyncableSides();
        automaticSlots = SyncableFlags.create(AutoSlots.values().length);
    }

    /*******************************************************************************************************************
     ****************************************** Compressor Functions ***************************************************
     *******************************************************************************************************************/

    private void updateSpeed() {
        if(energyTank.getEnergyLevel() == 0) {
            currentSpeed.set(0);
            return;
        }

        currentSpeed.set((energyTank.getEnergyLevel() * 20) / energyTank.getMaxCapacity());
        if(currentSpeed.get() == 0)
            currentSpeed.set(1);
    }

    private void compress() {
        if (energyTank.getEnergyLevel() > 0 && tank.getFluidAmount() < tank.getCapacity() && !isPowered()) {

            if (!isActive.get()) {
                PacketHandler.net.sendToDimension(new MachineSoundPacket.MachineSoundMessage(Constants.MODID + ":compressor", xCoord, yCoord, zCoord, 0.1F, 1.0F), worldObj.provider.dimensionId);
                isActive.set(true);
            }
            updateSpeed();
            energyTank.drainEnergy(currentSpeed.get());
            tank.fill(new FluidStack(BlockHandler.fluidCompressedAir, 10 * currentSpeed.get()), true);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        } else if (isActive.get())
            isActive.set(false);
    }

    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) {
            return;
        }

        if (energyTank.canAcceptEnergy()) {
            chargeFromCoils(energyTank);
        }

        compress();

        if(automaticSlots.get(AutoSlots.fluid)) {
            tank.distributeToSides(50, worldObj, getPosition(), fluidOutput.getValue());
        }
    }

    private SyncableSides selectSlotMap(AutoSlots slot) {
        switch (slot) {
            case fluid:
                return fluidOutput;
            default:
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    @Override
    public IValueProvider<Set<ForgeDirection>> createAllowedDirectionsProvider(AutoSlots slot) {
        return selectSlotMap(slot);
    }

    @Override
    public IWriteableBitMap<ForgeDirection> createAllowedDirectionsReceiver(AutoSlots slot) {
        SyncableSides dirs = selectSlotMap(slot);
        return BitMapUtils.createRpcAdapter(createRpcProxy(dirs, IRpcDirectionBitMap.class));
    }

    @Override
    public IValueProvider<Boolean> createAutoFlagProvider(AutoSlots slot) {
        return BitMapUtils.singleBitProvider(automaticSlots, slot.ordinal());
    }

    @Override
    public IValueReceiver<Boolean> createAutoSlotReceiver(AutoSlots slot) {
        IRpcIntBitMap bits = createRpcProxy(automaticSlots, IRpcIntBitMap.class);
        return BitMapUtils.singleBitReceiver(bits, slot.ordinal());
    }

    public IValueProvider<TeslaBank> getTeslaBankProvider() {
        return energyTank;
    }

    public IValueProvider<FluidStack> getFluidProvider() {
        return tank;
    }

    public List<String> getFluidToolTip() {
        List<String> toolTip = new ArrayList<>();
        if(tank.getValue() != null) {
            toolTip.add(GuiHelper.GuiColor.WHITE + tank.getValue().getLocalizedName());
            toolTip.add("" + GuiHelper.GuiColor.YELLOW + tank.getFluidAmount() + "/" + tank.getCapacity() + GuiHelper.GuiColor.BLUE + "mb");
        }
        else {
            toolTip.add(GuiHelper.GuiColor.RED + "Empty");
        }
        return toolTip;
    }

    public List<String> getEnergyToolTip() {
        List<String> toolTip = new ArrayList<>();
        toolTip.add(GuiHelper.GuiColor.WHITE + "Energy Stored");
        toolTip.add("" + GuiHelper.GuiColor.YELLOW + energyTank.getValue().getEnergyLevel() + "/" + energyTank.getValue().getMaxCapacity() + GuiHelper.GuiColor.BLUE + "T");
        return toolTip;
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerElectricBellows(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiElectricBellows(new ContainerElectricBellows(player.inventory, this));
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return true;
    }

    /*******************************************************************************************************************
     ********************************************** Misc Functions *****************************************************
     *******************************************************************************************************************/
   @Override
    public void returnWailaHead(List<String> head) {
        head.add(GuiHelper.GuiColor.YELLOW + "Is Compressing: " + GuiHelper.GuiColor.WHITE + (isActive() ? "Yes" : "No"));
        head.add(GuiHelper.GuiColor.YELLOW + "Energy: " + GuiHelper.GuiColor.WHITE + energyTank.getEnergyLevel() + "/" + energyTank.getMaxCapacity() + GuiHelper.GuiColor.TURQUISE + "T");
        head.add(GuiHelper.GuiColor.YELLOW + "Compressed Air: " + GuiHelper.GuiColor.WHITE + tank.getFluidAmount() + "/" + tank.getCapacity() + GuiHelper.GuiColor.TURQUISE + "mb");
    }

    @Override
    public void onWrench(EntityPlayer player, int side) {

    }

    @Override
    public boolean isActive() {
        return isActive.get();
    }
}
