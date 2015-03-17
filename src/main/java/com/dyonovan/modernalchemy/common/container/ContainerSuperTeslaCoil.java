package com.dyonovan.modernalchemy.common.container;

import com.dyonovan.modernalchemy.common.container.GenericInventory;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileSuperTeslaCoil;
import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaCoil;
import net.minecraft.inventory.IInventory;
import openmods.container.ContainerBase;

public class ContainerSuperTeslaCoil extends ContainerBase<TileSuperTeslaCoil> {
    public ContainerSuperTeslaCoil(IInventory playerInventory, TileSuperTeslaCoil owner) {
        super(playerInventory, new GenericInventory("null", false, 0), owner);
    }
}
