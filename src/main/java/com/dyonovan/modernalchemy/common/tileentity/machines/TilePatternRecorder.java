package com.dyonovan.modernalchemy.common.tileentity.machines;

import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.util.ReplicatorValues;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.common.items.ItemPattern;
import com.dyonovan.modernalchemy.common.tileentity.BaseMachine;
import com.dyonovan.modernalchemy.common.tileentity.InventoryTile;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class TilePatternRecorder extends BaseMachine implements  IInventory {

    private static final int PROCESS_TIME = 500; //5 mins with 1 T/Tick
    public static final int INPUT_SLOT = 1;
    public static final int ITEM_SLOT = 0;
    public static final int OUTPUT_SLOT = 2;

    public InventoryTile inventory;

    private int currentSpeed;
    private String itemCopy;
    private float lastQuality = 0.0F;
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
            updateSpeed();
            if (!isActive)
                isActive = true;

            if (currentProcessTime <= 0 && canStartWork()) {
                GameRegistry.UniqueIdentifier uniqueIdentifier = GameRegistry.findUniqueIdentifierFor(inventory.getStackInSlot(ITEM_SLOT).getItem());
                itemCopy = uniqueIdentifier.modId + ":" + uniqueIdentifier.name + ":" + inventory.getStackInSlot(ITEM_SLOT).getItemDamage();
                if(inventory.getStackInSlot(INPUT_SLOT).hasTagCompound()) {
                    lastQuality = inventory.getStackInSlot(INPUT_SLOT).getTagCompound().getFloat("Quality");
                    if(!inventory.getStackInSlot(INPUT_SLOT).getTagCompound().getString("Item").equalsIgnoreCase(itemCopy)) //Must be a pattern of same item type
                        return;
                }
                else
                    lastQuality = 0.0F;
                decrStackSize(ITEM_SLOT, 1);
                decrStackSize(INPUT_SLOT, 1);
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

    private ItemStack recordPattern(String item) {
        ItemStack pattern = new ItemStack(ItemHandler.itemReplicatorPattern, 1);
        ReplicatorValues values = ReplicatorUtils.getValueForItem(ReplicatorUtils.getReturn(item));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Item", item);
        tag.setInteger("Value", values.reqTicks);
        tag.setInteger("Qty", values.qtyReturn);
        float value = Math.max((1000 / values.reqTicks / 10), 0.1F);
        if(lastQuality > 0)
            tag.setFloat("Quality", lastQuality + value <= 100 ? lastQuality + value : 100);
        else
            tag.setFloat("Quality", value <= 100 ? value : 100);
        pattern.setTagCompound(tag);

        return pattern;
    }

    private boolean canStartWork() {
        return inventory.getStackInSlot(INPUT_SLOT) != null && inventory.getStackInSlot(ITEM_SLOT) != null &&
                inventory.getStackInSlot(INPUT_SLOT).getItem() instanceof ItemPattern &&
                inventory.getStackInSlot(OUTPUT_SLOT) == null &&
                ReplicatorUtils.getValueForItem(inventory.getStackInSlot(ITEM_SLOT)) != null && getEnergyLevel() >= 1;
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
                return true;
            case INPUT_SLOT:
                return stack.getItem() instanceof ItemPattern;
            default:
                return false;
        }
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

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.readFromNBT(tag, this);
        currentProcessTime = tag.getInteger("TimeProcessed");
        lastQuality = tag.getFloat("Quality");
        if(tag.hasKey("Item"))
            itemCopy = tag.getString("Item");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        inventory.writeToNBT(tag);
        tag.setInteger("TimeProcessed", currentProcessTime);
        tag.setFloat("Quality", lastQuality);
        if(itemCopy != null && itemCopy.length() > 0)
            tag.setString("Item", itemCopy);
    }

    /*******************************************************************************************************************
     ********************************************** Misc Functions *****************************************************
     *******************************************************************************************************************/
    @Override
    public void returnWailaHead(List<String> head) {
        head.add(GuiHelper.GuiColor.YELLOW + "Is Writing: " + GuiHelper.GuiColor.WHITE + (isActive() ? "Yes" : "No"));
        if(isActive())
            head.add(GuiHelper.GuiColor.YELLOW + "Item Copying: " + GuiHelper.GuiColor.WHITE + ReplicatorUtils.getReturn(itemCopy).getDisplayName());
        head.add(GuiHelper.GuiColor.YELLOW + "Energy: " + GuiHelper.GuiColor.WHITE + energyTank.getEnergyLevel() + "/" + energyTank.getMaxCapacity() + GuiHelper.GuiColor.TURQUISE + "T");
    }
}
