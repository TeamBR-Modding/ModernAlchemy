package com.dyonovan.modernalchemy.container;

import com.dyonovan.modernalchemy.tileentity.arcfurnace.TileArcFurnaceCore;
import com.dyonovan.teambrcore.container.BaseContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerArcFurnace extends BaseContainer {

    private TileArcFurnaceCore core;

    public ContainerArcFurnace(InventoryPlayer playerInventory, TileArcFurnaceCore tileArcFurnaceCore) {
        core = tileArcFurnaceCore;

        // Input
        addSlotToContainer(new Slot(core, 0, 72, 10));

        // Catalyst
        addSlotToContainer(new Slot(core, 1, 72, 60));
        bindPlayerInventory(playerInventory, 8, 84);
    }

    @Override
    public String getNEILabel() {
        return "modernalchemy.arcfurnace.recipes";
    }
}
