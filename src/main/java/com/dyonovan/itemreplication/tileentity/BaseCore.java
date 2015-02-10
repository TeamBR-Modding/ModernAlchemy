package com.dyonovan.itemreplication.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public abstract class BaseCore extends BaseTile {

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
}
