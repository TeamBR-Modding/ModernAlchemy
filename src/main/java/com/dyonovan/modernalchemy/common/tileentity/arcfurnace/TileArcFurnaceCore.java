package com.dyonovan.modernalchemy.common.tileentity.arcfurnace;

import com.dyonovan.modernalchemy.common.blocks.arcfurnace.dummies.*;
import com.dyonovan.modernalchemy.common.container.machines.ContainerArcFurnace;
import com.dyonovan.modernalchemy.common.tileentity.arcfurnace.dummies.*;
import com.dyonovan.modernalchemy.crafting.ArcFurnaceRecipeRegistry;
import com.dyonovan.modernalchemy.energy.ITeslaHandler;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.client.gui.machines.GuiArcFurnace;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.util.Location;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import openmods.api.IHasGui;
import openmods.api.IValueProvider;
import openmods.inventory.GenericInventory;
import openmods.inventory.IInventoryProvider;
import openmods.inventory.TileEntityInventory;
import openmods.sync.SyncableInt;
import openmods.sync.SyncableTank;

import java.util.ArrayList;
import java.util.List;

public class TileArcFurnaceCore extends BaseCore implements IHasGui, IFluidHandler, ITeslaHandler, IInventoryProvider {

    /**
     * Duration in ticks for the Cook Process
     */
    public static final int COOK_TIME = 500;

    public static final int TANK_CAPACITY = FluidContainerRegistry.BUCKET_VOLUME * 10;

    //Tanks
    public SyncableTank outputTank;
    public SyncableTank airTank;

    //Energy
    public TeslaBank energyTank;

    //Inventory
    public static final int INPUT_SLOT = 0;
    public static final int CATALYST_SLOT = 1;

    //Cook Variables
    private SyncableInt currentSpeed;
    private SyncableInt timeCooked;
    private SyncableInt fillVariable;

    //Inventory
    private final GenericInventory inventory = registerInventoryCallback(new TileEntityInventory(this, "arcFurnaceCore", true, 2) {
        @Override
        public boolean isItemValidForSlot(int i, ItemStack itemstack) {
            if(i == 0) return itemstack.getItem() != Items.coal;
            else if(i == 1) return itemstack.getItem() == Items.coal;
            return false;
        }
    });

    /**
     * Creates the Arc Furnace, Default fluids at 10 * BUCKET_VOLUME and energy to 1000T and an inventory of two slots
     */
    @Override
    protected void createSyncedFields() {
        outputTank = new SyncableTank(TANK_CAPACITY, new FluidStack(BlockHandler.fluidActinium, 0));
        airTank = new SyncableTank(TANK_CAPACITY, new FluidStack(BlockHandler.fluidCompressedAir, 0));
        energyTank = new TeslaBank(0, 1000);

        currentSpeed = new SyncableInt();
        timeCooked = new SyncableInt();
        fillVariable = new SyncableInt();
    }

