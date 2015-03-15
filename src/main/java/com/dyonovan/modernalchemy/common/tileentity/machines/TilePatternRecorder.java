package com.dyonovan.modernalchemy.common.tileentity.machines;

import com.dyonovan.modernalchemy.client.gui.machines.GuiPatternRecorder;
import com.dyonovan.modernalchemy.common.container.machines.ContainerPatternRecorder;
import com.dyonovan.modernalchemy.common.items.ItemPattern;
import com.dyonovan.modernalchemy.common.tileentity.TileModernAlchemy;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.helpers.GuiHelper;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import com.dyonovan.modernalchemy.util.ReplicatorValues;
import cpw.mods.fml.common.registry.GameRegistry;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TilePatternRecorder extends TileModernAlchemy implements IInventoryProvider, IHasGui, IConfigurableGuiSlots<TilePatternRecorder.AUTO_SLOTS> {

    public enum AUTO_SLOTS {
        output,
        input
    }

    public static final int PROCESS_TIME = 500; //5 mins with 1 T/Tick
    public static final int INPUT_SLOT = 1;
    public static final int ITEM_SLOT = 0;
    public static final int OUTPUT_SLOT = 2;

    public final GenericInventory inventory = registerInventoryCallback(new TileEntityInventory(this, "patternrecorder", true, 3) {
        @Override
        public boolean isItemValidForSlot(int i, ItemStack itemstack) {
            if(i == INPUT_SLOT) return itemstack.getItem() instanceof ItemPattern;
            if(i == ITEM_SLOT) return !(itemstack.getItem() instanceof ItemPattern);
            return false;
        }
    });

    public SyncableSides sideOut;
    public SyncableSides sideIn;
    private SyncableInt currentSpeed;
    private SyncableString itemCopy;
    private SyncableFloat lastQuality;
    public SyncableInt currentProcessTime;
    public SyncableBoolean isActive;
    private SyncableFlags automaticSlots;

    protected TeslaBank energyTank;

    @IncludeInterface(ISidedInventory.class)
    private final SidedInventoryAdapter sided = new SidedInventoryAdapter(inventory);

    @Override
    protected void createSyncedFields() {
        sideOut = new SyncableSides();
        sideIn = new SyncableSides();
        currentSpeed = new SyncableInt();
        itemCopy = new SyncableString("");
        lastQuality = new SyncableFloat(0.0F);
        currentProcessTime = new SyncableInt(0);
        isActive = new SyncableBoolean(false);
        automaticSlots = SyncableFlags.create(AUTO_SLOTS.values().length);
        energyTank = new TeslaBank(0, 1000);
    }

    public TilePatternRecorder() {
        sided.registerSlots(0, 2, sideIn, false, false);
        sided.registerSlot(2, sideOut, false, true);
    }

    /*******************************************************************************************************************
     ************************************* Pattern Recorder Functions **************************************************
     *******************************************************************************************************************/

    private void doRecording() {
        if (canStartWork() || currentProcessTime.get() > 0) {
            updateSpeed();
            if (!isActive.get())
                isActive.set(true);

            if (currentProcessTime.get() <= 0 && canStartWork()) {
                GameRegistry.UniqueIdentifier uniqueIdentifier = GameRegistry.findUniqueIdentifierFor(inventory.getStackInSlot(ITEM_SLOT).getItem());
                itemCopy.setValue(uniqueIdentifier.modId + ":" + uniqueIdentifier.name + ":" + inventory.getStackInSlot(ITEM_SLOT).getItemDamage());
                if(inventory.getStackInSlot(INPUT_SLOT).hasTagCompound()) {
                    lastQuality.set(inventory.getStackInSlot(INPUT_SLOT).getTagCompound().getFloat("Quality"));
                    if(!inventory.getStackInSlot(INPUT_SLOT).getTagCompound().getString("Item").equalsIgnoreCase(itemCopy.getValue())) //Must be a pattern of same item type
                        return;
                }
                else
                    lastQuality.set(0.0F);
                inventory.decrStackSize(ITEM_SLOT, 1);
                inventory.decrStackSize(INPUT_SLOT, 1);
                currentProcessTime.set(1);
            }

            if (currentProcessTime.get() < PROCESS_TIME) {
                if (energyTank.getEnergyLevel() > 0) {
                    energyTank.drainEnergy(currentSpeed.get());
                    currentProcessTime.modify(currentSpeed.get());
                } else {
                    currentProcessTime.set(0);
                    itemCopy.setValue(null);
                }
            }
            if (currentProcessTime.get() >= PROCESS_TIME) {
                inventory.setInventorySlotContents(OUTPUT_SLOT, recordPattern(itemCopy.getValue()));
                currentProcessTime.set(0);
            }
        } else if (isActive.get()) {
            isActive.set(false);
            currentProcessTime.set(0);
        }
        sync();
    }

    private void updateSpeed() {
        if (energyTank.getEnergyLevel() == 0) {
            currentSpeed.set(0);
            return;
        }

        currentSpeed.set((energyTank.getEnergyLevel() * 20) / energyTank.getMaxCapacity());
        if (currentSpeed.get() == 0)
            currentSpeed.set(1);
    }

    private ItemStack recordPattern(String item) {
        ItemStack pattern = new ItemStack(ItemHandler.itemReplicatorPattern, 1);
        ReplicatorValues values = ReplicatorUtils.getValueForItem(ReplicatorUtils.getReturn(item));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Item", item);
        tag.setInteger("Value", values.reqTicks);
        tag.setInteger("Qty", values.qtyReturn);
        float value = Math.max((1000 / values.reqTicks / 10), 0.1F);
        if(lastQuality.get() > 0)
            tag.setFloat("Quality", lastQuality.get() + value <= 100 ? lastQuality.get() + value : 100);
        else
            tag.setFloat("Quality", value <= 100 ? value : 100);
        pattern.setTagCompound(tag);

        return pattern;
    }

    private boolean canStartWork() {
        return inventory.getStackInSlot(INPUT_SLOT) != null && inventory.getStackInSlot(ITEM_SLOT) != null &&
                inventory.getStackInSlot(INPUT_SLOT).getItem() instanceof ItemPattern &&
                (inventory.getStackInSlot(INPUT_SLOT).hasTagCompound() ? inventory.getStackInSlot(INPUT_SLOT).getTagCompound().getFloat("Quality") < 100 : true) &&
                inventory.getStackInSlot(OUTPUT_SLOT) == null &&
                ReplicatorUtils.getValueForItem(inventory.getStackInSlot(ITEM_SLOT)) != null && energyTank.getEnergyLevel() >= 1;
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
        if(energyTank.canAcceptEnergy())
            chargeFromCoils(energyTank);
        doRecording();

        if(automaticSlots.get(AUTO_SLOTS.output)) {
            ItemDistribution.moveItemsToOneOfSides(this, getInventory(), OUTPUT_SLOT, 1, Arrays.asList(ForgeDirection.VALID_DIRECTIONS), true);
        }

        if(automaticSlots.get(AUTO_SLOTS.input)) {
            ItemDistribution.moveItemsFromOneOfSides(this, getInventory(), 1, ITEM_SLOT, Arrays.asList(ForgeDirection.VALID_DIRECTIONS), true);
            ItemDistribution.moveItemsFromOneOfSides(this, getInventory(), 1, INPUT_SLOT, Arrays.asList(ForgeDirection.VALID_DIRECTIONS), true);
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
    /*@Override
    public void returnWailaHead(List<String> head) {
        head.add(GuiHelper.GuiColor.YELLOW + "Is Writing: " + GuiHelper.GuiColor.WHITE + (isActive() ? "Yes" : "No"));
        if(isActive())
            head.add(GuiHelper.GuiColor.YELLOW + "Item Copying: " + GuiHelper.GuiColor.WHITE + ReplicatorUtils.getReturn(itemCopy.getValue()).getDisplayName());
        head.add(GuiHelper.GuiColor.YELLOW + "Energy: " + GuiHelper.GuiColor.WHITE + energyTank.getEnergyLevel() + "/" + energyTank.getMaxCapacity() + GuiHelper.GuiColor.TURQUISE + "T");
    }*/

    private SyncableSides selectSlotMap(AUTO_SLOTS slot) {
        switch (slot) {
            case output:
                return sideOut;
            case input :
                return sideIn;
            default:
                throw MiscUtils.unhandledEnum(slot);
        }
    }

    @Override
    public IValueProvider<Set<ForgeDirection>> createAllowedDirectionsProvider(TilePatternRecorder.AUTO_SLOTS slot) {
        return selectSlotMap(slot);
    }

    @Override
    public IWriteableBitMap<ForgeDirection> createAllowedDirectionsReceiver(TilePatternRecorder.AUTO_SLOTS slot) {
        SyncableSides dirs = selectSlotMap(slot);
        return BitMapUtils.createRpcAdapter(createRpcProxy(dirs, IRpcDirectionBitMap.class));
    }

    @Override
    public IValueProvider<Boolean> createAutoFlagProvider(TilePatternRecorder.AUTO_SLOTS slot) {
        return BitMapUtils.singleBitProvider(automaticSlots, slot.ordinal());
    }

    @Override
    public IValueReceiver<Boolean> createAutoSlotReceiver(TilePatternRecorder.AUTO_SLOTS slot) {
        IRpcIntBitMap bits = createRpcProxy(automaticSlots, IRpcIntBitMap.class);
        return BitMapUtils.singleBitReceiver(bits, slot.ordinal());
    }

    public IValueProvider<TeslaBank> getTeslaBankProvider() {
        return energyTank;
    }

    public List<String> getEnergyToolTip() {
        List<String> toolTip = new ArrayList<>();
        toolTip.add(GuiHelper.GuiColor.WHITE + "Energy Stored");
        toolTip.add("" + GuiHelper.GuiColor.YELLOW + energyTank.getValue().getEnergyLevel() + "/" + energyTank.getValue().getMaxCapacity() + GuiHelper.GuiColor.BLUE + "T");
        return toolTip;
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerPatternRecorder(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiPatternRecorder(new ContainerPatternRecorder(player.inventory, this));
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
