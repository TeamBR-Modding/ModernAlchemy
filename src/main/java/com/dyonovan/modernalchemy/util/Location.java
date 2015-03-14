package com.dyonovan.modernalchemy.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.sync.SyncableObjectBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Location extends SyncableObjectBase {
    public int x;
    public int y;
    public int z;

    public Location() {
        this(0, -10000, 0);
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

    public boolean isValue() {
        return y != -10000;
    }

    public void inValidate() {
        y = -10000;
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

    @Override
    public void readFromStream(DataInputStream stream) throws IOException {
        x = stream.readInt();
        y = stream.readInt();
        z = stream.readInt();
    }

    @Override
    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(z);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, String name) {
        tag.setInteger("X-" + name, x);
        tag.setInteger("Y-" + name, y);
        tag.setInteger("Z-" + name, z);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, String name) {
        x = tag.getInteger("X-" + name);
        y = tag.getInteger("Y-" + name);
        z = tag.getInteger("Z-" + name);
    }
}
