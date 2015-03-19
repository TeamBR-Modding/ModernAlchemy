package com.dyonovan.modernalchemy.common.tileentity.machines;

import com.dyonovan.modernalchemy.client.audio.SoundHelper;
import com.dyonovan.modernalchemy.client.gui.machines.GuiReplicator;
import com.dyonovan.modernalchemy.client.rpc.IRedstoneRequired;
import com.dyonovan.modernalchemy.common.container.machines.ContainerReplicatorCpu;
import com.dyonovan.modernalchemy.common.items.ItemPattern;
import com.dyonovan.modernalchemy.common.items.ItemReplicatorMedium;
import com.dyonovan.modernalchemy.common.tileentity.TileModernAlchemy;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.IHasGui;
import openmods.api.IValueProvider;
import openmods.api.IValueReceiver;
import openmods.gui.misc.IConfigurableGuiSlots;
import openmods.include.IncludeInterface;
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
import scala.actors.threadpool.Arrays;

import java.util.*;

public class TileReplicator extends TileModernAlchemy implements IRedstoneRequired, IInventoryProvider, IHasGui, IConfigurableGuiSlots<TileReplicator.AUTO_SLOTS> {

    private static Random rand = new Random();

    public static final int MEDIUM_INPUT = 0;
    public static final int PATTERN_INPUT = 1;
    public static final int OUTPUT = 2;

    public enum AUTO_SLOTS {
        medium_input,
        output
    }

