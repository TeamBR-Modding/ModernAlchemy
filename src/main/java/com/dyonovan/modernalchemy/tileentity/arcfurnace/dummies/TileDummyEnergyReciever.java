package com.dyonovan.modernalchemy.tileentity.arcfurnace.dummies;

import com.dyonovan.modernalchemy.energy.ITeslaHandler;
import com.dyonovan.modernalchemy.energy.TeslaBank;
import com.dyonovan.modernalchemy.handlers.BlockHandler;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.modernalchemy.tileentity.teslacoil.TileTeslaCoil;
import com.dyonovan.modernalchemy.util.RenderUtils;
import com.dyonovan.modernalchemy.util.WorldUtils;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TileDummyEnergyReciever extends TileDummy implements ITeslaHandler {

    public List<TileTeslaCoil> findCoils(World world) {

        List<TileTeslaCoil> list = new ArrayList<TileTeslaCoil>();

        int tileX = xCoord;
        int tileY = yCoord;
        int tileZ = zCoord;

        for (int x = -ConfigHandler.searchRange; x <= ConfigHandler.searchRange; x++) {
            for (int y = -ConfigHandler.searchRange; y <= ConfigHandler.searchRange; y++) {
                for (int z = -ConfigHandler.searchRange; z <= ConfigHandler.searchRange; z++) {
                    if (world.getTileEntity(tileX + x, tileY + y, tileZ + z) instanceof TileTeslaCoil) {
                        if(hasClearPath(tileX, tileY, tileZ, tileX + x + 0.5, tileY + y + 0.5, tileZ + z + 0.5))
                            list.add((TileTeslaCoil) world.getTileEntity(tileX + x, tileY + y, tileZ + z));
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
                    (worldObj.getBlock((int)Math.floor(checkX), (int)Math.floor(checkY), (int)Math.floor(checkZ)) != BlockHandler.blockCoil)) {
                return false;
            }
            t += 0.01F;
        }
        return true;
    }

    public void chargeFromCoils() {
        TileArcFurnaceCore core = (TileArcFurnaceCore)getCore();
        if(core != null) {
            int maxFill = getEnergyBank().getMaxCapacity() - getEnergyBank().getEnergyLevel();
            List<TileTeslaCoil> coils = findCoils(worldObj);
            int currentDrain = 0;
            for (TileTeslaCoil coil : coils) {
                if (coil.getEnergyLevel() <= 0) continue; //fixes looking like its working when coil is empty
                int fill = coil.getEnergyLevel() > ConfigHandler.maxCoilTransfer ? ConfigHandler.maxCoilTransfer : coil.getEnergyLevel();
                if (currentDrain + fill > maxFill)
                    fill = maxFill - currentDrain;
                currentDrain += fill;
                coil.drainEnergy(fill);

                RenderUtils.sendBoltToClient(xCoord, yCoord, zCoord, coil, fill);
                WorldUtils.hurtEntitiesInRange(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, coil.xCoord + 0.5, coil.yCoord + 0.5, coil.zCoord + 0.5);

                if (currentDrain >= maxFill) //Don't want to drain other coils we don't need to
                    break;
            }
            while (currentDrain > 0) {
                addEnergy(1);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                currentDrain--;
            }
        }
    }

    @Override
    public void updateEntity() {
        if(worldObj.isRemote) return;
        TileArcFurnaceCore core = (TileArcFurnaceCore)getCore();
        if(core != null) {
            if(core.getEnergyBank().canAcceptEnergy())
                chargeFromCoils();
        }
    }

    @Override
    public void addEnergy(int maxAmount) {
        TileArcFurnaceCore core = (TileArcFurnaceCore)getCore();
        if(core != null) {
            core.addEnergy(maxAmount);
            updateCore();
        }
    }

    @Override
    public int drainEnergy(int maxAmount) {
        TileArcFurnaceCore core = (TileArcFurnaceCore)getCore();
        if(core != null) {
            int drain = core.drainEnergy(maxAmount);
            updateCore();
            return drain;
        }
        return 0;
    }

    @Override
    public int getEnergyLevel() {
        TileArcFurnaceCore core = (TileArcFurnaceCore)getCore();
        if(core != null) {
            return core.getEnergyLevel();
        }
        return 0;
    }

    @Override
    public TeslaBank getEnergyBank() {
        TileArcFurnaceCore core = (TileArcFurnaceCore)getCore();
        if(core != null) {
            updateCore();
            return core.getEnergyBank();
        }
        return null;
    }
}
