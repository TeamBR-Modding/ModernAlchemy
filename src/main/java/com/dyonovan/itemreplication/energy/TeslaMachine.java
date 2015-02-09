package com.dyonovan.itemreplication.energy;

import com.dyonovan.itemreplication.handlers.ConfigHandler;
import com.dyonovan.itemreplication.helpers.RenderUtils;
import com.dyonovan.itemreplication.tileentity.TileTeslaCoil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 2/8/2015.
 */
public final class TeslaMachine implements ITeslaHandler {

    private int totalWorkTime, workProgress, currentSpeed;

    private TeslaBank energyTank;
    private boolean isRunning;
    private ITeslaWorker iWorker;

    public TeslaMachine(ITeslaWorker worker) {
        totalWorkTime = 500;
        workProgress = 0;
        currentSpeed = 0;
        isRunning = false;
        energyTank = new TeslaBank(0, 1000);
        iWorker = worker;
    }

    public void SetTotalWorkTime(int workTime){
        if(!isRunning)
            totalWorkTime = workTime;
    }

    public boolean IsRunning() {
        return isRunning;
    }

    public int getProgressScaled(int scale) {
        return workProgress / totalWorkTime * scale;
    }

    public void readFromNBT(NBTTagCompound tag) {
        energyTank.readFromNBT(tag);
        totalWorkTime = tag.getInteger("totalWorkTime");
        workProgress = tag.getInteger("workProgress");
        currentSpeed = tag.getInteger("currentSpeed");
        isRunning = tag.getBoolean("isRunning");
    }

    public void writeToNBT(NBTTagCompound tag) {
        energyTank.writeToNBT(tag);
        tag.setInteger("totalWorkTime", totalWorkTime);
        tag.setInteger("workProgress", workProgress);
        tag.setInteger("currentSpeed", currentSpeed);
        tag.setBoolean("isRunning", isRunning);
    }

    public void update(World world, TileEntity tile) {
        if(iWorker.machineCanOperate()) {
            // work first to drain energy
            doWork();
            // then accept energy so our tank can stay "full"
            chargeFromCoils(world, tile);
            // update speed after draining/charging
            updateSpeed();
        }
    }

    private void doWork() {
        // check if resources are available when we start
        if(workProgress <= 0 && iWorker.canStartWork()) {
            isRunning = true;
            iWorker.onWorkStart();

            // set some progress time so we know we can do work
            workProgress = 1;
        }

        // try to do work
        if(workProgress > 0 &&
                iWorker.doWork(currentSpeed, workProgress, totalWorkTime) &&
                energyTank.drainEnergy(currentSpeed + 1)) {
            workProgress += currentSpeed;
        }
        else {
            // ran out of energy or unable to do work.
            // cancel all work done without producing products.
            isRunning = false;
            workProgress = 0;
        }

        if(workProgress >= totalWorkTime) {
            workProgress = 0;
            isRunning = false;
            iWorker.onWorkFinish();
        }
    }

    private void chargeFromCoils(World world, TileEntity tile) {
        if (energyTank.canAcceptEnergy()) {
            int maxFill = energyTank.getMaxCapacity() - energyTank.getEnergyLevel();
            List<TileTeslaCoil> coils = findCoils(world, tile);
            int currentDrain = 0;
            for (TileTeslaCoil coil : coils) {
                int fill = coil.getEnergyLevel() > ConfigHandler.tickTesla ? ConfigHandler.tickTesla : coil.getEnergyLevel();
                if (currentDrain + fill > maxFill)
                    fill = maxFill - currentDrain;
                currentDrain += fill;
                coil.drainEnergy(fill);

                if (world.isRemote) {
                    RenderUtils.renderLightningBolt(world, tile.xCoord, tile.yCoord, tile.zCoord, coil, fill > 4 ? fill : 4);
                }
                if(currentDrain >= maxFill)
                    break;
            }
            while (currentDrain > 0) {
                energyTank.addEnergy(1);
                currentDrain--;
            }
        }
    }

    // TODO: make non-public after other code is refactored
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

    private void updateSpeed() {
        if(energyTank.getEnergyLevel() == 0) {
            currentSpeed = 0;
        }
        else {
            currentSpeed = (energyTank.getEnergyLevel() * 20) / energyTank.getMaxCapacity();
            if (currentSpeed == 0)
                currentSpeed = 1;
        }
    }

    @Override
    public void addEnergy(int maxAmount) { energyTank.addEnergy(maxAmount); }

    @Override
    public void drainEnergy(int maxAmount) { energyTank.drainEnergy(maxAmount); }

    @Override
    public int getEnergyLevel() { return energyTank.getEnergyLevel(); }

    @Override
    public TeslaBank getEnergyBank() { return energyTank; }
}
