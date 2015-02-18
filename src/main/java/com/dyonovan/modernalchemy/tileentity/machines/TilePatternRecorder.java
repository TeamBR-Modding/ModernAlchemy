package com.dyonovan.modernalchemy.tileentity.machines;

import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.items.ItemPattern;
import com.dyonovan.modernalchemy.tileentity.BaseMachine;
import com.dyonovan.modernalchemy.tileentity.InventoryTile;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TilePatternRecorder extends BaseMachine implements  IInventory {

    private static final int PROCESS_TIME = 500; //5 mins with 1 T/Tick
    public static final int INPUT_SLOT = 1;
    public static final int ITEM_SLOT = 0;
    public static final int OUTPUT_SLOT = 2;

    public InventoryTile inventory;

    private int currentSpeed;
    private String itemCopy;
    public int currentProcessTime;

    public TilePatternRecorder() {
        inventory = new InventoryTile(3);
        currentProcessTime = 0;
        energyTank = new TeslaBank(0, 1000);
        itemCopy = "";
    }

    /*******************************************************************************************************************
     ************************************* Pattern Recorder Functions **************************************************
     *******************************************************************************************************************/

    private void doRecording() {
        if (canStartWork() || currentProcessTime > 0) {
            //todo make sure item has replicator value
            updateSpeed();
            if (!isActive)
                isActive = true;

            if (currentProcessTime <= 0 && canStartWork()) {
                GameRegistry.UniqueIdentifier uniqueIdentifier = GameRegistry.findUniqueIdentifierFor(inventory.getStackInSlot(0).getItem());
                itemCopy = uniqueIdentifier.modId + ":" + uniqueIdentifier.name + ":" + inventory.getStackInSlot(0).getItemDamage();

                decrStackSize(0, 1);
                decrStackSize(1, 1);
                currentProcessTime = 1;
            }

            if (currentProcessTime < PROCESS_TIME) {
                if (energyTank.getEnergyLevel() > 0) {
                    energyTank.drainEnergy(currentSpeed);
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
            isActive = false;
            currentProcessTime = 0;
        }
    }

    private void updateSpeed() {
        if (energyTank.getEnergyLevel() == 0) {
            currentSpeed = 0;
            return;
        }

        currentSpeed = (energyTank.getEnergyLevel() * 20) / energyTank.getMaxCapacity();
        if (currentSpeed == 0)
            currentSpeed = 1;
    }

    private static ItemStack recordPattern(String item) {
        ItemStack pattern = new ItemStack(ItemHandler.itemPattern, 1);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Item", item);
        tag.setInteger("Value", ReplicatorUtils.getValueForItem(ReplicatorUtils.getReturn(item)));
        pattern.setTagCompound(tag);
        return pattern;
    }

    private boolean canStartWork() {
        return inventory.getStackInSlot(INPUT_SLOT) != null && inventory.getStackInSlot(ITEM_SLOT) != null &&
                inventory.getStackInSlot(INPUT_SLOT).getItem() instanceof ItemPattern &&
                inventory.getStackInSlot(OUTPUT_SLOT) == null;
    }

    public int getProgressScaled(int scale) { return this.currentProcessTime * scale / PROCESS_TIME; }

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
    public void openInventory() {}

    @Override
    public void closeInventory() {}

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
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.readFromNBT(tag, this);
        currentProcessTime = tag.getInteger("TimeProcessed");
        if(tag.hasKey("Item"))
            itemCopy = tag.getString("Item");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        inventory.writeToNBT(tag);
        tag.setInteger("TimeProcessed", currentProcessTime);
        if(itemCopy != null && itemCopy.length() > 0)
            tag.setString("Item", itemCopy);
    }

    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;
        if(energyTank.canAcceptEnergy())
            chargeFromCoils();
        doRecording();
        markDirty();
    }
}
