package com.dyonovan.itemreplication.util;

import net.minecraft.nbt.NBTTagCompound;

public class Location {
    private int x;
    private int y;
    private int z;

    public Location() {
        this(0, 0, 0);
    }

    public Location(int xLocation, int yLocation, int zLocation) {
        this.x = xLocation;
        this.y = yLocation;
        this.z = zLocation;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getZ() { return z; }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("LocationX", x);
        tag.setInteger("LocationY", y);
        tag.setInteger("LocationZ", z);
    }

    public void readFromNBT(NBTTagCompound tag) {
        x = tag.getInteger("LocationX");
        y = tag.getInteger("LocationY");
        z = tag.getInteger("LocationZ");
    }

    @Override
    public boolean equals(Object obj) {
        Location loc = (Location)obj;
        return x == loc.x && y == loc.y && z == loc.z;
    }
}
