package com.dyonovan.modernalchemy.tileentity.arcfurnace;

import com.dyonovan.modernalchemy.blocks.arcfurnace.dummies.BlockDummy;
import com.dyonovan.modernalchemy.energy.ITeslaHandler;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.tileentity.InventoryTile;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies.TileDummy;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

public class TileArcFurnaceCore extends BaseCore implements IFluidHandler, ITeslaHandler, IInventory {

    /**
     * Duration in ticks for the Cook Process
     */
    private static final int COOK_TIME = 500;

    //Tanks
    private FluidTank outputTank;
    private FluidTank airTank;

    //Energy
    private TeslaBank energyTank;

    //Inventory
    public InventoryTile inventory;
    private static final int INPUT_SLOT = 0;
    private static final int CATALYST_SLOT = 1;

    //Cook Variables
    private int currentSpeed;
    private int timeCooked;

    /**
     * Creates the Arc Furnace, Default fluids at 10 * BUCKET_VOLUME and energy to 1000T and an inventory of two slots
     */
    public TileArcFurnaceCore() {
        outputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        airTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);

        energyTank = new TeslaBank(0, 1000);

        inventory = new InventoryTile(2);
    }

    /*******************************************************************************************************************
     **************************************** MultiBlock Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public boolean isWellFormed() {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                for(int k = -1; k <= 1; k++) {
                    if(i == 0 && j == 0 && k == 0)
                        continue;
                    if(!(worldObj.getBlock(xCoord + i, yCoord + j, zCoord + k) instanceof BlockDummy))
                        return false;
                }
            }
        }
        buildStructure();
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
                    worldObj.setBlockMetadataWithNotify(xCoord + i, yCoord + j, zCoord + k, 1, 2);
                }
            }
        }
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
                            worldObj.setBlockMetadataWithNotify(xCoord + i, yCoord + j, zCoord + k, 0, 2);
                            dummy.setCoreLocation(new Location(-100, -100, -100));
                        }
                    }
                }
            }
        }
        isValid = false;
    }

    /*******************************************************************************************************************
     ***************************************** Arc Furnace Functions ***************************************************
     *******************************************************************************************************************/

    public void doSmelting() {
        if(canSmelt() && timeCooked == 0) {
            //Consume Resources
            airTank.drain(100, true);
            inventory.getStackInSlot(0).stackSize--;
            if(inventory.getStackInSlot(0).stackSize == 0)
                inventory.setStackInSlot(null, 0);
            inventory.getStackInSlot(1).stackSize--;
            if(inventory.getStackInSlot(1).stackSize == 0)
                inventory.setStackInSlot(null, 1);
            timeCooked += currentSpeed;
        }
        else if(timeCooked > 0 && timeCooked < COOK_TIME) {
            timeCooked += currentSpeed;
            energyTank.drainEnergy(currentSpeed);
        }
        else if(timeCooked >= COOK_TIME)
            smelt();
    }

    public void smelt() {
        timeCooked = 0;
        outputTank.fill(new FluidStack(BlockHandler.fluidActinium, FluidContainerRegistry.BUCKET_VOLUME), true);
    }

    public boolean canSmelt() {
        if(inventory.getStackInSlot(0) != null && inventory.getStackInSlot(1) != null)
            return airTank.getFluidAmount() > 100 &&
                    inventory.getStackInSlot(0).getItem() == Item.getItemFromBlock(BlockHandler.blockOreActinium) &&
                    inventory.getStackInSlot(1).getItem() == Items.coal &&
                    outputTank.getCapacity() - outputTank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME;
        else
            return false;
    }

    public void updateSpeed() {
        if(energyTank.getEnergyLevel() == 0) {
            currentSpeed = 0;
            return;
        }

        currentSpeed = (energyTank.getEnergyLevel() * 20) / energyTank.getMaxCapacity();
        if(currentSpeed == 0)
            currentSpeed = 1;
    }

    public int getCookTimeScaled(int scale) {
        return (timeCooked * scale) / COOK_TIME;
    }

    /*******************************************************************************************************************
     ******************************************** Energy Functions *****************************************************
     *******************************************************************************************************************/
    public void chargeFromCoils() {
        int maxFill = energyTank.getMaxCapacity() - energyTank.getEnergyLevel();
        List<TileTeslaCoil> coils = findCoils(worldObj, this);
        int currentDrain = 0;
        for(TileTeslaCoil coil : coils) {
            if (coil.getEnergyLevel() <= 0) continue; //fixes looking like its working when coil is empty
            int fill = coil.getEnergyLevel() > ConfigHandler.maxCoilTransfer ? ConfigHandler.maxCoilTransfer : coil.getEnergyLevel();
            if(currentDrain + fill > maxFill)
                fill = maxFill - currentDrain;
            currentDrain += fill;
            coil.drainEnergy(fill);

            RenderUtils.sendBoltToClient(xCoord, yCoord, zCoord, coil, fill);

            if(currentDrain >= maxFill) //Don't want to drain other coils we don't need to
                break;
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
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setStackInSlot(stack, slot);
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= amt) {
                setInventorySlotContents(slot, null);
            } else {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        switch(slot) {
        case INPUT_SLOT :
            return itemStack.getItem() == Item.getItemFromBlock(BlockHandler.blockOreActinium);
        case CATALYST_SLOT :
            return itemStack.getItem() == Items.coal;

        }
        return false;
    }

    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        timeCooked = tagCompound.getInteger("TimeCooking");

        inventory.readFromNBT(tagCompound, this);
        energyTank.readFromNBT(tagCompound);

        if(tagCompound.getBoolean("hasOutputFluid")) {
            outputTank.setFluid(FluidRegistry.getFluidStack(tagCompound.getString("outputFluid"), tagCompound.getInteger("outputFluidAmount")));
        }
        else
            outputTank.setFluid(null);

        if(tagCompound.getBoolean("hasAir")) {
            airTank.setFluid(FluidRegistry.getFluidStack(tagCompound.getString("air"), tagCompound.getInteger("airAmount")));
        }
        else
            airTank.setFluid(null);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("TimeCooking", timeCooked);

        energyTank.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);

        tagCompound.setBoolean("hasOutputFluid", outputTank.getFluid() != null);
        tagCompound.setBoolean("hasAir", airTank.getFluid() != null);
        if(outputTank.getFluid() != null) {
            tagCompound.setString("outputFluid", outputTank.getFluid().getFluid().getName());
            tagCompound.setInteger("outputFluidAmount", outputTank.getFluid().amount);
        }
        if(airTank.getFluid() != null) {
            tagCompound.setString("air", airTank.getFluid().getFluid().getName());
            tagCompound.setInteger("airAmount", airTank.getFluid().amount);
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(worldObj.isRemote) return;
        updateSpeed();
        if(energyTank.canAcceptEnergy() && isValid) {
            chargeFromCoils();
        }
        doSmelting();
    }
}
