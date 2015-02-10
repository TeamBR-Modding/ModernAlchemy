package com.dyonovan.itemreplication.energy;

import com.dyonovan.itemreplication.handlers.ConfigHandler;
import com.dyonovan.itemreplication.tileentity.TileTeslaCoil;
import com.dyonovan.itemreplication.util.RenderUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 2/9/2015.
 */
public class TeslaReceiver {

    private TeslaBank tank;

    public TeslaReceiver(TeslaBank tank){
        this.tank = tank;
    }

    // TODO: would like to use these algorithims instead
    /**
     * Add energy to tank if needed.  Call during update.
     * Will render lightning from coils to the tile entity when charging.
     */
//    public void chargeFromCoils(World world, TileEntity tile) {
//        if (tank.canAcceptEnergy()) {
//            List<ITeslaTransmitter> coils = findCoils(world, tile);
//            int maxFill = tank.getMaxCapacity() - tank.getEnergyLevel();
//            for (ITeslaTransmitter coil : coils) {
//
//                int energy = Math.min(ConfigHandler.maxCoilTransfer, maxFill);
//
//                // request energy - we may not get all we want
//                energy = coil.transmitEnergy(energy);
//
//                if (energy > 0) {
//                    tank.addEnergy(energy);
//
//                    if (world.isRemote) {
//                        RenderUtils.renderLightningBolt(world, tile.xCoord, tile.yCoord, tile.zCoord, (TileEntity)coil, energy > 4 ? energy : 4);
//                    }
//                    if (!tank.canAcceptEnergy()) {
//                        break;
//                    }
//                    maxFill -= energy;
//                }
//            }
//        }
//    }

//    public static List<ITeslaTransmitter> findCoils(World world, TileEntity tile) {
//
//        List<ITeslaTransmitter> list = new ArrayList<ITeslaTransmitter>();
//
//        int tileX = tile.xCoord;
//        int tileY = tile.yCoord;
//        int tileZ = tile.zCoord;
//
//        for (int x = -ConfigHandler.searchRange; x <= ConfigHandler.searchRange; x++) {
//            for (int y = -ConfigHandler.searchRange; y <= ConfigHandler.searchRange; y++) {
//                for (int z = -ConfigHandler.searchRange; z <= ConfigHandler.searchRange; z++) {
//                    if (world.getTileEntity(tileX + x, tileY + y, tileZ + z) instanceof ITeslaTransmitter) {
//                        list.add((ITeslaTransmitter) world.getTileEntity(tileX + x, tileY + y, tileZ + z));
//                    }
//                }
//            }
//        }
//        return list;
//    }

    // this is the current implementation copy-pasted across all the classes
    public static void chargeFromCoils(World worldObj, TileEntity tile, TeslaBank energyTank) {
        int maxFill = energyTank.getMaxCapacity() - energyTank.getEnergyLevel();
        List<TileTeslaCoil> coils = findCoils(worldObj, tile);
        int currentDrain = 0;
        for(TileTeslaCoil coil : coils) {
            if (coil.getEnergyLevel() <= 0) continue; //fixes looking like its working when coil is empty
            int fill = coil.getEnergyLevel() > ConfigHandler.tickTesla ? ConfigHandler.tickTesla : coil.getEnergyLevel();
            if(currentDrain + fill > maxFill)
                fill = maxFill - currentDrain;
            currentDrain += fill;
            coil.drainEnergy(fill);

            if(worldObj.isRemote) {
                RenderUtils.renderLightningBolt(worldObj, tile.xCoord, tile.yCoord, tile.zCoord, coil, fill);
            }
            if(currentDrain >= maxFill) //Don't want to drain other coils we don't need to
                break;
        }
        while(currentDrain > 0) {
            energyTank.addEnergy(1);
            currentDrain--;
        }
    }

    public static List<TileTeslaCoil> findCoils(World world, TileEntity tile) {

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