    /*******************************************************************************************************************
     **************************************** MultiBlock Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public boolean isWellFormed() {
        //Check base
        for(int xCheck = -1; xCheck <= 1; xCheck++) {
            for(int zCheck = -1; zCheck <= 1; zCheck++) {
                if(!(worldObj.getBlock(xCoord + xCheck, yCoord - 1, zCoord + zCheck) instanceof BlockDummy))
                    return false;
            }
        }

        //Check middle
        for(int xCheck = -1; xCheck <= 1; xCheck++) {
            for (int zCheck = -1; zCheck <= 1; zCheck++) {
                if(xCheck == 0 && zCheck == 0)
                    continue;
                if(!(zCheck == 0 || xCheck == 0)) {
                    if((worldObj.getBlock(xCoord + xCheck, yCoord, zCoord + zCheck) instanceof BlockDummy)) {
                        if(!isBasicDummy(worldObj.getBlock(xCoord + xCheck, yCoord, zCoord + zCheck)))
                            return false;
                    } else
                        return false;
                } else if(!(worldObj.getBlock(xCoord + xCheck, yCoord, zCoord + zCheck) instanceof BlockDummy))
                    return false;
            }
        }

        //Check Top Layer
        for(int xCheck = -1; xCheck <= 1; xCheck++) {
            for (int zCheck = -1; zCheck <= 1; zCheck++) {
                if((worldObj.getBlock(xCoord + xCheck, yCoord + 1, zCoord + zCheck) instanceof BlockDummy)) {
                    if(!isBasicDummy(worldObj.getBlock(xCoord + xCheck, yCoord + 1, zCoord + zCheck)))
                        return false;
                } else
                    return false;
            }
        }

        //Check for energy reciever
        if(worldObj.getBlock(xCoord, yCoord + 2, zCoord) != BlockHandler.blockArcFurnaceDummyEnergy)
            return false;

        isValid = true;
        return true;
    }

    @Override
    public void buildStructure() {
        for(int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0)
                        continue;
                    TileDummy dummy = (TileDummy)worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                    dummy.setCoreLocation(new Location(xCoord, yCoord, zCoord));
                    if(!worldObj.isRemote)
                        dummy.sync();
                    worldObj.markBlockForUpdate(xCoord + i, yCoord + j, zCoord + k);
                }
            }
        }
        TileDummy dummy = (TileDummy)worldObj.getTileEntity(xCoord, yCoord + 2, zCoord);
        dummy.setCoreLocation(new Location(xCoord, yCoord, zCoord));
        if(!worldObj.isRemote)
            dummy.sync();
        worldObj.markBlockForUpdate(xCoord, yCoord + 2, zCoord);
    }

    @Override
    public void deconstructStructure() {
        for(int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0)
                        continue;
                    TileEntity tile = worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                    if(tile != null) {
                        if(tile instanceof TileDummy) {
                            TileDummy dummy = (TileDummy)worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                            dummy.setCoreLocation(new Location());
                            if(!worldObj.isRemote)
                                dummy.sync();
                            worldObj.markBlockForUpdate(xCoord + i, yCoord + j, zCoord + k);
                        }
                    }
                }
            }
        }
        TileEntity tile = worldObj.getTileEntity(xCoord, yCoord + 2, zCoord);
        if(tile != null) {
            if(tile instanceof TileDummy) {
                TileDummy dummy = (TileDummy)worldObj.getTileEntity(xCoord, yCoord + 2, zCoord);
                dummy.setCoreLocation(new Location());
                if(!worldObj.isRemote)
                    dummy.sync();
                worldObj.markBlockForUpdate(xCoord, yCoord + 2, zCoord);
            }
        }
        if(!worldObj.isRemote)
            reset();
        isValid = false;
    }

    public boolean isBasicDummy(Block dummy) {
        return !(dummy instanceof BlockDummyAirValve) && !(dummy instanceof BlockDummyEnergyReceiver) &&
                !(dummy instanceof BlockItemIODummy) && !(dummy instanceof BlockDummyOutputValve);
    }

    public void reset() {
        airTank.setFluid(null);
        outputTank.setFluid(null);
        energyTank.setEnergyLevel(0);
        timeCooked.set(0);
        currentSpeed.set(0);
        fillVariable.set(0);
        airTank.markDirty();
        outputTank.markDirty();
        resetSoft();
    }

    public void resetSoft() {
        timeCooked.set(0);
        fillVariable.set(0);
        sync();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    /*******************************************************************************************************************
     ***************************************** Arc Furnace Functions ***************************************************
     *******************************************************************************************************************/

