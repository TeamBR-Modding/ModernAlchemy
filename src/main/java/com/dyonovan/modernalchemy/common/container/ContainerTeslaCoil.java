package com.dyonovan.modernalchemy.common.container;

import com.dyonovan.modernalchemy.common.tileentity.teslacoil.TileTeslaCoil;
import net.minecraft.inventory.IInventory;
import openmods.container.ContainerBase;

public class ContainerTeslaCoil extends ContainerBase<TileTeslaCoil> {
    public ContainerTeslaCoil(IInventory playerInventory, TileTeslaCoil owner) {
        super(playerInventory, new GenericInventory("null", false, 0), owner);
    }
}
