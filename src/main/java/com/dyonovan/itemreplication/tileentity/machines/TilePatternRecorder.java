package com.dyonovan.itemreplication.tileentity.machines;

import com.dyonovan.itemreplication.blocks.machines.BlockPatternRecorder;
import com.dyonovan.itemreplication.blocks.machines.BlockSolidifier;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.handlers.ConfigHandler;
import com.dyonovan.itemreplication.handlers.ItemHandler;
import com.dyonovan.itemreplication.items.ItemPattern;
import com.dyonovan.itemreplication.tileentity.BaseTile;
import com.dyonovan.itemreplication.tileentity.InventoryTile;
import com.dyonovan.itemreplication.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.itemreplication.util.RenderUtils;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class TilePatternRecorder extends BaseTile implements ITeslaHandler, IInventory {

    private static final int PROCESS_TIME = 5000;
    public static final int INPUT_SLOT = 0;
    public static final int ITEM_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    public InventoryTile inventory;

    private TeslaBank energy;
    private boolean isActive;
    private int currentSpeed;
    private ItemStack itemCopy;

    public int currentProcessTime;

    public TilePatternRecorder() {
        inventory = new InventoryTile(3);
        currentProcessTime = 0;
        this.isActive = false;
        energy = new TeslaBank(0, 1000);
    }

    public int getProgressScaled(int scale) { return this.currentProcessTime * scale / PROCESS_TIME; }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energy.readFromNBT(tag);
        inventory.readFromNBT(tag, this);
        currentProcessTime = tag.getInteger("TimeProcessed");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energy.writeToNBT(tag);
        inventory.writeToNBT(tag);
        tag.setInteger("TimeProcessed", currentProcessTime);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        
        if (worldObj.isRemote) return;

        if(energy.canAcceptEnergy())
            chargeFromCoils();

        if (canStartWork() || currentProcessTime > 0) {

            updateSpeed();
            if (!isActive)
                isActive = BlockPatternRecorder.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            
            if (currentProcessTime <= 0 && canStartWork()) {
                itemCopy = inventory.getStackInSlot(INPUT_SLOT);
                decrStackSize(0, 1);
                decrStackSize(1, 1);
                currentProcessTime = 1;
            }

            if (currentProcessTime < PROCESS_TIME) {
                if (energy.getEnergyLevel() > 0) {
                    energy.drainEnergy(currentSpeed);
                    currentProcessTime += currentSpeed;
                } else {
                    currentProcessTime = 0;
                    itemCopy = null;
                }
            }
            if (currentProcessTime >= PROCESS_TIME) {
                inventory.setStackInSlot(recordPattern(itemCopy), 2);
                currentProcessTime = 0;
            }
            super.markDirty();
        } else if (isActive) {
            isActive = BlockSolidifier.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            currentProcessTime = 0;
        } 
    }

    public static ItemStack recordPattern(ItemStack item) {

        ItemStack pattern = new ItemStack(ItemHandler.itemPattern, 1);
        NBTTagCompound tag = new NBTTagCompound();
        GameRegistry.UniqueIdentifier uniqueIdentifier = GameRegistry.findUniqueIdentifierFor(item.getItem());
        tag.setString("Item", uniqueIdentifier.modId +
                ":" + uniqueIdentifier.name + ":" + item.getItemDamage());
        pattern.setTagCompound(tag);

        //((ItemPattern) pattern.getItem()).setIconRecordedPattern();
        return pattern;
    }

    public void updateSpeed() {
        if(energy.getEnergyLevel() == 0) {
            currentSpeed = 0;
            return;
        }

        currentSpeed = (energy.getEnergyLevel() * 20) / energy.getMaxCapacity();
        if(currentSpeed == 0)
            currentSpeed = 1;
    }

    public void chargeFromCoils() {
        int maxFill = energy.getMaxCapacity() - energy.getEnergyLevel();
        List<TileTeslaCoil> coils = findCoils(worldObj, this);
        int currentDrain = 0;
        for (TileTeslaCoil coil : coils) {
            if (coil.getEnergyLevel() <= 0) continue;
            int fill = coil.getEnergyLevel() > ConfigHandler.maxCoilTransfer ? ConfigHandler.maxCoilTransfer : coil.getEnergyLevel();
            if (currentDrain + fill > maxFill)
                fill = maxFill - currentDrain;
            currentDrain += fill;
            coil.drainEnergy(fill);

            RenderUtils.sendBoltToClient(xCoord, yCoord, zCoord, coil, fill);
        }
        while (currentDrain > 0) {
            energy.addEnergy(ConfigHandler.maxCoilTransfer);
            currentDrain--;
        }
    }

    private boolean canStartWork() {
        return inventory.getStackInSlot(INPUT_SLOT) != null && inventory.getStackInSlot(ITEM_SLOT) != null &&
                inventory.getStackInSlot(INPUT_SLOT).getItem() instanceof ItemPattern &&
                inventory.getStackInSlot(OUTPUT_SLOT) == null;
    }

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
        return 1;
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
    public boolean isItemValidForSlot(int slot, ItemStack stack) {

        switch(slot){
            case ITEM_SLOT:
                return true; // TODO: how should this be limited?
            case INPUT_SLOT:
                return stack.getItem() instanceof ItemPattern;
            default:
                return false;
        }
    }



    @Override
    public void addEnergy(int maxAmount) {
        energy.addEnergy(maxAmount);
    }

    @Override
    public int drainEnergy(int maxAmount) {
        return energy.drainEnergy(maxAmount);
    }

    @Override
    public int getEnergyLevel() {
        return energy.getEnergyLevel();
    }

    @Override
    public TeslaBank getEnergyBank() {
        return energy;
    }

    public void setEnergy(int amount) {
        energy.setEnergyLevel(amount);
    }
}