    private final GenericInventory inventory = registerInventoryCallback(new TileEntityInventory(this, "replicatorcpu", true, 3) {
       @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
           if (slot == MEDIUM_INPUT) return itemstack.getItem() == ItemHandler.itemReplicationMedium;
           return slot == PATTERN_INPUT && itemstack.getItem() instanceof ItemPattern;
       }
    });

    public SyncableInt currentProcessTime;
    public SyncableInt requiredProcessTime;
    public SyncableSides patternIn;
    public SyncableSides mediumIn;
    public SyncableSides itemOut;

    private SyncableString item;
    private SyncableBoolean isActive;
    private SyncableFlags automaticSlots;
    private SyncableItemStack stackReturn;
    private SyncableInt qtyReturn;
    private SyncableBoolean requiresRedstone;

    protected TeslaBank energyTank;

    @SuppressWarnings("FieldCanBeLocal")
    @IncludeInterface(ISidedInventory.class)
    private final SidedInventoryAdapter sided = new SidedInventoryAdapter(inventory);

    @Override
    protected void createSyncedFields() {
        currentProcessTime = new SyncableInt(0);
        requiredProcessTime = new SyncableInt(0);
        patternIn = new SyncableSides();
        mediumIn = new SyncableSides();
        itemOut = new SyncableSides();

        item = new SyncableString("null");
        isActive = new SyncableBoolean(false);
        automaticSlots = SyncableFlags.create(AUTO_SLOTS.values().length);
        stackReturn = new SyncableItemStack();
        qtyReturn = new SyncableInt(1);
        requiresRedstone = new SyncableBoolean(true);

        energyTank = new TeslaBank(0, 1000);
    }


    public TileReplicator() {
        sided.registerSlot(MEDIUM_INPUT, mediumIn, true, false);
        sided.registerSlot(PATTERN_INPUT, patternIn, false, false);
        sided.registerSlot(OUTPUT, itemOut, false, true);
    }

    /*******************************************************************************************************************
     ****************************************** Replicator Functions ***************************************************
     *******************************************************************************************************************/

    private void doReplication() {
        if (canStartWork() || currentProcessTime.get() > 0) { //Must have a pattern
            if(inventory.getStackInSlot(1) == null) {
                fail();
                return;
            }
            if (inventory.getStackInSlot(1) != null) {
                if (Objects.equals(item.getValue(), "null")) {
                    item.setValue(inventory.getStackInSlot(1).getTagCompound().getString("Item"));
                    requiredProcessTime.set(inventory.getStackInSlot(1).getTagCompound().getInteger("Value"));
                    qtyReturn.set(inventory.getStackInSlot(1).getTagCompound().getInteger("Qty"));
                    stackReturn.set(ReplicatorUtils.getReturn(item.getValue(), qtyReturn.get()));
                    if (inventory.getStackInSlot(OUTPUT) != null && inventory.getStackInSlot(OUTPUT).stackSize + qtyReturn.get() >
                            inventory.getStackInSlot(OUTPUT).getMaxStackSize()) return;
                }
                if (inventory.getStackInSlot(2) != null && !(Objects.equals(item.getValue(), "null")) &&
                        stackReturn != null &&
                        (inventory.getStackInSlot(2).getItem() != stackReturn.get().getItem() ||
                                inventory.getStackInSlot(2).stackSize >= inventory.getStackInSlot(2).getMaxStackSize())) {
                    resetCounts();
                    return;
                }
                if (currentProcessTime.get() <= 0 && canStartWork()) {
                    currentProcessTime.set(1);
                    inventory.decrStackSize(0, 1);
                    isActive.set(true);
                }

                if (currentProcessTime.get() != 0 && currentProcessTime.get()  < requiredProcessTime.get()) {
                    if (energyTank.getEnergyLevel() >= 10) {
                        energyTank.drainEnergy(10);
                        currentProcessTime.modify(1);
                        isActive.set(true);
                    } else {
                        fail();
                    }
                }

                if (currentProcessTime.get() != 0 && currentProcessTime.get() >= requiredProcessTime.get() ) {
                    if(rand.nextInt(101) <= inventory.getStackInSlot(1).getTagCompound().getFloat("Quality")) {
                        if (inventory.getStackInSlot(2) == null) inventory.setInventorySlotContents(2, stackReturn.get());
                        else {
                            inventory.getStackInSlot(2).stackSize += qtyReturn.get();
                        }
                        resetCounts();
                    }
                    else
                        fail();
                }
                sync();
            }
        } else {
            resetCounts();
        }
    }

    private void fail() {
        resetCounts();
        WorldUtils.expelItem(worldObj, xCoord, yCoord + 1, zCoord, new ItemStack(ItemHandler.itemSlag));
    }

    private void resetCounts() {
        currentProcessTime.set(0);
        requiredProcessTime.set(0);
        item.setValue("null");
        stackReturn.set(null);
        isActive.set(false);
        qtyReturn.set(1);
    }

    private boolean canStartWork() {
        return inventory.getStackInSlot(0) != null && inventory.getStackInSlot(1) != null &&
                inventory.getStackInSlot(1).getItem() instanceof ItemPattern &&
                inventory.getStackInSlot(0).getItem() instanceof ItemReplicatorMedium &&
                inventory.getStackInSlot(1).hasTagCompound() &&
                (requiresRedstone.get() ? isPowered() : true);
    }

    public IValueProvider<Integer> getProgress() {
        return currentProcessTime;
    }

    /*******************************************************************************************************************
     ********************************************** Item Functions *****************************************************
     *******************************************************************************************************************/


    /*******************************************************************************************************************
     ********************************************** Tile Functions *****************************************************
     *******************************************************************************************************************/

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;
        if(energyTank.canAcceptEnergy()) {
            chargeFromCoils(energyTank);
        }
        doReplication();

        if(automaticSlots.get(AUTO_SLOTS.output)) {
            ItemDistribution.moveItemsToOneOfSides(this, inventory, OUTPUT, 1, itemOut.getValue(), true);
        }

        if (automaticSlots.get(AUTO_SLOTS.medium_input)) {
            //noinspection unchecked
            ItemDistribution.moveItemsFromOneOfSides(this, getInventory(), 1, MEDIUM_INPUT, Arrays.asList(ForgeDirection.VALID_DIRECTIONS), true);
        }
        sync();
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
        head.add(GuiHelper.GuiColor.YELLOW + "Is Replicating : " + GuiHelper.GuiColor.WHITE + (isActive() ? "Yes" : "No"));
        if(isActive()) {
            head.add(GuiHelper.GuiColor.YELLOW + "Item Replicating: " + GuiHelper.GuiColor.WHITE + ReplicatorUtils.getReturn(item.getValue(), qtyReturn.get()).getDisplayName());
            head.add(GuiHelper.GuiColor.YELLOW + "Success Rate: " + GuiHelper.GuiColor.WHITE + inventory.getStackInSlot(1).getTagCompound().getFloat("Quality") + "%");
        }
        head.add(GuiHelper.GuiColor.YELLOW + "Energy: " + GuiHelper.GuiColor.WHITE + energyTank.getEnergyLevel() + "/" + energyTank.getMaxCapacity() + GuiHelper.GuiColor.TURQUISE + "T");
    }

    public IValueProvider<Boolean> getRedstoneRequiredProvider() {
        return requiresRedstone;
    }

    @Override
    public void setRequirement(boolean bool) {
        requiresRedstone.set(bool);
        sync();
    }

    private SyncableSides selectSlotMap(AUTO_SLOTS slot) {
        switch (slot) {
            case medium_input:
                return mediumIn;
            case output:
                return itemOut;
            default:
                throw MiscUtils.unhandledEnum(slot);
        }
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

    public IValueProvider<TeslaBank> getTeslaBankProvider() {
        return energyTank;
    }

    public List<String> getEnergyToolTip() {
        List<String> toolTip = new ArrayList<>();
        toolTip.add(GuiHelper.GuiColor.WHITE + "Energy Stored");
        toolTip.add("" + GuiHelper.GuiColor.YELLOW + energyTank.getEnergyLevel() + "/" + energyTank.getMaxCapacity() + GuiHelper.GuiColor.BLUE + "T");
        return toolTip;
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerReplicatorCpu(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiReplicator(new ContainerReplicatorCpu(player.inventory, this));
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
    public void onWrench(EntityPlayer player, int side) {

    }

    @Override
    public boolean isActive() {
        return currentProcessTime.get() > 0;
    }
}
