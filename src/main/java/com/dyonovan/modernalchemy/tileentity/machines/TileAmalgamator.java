package com.dyonovan.modernalchemy.tileentity.machines;

import com.dyonovan.modernalchemy.container.ContainerAmalgamator;
import com.dyonovan.modernalchemy.energy.ITeslaProvider;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.gui.GuiAmalgamator;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.tileentity.TileModernAlchemy;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.RenderUtils;
import com.dyonovan.modernalchemy.util.WorldUtils;
import com.dyonovan.teambrcore.helpers.GuiHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import openmods.api.IHasGui;
import openmods.api.IValueProvider;
import openmods.api.IValueReceiver;
import openmods.gui.misc.IConfigurableGuiSlots;
import openmods.include.IncludeInterface;
import openmods.inventory.GenericInventory;
import openmods.inventory.IInventoryProvider;
import openmods.inventory.TileEntityInventory;
import openmods.inventory.legacy.ItemDistribution;
import openmods.liquids.SidedFluidHandler;
import openmods.sync.*;
import openmods.tileentity.SyncedTileEntity;
import openmods.utils.MiscUtils;
import openmods.utils.SidedInventoryAdapter;
import openmods.utils.bitmap.BitMapUtils;
import openmods.utils.bitmap.IRpcDirectionBitMap;
import openmods.utils.bitmap.IRpcIntBitMap;
import openmods.utils.bitmap.IWriteableBitMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TileAmalgamator extends TileModernAlchemy implements IInventoryProvider, IHasGui, IConfigurableGuiSlots<TileAmalgamator.AUTO_SLOTS> {

    public static final int TANK_CAPACITY = FluidContainerRegistry.BUCKET_VOLUME * 10;
    public static final int PROCESS_TIME = 500;

    public enum Slots {
        liquid,
        output
    }

    public enum AUTO_SLOTS {
        liquid,
        output
    }

    private SyncableTank tank;
    protected TeslaBank energyTank;

    public SyncableSides fluidOutput;
    public SyncableSides itemOutputs;

    private SyncableInt currentSpeed;
    private SyncableInt timeProcessed;
    private SyncableBoolean isActive;
    private SyncableFlags automaticSlots;

    private final GenericInventory inventory = registerInventoryCallback(new TileEntityInventory(this, "amalgamator", true, 1));

    @IncludeInterface(ISidedInventory.class)
    private final SidedInventoryAdapter sided = new SidedInventoryAdapter(inventory);

    @IncludeInterface
    private final IFluidHandler tankWrapper = new SidedFluidHandler.Source(fluidOutput, tank);

    @Override
    protected void createSyncedFields() {
        tank = new SyncableTank(TANK_CAPACITY, new FluidStack(BlockHandler.fluidActinium, 0));
        fluidOutput = new SyncableSides();
        itemOutputs = new SyncableSides();
        currentSpeed = new SyncableInt();
        timeProcessed = new SyncableInt();
        isActive = new SyncableBoolean();
        automaticSlots = SyncableFlags.create(AUTO_SLOTS.values().length);
        energyTank = new TeslaBank(1000, 1000);
    }

    public TileAmalgamator() {
        sided.registerSlot(Slots.output, itemOutputs, false, true);
    }

    /*******************************************************************************************************************
     ****************************************** Solidifier Functions ***************************************************
     *******************************************************************************************************************/

    private void doReset() {
        isActive.set(false);
        timeProcessed.set(0);
        currentSpeed.set(0);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void updateSpeed() {
        if(energyTank.getEnergyLevel() == 0) {
            currentSpeed.set(0);
            return;
        }

        currentSpeed.set((energyTank.getEnergyLevel() * 20) / energyTank.getMaxCapacity());
        if(currentSpeed.equals(0))
            currentSpeed.set(1);
    }

    private void doSolidify() {
        if (canSolidify()) { //Must have power and redstone signal
            updateSpeed();
            if (timeProcessed.get() == 0 && tank.getFluidAmount() > 0 && isPowered()) { //Set the block to active and continue
                isActive.set(true);
                timeProcessed.set(timeProcessed.get() + 1);
            }
            if (timeProcessed.get() > 0 && timeProcessed.get() < PROCESS_TIME) { //Still cooking
                if (tank.getValue() != null) { //Drain until there is nothing left
                    energyTank.drainEnergy(currentSpeed.get());
                    tank.drain(5, true);
                    timeProcessed.set(timeProcessed.get() + currentSpeed.get());
                }
                else
                    doReset();
            }

            if (timeProcessed.get() >= PROCESS_TIME) { //Completed
                if (inventory.getStackInSlot(0) == null)
                    getInventory().setInventorySlotContents(0, new ItemStack(ItemHandler.itemReplicationMedium));
                else
                    inventory.getStackInSlot(0).stackSize++;
                doReset();
            }
        } else {
            doReset();
        }
    }

    private boolean canSolidify() {
        if(this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(0).stackSize >= 64) //Return false if there is no room
            return false;

        return energyTank.getEnergyLevel() > 0;
    }

    public int getCookTimeScaled(int scale) {
        return timeProcessed.get() * scale / PROCESS_TIME;
    }

    public IValueProvider<Integer> getProgress() {
        return timeProcessed;
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) return;

        //Import Actinium
        if(automaticSlots.get(AUTO_SLOTS.liquid)) {
            tank.fillFromSides(50, worldObj, getPosition(), fluidOutput.getValue());
        }

        if(automaticSlots.get(AUTO_SLOTS.output)) {
            ItemDistribution.moveItemsToOneOfSides(this, inventory, 0, 1, itemOutputs.getValue(), true);
        }

        if (energyTank.canAcceptEnergy()) {
            chargeFromCoils(energyTank);
        }

        doSolidify();
        if(tank.isDirty()) sync();

        super.updateEntity();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
    }

    private SyncableSides selectSlotMap(AUTO_SLOTS slot) {
        switch (slot) {
            case liquid:
                return fluidOutput;
            case output:
                return itemOutputs;
            default:
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    @Override
    public IValueProvider<Set<ForgeDirection>> createAllowedDirectionsProvider(AUTO_SLOTS slot) {
        return selectSlotMap(slot);
    }

    @Override
    public IWriteableBitMap<ForgeDirection> createAllowedDirectionsReceiver(AUTO_SLOTS slot) {
        SyncableSides dirs = selectSlotMap(slot);
        return BitMapUtils.createRpcAdapter(createRpcProxy(dirs, IRpcDirectionBitMap.class));
    }

    @Override
    public IValueProvider<Boolean> createAutoFlagProvider(AUTO_SLOTS slot) {
        return BitMapUtils.singleBitProvider(automaticSlots, slot.ordinal());
    }

    @Override
    public IValueReceiver<Boolean> createAutoSlotReceiver(AUTO_SLOTS slot) {
        IRpcIntBitMap bits = createRpcProxy(automaticSlots, IRpcIntBitMap.class);
        return BitMapUtils.singleBitReceiver(bits, slot.ordinal());
    }

    public IValueProvider<TeslaBank> getTeslaBankProvider() {
        return energyTank;
    }

    public IValueProvider<FluidStack> getFluidProvider() {
        return tank;
    }

    public IValueProvider<List<String>> getFluidTooltip() {
        IValueProvider<List<String>> toolTip = new IValueProvider<List<String>>() {
            @Override
            public List<String> getValue() {
                List<String> toolTip = new ArrayList<>();
                toolTip.add(GuiHelper.GuiColor.WHITE + tank.getValue().getLocalizedName());
                toolTip.add("" + GuiHelper.GuiColor.YELLOW + tank.getFluidAmount() + "/" + tank.getCapacity() + GuiHelper.GuiColor.BLUE + "mb");
                return toolTip;
            }
        };
        return toolTip;
    }

    public IValueProvider<List<String>> getEnergyToolTip() {
        IValueProvider<List<String>> toolTip = new IValueProvider<List<String>>() {
            @Override
            public List<String> getValue() {
                List<String> toolTip = new ArrayList<>();
                toolTip.add(GuiHelper.GuiColor.WHITE + "Energy Stored");
                toolTip.add("" + GuiHelper.GuiColor.YELLOW + energyTank.getEnergyLevel() + "/" + tank.getCapacity() + GuiHelper.GuiColor.BLUE + "T");
                return toolTip;
            }
        };
        return toolTip;
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerAmalgamator(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiAmalgamator(new ContainerAmalgamator(player.inventory, this));
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return true;
    }

    @Override
    public IInventory getInventory() {
        return this.inventory;
    }

    @Override
    public void onWrench(EntityPlayer player, int side) {

    }
}
