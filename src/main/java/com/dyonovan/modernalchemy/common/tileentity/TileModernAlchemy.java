package com.dyonovan.modernalchemy.common.tileentity;

import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileSuperTeslaCoil;
import com.dyonovan.modernalchemy.energy.ITeslaProvider;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.Location;
import com.dyonovan.modernalchemy.util.RenderUtils;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.tileentity.SyncedTileEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class TileModernAlchemy extends SyncedTileEntity {

    public abstract void onWrench(EntityPlayer player, int side);
    public abstract void returnWailaHead(List<String> tip);


    public abstract boolean isActive();

    public boolean isPowered() {
        return isPoweringTo(worldObj, xCoord, yCoord + 1, zCoord, 0) ||
                isPoweringTo(worldObj, xCoord, yCoord - 1, zCoord, 1) ||
                isPoweringTo(worldObj, xCoord, yCoord, zCoord + 1, 2) ||
                isPoweringTo(worldObj, xCoord, yCoord, zCoord - 1, 3) ||
                isPoweringTo(worldObj, xCoord + 1, yCoord, zCoord, 4) ||
                isPoweringTo(worldObj, xCoord - 1, yCoord, zCoord, 5);
    }

    public static boolean isPoweringTo(World world, int x, int y, int z, int side) {
        return world.getBlock(x, y, z).isProvidingWeakPower(world, x, y, z, side) > 0;
    }

    public TileEntity getTileInDirection(ForgeDirection direction) {
        int x = xCoord + direction.offsetX;
        int y = yCoord + direction.offsetY;
        int z = zCoord + direction.offsetZ;

        if (worldObj != null && worldObj.blockExists(x, y, z)) { return worldObj.getTileEntity(x, y, z); }
        return null;
    }

    /*******************************************************************************************************************
     ******************************************** Energy Functions *****************************************************
     *******************************************************************************************************************/

    public List<ITeslaProvider> findCoils(World world) {

        List<ITeslaProvider> list = new ArrayList<>();

        int tileX = xCoord;
        int tileY = yCoord;
        int tileZ = zCoord;

        for (int x = -ConfigHandler.searchRange; x <= ConfigHandler.searchRange; x++) {
            for (int y = -ConfigHandler.searchRange; y <= ConfigHandler.searchRange; y++) {
                for (int z = -ConfigHandler.searchRange; z <= ConfigHandler.searchRange; z++) {
                    if (world.getTileEntity(tileX + x, tileY + y, tileZ + z) instanceof TileTeslaCoil) {
                        if(hasClearPath(tileX + 0.5, tileY + 0.5, tileZ + 0.5, tileX + x + 0.5, tileY + y + 0.5, tileZ + z + 0.5))
                            list.add((TileTeslaCoil) world.getTileEntity(tileX + x, tileY + y, tileZ + z));
                    } else if (world.getTileEntity(tileX + x, tileY + y, tileZ + z) instanceof TileSuperTeslaCoil) {
                        if(hasClearPath(tileX + 0.5, tileY + 0.5, tileZ + 0.5, tileX + x + 0.5, tileY + y + 0.5, tileZ + z + 0.5))
                            list.add((TileSuperTeslaCoil) world.getTileEntity(tileX + x, tileY + y, tileZ + z));
                    }
                }
            }
        }
        return list;
    }

    public boolean hasClearPath(double x1, double y1, double z1, double x2, double y2, double z2) {
        float t = 0.1F;
        while(t < 1.0F) {
            double checkX = x1 + ((x2 - x1) * t);
            double checkY = y1 + ((y2 - y1) * t);
            double checkZ = z1 + ((z2 - z1) * t);
            if(!worldObj.isAirBlock((int)Math.floor(checkX), (int)Math.floor(checkY), (int)Math.floor(checkZ)) &&
                    (worldObj.getBlock((int)Math.floor(checkX), (int)Math.floor(checkY), (int)Math.floor(checkZ)) != this.getBlockType()) &&
                    (!(worldObj.getTileEntity((int)Math.floor(checkX), (int)Math.floor(checkY), (int)Math.floor(checkZ)) instanceof ITeslaProvider))) {
                return false;
            }
            t += 0.01F;
        }
        return true;
    }

    public void chargeFromCoils(TeslaBank energyTank) {
        int maxFill = energyTank.getMaxCapacity() - energyTank.getEnergyLevel();
        List<ITeslaProvider> coils = findCoils(worldObj);
        int currentDrain = 0;
        for(ITeslaProvider coil : coils) {
            if(coil instanceof TileTeslaCoil) {
                if (((TileTeslaCoil)coil).linkedMachines.contains(new Location(xCoord, yCoord, zCoord)) || ((TileTeslaCoil)coil).linkedMachines.size() == 0) {
                    if (((TileTeslaCoil)coil).energyTank.getEnergyLevel() <= 0) continue; //fixes looking like its working when coil is empty
                    int fill = ((TileTeslaCoil)coil).energyTank.getEnergyLevel() > coil.getEnergyProvided() ? coil.getEnergyProvided() : ((TileTeslaCoil)coil).energyTank.getEnergyLevel();
                    if (currentDrain + fill > maxFill)
                        fill = maxFill - currentDrain;
                    currentDrain += fill;
                    ((TileTeslaCoil)coil).energyTank.drainEnergy(fill);

                    RenderUtils.sendBoltToClient(xCoord, yCoord, zCoord, (TileEntity) coil, fill);
                    WorldUtils.hurtEntitiesInRange(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, ((TileTeslaCoil) coil).xCoord + 0.5, ((TileTeslaCoil) coil).yCoord + 0.5, ((TileTeslaCoil) coil).zCoord + 0.5);

                    if (currentDrain >= maxFill) //Don't want to drain other coils we don't need to
                        break;
                }
            } else if (coil instanceof TileSuperTeslaCoil) {
                if (((TileSuperTeslaCoil)coil).linkedMachines.contains(new Location(xCoord, yCoord, zCoord)) || ((TileSuperTeslaCoil)coil).linkedMachines.size() == 0) {
                    if (((TileSuperTeslaCoil) coil).energyTank.getEnergyLevel() <= 0)
                        continue; //fixes looking like its working when coil is empty
                    int fill = ((TileSuperTeslaCoil) coil).energyTank.getEnergyLevel() > coil.getEnergyProvided() ? coil.getEnergyProvided() : ((TileSuperTeslaCoil) coil).energyTank.getEnergyLevel();
                    if (currentDrain + fill > maxFill)
                        fill = maxFill - currentDrain;
                    currentDrain += fill;
                    ((TileSuperTeslaCoil) coil).energyTank.drainEnergy(fill);

                    RenderUtils.sendBoltToClient(xCoord, yCoord, zCoord, (TileEntity) coil, fill);
                    WorldUtils.hurtEntitiesInRange(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, ((TileSuperTeslaCoil) coil).xCoord + 0.5, ((TileSuperTeslaCoil) coil).yCoord + 0.5, ((TileSuperTeslaCoil) coil).zCoord + 0.5);

                    if (currentDrain >= maxFill) //Don't want to drain other coils we don't need to
                        break;
                }
            }
        }
        while(currentDrain > 0) {
            energyTank.addEnergy(1);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            sync();
            currentDrain--;
        }
    }
}
