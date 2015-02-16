package com.dyonovan.modernalchemy.tileentity.machines;

import com.dyonovan.modernalchemy.blocks.machines.BlockPatternRecorder;
import com.dyonovan.modernalchemy.blocks.machines.BlockSolidifier;
import com.dyonovan.modernalchemy.energy.ITeslaHandler;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.items.ItemPattern;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import com.dyonovan.modernalchemy.tileentity.InventoryTile;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.RenderUtils;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class TilePatternRecorder extends BaseTile implements ITeslaHandler, IInventory {

    private static final int PROCESS_TIME = 500; //5 mins with 1 T/Tick
    public static final int INPUT_SLOT = 1;
    public static final int ITEM_SLOT = 0;
    public static final int OUTPUT_SLOT = 2;

    public InventoryTile inventory;

    private TeslaBank energy;
    private int currentSpeed;
    private String itemCopy;
    public int currentProcessTime;

    public TilePatternRecorder() {
        inventory = new InventoryTile(3);
        currentProcessTime = 0;
        energy = new TeslaBank(0, 1000);
        itemCopy = "";
    }

    public int getProgressScaled(int scale) { return this.currentProcessTime * scale / PROCESS_TIME; }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energy.readFromNBT(tag);
        inventory.readFromNBT(tag, this);
        currentProcessTime = tag.getInteger("TimeProcessed");
        itemCopy = tag.getString("Item");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energy.writeToNBT(tag);
        inventory.writeToNBT(tag);
        tag.setInteger("TimeProcessed", currentProcessTime);
        tag.setString("Item", itemCopy);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        
        if (worldObj.isRemote) return;

        if(energy.canAcceptEnergy())
            chargeFromCoils();

        if (canStartWork() || currentProcessTime > 0) {
                                //todo make sure item has replicator value
            updateSpeed();
            if (!isActive)
                isActive = BlockPatternRecorder.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            
            if (currentProcessTime <= 0 && canStartWork()) {
                GameRegistry.UniqueIdentifier uniqueIdentifier = GameRegistry.findUniqueIdentifierFor(inventory.getStackInSlot(0).getItem());
                itemCopy = uniqueIdentifier.modId + ":" + uniqueIdentifier.name + ":" + inventory.getStackInSlot(0).getItemDamage();

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
        } else if (isActive) {
            isActive = BlockSolidifier.toggleIsActive(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            currentProcessTime = 0;
        }
        super.markDirty();
    }

    public static ItemStack recordPattern(String item) {

        ItemStack pattern = new ItemStack(ItemHandler.itemPattern, 1);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Item", item);
        tag.setInteger("Value", ReplicatorUtils.getValueForItem(ReplicatorUtils.getReturn(item)));
        pattern.setTagCompound(tag);
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
                return true; //TODO add check for replication value
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
