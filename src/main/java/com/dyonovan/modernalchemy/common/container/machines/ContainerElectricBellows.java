package com.dyonovan.modernalchemy.common.container.machines;

import com.dyonovan.modernalchemy.common.container.GenericInventory;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileElectricBellows;
import net.minecraft.inventory.IInventory;
import openmods.container.ContainerBase;

public class ContainerElectricBellows extends ContainerBase<TileElectricBellows> {
    public ContainerElectricBellows(IInventory player, TileElectricBellows tile) {
        super(player, new GenericInventory("null", false, 0), tile);
    }
}
