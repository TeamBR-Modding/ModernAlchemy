package com.dyonovan.modernalchemy.tileentity.replicator;

import com.dyonovan.modernalchemy.blocks.replicator.BlockReplicatorStand;
import com.dyonovan.modernalchemy.energy.ITeslaHandler;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.entities.EntityLaserNode;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.handlers.ItemHandler;
import com.dyonovan.modernalchemy.items.ItemPattern;
import com.dyonovan.modernalchemy.items.ItemReplicatorMedium;
import com.dyonovan.modernalchemy.lib.Constants;
import com.dyonovan.modernalchemy.tileentity.BaseTile;
import com.dyonovan.modernalchemy.tileentity.InventoryTile;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.RenderUtils;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class TileReplicatorCPU extends BaseTile implements ITeslaHandler, ISidedInventory {
    private TeslaBank energy;
    public InventoryTile inventory;
    public int currentProcessTime;
    public int requiredProcessTime;
    private Location stand;
    private String item;
    private List<EntityLaserNode> listLaser;
    private ItemStack stackReturn;


    public TileReplicatorCPU() {
        this.energy = new TeslaBank(1000);
        this.inventory = new InventoryTile(3);
        this.currentProcessTime = 0;
        this.requiredProcessTime = 0;
        this.item = "";
        this.isActive = false;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (worldObj.isRemote) return;

        if(energy.canAcceptEnergy()) {
            chargeFromCoils();
        }
        if (canStartWork() || currentProcessTime > 0) {
            if (findLasers() && findStand()) {
                if (item.equals("")) {
                    item = inventory.getStackInSlot(1).getTagCompound().getString("Item");
                    requiredProcessTime = inventory.getStackInSlot(1).getTagCompound().getInteger("Value");
                    stackReturn = ReplicatorUtils.getReturn(item);
                }
                if (inventory.getStackInSlot(2) != null && !item.equals("") &&
                                (inventory.getStackInSlot(2).getItem() != stackReturn.getItem() ||
                                inventory.getStackInSlot(2).stackSize >= inventory.getStackInSlot(2).getMaxStackSize())) {
                    resetCounts();
                    return;
                }
                if (currentProcessTime <= 0 && canStartWork() && getEnergyLevel() >= 2 * listLaser.size()) {
                    currentProcessTime = 1;
                    copyToStand(true);
                    decrStackSize(0, 1);
                    isActive = true;
                }

                if (currentProcessTime < requiredProcessTime) {
                    if (getEnergyLevel() >= 2 * listLaser.size()) {
                        energy.drainEnergy(2 * listLaser.size());
                        currentProcessTime += listLaser.size();
                        isActive = true;
                        for(EntityLaserNode node : listLaser)
                            node.fireLaser(stand.x + 0.5, stand.y + 1.5, stand.z + 0.5);
                    } else {
                        resetCounts();
                        WorldUtils.expelItem(worldObj, stand.x, stand.y + 1, stand.z, new ItemStack(ItemHandler.itemSlag));
                    }
                }

                if (currentProcessTime != 0 && currentProcessTime >= requiredProcessTime) {
                    copyToStand(false);
                    if (inventory.getStackInSlot(2) == null) inventory.setStackInSlot(stackReturn, 2);
                    else {
                        inventory.getStackInSlot(2).stackSize += 1;
                    }
                    resetCounts();
                }
            }
        } else {
            if (stand == null) findStand();
            if (stand != null) copyToStand(false);
        }
    }

    private void resetCounts() {
        currentProcessTime = 0;
        requiredProcessTime = 0;
        item = "";
        stackReturn = null;
        isActive = false;
        this.markDirty();
    }

    private void copyToStand(Boolean insert) {
        TileReplicatorStand tileStand = (TileReplicatorStand) worldObj.getTileEntity(stand.x, stand.y, stand.z);
        if (tileStand != null) {
            if (insert)
                tileStand.setInventorySlotContents(0, new ItemStack(ItemHandler.itemReplicationMedium));
            else tileStand.setInventorySlotContents(0, null);
        }
    }

    private boolean canStartWork() {

        return inventory.getStackInSlot(0) != null && inventory.getStackInSlot(1) != null &&
                inventory.getStackInSlot(1).getItem() instanceof ItemPattern &&
                inventory.getStackInSlot(0).getItem() instanceof ItemReplicatorMedium &&
                inventory.getStackInSlot(1).hasTagCompound();
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

    @SuppressWarnings("unchecked")
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
        currentProcessTime = tag.getInteger("TimeProcessed");
        requiredProcessTime = tag.getInteger("RequiredTime");
        item = tag.getString("ItemName");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        energy.writeToNBT(tag);
        inventory.writeToNBT(tag);
        tag.setInteger("TimeProcessed", currentProcessTime);
        tag.setInteger("RequiredTime", requiredProcessTime);
        tag.setString("ItemName", item);
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

    public int getProgressScaled(int scale) {
        return requiredProcessTime == 0 ? 0 : this.currentProcessTime * scale / requiredProcessTime;
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
