package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.handlers.ConfigHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BaseCore extends BaseTile {
    protected static Random random = new Random();

    protected boolean isValid = false;
    private boolean isDirty = true;
    private int cooldown = 20;

    @Override
    public void updateEntity() {
        cooldown--;
        if(cooldown < 0) {
            setDirty();
            cooldown = 20;
        }
        updateStructure();
        super.updateEntity();
    }
    public void setDirty() {
        isDirty = true;
    }

    protected void updateStructure() {
        if(isDirty) {
            if(isWellFormed()) {
                buildStructure();
            }
            else {
                deconstructStructure();
            }
            isDirty = false;
        }
    }

    public abstract boolean isWellFormed();

    public abstract void buildStructure();

    public abstract void deconstructStructure();

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.isDirty = tagCompound.getBoolean("isDirty");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("isDirty", isDirty);
    }

    private void expelItem(ItemStack itemstack) {
        if (itemstack != null) {
            EntityItem entityitem =
                    new EntityItem(
                            worldObj,
                            (double) xCoord + random.nextFloat() * 0.8F + 0.1F,
                            (double) yCoord + random.nextFloat() * 0.8F + 0.1F,
                            (double) zCoord + random.nextFloat() * 0.8F + 0.1F,
                            itemstack
                    );

            if (itemstack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
            }

            float f3 = 0.05F;
            entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
            entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
            worldObj.spawnEntityInWorld(entityitem);
        }
    }

    public List<TileTeslaCoil> findCoils(World world, TileEntity tile) {

        List<TileTeslaCoil> list = new ArrayList<TileTeslaCoil>();

        int tileX = tile.xCoord;
        int tileY = tile.yCoord;
        int tileZ = tile.zCoord;

        for (int x = -ConfigHandler.searchRange; x <= ConfigHandler.searchRange; x++) {
            for (int y = -ConfigHandler.searchRange; y <= ConfigHandler.searchRange; y++) {
                for (int z = -ConfigHandler.searchRange; z <= ConfigHandler.searchRange; z++) {
                    if (world.getTileEntity(tileX + x, tileY + y, tileZ + z) instanceof TileTeslaCoil) {
                        list.add((TileTeslaCoil) world.getTileEntity(tileX + x, tileY + y, tileZ + z));
                    }
                }
            }
        }
        return list;
    }
}
