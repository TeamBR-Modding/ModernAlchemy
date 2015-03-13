package com.dyonovan.modernalchemy.tileentity.machines;

import com.dyonovan.modernalchemy.blocks.machines.BlockAmalgamator;
import com.dyonovan.modernalchemy.container.ContainerAmalgamator;
import com.dyonovan.modernalchemy.energy.ITeslaProvider;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.gui.GuiAmalgamator;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.RenderUtils;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import openmods.api.*;
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
import openmods.utils.bitmap.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TileAmalgamator extends SyncedTileEntity implements IInventoryProvider, IHasGui, IConfigurableGuiSlots<TileAmalgamator.AUTO_SLOTS> {

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
            if (timeProcessed.get() == 0 && tank.getFluidAmount() > 0) { //Set the block to active and continue
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
            chargeFromCoils();
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

    public IValueProvider<FluidStack> getFluidProvider() {
        return tank;
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

    /*******************************************************************************************************************
     ******************************************** Energy Functions *****************************************************
     *******************************************************************************************************************/

    public List<ITeslaProvider> findCoils(World world) {

        List<ITeslaProvider> list = new ArrayList<>();

        int tileX = xCoord;
        int tileY = yCoord;
        int tileZ = zCoord;

        for (int x = -ConfigHandler.searchRange; x <= ConfigHandler.searchRange; x++) {
            for (int y = -ConfigHandler.searchRange; y <= ConfigHandler.searchRange; y++) {
                for (int z = -ConfigHandler.searchRange; z <= ConfigHandler.searchRange; z++) {
                    if (world.getTileEntity(tileX + x, tileY + y, tileZ + z) instanceof ITeslaProvider) {
                        if(hasClearPath(tileX + 0.5, tileY + 0.5, tileZ + 0.5, tileX + x + 0.5, tileY + y + 0.5, tileZ + z + 0.5))
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
                    (!(worldObj.getTileEntity((int)Math.floor(checkX), (int)Math.floor(checkY), (int)Math.floor(checkZ)) instanceof ITeslaProvider))) {
                return false;
            }
            t += 0.01F;
        }
        return true;
    }

    public void chargeFromCoils() {
        int maxFill = energyTank.getMaxCapacity() - energyTank.getEnergyLevel();
        List<ITeslaProvider> coils = findCoils(worldObj);
        int currentDrain = 0;
        for(ITeslaProvider coil : coils) {
            if(coil instanceof TileTeslaCoil) {
                if (((TileTeslaCoil)coil).linkedMachines.contains(new Location(xCoord, yCoord, zCoord)) || ((TileTeslaCoil)coil).linkedMachines.size() == 0) {
                    if (((TileTeslaCoil)coil).getEnergyLevel() <= 0) continue; //fixes looking like its working when coil is empty
                    int fill = ((TileTeslaCoil)coil).getEnergyLevel() > coil.getEnergyProvided() ? coil.getEnergyProvided() : ((TileTeslaCoil)coil).getEnergyLevel();
                    if (currentDrain + fill > maxFill)
                        fill = maxFill - currentDrain;
                    currentDrain += fill;
                    ((TileTeslaCoil)coil).drainEnergy(fill);

                    RenderUtils.sendBoltToClient(xCoord, yCoord, zCoord, (TileEntity) coil, fill);
                    WorldUtils.hurtEntitiesInRange(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, ((TileTeslaCoil) coil).xCoord + 0.5, ((TileTeslaCoil) coil).yCoord + 0.5, ((TileTeslaCoil) coil).zCoord + 0.5);

                    if (currentDrain >= maxFill) //Don't want to drain other coils we don't need to
                        break;
                }
            }
        }
        while(currentDrain > 0) {
            energyTank.addEnergy(1);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            currentDrain--;
        }
    }
}
