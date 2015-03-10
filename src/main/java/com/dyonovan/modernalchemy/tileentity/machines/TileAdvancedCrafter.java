package com.dyonovan.modernalchemy.tileentity.machines;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.audio.SoundHelper;
import com.dyonovan.modernalchemy.collections.PermutableSet;
import com.dyonovan.modernalchemy.crafting.AdvancedCrafterRecipeRegistry;
import com.dyonovan.modernalchemy.crafting.OreDictStack;
import com.dyonovan.modernalchemy.crafting.RecipeAdvancedCrafter;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import com.dyonovan.modernalchemy.tileentity.InventoryTile;
import com.dyonovan.modernalchemy.util.InventoryUtils;
import com.dyonovan.teambrcore.helpers.GuiHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileAdvancedCrafter extends BaseTile implements IEnergyHandler, ISidedInventory {

    /**
     * Energy used per tick
     */
    private static final int RF_TICK = 100;

    /**
     * Inventory Slots
     */
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int INPUT_SLOT_3 = 2;
    public static final int INPUT_SLOT_4 = 3;
    public static final int OUTPUT_SLOT = 4;

    /**
     * Crafting Modes
     */
    public static final int ENRICH = 0;
    public static final int EXTRUDE = 1;
    public static final int BEND = 2;
    public static final int FURNACE = 3;

    private EnergyStorage energyRF;
    public InventoryTile inventory;
    private int currentProcessTime;
    private boolean isActive;
    private ItemStack outputItem;
    private List<Object> currentRecipe;
    public int currentMode;
    public int requiredProcessTime;
    private int qtyOutput;
    private List<Integer> furnaceSlot;

    private int coolDown = 60;


    public TileAdvancedCrafter() {
        super();
        energyRF = new EnergyStorage(10000, 1000, 0);
        inventory = new InventoryTile(5);
        this.isActive = false;
        this.currentProcessTime = 0;
        currentMode = ENRICH;
        requiredProcessTime = 0;
        qtyOutput = 0;
        currentRecipe = new ArrayList<>();
        furnaceSlot = new ArrayList<>();
    }

    /*******************************************************************************************************************
     ************************************* Furnace Functions ***********************************************************
     *******************************************************************************************************************/

    /**
     * Checks to see if the current inventory setup matches anything in the {@link com.dyonovan.modernalchemy.crafting.AdvancedCrafterRecipeRegistry}
     * @return true if we are capable of smelting
     */
    private boolean canSmelt() {
        if(coolDown > 0)
            return false;


        if (currentMode == FURNACE) {
            for (int i = 0; i < 4; i++) {
                if (inventory.getStackInSlot(i) != null) {
                    outputItem = FurnaceRecipes.smelting().getSmeltingResult(inventory.getStackInSlot(i));
                    //Check if there is a recipe
                    if (outputItem == null) continue;
                    //Return false if item in output is differrent then smelting result
                    if (inventory.getStackInSlot(4) != null && !inventory.getStackInSlot(4).isItemEqual(outputItem)) continue;

                    furnaceSlot.clear();
                    int count = Math.min(inventory.getStackInSlot(i).stackSize, 4);
                    if (count < 4) {
                        for (int j = i + 1; j < 4; j++) {
                            if (inventory.getStackInSlot(j) != null && inventory.getStackInSlot(j).isItemEqual(inventory.getStackInSlot(i)) && count < 4) {
                                count = Math.min(inventory.getStackInSlot(j).stackSize + count,4);
                                furnaceSlot.add(j);
                            }
                        }
                    }

                    //If nothing in output go ahead and start
                    if (inventory.getStackInSlot(4) != null) {
                        while (inventory.getStackInSlot(4).stackSize + count > inventory.getStackInSlot(4).getMaxStackSize()) {
                            if (count == 0) return false;
                            count--;
                        }
                    }
                    this.qtyOutput = count;
                    this.requiredProcessTime = 200;
                    furnaceSlot.add(i);
                    return true;
                }
            }
        }

        List<Object> itemInput = new ArrayList<>(4); //Build our current input array
        for (int i = 0; i < 4; i++) {
            itemInput.add(getSpecialStackInSlot(i));
        }

        PermutableSet<Object> set = new PermutableSet<>(itemInput);
        List<List<Object>> listOfCombinations = new ArrayList<>();
        for(int i = 4; i > 0; i--) {
            for (List<Object> list : set.getPermutationsList(i)) {
                listOfCombinations.add(list);
            }
        }

        for (List<Object> combination : listOfCombinations) {
            for (int i = 0; i < 4; i++) {
                if (i >= combination.size())
                    combination.add(null);
            }
            Collections.sort(combination, InventoryUtils.itemStackComparator);
            for (RecipeAdvancedCrafter recipe : AdvancedCrafterRecipeRegistry.instance.recipes) { //See if a recipe matches our current setup
                if (currentMode != recipe.getRequiredMode()) //Must be the correct mode. No sense otherwise
                    continue;


                List<Object> tempInput = new ArrayList<>(); //Build a new list
                for (int i = 0; i < 4; i++) {
                    if (i < recipe.getInput().size()) { //If something exists in the recipe
                        tempInput.add(recipe.getInput().get(i));
                    } else //Fill the rest with nulls
                        tempInput.add(null);
                }

                boolean valid = true;


                Collections.sort(tempInput, InventoryUtils.itemStackComparator);
                for (int i = 0; i < tempInput.size(); i++) { //Compare stacks, must be the same order
                    if (!InventoryUtils.areStacksEqual(tempInput.get(i), combination.get(i)))
                        valid = false;
                }

                if (!valid) //Recipe didn't match, move on
                    continue;

                if ((this.inventory.getStackInSlot(4) == null ||
                        (InventoryUtils.areStacksEqual(recipe.getOutputItem(), this.inventory.getStackInSlot(4)) &&
                                this.inventory.getStackInSlot(4).stackSize + recipe.getQtyOutput() <= this.inventory.getStackInSlot(4).getMaxStackSize()))) {
                    this.outputItem = recipe.getOutputItem();
                    currentRecipe.clear();
                    for(Object obj : recipe.getInput()) {
                        if(obj instanceof ItemStack)
                            currentRecipe.add(obj);
                        else if(obj instanceof OreDictStack)
                            currentRecipe.add(((OreDictStack) obj).getItemList());
                    }
                    this.requiredProcessTime = recipe.getProcessTime();
                    this.qtyOutput = recipe.getQtyOutput();
                    return true;
                }
            }
        }
        return false;
    }

    public Object getSpecialStackInSlot(int i) {
        if(inventory.getStackInSlot(i) != null && OreDictionary.getOreIDs(inventory.getStackInSlot(i)).length > 0) {
            return new OreDictStack(OreDictionary.getOreName(OreDictionary.getOreIDs(inventory.getStackInSlot(i))[0]), inventory.getStackInSlot(i).stackSize);
        }
        else
            return this.inventory.getStackInSlot(i);
    }

    /**
     * Checks if can smelt, if it can processes the resources and then outputs
     */
    private void doSmelt() {

        if(outputItem == null || currentRecipe == null) //In case we loose our output item, remake it
            doReset();
        if (currentProcessTime == 0 && canSmelt()) { //Begin
            currentProcessTime = 1;
            this.isActive = true;
            if (currentMode == FURNACE) {
                if (inventory.getStackInSlot(furnaceSlot.get(furnaceSlot.size() - 1)).stackSize == qtyOutput) {
                    inventory.setStackInSlot(null, furnaceSlot.get(furnaceSlot.size() - 1));
                } else if (inventory.getStackInSlot(furnaceSlot.get(furnaceSlot.size() - 1)).stackSize > qtyOutput) {
                    inventory.getStackInSlot(furnaceSlot.get(furnaceSlot.size() - 1)).stackSize -= qtyOutput;
                } else {
                    int count = qtyOutput - inventory.getStackInSlot(furnaceSlot.get(furnaceSlot.size() - 1)).stackSize;
                    inventory.setStackInSlot(null, furnaceSlot.get(furnaceSlot.size() - 1));
                    for (int i = 0; i < furnaceSlot.size() - 1; i++) {
                        if (inventory.getStackInSlot(furnaceSlot.get(i)).stackSize <= count) {
                            count -=  inventory.getStackInSlot(furnaceSlot.get(i)).stackSize;
                            inventory.setStackInSlot(null, furnaceSlot.get(i));
                        } else if (inventory.getStackInSlot(furnaceSlot.get(i)).stackSize > count) {
                            inventory.getStackInSlot(furnaceSlot.get(i)).stackSize -= count;
                        }
                    }
                }
            } else {
                while (currentRecipe.size() > 0) {
                    if (currentRecipe.get(0) instanceof ItemStack) {
                        for (int i = 0; i < 4; i++) {
                            if (InventoryUtils.areStacksEqual(currentRecipe.get(0), inventory.getStackInSlot(i))) {
                                inventory.getStackInSlot(i).stackSize -= ((ItemStack) currentRecipe.get(0)).stackSize;
                                currentRecipe.remove(0);
                                if (inventory.getStackInSlot(i).stackSize <= 0)
                                    inventory.setStackInSlot(null, i);
                                break;
                            }
                        }
                    } else if (currentRecipe.get(0) instanceof List) {
                        boolean doMore = true;
                        for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
                            if (doMore) {
                                //noinspection unchecked
                                for (ItemStack oreStack : (ArrayList<ItemStack>) currentRecipe.get(0)) {
                                    if (InventoryUtils.areStacksEqual(oreStack, inventory.getStackInSlot(i)) && oreStack.getItemDamage() == inventory.getStackInSlot(i).getItemDamage()) {
                                        inventory.getStackInSlot(i).stackSize -= oreStack.stackSize;
                                        currentRecipe.remove(0);
                                        if (inventory.getStackInSlot(i).stackSize <= 0)
                                            inventory.setStackInSlot(null, i);
                                        doMore = false;
                                        break;
                                    }
                                }
                            } else
                                break;
                        }
                    } else
                        currentRecipe.remove(0);
                }
            }

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        if (currentProcessTime > 0 && currentProcessTime < requiredProcessTime) { //Process
            if (this.energyRF.getEnergyStored() < RF_TICK) {
                doFail();
                return;
            }
            this.energyRF.modifyEnergyStored(-RF_TICK);
            currentProcessTime += 1;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        if (currentProcessTime > 0 && currentProcessTime >= requiredProcessTime) { //Output
            if (this.inventory.getStackInSlot(4) == null) {
                this.setInventorySlotContents(4, new ItemStack(outputItem.getItem(), this.qtyOutput, outputItem.getItemDamage()));
            } else {
                this.inventory.getStackInSlot(4).stackSize += this.qtyOutput;
            }
            doReset();
        }
    }

    /**
     * Reset values (usually if something fails or process complete)
     */
    private void doReset() {
        currentProcessTime = 0;
        requiredProcessTime = 0;
        this.isActive = false;
        qtyOutput = 0;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void doFail() {
        if(worldObj.isRemote)
            SoundHelper.playSound("shutdown", xCoord, yCoord, zCoord, 1.0F, 1.0F);
        coolDown = 60;
        doReset();
    }

    /**
     * Get the scaled progress (usually used to scale to gui's)
     * @param scale Max number to scale down to
     * @return A number ranging from 0 - scale equivalent to the current process time
     */
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
        return true;
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
        if (inventory.getStackInSlot(0) == null && inventory.getStackInSlot(1) == null &&
                inventory.getStackInSlot(2) == null && inventory.getStackInSlot(3) == null && currentProcessTime == 0) return;
        coolDown--;
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
        coolDown = tag.getInteger("CoolDown");
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
        tag.setInteger("CoolDown", coolDown);
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
