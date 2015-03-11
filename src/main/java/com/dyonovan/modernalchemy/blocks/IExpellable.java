package com.dyonovan.modernalchemy.blocks;

import com.dyonovan.modernalchemy.tileentity.BaseTile;
import net.minecraft.world.World;

public interface IExpellable {
    public void expelItems(World world, int x, int y, int z);
}
