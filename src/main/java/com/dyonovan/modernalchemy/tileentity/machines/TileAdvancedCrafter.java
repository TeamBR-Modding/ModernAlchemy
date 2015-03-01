package com.dyonovan.modernalchemy.tileentity.machines;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.crafting.AdvancedCrafterRecipeRegistry;
import com.dyonovan.modernalchemy.crafting.RecipeAdvancedCrafter;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import com.dyonovan.modernalchemy.tileentity.InventoryTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TileAdvancedCrafter extends BaseTile implements IEnergyHandler, ISidedInventory {

    private static final int RF_TICK = 100;
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int INPUT_SLOT_3 = 2;
    public static final int INPUT_SLOT_4 = 3;
    public static final int OUTPUT_SLOT = 4;
    public static final int COOK = 1;
    public static final int EXTRUDE = 2;
    public static final int BEND = 3;

    private EnergyStorage energyRF;
    public InventoryTile inventory;
    private int currentProcessTime;
    private boolean isActive;
    private Item outputItem;
    public int currentMode;
    public int requiredProcessTime;
    private int qtyOutput;


    public TileAdvancedCrafter() {
        super();
        energyRF = new EnergyStorage(10000, 1000, 0);
        inventory = new InventoryTile(5);
        this.isActive = false;
        this.currentProcessTime = 0;
        currentMode = COOK;
        requiredProcessTime = 0;
        qtyOutput = 0;
    }

    /*******************************************************************************************************************
     ************************************* Furnace Functions ***********************************************************
     *******************************************************************************************************************/

    private boolean canSmelt() {

        ArrayList<Item> itemInput = new ArrayList<Item>();

        for (int i = 0; i < 4; i++) {
            if (this.inventory.getStackInSlot(i) != null) {
                itemInput.add(this.inventory.getStackInSlot(i).getItem());
            }
        }
        if (itemInput.size() <= 0) return false;

        Collections.sort(itemInput, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getUnlocalizedName().compareTo(o2.getUnlocalizedName());
            }
        });

        for (RecipeAdvancedCrafter recipe : AdvancedCrafterRecipeRegistry.instance.recipes) {
            if (recipe.getInput().equals(itemInput)) {
                if (this.currentMode == recipe.getRequiredMode() && (this.inventory.getStackInSlot(4) == null ||
                        (recipe.getOutputItem() == this.inventory.getStackInSlot(4).getItem() &&
                        this.inventory.getStackInSlot(4).stackSize + recipe.getQtyOutput() <= this.inventory.getStackInSlot(4).getMaxStackSize()))) {
                    this.outputItem = recipe.getOutputItem();
                    this.requiredProcessTime = recipe.getProcessTime();
                    this.qtyOutput = recipe.getQtyOutput();
                    return true;
                }
            }
        }
        return false;
    }

    private void doSmelt() {

        if (currentProcessTime == 0 && canSmelt()) {
            currentProcessTime = 1;
            for (int i = 0; i < 4; i++) {
                if (this.inventory.getStackInSlot(i) != null)
                    this.decrStackSize(i, 1);
            }
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        if (currentProcessTime > 0 && currentProcessTime < requiredProcessTime) {

            if (this.energyRF.getEnergyStored() < RF_TICK) {
                doReset();
                return;
            }
            this.energyRF.modifyEnergyStored(-RF_TICK);
            currentProcessTime += 1;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        if (currentProcessTime > 0 && currentProcessTime >= requiredProcessTime) {
            if (this.inventory.getStackInSlot(4) == null) {
                this.setInventorySlotContents(4, new ItemStack(outputItem, qtyOutput));
            } else {
                this.inventory.getStackInSlot(4).stackSize = this.inventory.getStackInSlot(4).stackSize + qtyOutput;
            }
            doReset();
        }
    }

    private void doReset() {
        currentProcessTime = 0;
        requiredProcessTime = 0;
        qtyOutput = 0;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public int getProgressScaled(int scale) { return requiredProcessTime == 0 ? 0 : this.currentProcessTime * scale / requiredProcessTime; }

    /*******************************************************************************************************************
     ******************************************** Energy Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate) {
        if (!simulate) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return energyRF.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate) {
        return energyRF.extractEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        return true;
    }

    /*******************************************************************************************************************
     ********************************************** Item Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        /*(switch(side) {
            case 1:
                return new int[] {OUTPUT_SLOT};
            case 2:
                return new int[] {INPUT_SLOT_1};
            case 3:
                return new int[] {INPUT_SLOT_2};
            case 4:
                return new int[] {INPUT_SLOT_3};
            case 5:
                return new int[] {INPUT_SLOT_4};
            default:
                return new int[0];
        }*/
        return new int[] {0, 1, 2, 3, 4};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        return slot == INPUT_SLOT_1 || slot == INPUT_SLOT_2 || slot == INPUT_SLOT_3 || slot == INPUT_SLOT_4;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        return slot == OUTPUT_SLOT;
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
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack itemstack = getStackInSlot(slot);

        if(itemstack != null) {
            if(itemstack.stackSize <= count) {
                setInventorySlotContents(slot, null);
            }
            itemstack = itemstack.splitStack(count);
        }
        super.markDirty();
        return itemstack;
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
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        inventory.setStackInSlot(itemStack, slot);
        worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public String getInventoryName() {
        return Constants.MODID + ":blockAdvancedFurnace";
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
        return slot >= 0 && slot <= 3 && AdvancedCrafterRecipeRegistry.instance.checkInput(itemStack.getItem());
    }

    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void onWrench(EntityPlayer player) {

    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;
        doSmelt();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energyRF.readFromNBT(tag);
        inventory.readFromNBT(tag, this);
        currentProcessTime = tag.getInteger("TimeProcessed");
        requiredProcessTime = tag.getInteger("RequiredTime");
        isActive = tag.getBoolean("isActive");
        currentMode = tag.getInteger("CurrentMode");
        qtyOutput = tag.getInteger("Qty");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energyRF.writeToNBT(tag);
        inventory.writeToNBT(tag);
        tag.setInteger("TimeProcessed", currentProcessTime);
        tag.setInteger("RequiredTime", requiredProcessTime);
        tag.setBoolean("isActive", isActive);
        tag.setInteger("CurrentMode", currentMode);
        tag.setInteger("Qty", qtyOutput);
    }

    /*******************************************************************************************************************
     ********************************************** Misc Functions *****************************************************
     *******************************************************************************************************************/
    @Override
    public void returnWailaHead(List<String> head) {
        head.add(GuiHelper.GuiColor.YELLOW + "Working: " + GuiHelper.GuiColor.WHITE + (isActive ? "Yes" : "No"));
        //head.add(GuiHelper.GuiColor.YELLOW + "Energy: " + GuiHelper.GuiColor.WHITE + energyRF.getEnergyStored() + "/" +  energyRF.getMaxEnergyStored() + GuiHelper.GuiColor.TURQUISE + "RF");
    }
}
