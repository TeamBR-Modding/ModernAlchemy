package com.dyonovan.modernalchemy.common.tileentity.machines;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.dyonovan.modernalchemy.client.audio.SoundHelper;
import com.dyonovan.modernalchemy.collections.PermutableSet;
import com.dyonovan.modernalchemy.common.container.machines.ContainerAdvancedCrafter;
import com.dyonovan.modernalchemy.crafting.AdvancedCrafterRecipeRegistry;
import com.dyonovan.modernalchemy.crafting.OreDictStack;
import com.dyonovan.modernalchemy.crafting.RecipeAdvancedCrafter;
import com.dyonovan.modernalchemy.energy.SyncableRF;
import com.dyonovan.modernalchemy.client.gui.machines.GuiAdvancedCrafter;
import com.dyonovan.modernalchemy.client.rpc.ILevelChanger;
import com.dyonovan.modernalchemy.common.tileentity.TileModernAlchemy;
import com.dyonovan.modernalchemy.util.InventoryUtils;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import openmods.api.IHasGui;
import openmods.api.IValueProvider;
import openmods.api.IValueReceiver;
import openmods.gui.misc.IConfigurableGuiSlots;
import openmods.inventory.GenericInventory;
import openmods.inventory.IInventoryProvider;
import openmods.inventory.TileEntityInventory;
import openmods.inventory.legacy.ItemDistribution;
import openmods.sync.*;
import openmods.utils.MiscUtils;
import openmods.utils.SidedInventoryAdapter;
import openmods.utils.bitmap.BitMapUtils;
import openmods.utils.bitmap.IRpcDirectionBitMap;
import openmods.utils.bitmap.IRpcIntBitMap;
import openmods.utils.bitmap.IWriteableBitMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TileAdvancedCrafter extends TileModernAlchemy implements ILevelChanger, IEnergyHandler, IInventoryProvider, IHasGui, IConfigurableGuiSlots<TileAdvancedCrafter.AUTO_SLOTS> {

    public enum AUTO_SLOTS {
        output
    }

    /**
     * Energy used per tick
     */
    private static final int RF_TICK = 100;

    /**
     * Crafting Modes
     */
    public static final int ENRICH = 0;
    public static final int EXTRUDE = 1;
    public static final int BEND = 2;
    public static final int FURNACE = 3;

    protected SyncableRF energyRF;

    public SyncableSides sideOut;
    public SyncableSides sideIn;
    private SyncableInt currentProcessTime;
    public SyncableInt currentMode;
    public SyncableInt requiredProcessTime;
    private SyncableInt qtyOutput;
    private SyncableBoolean isActive;
    private SyncableItemStack outputItem;
    private SyncableFlags automaticSlots;


    private final GenericInventory inventory = registerInventoryCallback(new TileEntityInventory(this, "advancedcrafter", true, 5));

    private List<Object> currentRecipe;
    private ArrayList<Integer> furnaceSlot;

    private int coolDown = 60;


    public TileAdvancedCrafter() {
        SidedInventoryAdapter sided = new SidedInventoryAdapter(inventory);
        sided.registerSlots(0, 4, sideIn, true, false);
        sided.registerSlot(4, sideOut, false, true);

        furnaceSlot = new ArrayList<>();
        currentRecipe = new ArrayList<>();
    }

    @Override
    protected void createSyncedFields() {
        energyRF = new SyncableRF(new EnergyStorage(10000, 1000, 0));
        isActive = new SyncableBoolean(false);
        currentProcessTime = new SyncableInt(0);
        currentMode = new SyncableInt(ENRICH);
        requiredProcessTime = new SyncableInt(0);
        qtyOutput = new SyncableInt(0);
        outputItem = new SyncableItemStack();
        sideOut = new SyncableSides();
        sideIn = new SyncableSides();
        automaticSlots = SyncableFlags.create(AUTO_SLOTS.values().length);
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


        if (currentMode.get() == FURNACE) {
            for (int i = 0; i < 4; i++) {
                if (inventory.getStackInSlot(i) != null) {
                    outputItem.set(FurnaceRecipes.smelting().getSmeltingResult(inventory.getStackInSlot(i)));
                    //Check if there is a recipe
                    if (outputItem.get() == null) continue;
                    //Return false if item in output is differrent then smelting result
                    if (inventory.getStackInSlot(4) != null && !inventory.getStackInSlot(4).isItemEqual(outputItem.get())) continue;

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
                    qtyOutput.set(count);
                    requiredProcessTime.set(200);
                    furnaceSlot.add(i);
                    sync();
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
                if (currentMode.getValue() != recipe.getRequiredMode()) //Must be the correct mode. No sense otherwise
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
                    this.outputItem.set(recipe.getOutputItem());
                    currentRecipe.clear();
                    for(Object obj : recipe.getInput()) {
                        if(obj instanceof ItemStack)
                            currentRecipe.add(obj);
                        else if(obj instanceof OreDictStack)
                            currentRecipe.add(((OreDictStack) obj).getItemList());
                    }
                    this.requiredProcessTime.set(recipe.getProcessTime());
                    this.qtyOutput.set(recipe.getQtyOutput());
                    sync();
                    return true;
                }
            }
        }
        return false;
    }

    public Object getSpecialStackInSlot(int i) {
        if(inventory.getStackInSlot(i) != null && OreDictionary.getOreIDs(inventory.getStackInSlot(i)).length > 0) {
            return new OreDictStack(OreDictionary.getOreName(OreDictionary.getOreIDs(inventory.getStackInSlot(i))[0]),
                    inventory.getStackInSlot(i).stackSize);
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
        if (currentProcessTime.get() == 0 && canSmelt()) { //Begin
            currentProcessTime.set(1);
            this.isActive.set(true);
            if (currentMode.get() == FURNACE) {
                if (inventory.getStackInSlot(furnaceSlot.get(furnaceSlot.size() - 1)).stackSize == qtyOutput.get()) {
                    inventory.setInventorySlotContents(furnaceSlot.get(furnaceSlot.size() - 1), null);
                } else if (inventory.getStackInSlot(furnaceSlot.get(furnaceSlot.size() - 1)).stackSize > qtyOutput.get()) {
                    inventory.getStackInSlot(furnaceSlot.get(furnaceSlot.size() - 1)).stackSize -= qtyOutput.get();
                } else {
                    int count = qtyOutput.get() - inventory.getStackInSlot(furnaceSlot.get(furnaceSlot.size() - 1)).stackSize;
                    inventory.setInventorySlotContents(furnaceSlot.get(furnaceSlot.size() - 1), null);
                    for (int i = 0; i < furnaceSlot.size() - 1; i++) {
                        if (inventory.getStackInSlot(furnaceSlot.get(i)).stackSize <= count) {
                            count -=  inventory.getStackInSlot(furnaceSlot.get(i)).stackSize;
                            inventory.setInventorySlotContents(furnaceSlot.get(i), null);
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
                                    inventory.setInventorySlotContents(i, null);
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
                                            inventory.setInventorySlotContents(i, null);
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

        if (currentProcessTime.get() > 0 && currentProcessTime.get() < requiredProcessTime.get() ) { //Process
            if (this.energyRF.getValue().getEnergyStored() < RF_TICK) {
                doFail();
                return;
            }
            this.energyRF.getValue().modifyEnergyStored(-RF_TICK);
            currentProcessTime.set(currentProcessTime.get() + 1);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        if (currentProcessTime.get() > 0 && currentProcessTime.get() >= requiredProcessTime.get()) { //Output
            if (this.inventory.getStackInSlot(4) == null) {
                this.inventory.setInventorySlotContents(4, new ItemStack(outputItem.get().getItem(), this.qtyOutput.get(),
                        outputItem.get().getItemDamage()));
            } else {
                this.inventory.getStackInSlot(4).stackSize += this.qtyOutput.get();
            }
            doReset();
        }
    }

    /**
     * Reset values (usually if something fails or process complete)
     */
    private void doReset() {
        currentProcessTime.set(0);
        requiredProcessTime.set(0);
        this.isActive.set(false);
        qtyOutput.set(0);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void doFail() {
        if(worldObj.isRemote)
            SoundHelper.playSound("shutdown", xCoord, yCoord, zCoord, 1.0F, 1.0F);
        coolDown = 60;
        doReset();
    }

    public IValueProvider<Integer> getProgress() {
        return currentProcessTime;
    }

    /*******************************************************************************************************************
     ******************************************** Energy Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate) {
        if (!simulate) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return energyRF.getValue().receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate) {
        return energyRF.getValue() .extractEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getValue().getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return energyRF.getValue().getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        return true;
    }

    /*******************************************************************************************************************
     ********************************************** Item Functions *****************************************************
     *******************************************************************************************************************/



    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) return;

        if(automaticSlots.get(AUTO_SLOTS.output)) {
            ItemDistribution.moveItemsToOneOfSides(this, inventory, 0, 1, sideOut.getValue(), true);
        }

        if (inventory.getStackInSlot(0) == null && inventory.getStackInSlot(1) == null &&
                inventory.getStackInSlot(2) == null && inventory.getStackInSlot(3) == null && currentProcessTime.get() == 0) return;
        if (coolDown > 0) coolDown--;
        doSmelt();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        inventory.writeToNBT(tag);
    }

    /*******************************************************************************************************************
     ********************************************** Misc Functions *****************************************************
     *******************************************************************************************************************/
    @Override
    public void returnWailaHead(List<String> head) {
        head.add(GuiHelper.GuiColor.YELLOW + "Working: " + GuiHelper.GuiColor.WHITE + (isActive.get() ? "Yes" : "No"));
        head.add(GuiHelper.GuiColor.YELLOW + "Energy: " + GuiHelper.GuiColor.WHITE + energyRF.getValue().getEnergyStored() + "/" +  energyRF.getValue().getMaxEnergyStored() + GuiHelper.GuiColor.TURQUISE + "RF");
    }

    private SyncableSides selectSlotMap(AUTO_SLOTS slot) {
        switch (slot) {
            case output:
                return sideOut;
            default:
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    public IValueProvider<EnergyStorage> getRFEnergyStorageProvider() {
        return energyRF;
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

    public List<String> getEnergyToolTip() {
        List<String> toolTip = new ArrayList<>();
        toolTip.add(GuiHelper.GuiColor.WHITE + "RF Energy");
        toolTip.add("" + GuiHelper.GuiColor.YELLOW + energyRF.getValue().getEnergyStored() + "/" + energyRF.getValue().getMaxEnergyStored() + GuiHelper.GuiColor.RED + "RF");
        return toolTip;
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerAdvancedCrafter(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiAdvancedCrafter(new ContainerAdvancedCrafter(player.inventory, this));
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return true;
    }

    @Override
    public IInventory getInventory() {
        return this.inventory;
    }

    @Override
    public void changeLevel(int level) {
        currentMode.set(level);
        sync();
    }

    @Override
    public void onWrench(EntityPlayer player, int side) {

    }

    @Override
    public boolean isActive() {
        return currentProcessTime.get() > 0;
    }
}
