package com.dyonovan.itemreplication.tileentity;

import com.dyonovan.itemreplication.blocks.BlockDummy;
import com.dyonovan.itemreplication.helpers.Location;

public class TileBlastFurnaceCore extends BaseCore {

    @Override
    public boolean isWellFormed() {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                for(int k = -1; k <= 1; k++) {
                    if(i == 0 && j == 0 && k == 0)
                        continue;
                    if(!(worldObj.getBlock(xCoord + i, yCoord + j, zCoord + k) instanceof BlockDummy))
                        return false;
                }
            }
        }
        buildStructure();
        return true;
    }

    @Override
    public void buildStructure() {
        for(int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0)
                        continue;
                    TileDummy dummy = (TileDummy)worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                    dummy.setCoreLocation(new Location(xCoord, yCoord, zCoord));
                }
            }
        }
    }

    @Override
    public void deconstructStructure() {
        for(int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0)
                        continue;
                    TileDummy dummy = (TileDummy)worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                    if(dummy != null)
                        dummy.setCoreLocation(new Location(-100, -100, -100));
                }
            }
        }
    }
}
