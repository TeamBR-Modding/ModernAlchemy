package com.dyonovan.itemreplication.tileentity.replicator;

import com.dyonovan.itemreplication.blocks.replicator.BlockReplicatorStand;
import com.dyonovan.itemreplication.energy.ITeslaHandler;
import com.dyonovan.itemreplication.energy.TeslaBank;
import com.dyonovan.itemreplication.entities.EntityLaserNode;
import com.dyonovan.itemreplication.handlers.ConfigHandler;
import com.dyonovan.itemreplication.items.ItemPattern;
import com.dyonovan.itemreplication.items.ItemReplicatorMedium;
import com.dyonovan.itemreplication.lib.Constants;
import com.dyonovan.itemreplication.tileentity.BaseTile;
import com.dyonovan.itemreplication.tileentity.InventoryTile;
import com.dyonovan.itemreplication.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.itemreplication.util.Location;
import com.dyonovan.itemreplication.util.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class TileReplicatorCPU extends BaseTile implements ITeslaHandler, ISidedInventory {

    private static final int PROCESS_TIME = 5000;

    private TeslaBank energy;
    public InventoryTile inventory;
    public int currentProcessTime;
    private Location stand;
    private List<EntityLaserNode> listLaser;


    public TileReplicatorCPU() {
        this.energy = new TeslaBank(1000);
        this.inventory = new InventoryTile(3);
        this.currentProcessTime = 0;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (worldObj.isRemote) return;

        if(energy.canAcceptEnergy()) {
            chargeFromCoils();
        }

        if (inventory.getStackInSlot(0) == null || inventory.getStackInSlot(1) == null) return;

        if (findLasers() && findStand()) {

        }
    }

    private boolean findStand() {
        stand = null;

        for (int i = 0; i < 4; i++) {
            if (stand != null) break;
            switch (i) {
                case 0:
                    if (worldObj.getBlock(xCoord + 2, yCoord, zCoord + 2) instanceof BlockReplicatorStand)
                        stand = new Location(xCoord + 2, yCoord, zCoord + 2);
                case 1:
                    if (worldObj.getBlock(xCoord + 2, yCoord, zCoord - 2) instanceof BlockReplicatorStand)
                        stand = new Location(xCoord + 2, yCoord, zCoord - 2);
                case 2:
                    if (worldObj.getBlock(xCoord - 2, yCoord, zCoord + 2) instanceof BlockReplicatorStand)
                        stand = new Location(xCoord - 2, yCoord, zCoord + 2);
                case 3:
                    if (worldObj.getBlock(xCoord - 2, yCoord, zCoord - 2) instanceof BlockReplicatorStand)
                        stand = new Location(xCoord - 2, yCoord, zCoord - 2);
            }
        }
        return (stand != null);
    }

    private boolean findLasers() {
        listLaser = null;
        AxisAlignedBB bounds = null;

        for (int i = 0; i < 4; i++) {
            if (listLaser != null && listLaser.size() > 0) break;
            switch (i) {
                case 0:
                    bounds = AxisAlignedBB.getBoundingBox(xCoord, yCoord + 2, zCoord, xCoord + 4, yCoord + 4, zCoord + 4);
                    break;
                case 1:
                    bounds = AxisAlignedBB.getBoundingBox(xCoord, yCoord + 2, zCoord - 4, xCoord + 4, yCoord + 4, zCoord);
                    break;
                case 2:
                    bounds = AxisAlignedBB.getBoundingBox(xCoord - 4, yCoord + 2, zCoord - 4, xCoord, yCoord + 4, zCoord);
                    break;
                case 3:
                    bounds = AxisAlignedBB.getBoundingBox(xCoord - 4, yCoord + 2, zCoord, xCoord, yCoord + 4, zCoord + 4);
                    break;
            }
            listLaser = worldObj.getEntitiesWithinAABB(EntityLaserNode.class, bounds);
        }
        return listLaser.size() > 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        energy.readFromNBT(tag);
        inventory.readFromNBT(tag, this);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energy.writeToNBT(tag);
        inventory.writeToNBT(tag);
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

    public int getProgressScaled(int scale) { return this.currentProcessTime * scale / PROCESS_TIME; }

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

    @Override
    public int[] getAccessibleSlotsFromSide(int i) {
        return new int[] {0, 1};
    }

    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        return isItemValidForSlot(slot, itemstack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        return !(slot == 0 || side != 0);
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
        ItemStack itemStack = getStackInSlot(slot);
        setInventorySlotContents(slot, null);
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        inventory.setStackInSlot(itemStack, slot);
        worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public String getInventoryName() {
        return Constants.MODID + ":blockReplicatorCPU";
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
        switch (slot) {
            case 0:
                return itemStack.getItem() instanceof ItemReplicatorMedium;
            case 1:
                return itemStack.getItem() instanceof ItemPattern;
            default:
                return false;
        }
    }
}
