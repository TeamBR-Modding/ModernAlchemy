package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.blocks.BlockDummy;
import com.dyonovan.itemreplication.effects.LightningBolt;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.handlers.BlockHandler;
import com.dyonovan.itemreplication.handlers.ConfigHandler;
import com.dyonovan.itemreplication.helpers.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.awt.*;
import java.util.List;

public class TileArcFurnaceCore extends BaseCore implements IFluidHandler, ITeslaHandler, IInventory {

    private static final int COOK_TIME = 500;

    private FluidTank outputTank;
    private FluidTank airTank;

    private TeslaBank energyTank;

    public ItemStack inventory[];
    private static final int INPUT_SLOT = 0;
    private static final int CATALYST_SLOT = 1;

    private int currentSpeed;
    private int timeCooked;

    public TileArcFurnaceCore() {
        outputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        airTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        airTank.fill(new FluidStack(BlockHandler.fluidCompressedAir, FluidContainerRegistry.BUCKET_VOLUME * 10), true);
        energyTank = new TeslaBank(0, 1000);

        inventory = new ItemStack[2];
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        updateSpeed();
        if(energyTank.canAcceptEnergy() && isValid) {
            chargeFromCoils();
        }
        doSmelting();
    }

    public void doSmelting() {
        if(canSmelt() && timeCooked == 0) {
            //Consume Resources
            airTank.drain(100, true);
            inventory[0].stackSize--;
            if(inventory[0].stackSize == 0)
                inventory[0] = null;
            inventory[1].stackSize--;
            if(inventory[1].stackSize == 0)
                inventory[1] = null;
            timeCooked += currentSpeed;
        }
        else if(timeCooked > 0 && timeCooked < COOK_TIME) {
            timeCooked += currentSpeed;
            energyTank.drainEnergy(currentSpeed + 1);
        }
        else if(timeCooked >= COOK_TIME)
            smelt();
    }

    public void smelt() {
        timeCooked = 0;
        outputTank.fill(new FluidStack(BlockHandler.fluidActinium, FluidContainerRegistry.BUCKET_VOLUME), true);
    }

    public boolean canSmelt() {
        if(inventory[0] != null && inventory [1] != null)
            return airTank.getFluidAmount() > 100 &&
                    inventory[0].getItem() == Item.getItemFromBlock(BlockHandler.blockOreActinium) &&
                    inventory[1].getItem() == Items.coal &&
                    outputTank.getCapacity() - outputTank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME;
        else
            return false;
    }

    public void updateSpeed() {
        currentSpeed = (energyTank.getEnergyLevel() * 20) / energyTank.getMaxCapacity();
        if(currentSpeed == 0)
            currentSpeed = 1;
    }

    public int getCookTimeScaled(int scale) {
        return (timeCooked * scale) / COOK_TIME;
    }

    public void chargeFromCoils() {
        int maxFill = energyTank.getMaxCapacity() - energyTank.getEnergyLevel();
        List<TileTeslaCoil> coils = findCoils(worldObj, this);
        int currentDrain = 0;
        for(TileTeslaCoil coil : coils) {
            if (coil.getEnergyLevel() <= 0) continue; //fixes looking like its working when coil is empty
            int fill = coil.getEnergyLevel() > ConfigHandler.tickTesla ? ConfigHandler.tickTesla : coil.getEnergyLevel();
            if(currentDrain + fill > maxFill)
                fill = maxFill - currentDrain;
            currentDrain += fill;
            coil.drainEnergy(fill);

            if(worldObj.isRemote) {
                Minecraft.getMinecraft().effectRenderer.addEffect(new LightningBolt(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, coil.xCoord + 0.5, coil.yCoord + 0.5, coil.zCoord + 0.5, fill > 4 ? fill : 4, new Color(255, 255, 255, 255)));
            }
        }
        while(currentDrain > 0) {
            energyTank.addEnergy(1);
            currentDrain--;
        }
    }

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

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        timeCooked = tagCompound.getInteger("TimeCooking");

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

        NBTTagList itemsTag = tagCompound.getTagList("Items", 10);
        this.inventory = new ItemStack[getSizeInventory()];
        for (int i = 0; i < itemsTag.tagCount(); i++)
        {
            NBTTagCompound nbtTagCompound1 = itemsTag.getCompoundTagAt(i);
            NBTBase nbt = nbtTagCompound1.getTag("Slot");
            int j = -1;
            if ((nbt instanceof NBTTagByte)) {
                j = nbtTagCompound1.getByte("Slot") & 0xFF;
            } else {
                j = nbtTagCompound1.getShort("Slot");
            }
            if ((j >= 0) && (j < this.inventory.length)) {
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbtTagCompound1);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("TimeCooking", timeCooked);

        energyTank.writeToNBT(tagCompound);
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

        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < this.inventory.length; i++) {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbtTagCompound1 = new NBTTagCompound();
                nbtTagCompound1.setShort("Slot", (short)i);
                this.inventory[i].writeToNBT(nbtTagCompound1);
                nbtTagList.appendTag(nbtTagCompound1);
            }
        }
        tagCompound.setTag("Items", nbtTagList);
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;
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

    @Override
    public void addEnergy(int maxAmount) {
        energyTank.addEnergy(maxAmount);
    }

    @Override
    public void drainEnergy(int maxAmount) {
        energyTank.drainEnergy(maxAmount);
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