    public void doSmelting() {
        if(canSmelt() && timeCooked.get() == 0) {
            //Consume Resources
            fillVariable.set(ArcFurnaceRecipeRegistry.instance.getReturn(inventory.getStackInSlot(INPUT_SLOT).getItem()));
            inventory.getStackInSlot(INPUT_SLOT).stackSize--;
            if(inventory.getStackInSlot(INPUT_SLOT).stackSize == 0)
                inventory.setInventorySlotContents(0, null);
            inventory.getStackInSlot(CATALYST_SLOT).stackSize--;
            if(inventory.getStackInSlot(CATALYST_SLOT).stackSize == 0)
                inventory.setInventorySlotContents(1, null);
            timeCooked.set(timeCooked.get() + currentSpeed.get());
        }
        else if(timeCooked.get() > 0 && timeCooked.get() < COOK_TIME && energyTank.getValue().getEnergyLevel() - currentSpeed.get() > 0) {
            timeCooked.set(timeCooked.get() + currentSpeed.get());
            energyTank.getValue().drainEnergy(currentSpeed.get());
            airTank.drain(currentSpeed.get() * 4, true);
        }
        else if(timeCooked.get() > 0 && timeCooked.get() < COOK_TIME && energyTank.getValue().getEnergyLevel() - currentSpeed.get() <= 0)
            resetSoft();
        else if(timeCooked.get() >= COOK_TIME)
            smelt();
    }

    public void smelt() {
        timeCooked.set(0);
        outputTank.fill(new FluidStack(BlockHandler.fluidActinium, fillVariable.get()), true);
    }

    public boolean canSmelt() {
        if(inventory.getStackInSlot(0) != null && inventory.getStackInSlot(1) != null)
            return airTank.getFluidAmount() > 100 &&
                    ArcFurnaceRecipeRegistry.instance.getReturn(inventory.getStackInSlot(0).getItem()) >  0 &&
                    inventory.getStackInSlot(1).getItem() == Items.coal &&
                    outputTank.getCapacity() - outputTank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME &&
                    energyTank.getValue().getEnergyLevel() > currentSpeed.get();
        else
            return false;
    }

    public void updateSpeed() {
        if(energyTank.getEnergyLevel() == 0) {
            currentSpeed.set(0);
            return;
        }

        currentSpeed.set((energyTank.getEnergyLevel() * 20) / energyTank.getMaxCapacity());
        if(currentSpeed.get() == 0)
            currentSpeed.set(1);
    }

    @Override
    public void onWrench(EntityPlayer player, int side) {

    }

    @Override
    public boolean isActive() {
        return timeCooked.get() > 0;
    }

    /*******************************************************************************************************************
     ******************************************** Energy Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void addEnergy(int maxAmount) {
        energyTank.getValue().addEnergy(maxAmount);
    }

    @Override
    public int drainEnergy(int maxAmount) {
        return energyTank.getValue().drainEnergy(maxAmount);
    }

    @Override
    public int getEnergyLevel() {
        return energyTank.getValue().getEnergyLevel();
    }

    @Override
    public TeslaBank getEnergyBank() {
        return energyTank.getValue();
    }

    /*******************************************************************************************************************
     ********************************************* Fluid Functions *****************************************************
     *******************************************************************************************************************/

    public FluidTank getOutputTank() {
        return outputTank;
    }

