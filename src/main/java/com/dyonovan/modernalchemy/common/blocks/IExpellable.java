package com.dyonovan.modernalchemy.common.blocks;

import net.minecraft.world.World;

public interface IExpellable {
    public void expelItems(World world, int x, int y, int z);
}
