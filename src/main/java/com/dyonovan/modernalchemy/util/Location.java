package com.dyonovan.modernalchemy.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class Location {
    public int x;
    public int y;
    public int z;

    public Location() {
        this(0, 0, 0);
    }

    public Location(Location l1, Location l2) {
        x = l1.x + l2.x;
        y = l1.y + l2.y;
        z = l1.z + l2.z;
    }

    public Location(int xLocation, int yLocation, int zLocation) {
        this.x = xLocation;
        this.y = yLocation;
        this.z = zLocation;
    }

    public int X() { return x; }

    public int Y() { return y; }

    public int Z() { return z; }

    public void moveInDirection(ForgeDirection dir) {
        switch (dir) {
        case UP :
            y++;
            break;
        case DOWN :
            y--;
            break;
        case NORTH :
            z--;
            break;
        case EAST :
            x++;
            break;
        case SOUTH :
            z++;
            break;
        case WEST :
            x--;
            break;
        }
    }

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

    @Override
    public String toString() {
        return "X: " + x + " Y: " + y + " Z: " + z;
    }
}