    public FluidTank getAirTank() {
        return airTank;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int amount = 0;
        if(canFill(from, resource.getFluid())) {
            switch(from) {
                case NORTH :
                    amount = airTank.fill(resource, doFill);
                    break;
                case SOUTH :
                    amount = outputTank.fill(resource, doFill);
                    break;
            }
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return amount;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return this.drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        switch(from) {
            case NORTH :
                return airTank.drain(maxDrain, doDrain);
            case SOUTH :
                return outputTank.drain(maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid == BlockHandler.fluidCompressedAir;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid == BlockHandler.fluidActinium;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[0];
    }

    /*******************************************************************************************************************
     ********************************************** Item Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public IInventory getInventory() {
        return inventory;
    }

    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(worldObj.isRemote) return;
        if(isWellFormed()) {
            updateSpeed();
            if (energyTank.canAcceptEnergy() && isValid) {
                chargeFromCoils(energyTank);
            }
            doSmelting();
            if (airTank.isDirty() || outputTank.isDirty()) sync();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setBoolean("hasOutputFluid", outputTank.getFluid() != null);
        tag.setBoolean("hasAir", airTank.getFluid() != null);
        if(outputTank.getFluid() != null) {
            tag.setString("outputFluid", outputTank.getFluid().getFluid().getName());
            tag.setInteger("outputFluidAmount", outputTank.getFluid().amount);
        }
        if(airTank.getFluid() != null) {
            tag.setString("air", airTank.getFluid().getFluid().getName());
            tag.setInteger("airAmount", airTank.getFluid().amount);
        }
        inventory.writeToNBT(tag);
        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.readFromNBT(tag);
        if(tag.getBoolean("hasOutputFluid")) {
            outputTank.setFluid(FluidRegistry.getFluidStack(tag.getString("outputFluid"), tag.getInteger("outputFluidAmount")));
        }
        else
            outputTank.setFluid(null);

        if(tag.getBoolean("hasAir")) {
            airTank.setFluid(FluidRegistry.getFluidStack(tag.getString("air"), tag.getInteger("airAmount")));
        }
        else
            airTank.setFluid(null);
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerArcFurnace(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiArcFurnace(new ContainerArcFurnace(player.inventory, this));
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return isWellFormed();
    }

    /*******************************************************************************************************************
     ********************************************** Misc Functions *****************************************************
     *******************************************************************************************************************/
/*
    @Override
    public void returnWailaHead(List<String> head) {
        head.add(GuiHelper.GuiColor.YELLOW + "Energy: " + GuiHelper.GuiColor.WHITE + energyTank.getEnergyLevel() + "/" + energyTank.getMaxCapacity() + GuiHelper.GuiColor.TURQUISE + "T");
        head.add(GuiHelper.GuiColor.YELLOW + "Air: " + GuiHelper.GuiColor.WHITE + airTank.getFluidAmount() + "/" + airTank.getCapacity() + GuiHelper.GuiColor.TURQUISE + "mb");
        head.add(GuiHelper.GuiColor.YELLOW + "Actinium: " + GuiHelper.GuiColor.WHITE + outputTank.getFluidAmount() + "/" + outputTank.getCapacity() + GuiHelper.GuiColor.TURQUISE + "mb");
        head.add(GuiHelper.GuiColor.YELLOW + "Speed: " + GuiHelper.GuiColor.WHITE + currentSpeed);
    }
*/

    public List<String> getAirTankToolTip() {
        List<String> toolTip = new ArrayList<>();
        if(airTank.getValue() != null) {
            toolTip.add(GuiHelper.GuiColor.WHITE + airTank.getValue().getLocalizedName());
            toolTip.add("" + GuiHelper.GuiColor.YELLOW + airTank.getFluidAmount() + "/" + airTank.getCapacity() + GuiHelper.GuiColor.BLUE + "mb");
        }
        else {
            toolTip.add(GuiHelper.GuiColor.YELLOW + "Requires Compressed Air");
        }
        return toolTip;
    }

    public List<String> getOutputTankToolTip() {
        List<String> toolTip = new ArrayList<>();
        if(outputTank.getValue() != null) {
            toolTip.add(GuiHelper.GuiColor.WHITE + outputTank.getValue().getLocalizedName());
            toolTip.add("" + GuiHelper.GuiColor.YELLOW + outputTank.getFluidAmount() + "/" + outputTank.getCapacity() + GuiHelper.GuiColor.BLUE + "mb");
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

    public IValueProvider<FluidStack> getAirTankProvider() {
        return airTank;
    }

    public IValueProvider<FluidStack> getOutputTankProvider() {
        return outputTank;
    }

    public IValueProvider<TeslaBank> getEnergyProvider() {
        return energyTank;
    }

    public IValueProvider<Integer> getProgress() {
        return timeCooked;
    }
}
